package club.mobile.d21.personalassistant_ver2_kotlin.ui.calendar

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import club.mobile.d21.personalassistant_ver2_kotlin.data.AppDatabase
import club.mobile.d21.personalassistant_ver2_kotlin.data.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class CalendarViewModel(application: Application): AndroidViewModel(application) {
    private val data = AppDatabase.getNoteDatabase(application.applicationContext)
    private val noteDao = data.noteDao()
    private var _note = MutableLiveData<List<Note>>()
    init {
        _note.postValue(noteDao.getNote(LocalDate.now()))
    }
    internal fun addNote(note: Note){
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.addNote(note)
            _note.postValue(noteDao.getNote(note.date))
        }
    }
    internal fun deleteNote(note: Note){
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.deleteNote(note)
            _note.postValue(noteDao.getNote(note.date))
        }
    }
    internal fun postValue(date: LocalDate){
        _note.postValue(noteDao.getNote(date))
    }
    internal fun postValueForThis(startDate: LocalDate, endDate: LocalDate){
        _note.postValue(noteDao.getNotesFromOneDayToOtherDay(startDate,endDate))
    }
    val note: LiveData<List<Note>> = _note
}