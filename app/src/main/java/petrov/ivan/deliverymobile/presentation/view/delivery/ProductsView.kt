package petrov.ivan.deliverymobile.presentation.view.delivery

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import petrov.delivery.webapi.CategoryWithProductList
import petrov.delivery.webapi.Product

@StateStrategyType(OneExecutionStateStrategy::class)
interface ProductsView : MvpView {

    fun showError(errorCode: Int)

    fun showCategoryes(categoryes: List<CategoryWithProductList>)

    fun showProducts(products: List<Product>?)

    fun showRefresh(isRefreshing: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun chooseCategory(position: Int)

    fun showFormProductInfo(product: Product)
}
