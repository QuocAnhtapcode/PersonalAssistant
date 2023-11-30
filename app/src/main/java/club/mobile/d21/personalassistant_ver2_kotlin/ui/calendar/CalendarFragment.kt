package club.mobile.d21.personalassistant_ver2_kotlin.ui.calendar

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import club.mobile.d21.personalassistant_ver2_kotlin.data.Note
import club.mobile.d21.personalassistant_ver2_kotlin.databinding.FragmentCalendarBinding
import club.mobile.d21.personalassistant_ver2_kotlin.ui.adapter.NoteAdapter
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters

class CalendarFragment : Fragment() {
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private val calendarViewModel: CalendarViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val noteList: RecyclerView = binding.noteList
        var type:String
        val options = arrayOf("Specific dates", "This month", "This year")
        var selectedDate = LocalDate.now()
        val startOfMonth = selectedDate.with(TemporalAdjusters.firstDayOfMonth())
        val endOfMonth = selectedDate.with(TemporalAdjusters.lastDayOfMonth())
        val startOfYear = selectedDate.with(TemporalAdjusters.firstDayOfYear())
        val endOfYear = selectedDate.with(TemporalAdjusters.lastDayOfYear())
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.chooseType.adapter = adapter
        binding.chooseType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                type = options[position]
                when (type) {
                    "Specific dates" -> {
                        calendarViewModel.postValue(selectedDate)
                        binding.addButton.visibility = View.VISIBLE
                        binding.addButton.setOnClickListener {
                            calendarViewModel.addNote(
                                Note(
                                    0,
                                    "Test",
                                    selectedDate,
                                    LocalTime.now()
                                )
                            )

                        }
                        calendarViewModel.note.observe(viewLifecycleOwner) { list ->
                            noteList.adapter = NoteAdapter(list,
                                onDeleteClick = { selectedNote ->
                                    val alertDialogBuilder = AlertDialog.Builder(requireContext())
                                    alertDialogBuilder.setTitle(selectedNote.title)
                                    alertDialogBuilder.setMessage("You want to delete this task ?")
                                    alertDialogBuilder.setPositiveButton("YES") { _, _ ->
                                        calendarViewModel.deleteNote(selectedNote)
                                        calendarViewModel.postValue(selectedNote.date)
                                    }
                                    alertDialogBuilder.setNegativeButton("NO") { _, _ -> }
                                    val alertDialog = alertDialogBuilder.create()
                                    alertDialog.show()
                                })
                        }
                        binding.calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
                            selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                            calendarViewModel.postValue(selectedDate)
                        }
                    }

                    "This month" -> {
                        calendarViewModel.postValueForThis(startOfMonth, endOfMonth)
                        binding.addButton.visibility = View.GONE
                        calendarViewModel.note.observe(viewLifecycleOwner) { list ->
                            noteList.adapter = NoteAdapter(list,
                                onDeleteClick = {})
                        }
                    }

                    "This year" -> {
                        calendarViewModel.postValueForThis(startOfYear, endOfYear)
                        binding.addButton.visibility = View.GONE
                        calendarViewModel.note.observe(viewLifecycleOwner) { list ->
                            noteList.adapter = NoteAdapter(list,
                                onDeleteClick = {})
                        }
                    }
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }
        noteList.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleEvents()
    }
    private fun handleEvents() {

    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}