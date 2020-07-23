package petrov.ivan.deliverymobile.ui.fragment.delivery.products.features

import dagger.Component
import petrov.ivan.deliverymobile.components.DeliveryComponents
import petrov.ivan.deliverymobile.ui.fragment.delivery.products.ProductsFragment

@FragmentProductScope
@Component(dependencies = arrayOf(DeliveryComponents::class), modules = arrayOf(FragmentProductsModule::class))
interface FragmentProductsComponent {
    fun injectProductsFragment(productsFragment: ProductsFragment)
}