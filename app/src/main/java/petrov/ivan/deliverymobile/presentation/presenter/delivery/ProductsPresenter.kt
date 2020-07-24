package petrov.ivan.deliverymobile.presentation.presenter.delivery

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import petrov.ivan.deliverymobile.AppConstants.CONNECTION_TIMEOUT_SEC
import petrov.ivan.deliverymobile.R
import petrov.ivan.deliverymobile.data.CategoryWithProductList
import petrov.ivan.deliverymobile.data.ParamRespProduct
import petrov.ivan.deliverymobile.data.Product
import petrov.ivan.deliverymobile.presentation.view.delivery.ProductsView
import petrov.ivan.deliverymobile.service.IMobileClientApiRX
import petrov.ivan.deliverymobile.ui.adapters.IAdapterCategory
import petrov.ivan.deliverymobile.ui.adapters.IAdapterProduct
import java.math.BigDecimal
import java.util.concurrent.TimeUnit

@InjectViewState
class ProductsPresenter(val restApi: IMobileClientApiRX) : MvpPresenter<ProductsView>(), IAdapterCategory, IAdapterProduct {
    private val compositeDisposable = CompositeDisposable()
    private var idxChoosedCategory: Int? = null
        set(value) {
            field = value
            showProducts()
        }
    private var respProducts: ParamRespProduct? = null

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    fun loadProducts(force: Boolean = false) {
        if (!force && respProducts != null) {
            viewState.showCategoryes(respProducts!!.results)
            showProducts()
            return
        }

        viewState.showRefresh(true)
        viewState.showProducts(null)

        // todo rest
        val request= restApi.getProducts()
        val disposable = request
            .subscribeOn(Schedulers.newThread())
            .timeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
            .delay(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { resp ->
                    viewState.showRefresh(false)

                    if (idxChoosedCategory == null) {
                        idxChoosedCategory = resp.results.first().idx
                    }

                    respProducts = resp

                    viewState.showCategoryes(resp.results)
                    showProducts()
                },
                { error ->
                    viewState.showRefresh(false)
                    viewState.showError(R.string.error_load_data)
                })
        compositeDisposable.add(disposable)
    }

    fun showProducts() {
        viewState.showProducts(respProducts?.results?.filter { it.idx == idxChoosedCategory }?.first()?.productList)
    }

    override fun itemCategoryTouch(idx: Int, position: Int) {
        idxChoosedCategory = idx
        viewState.chooseCategory(position)
    }

    override fun itemProductTouch(product: Product) {
        viewState.showFormProductInfo(product)
    }

    override fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun removeDisposable(disposable: Disposable) {
        compositeDisposable.remove(disposable)
    }

    override fun getChoosedIdx(): Int {
        return idxChoosedCategory!!
    }
}
