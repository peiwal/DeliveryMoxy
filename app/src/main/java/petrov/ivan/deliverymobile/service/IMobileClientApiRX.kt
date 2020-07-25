package petrov.ivan.deliverymobile.service

import io.reactivex.Single
import petrov.delivery.webapi.ParamRespCompanyInfo
import petrov.delivery.webapi.ParamRespProduct
import retrofit2.http.GET

interface IMobileClientApiRX {
    @GET("getallproducts")
    fun getProducts(): Single<ParamRespProduct>

    @GET("getcompanyinfo")
    fun getCompanyInfo(): Single<ParamRespCompanyInfo>
}