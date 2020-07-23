package petrov.ivan.deliverymobile.ui.fragment.delivery.basket.features

import dagger.Component
import petrov.ivan.deliverymobile.components.DeliveryComponents
import petrov.ivan.deliverymobile.ui.adapters.BasketAdapter

@FragmentBasketScope
@Component(dependencies = arrayOf(DeliveryComponents::class), modules = arrayOf(FragmentBasketModule::class))
interface FragmentBasketComponent {
    fun getAdapter(): BasketAdapter
}