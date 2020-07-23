package petrov.ivan.deliverymobile.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable

@Dao
interface BasketDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(product: ProductDB)

    @Query("DELETE from basket_product WHERE idx = :idx ")
    fun delete(idx: Int)

    @Query("DELETE from basket_product")
    fun deleteAll()

    @Query("SELECT * from basket_product ORDER BY name desc")
    fun getAllRecords(): Flowable<List<ProductDB>?>

    @Query("SELECT * from basket_product WHERE idx = :idx")
    fun getById(idx: Int): ProductDB?
}