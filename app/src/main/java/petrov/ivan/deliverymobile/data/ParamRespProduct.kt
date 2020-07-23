package petrov.ivan.deliverymobile.data

import java.io.Serializable


data class ParamRespProduct (
    val results: List<CategoryWithProductList>
): Serializable