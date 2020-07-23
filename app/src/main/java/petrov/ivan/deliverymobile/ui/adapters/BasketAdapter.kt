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
import kotlinx.android.synthetic.main.item_basket_adapter.view.*
import petrov.ivan.deliverymobile.R
import petrov.ivan.deliverymobile.data.ProductInBasket
import petrov.ivan.deliverymobile.presentation.presenter.delivery.IAdapterBasket
import petrov.ivan.deliverymobile.utils.toPx
import java.math.BigDecimal


class BasketAdapter(val presenter: IAdapterBasket, private val requestManager: RequestManager) : RecyclerView.Adapter<BasketAdapter.ViewHolder>() {
    var items: List<ProductInBasket> = ArrayList()
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

        holder.view.btnMinus.setOnClickListener {
            presenter.reduceCount(item.product, item.count)
        }

        holder.view.btnPlus.setOnClickListener {
            presenter.addCount(item.product, item.count)
        }

        holder.bind(item)
    }

    class ViewHolder private constructor(val view: View, private val requestManager: RequestManager) : RecyclerView.ViewHolder(view) {
        fun bind(product: ProductInBasket) {
            with(product.product) {
                view.tvTitle.text = this.name
                view.tvDescription.text = this.description
                view.tvCost.text =
                    String.format(view.resources.getString(R.string.btn_price_fixed_text), (this.cost.multiply(
                        BigDecimal(product.count))).toString())
                loadImage(this.imgUrl)
            }
            view.tvCount.text = product.count.toString()
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
                val view = layoutInflater.inflate(R.layout.item_basket_adapter, parent, false)
                return ViewHolder(view, requestManager)
            }
        }
    }
}
