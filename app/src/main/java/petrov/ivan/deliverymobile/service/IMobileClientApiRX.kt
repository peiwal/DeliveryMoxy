package petrov.ivan.deliverymobile.service

import io.reactivex.Single
import petrov.ivan.deliverymobile.data.ParamRespProduct
import retrofit2.http.GET

interface IMobileClientApiRX {
    @GET("getallproducts")
    fun getProducts(): Single<ParamRespProduct>
}