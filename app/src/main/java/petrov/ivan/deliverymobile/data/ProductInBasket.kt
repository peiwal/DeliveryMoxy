package petrov.ivan.deliverymobile.data

import petrov.delivery.webapi.Product
import java.io.Serializable

data class ProductInBasket(
    val product: Product,
    var count: Int
): Serializable