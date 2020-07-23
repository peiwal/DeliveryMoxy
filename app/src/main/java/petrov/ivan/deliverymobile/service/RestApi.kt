package petrov.ivan.deliverymobile.service

import io.reactivex.Single
import petrov.ivan.deliverymobile.data.ParamRespProduct
import retrofit2.http.GET
import retrofit2.http.Query

interface RestApi{
    @GET("products")
    fun getProducts(@Query("param") language: String): Single<ParamRespProduct>
}