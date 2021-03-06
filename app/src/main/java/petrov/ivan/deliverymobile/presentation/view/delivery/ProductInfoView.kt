package petrov.ivan.deliverymobile.presentation.view.delivery

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import petrov.delivery.webapi.Product

@StateStrategyType(OneExecutionStateStrategy::class)
interface ProductInfoView : MvpView {
    fun showProduct(product: Product)
    fun succesAddToBusket()
    fun showError(errorCode: Int)
}
