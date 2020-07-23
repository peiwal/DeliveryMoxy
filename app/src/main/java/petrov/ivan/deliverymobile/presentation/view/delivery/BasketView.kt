package petrov.ivan.deliverymobile.presentation.view.delivery

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import petrov.ivan.deliverymobile.data.ProductInBasket
import java.math.BigDecimal

@StateStrategyType(AddToEndStrategy::class)
interface BasketView : MvpView {
    fun showProducts(products: List<ProductInBasket>)
    fun isEmptyBasket(isEmpty: Boolean)
    fun orderCreated()
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showError(message: String?)
    fun updateFullCost(cost: BigDecimal)
}
