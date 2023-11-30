package club.mobile.d21.personalassistant_ver2_kotlin.data

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "note")
data class Note(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "date") var date: LocalDate,
    @ColumnInfo(name = "time") var time: LocalTime
)
@Dao
interface NoteDao{
    @Query("SELECT * FROM note WHERE date = :date ORDER BY time ASC")
    fun getNote(date: LocalDate): List<Note>

    @Query("SELECT * FROM note WHERE date BETWEEN :startDate AND :endDate ORDER BY time ASC")
    fun getNotesFromOneDayToOtherDay(startDate: LocalDate, endDate: LocalDate): List<Note>

    @Delete
    fun deleteNote(oldNote: Note)

    @Insert
    fun addNote(newNote: Note)
}