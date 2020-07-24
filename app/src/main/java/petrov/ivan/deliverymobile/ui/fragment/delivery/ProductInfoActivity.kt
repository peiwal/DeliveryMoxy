package petrov.ivan.deliverymobile.ui.fragment.delivery

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.android.synthetic.main.activity_product_info.*
import petrov.ivan.deliverymobile.Basket
import petrov.ivan.deliverymobile.R
import petrov.ivan.deliverymobile.data.Product
import petrov.ivan.deliverymobile.presentation.presenter.delivery.ProductInfoPresenter
import petrov.ivan.deliverymobile.presentation.view.delivery.ProductInfoView
import petrov.ivan.deliverymobile.ui.MainActivity.Companion.PRODUCT_RESULT_OPEN_BASKET
import petrov.ivan.deliverymobile.ui.base.BaseActivity
import petrov.ivan.deliverymobile.utils.BigDecimalAdapter
import petrov.ivan.deliverymobile.utils.toPx

class ProductInfoActivity : BaseActivity(), ProductInfoView {
    private lateinit var product: Product

    private val basket: Basket by lazy(mode = LazyThreadSafetyMode.NONE) {
        deliveryComponents.getBasket()
    }

    @InjectPresenter
    lateinit var presenter: ProductInfoPresenter

    @ProvidePresenter
    fun providePresenter(): ProductInfoPresenter = ProductInfoPresenter(basket)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_info)

        intent.extras?.let {
            if (it.containsKey(PARAM_PRODUCT)) {
                it.getString(PARAM_PRODUCT)?.let {
                    product = getProductAdapter().fromJson(it)!!
                }
            }
        }

        fabBack.setOnClickListener {
            finish()
        }

        fabBasket.setOnClickListener {
            setResult(PRODUCT_RESULT_OPEN_BASKET)
            finish()
        }

        presenter.showProduct(product)
    }

    override fun showProduct(product: Product) {
        deliveryComponents.getGlideRequestManager()
            .load(product.imgUrl)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.ic_pizza)
            .apply(RequestOptions().apply {
                transform(CenterCrop(), RoundedCorners(resources.getInteger(R.integer.product_image_corner).toPx()))
            })
            .into(imageView)

        tvName.text = product.name
        tvDescription.text = product.description
        tvIngredients.text = product.ingredients

        btnAddToBasket.text = String.format(getString(R.string.product_info_add_basket), product.cost)

        btnAddToBasket.setOnClickListener {
            presenter.addToBasket(product)
        }
    }

    override fun succesAddToBusket() {
        Snackbar.make(btnAddToBasket, getString(R.string.product_info_succes_add), Snackbar.LENGTH_LONG).show()
    }

    override fun showError(errorCode: Int) {
        Snackbar.make(btnAddToBasket, getString(errorCode), Snackbar.LENGTH_LONG).show()
    }

    companion object {
        const val TAG = "ProductInfoActivity"
        const val PARAM_PRODUCT = "param_product"

        fun getProductAdapter(): JsonAdapter<Product> {
            val moshi = Moshi.Builder()
                .add(BigDecimalAdapter)
                .build()
            return moshi.adapter(Product::class.java)
        }

        fun startActivityForResult(activity: Activity, requestCode: Int, product: Product) {
            val intent = Intent(activity, ProductInfoActivity::class.java)
            intent.putExtra(PARAM_PRODUCT, getProductAdapter().toJson(product))
            activity.startActivityForResult(intent, requestCode)
        }
    }
}
