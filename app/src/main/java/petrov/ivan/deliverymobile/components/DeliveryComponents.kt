package petrov.ivan.deliverymobile.components

import android.content.Context
import com.bumptech.glide.RequestManager
import com.squareup.moshi.Moshi
import dagger.Component
import petrov.ivan.deliverymobile.Basket
import petrov.ivan.deliverymobile.modules.BasketModule
import petrov.ivan.deliverymobile.modules.DeliveryModule
import petrov.ivan.deliverymobile.modules.GlideModule
import petrov.ivan.deliverymobile.service.IMobileClientApiRX


@ApplicationScope
@Component(modules = arrayOf(DeliveryModule::class, BasketModule::class, GlideModule::class))
interface DeliveryComponents {
    fun getDeliveryService(): IMobileClientApiRX
    fun getMoshi(): Moshi
    fun getBasket(): Basket
    fun getGlideRequestManager(): RequestManager
    fun getContext(): Context
}