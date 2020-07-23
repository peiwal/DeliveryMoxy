package petrov.ivan.deliverymobile.utils

import androidx.room.TypeConverter
import java.math.BigDecimal

object BigDecimalConverter {
    @TypeConverter
    @JvmStatic
    fun toString(value: BigDecimal): String {
        return value.toString()
    }

    @TypeConverter
    @JvmStatic
    fun fromString(value: String): BigDecimal {
        return BigDecimal(value)
    }
}