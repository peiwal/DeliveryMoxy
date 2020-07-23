package petrov.ivan.deliverymobile.application

import android.app.Application
import petrov.ivan.deliverymobile.components.DaggerDeliveryComponents
import petrov.ivan.deliverymobile.components.DeliveryComponents
import petrov.ivan.deliverymobile.modules.ContextModule
import timber.log.Timber

class ClientApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

    private val deliveryComponents: DeliveryComponents by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerDeliveryComponents.builder()
            .contextModule(ContextModule(this))
            .build()
    }

    fun getDeliveryComponent() : DeliveryComponents {
        return deliveryComponents
    }
}