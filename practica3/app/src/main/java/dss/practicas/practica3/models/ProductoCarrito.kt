package dss.practicas.practica3.models

data class ProductoCarrito(
    val ID: Int,
    val name: String,
    val description: String,
    val price: Double,
    val image: String,
    val cantidad: Int
)
