package petrov.ivan.deliverymobile.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import petrov.ivan.deliverymobile.utils.BigDecimalConverter

@TypeConverters(BigDecimalConverter::class)
@Database(entities = [ProductDB::class], version = 3,  exportSchema = false)
abstract class BasketDatabase : RoomDatabase() {
    abstract val basketDatabaseDao: BasketDatabaseDao

    companion object {

        @Volatile private var instance: BasketDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            BasketDatabase::class.java,
            "basket_database")
            .fallbackToDestructiveMigration()
            .build()
    }
}