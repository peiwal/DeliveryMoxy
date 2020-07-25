package petrov.ivan.deliverymobile.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.item_product_adapter.view.*
import petrov.delivery.webapi.Product
import petrov.ivan.deliverymobile.R
import petrov.ivan.deliverymobile.utils.toPx
import java.util.concurrent.TimeUnit


class ProductsAdapter(private val presenter: IAdapterProduct, private val requestManager: RequestManager) : RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {
    var items: List<Product> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, requestManager)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.get(position)
        holder.bind(item)
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val item = items.get(holder.adapterPosition)
        holder.disposable = Observable.merge(holder.view.btnCost.clicks().map { item },
            holder.view.contentView.clicks().map { item }
        )
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { product: Product ->
                show(product)
            }.apply {
                presenter.addDisposable(this)
            }
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.disposable?.let {
            presenter.removeDisposable(it)
            it.dispose()
        }
    }

    private fun show(product: Product) {
        presenter.itemProductTouch(product)
    }

    class ViewHolder private constructor(val view: View, private val requestManager: RequestManager) : RecyclerView.ViewHolder(view) {
        var disposable: Disposable? = null

        fun bind(product: Product) {
            view.tvTitle.text = product.name
            view.tvDescription.text = product.description
            view.btnCost.text = String.format(view.resources.getString(R.string.btn_price_text), product.cost)

            loadImage(product.imgUrl)
        }

        private fun loadImage(url: String) {
            requestManager
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.ic_pizza)
                .apply(RequestOptions().apply {
                    transform(CenterCrop(), RoundedCorners(view.resources.getInteger(R.integer.product_image_corner).toPx()))
                })
                .into(view.imageView)
        }

        companion object {
            fun from(parent: ViewGroup, requestManager: RequestManager): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.item_product_adapter, parent, false)
                return ViewHolder(view, requestManager)
            }
        }
    }
}

interface IAdapterProduct {
    fun itemProductTouch(product: Product)
    fun addDisposable(disposable: Disposable)
    fun removeDisposable(disposable: Disposable)
}
