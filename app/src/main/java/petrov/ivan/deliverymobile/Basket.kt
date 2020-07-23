package petrov.ivan.deliverymobile

import android.content.Context
import io.reactivex.Flowable
import petrov.ivan.deliverymobile.data.Product
import petrov.ivan.deliverymobile.database.BasketDatabase
import petrov.ivan.deliverymobile.database.ProductDB

class Basket(val context: Context) {
    private val database = BasketDatabase.invoke(context)

    fun getProducts(): Flowable<List<ProductDB>?> {
        return database.basketDatabaseDao.getAllRecords()
    }

    fun addProduct(product: Product) {
        val count = (database.basketDatabaseDao.getById(product.idx)?.count ?: 0) + 1
        database.basketDatabaseDao.insert(ProductDB(product.idx, product.name, product.description, product.ingredients, product.imgUrl, product.cost, count))
    }

    fun updateProduct(product: Product, count: Int = 1) {
        database.basketDatabaseDao.insert(ProductDB(product.idx, product.name, product.description, product.ingredients, product.imgUrl, product.cost, count))
    }

    fun deleteProduct(idx: Int) {
        database.basketDatabaseDao.delete(idx)
    }

    fun deleteAllProducts() {
        database.basketDatabaseDao.deleteAll()
    }
}