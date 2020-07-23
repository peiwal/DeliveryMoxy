package petrov.ivan.deliverymobile.data

import java.io.Serializable

data class ProductInBasket(
    val product: Product,
    var count: Int
): Serializable