package club.mobile.d21.personalassistant_ver2_kotlin.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import club.mobile.d21.personalassistant_ver2_kotlin.data.AppDatabase
import club.mobile.d21.personalassistant_ver2_kotlin.data.DailyTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalTime

class HomeViewModel(application: Application): AndroidViewModel(application) {
    private val context = application.applicationContext
    private val data = AppDatabase.getDailyTaskDatabase(context)
    private val dailyTaskDao = data.dailyTaskDao()
    private val _dailyTask = MutableLiveData<List<DailyTask>>()
    init{
        viewModelScope.launch(Dispatchers.IO) {
            val defaultDailyTask = DailyTask(0,"Brush your teeth",
                LocalTime.now(),"Just brush your teeth")
            if(dailyTaskDao.getSize()==0){
                dailyTaskDao.addDailyTask(defaultDailyTask)
            }
            _dailyTask.postValue(dailyTaskDao.getAll())
        }
    }
    internal fun addDailyTask(newDailyTask:DailyTask){
        viewModelScope.launch(Dispatchers.IO){
            dailyTaskDao.addDailyTask(newDailyTask);
            _dailyTask.postValue(dailyTaskDao.getAll())
        }
    }
    internal fun done(id: Int){
        viewModelScope.launch(Dispatchers.IO){
            dailyTaskDao.done(id)
            _dailyTask.postValue(dailyTaskDao.getAll())
        }
    }
    internal fun undone(id: Int){
        viewModelScope.launch(Dispatchers.IO){
            dailyTaskDao.undone(id)
            _dailyTask.postValue(dailyTaskDao.getAll())
        }
    }
    internal fun delete(oldDailyTask:DailyTask){
        viewModelScope.launch(Dispatchers.IO){
            dailyTaskDao.deleteDailyTask(oldDailyTask)
            _dailyTask.postValue(dailyTaskDao.getAll())
        }
    }
    fun updateDailyTaskStatus() {
        val dailyTasks = dailyTaskDao.getAll()
        for (task in dailyTasks) {
            task.status = false
        }
        dailyTaskDao.updateDailyTasks(dailyTasks)
        _dailyTask.postValue(dailyTaskDao.getAll())
    }
    val dailyTask: LiveData<List<DailyTask>> = _dailyTask

}