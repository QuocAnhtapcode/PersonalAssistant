package club.mobile.d21.personalassistant_ver2_kotlin.data

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import java.time.LocalTime

@Entity(tableName = "dailyTask")
data class DailyTask(
    @PrimaryKey(autoGenerate = true) var id:Int,
    @ColumnInfo(name = "title") var name: String,
    @ColumnInfo(name = "time") var time: LocalTime,
    @ColumnInfo("description") var description:String?,
    @ColumnInfo("status") var status:Boolean = false
)
@Dao
interface DailyTaskDao{
    @Query("SELECT * FROM dailyTask ORDER BY time ASC")
    fun getAll():List<DailyTask>

    @Query("SELECT COUNT(*) FROM dailyTask")
    fun getSize(): Int

    @Query("UPDATE dailyTask SET status = 1 WHERE id = :taskId")
    fun done(taskId:Int)
    @Query("UPDATE dailyTask SET status = 0 WHERE id = :taskId")
    fun undone(taskId:Int)

    @Query("DELETE FROM dailyTask")
    fun clearAll()

    @Insert
    fun addDailyTask(newDailyTask: DailyTask)

    @Delete
    fun deleteDailyTask(oldDailyTask: DailyTask)

    @Update
    fun updateDailyTask(task: DailyTask)

    @Update
    fun updateDailyTasks(tasks: List<DailyTask>)

}