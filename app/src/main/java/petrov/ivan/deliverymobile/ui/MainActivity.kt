package petrov.ivan.deliverymobile.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.activity_main.*
import petrov.ivan.deliverymobile.Basket
import petrov.ivan.deliverymobile.R
import petrov.ivan.deliverymobile.presentation.presenter.delivery.MainActivityPresenter
import petrov.ivan.deliverymobile.presentation.view.delivery.MainActivityView
import petrov.ivan.deliverymobile.ui.base.BaseActivity


class MainActivity : BaseActivity(), MainActivityView {

    private val basket: Basket by lazy(mode = LazyThreadSafetyMode.NONE) {
        deliveryComponents.getBasket()
    }

    @InjectPresenter
    lateinit var presenter: MainActivityPresenter

    @ProvidePresenter
    fun providePresenter(): MainActivityPresenter = MainActivityPresenter(basket)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.nav_host_fragment)
        nav_view.setupWithNavController(navController)
        presenter.observeBasketCount()
    }

    override fun updateBasketCount(count: Int) {
        nav_view.getOrCreateBadge(R.id.navigation_basket).let {
            it.isVisible = count > 0
            it.number = count
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PRODUCT_INFO_REQUEST_CODE) {
            if (resultCode == PRODUCT_RESULT_OPEN_BASKET) {
                openBasket()
            }
        }
    }

    private fun openBasket() {
        val view = nav_view.findViewById<View>(R.id.navigation_basket)
        view.performClick()
    }

    private fun openMenu() {
        val view = nav_view.findViewById<View>(R.id.navigation_home)
        view.performClick()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent!= null && intent.getBooleanExtra(PARAM_OPEN_MENU, false)) {
            openMenu()
        }
    }

    companion object {
        const val PRODUCT_INFO_REQUEST_CODE = 100
        const val PRODUCT_RESULT_OPEN_BASKET = 1
        const val PARAM_OPEN_MENU = "param_open_menu"
    }
}
