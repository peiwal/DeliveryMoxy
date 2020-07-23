package petrov.ivan.deliverymobile.data

import java.io.Serializable
import java.math.BigDecimal

data class Product(
    val idx: Int,
    val name: String,
    val description: String,
    val ingredients: String,
    val imgUrl: String,
    val cost: BigDecimal
): Serializable