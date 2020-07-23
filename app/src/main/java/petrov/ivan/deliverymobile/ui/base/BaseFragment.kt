package petrov.ivan.deliverymobile.ui.base

import android.content.Context
import com.arellomobile.mvp.MvpAppCompatFragment
import petrov.ivan.deliverymobile.application.ClientApplication
import petrov.ivan.deliverymobile.components.DeliveryComponents

abstract class BaseFragment : MvpAppCompatFragment() {

    protected lateinit var application: ClientApplication

    protected val deliveryComponents: DeliveryComponents by lazy(mode = LazyThreadSafetyMode.NONE) {
        application.getDeliveryComponent()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        application = requireNotNull(this.activity).application as ClientApplication
    }
}