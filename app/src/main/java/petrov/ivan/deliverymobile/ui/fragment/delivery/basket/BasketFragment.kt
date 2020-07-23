package petrov.ivan.deliverymobile.ui.fragment.delivery.basket

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_basket.*
import kotlinx.android.synthetic.main.fragment_products.recyclerView
import petrov.ivan.deliverymobile.Basket
import petrov.ivan.deliverymobile.R
import petrov.ivan.deliverymobile.data.ProductInBasket
import petrov.ivan.deliverymobile.presentation.presenter.delivery.BasketPresenter
import petrov.ivan.deliverymobile.presentation.view.delivery.BasketView
import petrov.ivan.deliverymobile.ui.MainActivity
import petrov.ivan.deliverymobile.ui.MainActivity.Companion.PARAM_OPEN_MENU
import petrov.ivan.deliverymobile.ui.adapters.BasketAdapter
import petrov.ivan.deliverymobile.ui.base.BaseFragment
import petrov.ivan.deliverymobile.ui.fragment.delivery.basket.features.DaggerFragmentBasketComponent
import petrov.ivan.deliverymobile.ui.fragment.delivery.basket.features.FragmentBasketComponent
import petrov.ivan.deliverymobile.ui.fragment.delivery.basket.features.FragmentBasketModule
import java.math.BigDecimal

class BasketFragment : BaseFragment(), BasketView {
    private val basket: Basket by lazy(mode = LazyThreadSafetyMode.NONE) {
        deliveryComponents.getBasket()
    }

    @InjectPresenter
    lateinit var presenter: BasketPresenter

    @ProvidePresenter
    fun providePresenter(): BasketPresenter = BasketPresenter(basket)

    val basketComponent: FragmentBasketComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerFragmentBasketComponent.builder()
            .deliveryComponents(deliveryComponents)
            .fragmentBasketModule(FragmentBasketModule(presenter))
            .build()
    }

    private lateinit var adapter: BasketAdapter
    private var dialog: AlertDialog? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_basket, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = basketComponent.getAdapter()
        recyclerView.adapter = adapter

        btnCreateOrder.setOnClickListener {
            presenter.createOrder()
        }

        btnOpenProductMenu.setOnClickListener {
            val intent = Intent(activity, MainActivity::class.java)
            intent.putExtra(PARAM_OPEN_MENU, true)
            startActivity(intent)
        }

        presenter.subscribeChangeFullCost()
        presenter.showProducts()
    }

    override fun showProducts(products: List<ProductInBasket>) {
        adapter.items = products
    }

    override fun updateFullCost(cost: BigDecimal) {
        btnCreateOrder.text = String.format(getString(R.string.basket_create_order), cost)
    }

    override fun showError(message: String?) {
        Snackbar.make(recyclerView, message ?: getString(R.string.error_unknown), Snackbar.LENGTH_LONG).show()
    }

    override fun orderCreated() {
        dialog = AlertDialog.Builder(activity!!)
            .setTitle(getString(R.string.order_created))
            .setCancelable(false)
            .setPositiveButton(R.string.btn_ok) { _: DialogInterface, i: Int ->
                val intent = Intent(activity, MainActivity::class.java)
                intent.putExtra(PARAM_OPEN_MENU, true)
                startActivity(intent)
            }
            .create()
        dialog?.show()
    }

    override fun isEmptyBasket(isEmpty: Boolean) {
        if (isEmpty) {
            rlBasketEmpty.visibility = View.VISIBLE
        } else {
            rlBasketEmpty.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dialog?.dismiss()
    }
}
