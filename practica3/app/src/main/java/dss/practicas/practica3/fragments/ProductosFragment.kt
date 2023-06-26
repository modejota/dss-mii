package dss.practicas.practica3.fragments

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
import com.android.volley.toolbox.Volley
import dss.practicas.practica3.singleton.SharedData
import dss.practicas.practica3.adapters.ProductoAdapter
import dss.practicas.practica3.callbacks.VolleyCallback
import dss.practicas.practica3.databinding.FragmentProductosBinding
import dss.practicas.practica3.models.Producto
import org.json.JSONException


class ProductosFragment : Fragment() {

    private var _binding: FragmentProductosBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private var productos = ArrayList<Producto>()
    private val url = "http://10.0.2.2:8080/rest/products/"
    private val token = SharedData.getAuthToken()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductosBinding.inflate(inflater, container, false)
        _binding?.textView?.text = "Cargando productos..."
        initRecyclerView()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerView() {

        _binding?.productosRview?.layoutManager = LinearLayoutManager(context)
        productos = ArrayList()
        val productAdapter = ProductoAdapter(productos.toList())

        loadProducts(object : VolleyCallback {
            override fun onSuccess() {
                _binding?.textView?.visibility = View.GONE
                productAdapter.setItems(productos.toList())
                _binding?.productosRview?.adapter = productAdapter
            }
            override fun onError() {
                Toast.makeText(context, "Error al cargar los productos", Toast.LENGTH_SHORT).show()
                _binding?.textView?.text = "No hay productos que mostrar"
                _binding?.textView?.visibility = View.VISIBLE
            }
        })

    }

    private fun loadProducts(param: VolleyCallback) {
        val queue = Volley.newRequestQueue(context)
        val jsonArrayRequest: JsonArrayRequest = object : JsonArrayRequest(
            Method.GET, url, null,
            Response.Listener { response ->
                for (i in 0 until response.length()) {
                    try {
                        val responseObj = response.getJSONObject(i)
                        val id = responseObj.getInt("id")
                        val name = responseObj.getString("name")
                        val description = responseObj.getString("description")
                        val price = responseObj.getDouble("price")
                        val image = responseObj.getString("imageUrl")
                        val p = Producto(id, name, description, price, image)
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
}