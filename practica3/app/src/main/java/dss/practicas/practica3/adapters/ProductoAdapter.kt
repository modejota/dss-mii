package dss.practicas.practica3.adapters

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import dss.practicas.practica3.models.Producto
import dss.practicas.practica3.R
import dss.practicas.practica3.databinding.ItemProductosRvBinding
import dss.practicas.practica3.singleton.SharedData
import org.json.JSONObject
import java.io.UnsupportedEncodingException

class ProductoAdapter(
    private var productosList: List<Producto>
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ProductoViewHolder(layoutInflater.inflate(R.layout.item_productos_rv, parent, false))
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val item = productosList[position]
        holder.render(item)
    }

    override fun getItemCount() : Int = productosList.size

    fun setItems(items: List<Producto>) {
        productosList = items
        notifyDataSetChanged()
    }

    inner class ProductoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemProductosRvBinding.bind(view)

        private val url = "http://10.0.2.2:8080/rest/carts/"
        private val token = SharedData.getAuthToken()

        fun render(productoModel: Producto) {
            binding.tvNameProducto.text = productoModel.name
            binding.tvPrecioProducto.text = productoModel.price.toString() + " €"
            binding.tvDescripcionProducto.text = productoModel.description
            Glide.with(itemView.context).load(productoModel.image).into(binding.ivProducto)
            binding.buttonAniadir.setOnClickListener {
                val alert = AlertDialog.Builder(binding.root.context)
                alert.setTitle("Añadir al carrito")
                alert.setMessage("¿Estás seguro de que quieres añadir este producto al carrito?")
                alert.setPositiveButton("Sí") { _, _ ->
                    enviarACarrito(productoModel)
                    Toast.makeText(binding.root.context, "Producto añadido al carrito", Toast.LENGTH_SHORT).show()
                }
                alert.setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                alert.show()
            }
        }


        private fun enviarACarrito(productoModel: Producto) {
            // Create Volley RequestQueue
            val queue = Volley.newRequestQueue(binding.ivProducto.context)

            // Our data
            val jsonBody = JSONObject()
            jsonBody.put("name", productoModel.name)
            jsonBody.put("description", productoModel.description)
            jsonBody.put("price", productoModel.price)
            jsonBody.put("image", productoModel.image)
            val requestBody = jsonBody.toString()

            // Create request itself. Quite an overrided constructor
            val jsonObjectRequest: JsonObjectRequest =
                object : JsonObjectRequest(
                    Method.POST, url+productoModel.ID, null,
                    Response.Listener {
                        Toast.makeText(binding.ivProducto.context, "Producto añadido al carrito", Toast.LENGTH_SHORT).show()
                    },
                    Response.ErrorListener { response ->
                        Log.d("Error", response.toString())

                    }) {

                    override fun getBodyContentType(): String {
                        return "application/json; charset=utf-8"
                    }

                    // Encode our data to be sent
                    override fun getBody(): ByteArray {
                        return try {
                            requestBody.toByteArray(charset("utf-8"))
                        } catch (uee: UnsupportedEncodingException) {
                            return null!!
                        }
                    }

                    @Throws(AuthFailureError::class)
                    override fun getHeaders(): Map<String, String> {
                        val headerMap: MutableMap<String, String> = HashMap()
                        headerMap["Authorization"] = token
                        return headerMap
                    }

                }
            queue.add(jsonObjectRequest)
        }


    }

}