package petrov.ivan.deliverymobile.presentation.presenter.delivery

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import petrov.ivan.deliverymobile.AppConstants.CONNECTION_TIMEOUT_SEC
import petrov.ivan.deliverymobile.data.CategoryWithProductList
import petrov.ivan.deliverymobile.data.ParamRespProduct
import petrov.ivan.deliverymobile.data.Product
import petrov.ivan.deliverymobile.presentation.view.delivery.ProductsView
import petrov.ivan.deliverymobile.service.RestApi
import petrov.ivan.deliverymobile.ui.adapters.IAdapterCategory
import petrov.ivan.deliverymobile.ui.adapters.IAdapterProduct
import java.math.BigDecimal
import java.util.concurrent.TimeUnit

@InjectViewState
class ProductsPresenter(val restApi: RestApi) : MvpPresenter<ProductsView>(), IAdapterCategory, IAdapterProduct {
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

        // output on Model
        val request2 = Single.just(
            ParamRespProduct(
                listOf(
                    CategoryWithProductList(1, "Роллы",
                        listOf(Product(1, "Пицца", "Четыре сыра", "Тесто, сыр моцарела, сыр пармезан, сыр рикота, сыр фета",
                        "https://i.imgur.com/J9osDN7.jpeg", BigDecimal(370))
                                , Product(2, "King Бургер", "Сочный бургер, приготовленный на огне", "Тесто, говядина, помидор, соленый огурец, сырный соус",
                            "https://i.imgur.com/Oo543iV.jpeg", BigDecimal(180)),
                            Product(3, "Пицца3", "Четыре сыра", "Тесто, сыр моцарела, сыр пармезан, сыр рикота, сыр фета",
                                "https://i.imgur.com/J9osDN7.jpeg", BigDecimal(370))
                            , Product(4, "King Бургер4", "Сочный бургер, приготовленный на огне", "Тесто, говядина, помидор, соленый огурец, сырный соус",
                                "https://i.imgur.com/Oo543iV.jpeg", BigDecimal(180)),
                            Product(5, "Пицца5", "Четыре сыра", "Тесто, сыр моцарела, сыр пармезан, сыр рикота, сыр фета",
                                "https://i.imgur.com/J9osDN7.jpeg", BigDecimal(370))
                            , Product(6, "King Бургер6", "Сочный бургер, приготовленный на огне", "Тесто, говядина, помидор, соленый огурец, сырный соус",
                                "https://i.imgur.com/Oo543iV.jpeg", BigDecimal(180))
                        )
                    ),
                    CategoryWithProductList(2, "Пицца",
                        listOf(Product(3, "Шаурма", "Вкуснейшая куриная шаурма", "Курица, морковь, капуста, лук, перец",
                        "https://i.imgur.com/J9osDN7.jpeg", BigDecimal(140))
                        )
                    )
                )
            )
        )

        // todo rest
//        val request= restApi.getProducts(application.getString(R.string.response_language))
        val disposable = request2.subscribeOn(Schedulers.newThread())
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
                    viewState.showError(null)
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
