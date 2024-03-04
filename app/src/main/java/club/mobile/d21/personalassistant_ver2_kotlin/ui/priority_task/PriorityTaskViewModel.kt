package club.mobile.d21.personalassistant_ver2_kotlin.ui.priority_task

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import club.mobile.d21.personalassistant_ver2_kotlin.data.AppDatabase
import club.mobile.d21.personalassistant_ver2_kotlin.data.PriorityTask
import club.mobile.d21.personalassistant_ver2_kotlin.data.Subtask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PriorityTaskViewModel(application: Application): AndroidViewModel(application){
    private val data = AppDatabase.getPriorityTaskDatabase(application.applicationContext)
    private val priorityTaskDao = data.priorityTaskDao()

    private val _priorityTask = MutableLiveData<List<PriorityTask>>()
    private val _personalTask = MutableLiveData<List<PriorityTask>>()
    private val _heathTask = MutableLiveData<List<PriorityTask>>()
    private val _workTask = MutableLiveData<List<PriorityTask>>()
    private val _educationTask = MutableLiveData<List<PriorityTask>>()
    private val _familyTask = MutableLiveData<List<PriorityTask>>()
    private val _friendTask = MutableLiveData<List<PriorityTask>>()
    private val _entertainmentTask = MutableLiveData<List<PriorityTask>>()
    private val _financialIndependenceTask = MutableLiveData<List<PriorityTask>>()
    private val _skillDevelopmentTask = MutableLiveData<List<PriorityTask>>()
    private val _otherTask = MutableLiveData<List<PriorityTask>>()

    private val _subtask= MutableLiveData<List<Subtask>>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            postValue()
        }
    }
    fun getSubtasks(priorityTaskId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            // Lấy danh sách subtask của priorityTask với ID được chỉ định
            val subtasks = priorityTaskDao.getPriorityTaskById(priorityTaskId).subtasks
            // Cập nhật giá trị của _subtask
            _subtask.postValue(subtasks)
            postValue()
        }
    }
    internal fun addPriorityTask(newTask: PriorityTask){
        viewModelScope.launch(Dispatchers.IO){
            priorityTaskDao.addPriorityTask(newTask)
            postValue()
        }
    }
    internal fun delete(oldTask: PriorityTask){
        viewModelScope.launch(Dispatchers.IO){
            priorityTaskDao.deletePriorityTask(oldTask)
            postValue()
        }
    }
    internal fun doneSubtask(id: Int, subtask: Subtask){
        viewModelScope.launch(Dispatchers.IO){
            priorityTaskDao.doneSubtask(id,subtask)
            getSubtasks(id)
        }
    }
    internal fun undoneSubtask(id: Int, subtask: Subtask){
        viewModelScope.launch(Dispatchers.IO){
            priorityTaskDao.undoneSubtask(id,subtask)
            getSubtasks(id)
        }
    }
    internal fun addSubtask(id: Int, subtask: Subtask){
        viewModelScope.launch(Dispatchers.IO){
            priorityTaskDao.addSubTask(id,subtask)
            getSubtasks(id)
        }
    }
    internal fun deleteSubtask(id: Int, subtask: Subtask){
        viewModelScope.launch(Dispatchers.IO){
            priorityTaskDao.deleteSubTask(id,subtask)
            getSubtasks(id)
        }
    }
    private fun postValue(){
        _personalTask.postValue(priorityTaskDao.getPersonalTasks())
        _heathTask.postValue(priorityTaskDao.getHealthTasks())
        _workTask.postValue(priorityTaskDao.getWorkTasks())
        _educationTask.postValue(priorityTaskDao.getEducationTasks())
        _familyTask.postValue(priorityTaskDao.getFamilyTasks())
        _friendTask.postValue(priorityTaskDao.getFriendTasks())
        _entertainmentTask.postValue(priorityTaskDao.getEntertainmentTasks())
        _financialIndependenceTask.postValue(priorityTaskDao.getFinancialIndependenceTasks())
        _skillDevelopmentTask.postValue(priorityTaskDao.getSkillDevelopmentTasks())
        _otherTask.postValue(priorityTaskDao.getOtherTasks())
        _priorityTask.postValue(priorityTaskDao.getAll())
    }
    val priorityTask:LiveData<List<PriorityTask>> = _priorityTask
    val personalTask:LiveData<List<PriorityTask>> = _personalTask
    val healthTask:LiveData<List<PriorityTask>> = _heathTask
    val workTask:LiveData<List<PriorityTask>> = _workTask
    val educationTask: LiveData<List<PriorityTask>> = _educationTask
    val familyTask: LiveData<List<PriorityTask>> = _familyTask
    val friendTask: LiveData<List<PriorityTask>> = _friendTask
    val entertainmentTask: LiveData<List<PriorityTask>> = _entertainmentTask
    val financialIndependenceTask: LiveData<List<PriorityTask>> = _financialIndependenceTask
    val skillDevelopmentTask: LiveData<List<PriorityTask>> = _skillDevelopmentTask
    val otherTask: LiveData<List<PriorityTask>> = _otherTask

    val subTask: LiveData<List<Subtask>> = _subtask
}
