package club.mobile.d21.personalassistant_ver2_kotlin.ui.calendar

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.activityViewModels
import club.mobile.d21.personalassistant_ver2_kotlin.data.DailyTask
import club.mobile.d21.personalassistant_ver2_kotlin.data.Note
import club.mobile.d21.personalassistant_ver2_kotlin.databinding.BottomSheetAddNoteBinding
import club.mobile.d21.personalassistant_ver2_kotlin.service.NotificationReceiver
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class AddNoteBottomSheet(selectedDate: LocalDate): BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetAddNoteBinding
    private val calendarViewModel: CalendarViewModel by activityViewModels()
    private val selectedDate = selectedDate
    private val formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.setOnShowListener {
            val frameLayout: FrameLayout? =
                dialog?.findViewById(com.google.android.material.R.id.design_bottom_sheet)
            frameLayout?.setBackgroundResource(android.R.color.transparent)
        }
        binding = BottomSheetAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var selectedTime = LocalTime.now()
        binding.chooseTime.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                requireContext(),
                {_,hour,minute->
                    selectedTime = LocalTime.of(hour, minute,0)
                    binding.chooseTime.text = selectedTime.format(formatter)
                },
                selectedTime.hour,
                selectedTime.minute,
                true
            )
            timePickerDialog.show()
        }
        binding.createButton.setOnClickListener {
            if(binding.chooseTime.text.isNullOrEmpty() || binding.chooseNote.text.isNullOrEmpty()){
                return@setOnClickListener
            }
            val newNote = Note(0,
                binding.chooseNote.text.toString(),selectedDate, selectedTime)
            val alarmIntent = Intent(requireContext(), NotificationReceiver::class.java)
            alarmIntent.putExtra("title", "Note for "+newNote.time.format(formatter)+" today")
            alarmIntent.putExtra("description", newNote.title)
            val pendingIntent = PendingIntent.getBroadcast(
                requireContext(),
                newNote.id,
                alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

            val noteYear = newNote.date.year
            val noteMonth = newNote.date.month.value - 1
            val noteDay = newNote.date.dayOfMonth
            val noteHour = newNote.time.hour
            val noteMinute = newNote.time.minute

            val calendar = Calendar.getInstance().apply {
                set(Calendar.YEAR, noteYear)
                set(Calendar.MONTH, noteMonth)
                set(Calendar.DAY_OF_MONTH, noteDay)
                set(Calendar.HOUR_OF_DAY, noteHour)
                set(Calendar.MINUTE, noteMinute)
                set(Calendar.SECOND, 0)
            }
            val alarmManager =
                requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager

            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )

            calendarViewModel.addNote(newNote)
            dismiss()
        }
    }
}