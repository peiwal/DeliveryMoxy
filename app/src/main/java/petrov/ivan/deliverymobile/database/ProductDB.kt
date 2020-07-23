package petrov.ivan.deliverymobile.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "basket_product")
data class ProductDB(
    @PrimaryKey(autoGenerate = false)
    var idx: Int,
    val name: String,
    val description: String,
    val ingredients: String,
    val imgUrl: String,
    val cost: BigDecimal,
    val count: Int
)
