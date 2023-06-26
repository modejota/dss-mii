package dss.practicas.practica3.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import dss.practicas.practica3.R
import dss.practicas.practica3.adapters.CarritoAdapter
import dss.practicas.practica3.callbacks.VolleyCallback
import dss.practicas.practica3.databinding.FragmentCarritoBinding
import dss.practicas.practica3.models.ProductoCarrito
import dss.practicas.practica3.singleton.SharedData
import org.json.JSONException

class CarritoFragment : Fragment() {

    private var _binding: FragmentCarritoBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private var productos = ArrayList<ProductoCarrito>()
    private val url = "http://10.0.2.2:8080/rest/carts/"
    private val urlComprar = "http://10.0.2.2:8080/rest/carts/buy"
    private val token = SharedData.getAuthToken()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCarritoBinding.inflate(inflater, container, false)

        _binding?.textView?.text = "Recuperando carrito de la compra..."
        _binding?.tvTotalCarrito?.visibility = View.GONE
        _binding?.buttonConfirmarCompra?.visibility = View.GONE

        initRecyclerView()
        _binding?.buttonConfirmarCompra?.setOnClickListener {
            val alert = AlertDialog.Builder(_binding?.root?.context)
            alert.setTitle("Confirmar compra")
            alert.setMessage("¿Está seguro de que desea confirmar la compra?")
            alert.setPositiveButton("Sí") { _, _ ->
                confirmarCompra(object: VolleyCallback {
                    override fun onSuccess() {
                        productos.clear()
                        initRecyclerView()
                        Snackbar.make(_binding?.root!!, "Compra realizada con éxito. Recibirá un email con los detalles del pedido.", Snackbar.LENGTH_LONG).show()
                    }
                    override fun onError() {
                        Toast.makeText(context, "Error al realizar la compra", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            alert.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            alert.show()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerView() {

        _binding?.carritoRview?.layoutManager = LinearLayoutManager(context)
        val productAdapter = CarritoAdapter(productos.toList(), onItemDeleted = {
            productos.clear()
            initRecyclerView()
        })

        loadProducts(object : VolleyCallback {
            override fun onSuccess() {
                _binding?.textView?.visibility = View.GONE
                productAdapter.setItems(productos.toList())
                _binding?.carritoRview?.adapter = productAdapter
                if (productos.size > 0) {
                    _binding?.tvTotalCarrito?.text = "Total: " + calcularTotal() + "€"
                    _binding?.tvTotalCarrito?.visibility = View.VISIBLE
                    _binding?.buttonConfirmarCompra?.visibility = View.VISIBLE
                } else {
                    _binding?.textView?.text = getString(R.string.no_products_carrito)
                    _binding?.textView?.visibility = View.VISIBLE
                    _binding?.tvTotalCarrito?.text = "0"
                    _binding?.tvTotalCarrito?.visibility = View.GONE
                    _binding?.buttonConfirmarCompra?.visibility = View.GONE
                }
            }
            override fun onError() {
                Toast.makeText(context, "Error al recuperar el carrito", Toast.LENGTH_SHORT).show()
                _binding?.textView?.text = getString(R.string.no_products_carrito)
                _binding?.textView?.visibility = View.VISIBLE
            }
        })

    }

    private fun calcularTotal(): Double {
        var total = 0.0
        for (p in productos) { total += p.price * p.cantidad }
        return total
    }

    private fun loadProducts(param: VolleyCallback) {
        val queue = Volley.newRequestQueue(context)
        val jsonArrayRequest: JsonArrayRequest = object : JsonArrayRequest(
            Method.GET, url, null,
            Response.Listener { response ->
                for (i in 0 until response.length()) {
                    try {
                        val responseObj = response.getJSONObject(i)
                        val prod = responseObj.getJSONObject("productInCart")
                        val cantidad = responseObj.getInt("quantity")
                        val p = ProductoCarrito(prod.getInt("id"),
                            prod.getString("name"), prod.getString("description"),
                            prod.getDouble("price"), prod.getString("imageUrl"), cantidad)
                        productos.add(p)

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                param.onSuccess()
            },
            Response.ErrorListener {
                param.onError()
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headerMap: MutableMap<String, String> = HashMap()
                headerMap["Authorization"] = token
                return headerMap
            }
        }
        queue.add(jsonArrayRequest)
    }

    private fun confirmarCompra(volleyCallback: VolleyCallback) {
        val queue = Volley.newRequestQueue(context)
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, urlComprar,
            Response.Listener {
                volleyCallback.onSuccess()
            },
            Response.ErrorListener {
                volleyCallback.onError()
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headerMap: MutableMap<String, String> = HashMap()
                headerMap["Authorization"] = token
                return headerMap
            }
        }
        queue.add(stringRequest)
    }

}
