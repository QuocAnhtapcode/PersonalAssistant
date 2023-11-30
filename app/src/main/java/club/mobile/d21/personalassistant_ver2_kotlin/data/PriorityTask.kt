package club.mobile.d21.personalassistant_ver2_kotlin.data

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import java.time.LocalDate
import java.time.LocalTime

enum class Priority{
    CRITICAL,
    IMPORTANT,
    LOW_PRIORITY
}
enum class Category{
    PERSONAL,
    HEALTH,
    WORK,
    EDUCATION,
    FAMILY,
    FRIEND,
    ENTERTAINMENT,
    FINANCIAL_INDEPENDENCE,
    SKILL_DEVELOPMENT,
    OTHER
}
data class Subtask(
    var title: String,
    var status: Boolean = false
)
@Entity(tableName = "priorityTask")
data class PriorityTask(
    @PrimaryKey(autoGenerate = true) var id:Int,
    @ColumnInfo(name = "title") var name:String,
    @ColumnInfo(name = "category") var category: Category,
    @ColumnInfo(name = "startDate") var startDate: LocalDate,
    @ColumnInfo(name = "endDate") var endDate: LocalDate,
    @ColumnInfo(name = "startTime") var startTime: LocalTime,
    @ColumnInfo(name = "endTime") var endTime: LocalTime,
    @ColumnInfo(name = "priority") var priority: Priority,
    @ColumnInfo(name = "subtask") var subtasks: List<Subtask> = listOf(),
    @ColumnInfo("status") var status:Boolean = false
)
@Dao
interface PriorityTaskDao{
    @Query("SELECT * FROM priorityTask")
    fun getAll():List<PriorityTask>

    @Query("SELECT * FROM priorityTask WHERE category = :category ORDER BY priority DESC")
    fun getPersonalTasks(category: Category = Category.PERSONAL): List<PriorityTask>
    @Query("SELECT * FROM priorityTask WHERE category = :category ORDER BY priority DESC")
    fun getHealthTasks(category: Category = Category.HEALTH): List<PriorityTask>
    @Query("SELECT * FROM priorityTask WHERE category = :category ORDER BY priority DESC")
    fun getWorkTasks(category: Category = Category.WORK): List<PriorityTask>
    @Query("SELECT * FROM priorityTask WHERE category = :category ORDER BY priority DESC")
    fun getEducationTasks(category: Category = Category.EDUCATION): List<PriorityTask>
    @Query("SELECT * FROM priorityTask WHERE category = :category ORDER BY priority DESC")
    fun getFamilyTasks(category: Category = Category.FAMILY): List<PriorityTask>
    @Query("SELECT * FROM priorityTask WHERE category = :category ORDER BY priority DESC")
    fun getFriendTasks(category: Category = Category.FRIEND): List<PriorityTask>
    @Query("SELECT * FROM priorityTask WHERE category = :category ORDER BY priority DESC")
    fun getEntertainmentTasks(category: Category = Category.ENTERTAINMENT): List<PriorityTask>
    @Query("SELECT * FROM priorityTask WHERE category = :category ORDER BY priority DESC")
    fun getFinancialIndependenceTasks(category: Category = Category.FINANCIAL_INDEPENDENCE): List<PriorityTask>
    @Query("SELECT * FROM priorityTask WHERE category = :category ORDER BY priority DESC")
    fun getSkillDevelopmentTasks(category: Category = Category.SKILL_DEVELOPMENT): List<PriorityTask>
    @Query("SELECT * FROM priorityTask WHERE category = :category ORDER BY priority DESC")
    fun getOtherTasks(category: Category = Category.OTHER): List<PriorityTask>

    @Query("SELECT COUNT(*) FROM priorityTask")
    fun getSize(): Int

    @Query("DELETE FROM priorityTask")
    fun clearAll()

    @Insert
    fun addPriorityTask(newTask: PriorityTask)

    @Delete
    fun deletePriorityTask(oldTask: PriorityTask)

    @Query("SELECT * FROM priorityTask WHERE id = :taskId")
    fun getPriorityTaskById(taskId: Int): PriorityTask

    @Transaction
    fun doneSubtask(taskId: Int, updatedSubtask: Subtask) {
        val priorityTask = getPriorityTaskById(taskId)
        val subtasks = priorityTask.subtasks.toMutableList()
        val indexOfUpdated = subtasks.indexOfFirst { it.title == updatedSubtask.title }
        subtasks[indexOfUpdated].status = true
        updatePriorityTask(priorityTask)
        // Nếu tất cả subtask đều hoàn thành, đặt trạng thái của priorityTask là done
        priorityTask.status = subtasks.all { it.status }
        updatePriorityTask(priorityTask)
    }
    @Transaction
    fun undoneSubtask(taskId: Int, updatedSubtask: Subtask) {
        val priorityTask = getPriorityTaskById(taskId)
        val subtasks = priorityTask.subtasks.toMutableList()
        val indexOfUpdated = subtasks.indexOfFirst { it.title == updatedSubtask.title }
        subtasks[indexOfUpdated].status = false
        // Nếu có ít nhất một subtask chưa hoàn thành, đặt trạng thái của priorityTask là undone
        priorityTask.status = subtasks.all { it.status }
        updatePriorityTask(priorityTask)
    }

    @Transaction
    fun addSubTask(taskId: Int, newSubtask: Subtask) {
        val priorityTask = getPriorityTaskById(taskId)
        val subtasks = priorityTask.subtasks.toMutableList()
        subtasks.add(newSubtask)
        priorityTask.subtasks = subtasks
        priorityTask.status = subtasks.all { it.status }
        updatePriorityTask(priorityTask)
    }
    @Transaction
    fun deleteSubTask(taskId: Int, newSubtask: Subtask) {
        val priorityTask = getPriorityTaskById(taskId)
        val subtasks = priorityTask.subtasks.toMutableList()
        subtasks.remove(newSubtask)
        priorityTask.subtasks = subtasks
        priorityTask.status = subtasks.all { it.status }
        updatePriorityTask(priorityTask)
    }
    @Update
    fun updatePriorityTask(priorityTask: PriorityTask)
}