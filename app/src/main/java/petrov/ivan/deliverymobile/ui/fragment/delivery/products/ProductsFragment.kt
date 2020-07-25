package petrov.ivan.deliverymobile.ui.fragment.delivery.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_products.*
import petrov.delivery.webapi.CategoryWithProductList
import petrov.delivery.webapi.Product
import petrov.ivan.deliverymobile.R
import petrov.ivan.deliverymobile.presentation.presenter.delivery.ProductsPresenter
import petrov.ivan.deliverymobile.presentation.view.delivery.ProductsView
import petrov.ivan.deliverymobile.service.IMobileClientApiRX
import petrov.ivan.deliverymobile.ui.MainActivity.Companion.PRODUCT_INFO_REQUEST_CODE
import petrov.ivan.deliverymobile.ui.adapters.CenterLayoutManager
import petrov.ivan.deliverymobile.ui.adapters.ProductCategoryAdapter
import petrov.ivan.deliverymobile.ui.adapters.ProductsAdapter
import petrov.ivan.deliverymobile.ui.base.BaseFragment
import petrov.ivan.deliverymobile.ui.fragment.delivery.ProductInfoActivity
import petrov.ivan.deliverymobile.ui.fragment.delivery.products.features.DaggerFragmentProductsComponent
import petrov.ivan.deliverymobile.ui.fragment.delivery.products.features.FragmentProductsComponent
import petrov.ivan.deliverymobile.ui.fragment.delivery.products.features.FragmentProductsModule
import javax.inject.Inject

class ProductsFragment : BaseFragment(), ProductsView {
    private val deliveryService: IMobileClientApiRX by lazy(mode = LazyThreadSafetyMode.NONE) {
        deliveryComponents.getDeliveryService()
    }

    @InjectPresenter
    lateinit var presenter: ProductsPresenter

    @ProvidePresenter
    fun providePresenter(): ProductsPresenter = ProductsPresenter(deliveryService)

    val productsComponent: FragmentProductsComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerFragmentProductsComponent.builder()
            .deliveryComponents(deliveryComponents)
            .fragmentProductsModule(FragmentProductsModule(presenter))
            .build()
    }

    @Inject
    lateinit var adapterCategory: ProductCategoryAdapter
    @Inject
    lateinit var adapterProduct: ProductsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_products, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productsComponent.injectProductsFragment(this)

        rvCategoryes.layoutManager = CenterLayoutManager(application, LinearLayout.HORIZONTAL, false)
        rvCategoryes.adapter = adapterCategory

        recyclerView.adapter = adapterProduct

        swipeRefreshLayout.setOnRefreshListener {
            presenter.loadProducts(true)
        }

        presenter.loadProducts()
    }

    override fun showCategoryes(categoryes: List<CategoryWithProductList>) {
        adapterCategory.items = categoryes
    }

    override fun showProducts(products: List<Product>?) {
        adapterProduct.items = products  ?: ArrayList()
    }

    override fun showError(errorCode: Int) {
        Snackbar.make(recyclerView, getString(errorCode), Snackbar.LENGTH_LONG).show()
    }

    override fun showRefresh(isRefreshing: Boolean) {
        swipeRefreshLayout.isRefreshing = isRefreshing
    }

    override fun chooseCategory(position: Int) {
        rvCategoryes.smoothScrollToPosition(position)
        adapterCategory.notifyDataSetChanged()
    }

    override fun showFormProductInfo(product: Product) {
        ProductInfoActivity.startActivityForResult(
            activity!!,
            PRODUCT_INFO_REQUEST_CODE,
            product
        )
    }
}
