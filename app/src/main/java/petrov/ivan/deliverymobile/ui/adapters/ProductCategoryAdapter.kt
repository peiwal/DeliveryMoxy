package petrov.ivan.deliverymobile.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_product_category.view.*
import petrov.ivan.deliverymobile.R
import petrov.ivan.deliverymobile.data.CategoryWithProductList
import java.util.*

class ProductCategoryAdapter(private val presenter: IAdapterCategory) : RecyclerView.Adapter<ProductCategoryAdapter.ViewHolder>() {
    var items: List<CategoryWithProductList> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.get(position)
        holder.view.setOnClickListener {
            presenter.itemCategoryTouch(item.idx, position)
        }
        holder.bind(item, presenter.getChoosedIdx()==item.idx)
    }

    class ViewHolder private constructor(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(categoryWithProductList: CategoryWithProductList, isChoosed: Boolean) {
            view.tvName.setBackgroundResource(
                if (isChoosed) R.drawable.backgrkoud_category_selected
                else R.drawable.backgrkoud_category
            )

            view.tvName.text = categoryWithProductList.name
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.item_product_category, parent, false)
                return ViewHolder(view)
            }
        }
    }
}

interface IAdapterCategory {
    fun itemCategoryTouch(idx: Int, position: Int)
    fun getChoosedIdx(): Int
}
