package dss.practicas.practica3.adapters

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import dss.practicas.practica3.R
import dss.practicas.practica3.callbacks.VolleyCallback
import dss.practicas.practica3.databinding.ItemProductoCarritoRvBinding
import dss.practicas.practica3.models.ProductoCarrito
import dss.practicas.practica3.singleton.SharedData

class CarritoAdapter(
    private var carritoList: List<ProductoCarrito>,
    private val onItemDeleted:(Unit) -> Unit
) : RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CarritoViewHolder(layoutInflater.inflate(R.layout.item_producto_carrito_rv, parent, false))
    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        val item = carritoList[position]
        holder.render(item, onItemDeleted)
    }

    override fun getItemCount(): Int = carritoList.size

    fun setItems(items: List<ProductoCarrito>) {
        carritoList = items
    }

    inner class CarritoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemProductoCarritoRvBinding.bind(view)

        private val url = "http://10.0.2.2:8080/rest/carts/"
        private val token = SharedData.getAuthToken()

        fun render(carritoModel: ProductoCarrito, onItemDeleted:(Unit) -> Unit) {
            binding.tvNameProducto.text = carritoModel.name
            binding.tvCantidad.text = "Cantidad: ${carritoModel.cantidad} unidades"
            binding.tvPrecioTotal.text = "Precio: ${carritoModel.cantidad} x ${carritoModel.price} = ${carritoModel.cantidad * carritoModel.price} €"
            Glide.with(binding.ivProducto.context).load(carritoModel.image).into(binding.ivProducto)
            binding.imageButton.setOnClickListener {

                // Display a dialog to confirm the deletion
                val dialog = AlertDialog.Builder(binding.root.context)
                dialog.setTitle("Eliminar producto")
                dialog.setMessage("¿Estás seguro de que quieres eliminar este producto del carrito?")
                dialog.setPositiveButton("Sí") { _, _ ->
                    deleteItem(carritoModel.ID, object : VolleyCallback {
                        override fun onSuccess() {
                            Toast.makeText(binding.root.context, "Producto eliminado del carrito", Toast.LENGTH_SHORT).show()
                            val newList = carritoList.toMutableList()
                            newList.remove(carritoModel)
                            setItems(newList)
                            notifyItemRemoved(adapterPosition)
                            onItemDeleted(Unit)
                        }
                        override fun onError() {
                            Toast.makeText(binding.root.context, "Error al eliminar el producto del carrito", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
                dialog.setNegativeButton("No") { _, _ -> }
                dialog.show()
            }
        }

        private fun deleteItem(productoId: Int, callback: VolleyCallback) {
            val queue = Volley.newRequestQueue(binding.root.context)
            val url = url + productoId
            val stringRequest = object : StringRequest(
                Method.DELETE, url,
                Response.Listener {
                    callback.onSuccess()
                },
                Response.ErrorListener {
                    callback.onError()
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers: MutableMap<String, String> = HashMap()
                    headers["Authorization"] = token
                    return headers
                }
            }
            queue.add(stringRequest)
        }
    }
}