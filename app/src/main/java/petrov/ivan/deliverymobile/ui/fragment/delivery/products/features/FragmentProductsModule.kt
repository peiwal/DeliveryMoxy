package petrov.ivan.deliverymobile.ui.fragment.delivery.products.features

import com.bumptech.glide.RequestManager
import dagger.Module
import dagger.Provides
import petrov.ivan.deliverymobile.presentation.presenter.delivery.ProductsPresenter
import petrov.ivan.deliverymobile.ui.adapters.ProductCategoryAdapter
import petrov.ivan.deliverymobile.ui.adapters.ProductsAdapter

@Module
class FragmentProductsModule(private val presenter: ProductsPresenter) {

    @FragmentProductScope
    @Provides
    fun categoryAdapter(): ProductCategoryAdapter {
        return ProductCategoryAdapter(presenter)
    }

    @FragmentProductScope
    @Provides
    fun productsAdapter(requestManager: RequestManager): ProductsAdapter {
        return ProductsAdapter(presenter, requestManager)
    }
}
