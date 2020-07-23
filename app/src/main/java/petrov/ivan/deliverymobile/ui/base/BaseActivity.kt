package petrov.ivan.deliverymobile.ui.base

import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import petrov.ivan.deliverymobile.application.ClientApplication
import petrov.ivan.deliverymobile.components.DeliveryComponents

abstract class BaseActivity: MvpAppCompatActivity() {
    protected lateinit var application: ClientApplication

    protected val deliveryComponents: DeliveryComponents by lazy(mode = LazyThreadSafetyMode.NONE) {
        application.getDeliveryComponent()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        application = getApplication() as ClientApplication
        super.onCreate(savedInstanceState)
    }
}