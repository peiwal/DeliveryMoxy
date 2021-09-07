package petrov.ivan.deliverymobile.service

import io.reactivex.Single
import petrov.delivery.webapi.ParamRespCompanyInfo
import petrov.delivery.webapi.ParamRespProduct
import retrofit2.http.GET
import retrofit2.http.POST

interface IMobileClientApiRX {
    @GET("getallproducts")
    fun getProducts(): Single<ParamRespProduct>

    @GET("getcompanyinfo")
    fun getCompanyInfo(): Single<ParamRespCompanyInfo>

    @POST("sendAppData")
    fun setAppData(): Any
}