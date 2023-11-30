package club.mobile.d21.personalassistant_ver2_kotlin.data

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.time.LocalDate
import java.time.LocalTime

@ProvidedTypeConverter
class Converters {
    @TypeConverter
    fun fromLocalTime(localTime: LocalTime?): String? {
        return localTime?.toString()
    }
    @TypeConverter
    fun toLocalTime(timeString: String?): LocalTime? {
        return timeString?.let { LocalTime.parse(it) }
    }
    @TypeConverter
    fun fromLocalDate(localDate: LocalDate?): String? {
        return localDate?.toString()
    }
    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? {
        return dateString?.let { LocalDate.parse(it) }
    }
    @TypeConverter
    fun fromSubtasksList(subtasks: List<Subtask>?): String {
        // Chuyển List<Subtask> thành một chuỗi JSON để lưu trữ trong cơ sở dữ liệu
        return Gson().toJson(subtasks)
    }

    @TypeConverter
    fun toSubtasksList(subtasksString: String?): List<Subtask>? {
        // Chuyển chuỗi JSON thành List<Subtask> khi đọc từ cơ sở dữ liệu
        val type = object : TypeToken<List<Subtask>>() {}.type
        return Gson().fromJson(subtasksString, type)
    }
}
