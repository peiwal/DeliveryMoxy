package petrov.ivan.deliverymobile.data

import java.io.Serializable

data class CategoryWithProductList(val idx: Int, val name: String, val productList: List<Product>): Serializable