package club.mobile.d21.personalassistant_ver2_kotlin.ui.priority_task

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import club.mobile.d21.personalassistant_ver2_kotlin.R
import club.mobile.d21.personalassistant_ver2_kotlin.data.Category
import club.mobile.d21.personalassistant_ver2_kotlin.data.Priority
import club.mobile.d21.personalassistant_ver2_kotlin.data.PriorityTask
import club.mobile.d21.personalassistant_ver2_kotlin.data.Subtask
import club.mobile.d21.personalassistant_ver2_kotlin.databinding.FragmentAddPriorityTaskBinding
import club.mobile.d21.personalassistant_ver2_kotlin.ui.adapter.SubtaskAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class AddPriorityTaskFragment: Fragment() {
    private var _binding: FragmentAddPriorityTaskBinding? = null
    private val binding get() = _binding!!
    private val priorityTaskViewModel: PriorityTaskViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPriorityTaskBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var category: Category = Category.OTHER
        val options = arrayOf(Category.PERSONAL,Category.HEALTH, Category.WORK,
            Category.EDUCATION, Category.FAMILY, Category.FRIEND, Category.ENTERTAINMENT,
            Category.FINANCIAL_INDEPENDENCE, Category.SKILL_DEVELOPMENT, Category.OTHER)
        // Tạo Adapter cho Spinner
        val adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.chooseCategory.adapter = adapter
        // Xử lý sự kiện khi chọn một lựa chọn
        binding.chooseCategory.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                category = options[position]
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {
                category = Category.OTHER
            }
        })

        var startTime =  LocalTime.now()
        var endTime = LocalTime.now()
        var startDate = LocalDate.now()
        var endDate=  LocalDate.now()
        val currentDate = LocalDate.now()
        val currentTime = LocalTime.now()
        binding.chooseStartDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                {_,year,month,day->
                    startDate = LocalDate.of(year,month+1,day)
                    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
                    binding.chooseStartDate.text = startDate.format(formatter)
                },
                currentDate.year-1,
                currentDate.monthValue+1,
                currentDate.dayOfYear
            )
            datePickerDialog.show()
        }
        binding.chooseEndDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                {_,year,month,day->
                    endDate = LocalDate.of(year,month+1,day)
                    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
                    binding.chooseEndDate.text = endDate.format(formatter)
                },
                currentDate.year-1,
                currentDate.monthValue+1,
                currentDate.dayOfYear
            )
            datePickerDialog.show()
        }
        binding.chooseStartTime.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                requireContext(),
                {_,hour,minute->
                    startTime = LocalTime.of(hour, minute,0)
                    val formatter = DateTimeFormatter.ofPattern("HH:mm",Locale.getDefault())
                    binding.chooseStartTime.text = startTime.format(formatter)
                },
                currentTime.hour,
                currentTime.minute,
                true
            )
            timePickerDialog.show()
        }
        binding.chooseEndTime.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                requireContext(),
                {_,hour,minute->
                    endTime = LocalTime.of(hour, minute,0)
                    val formatter = DateTimeFormatter.ofPattern("HH:mm",Locale.getDefault())
                    binding.chooseEndTime.text = endTime.format(formatter)
                },
                currentTime.hour,
                currentTime.minute,
                true
            )
            timePickerDialog.show()
        }
        var priority: Priority = Priority.LOW_PRIORITY
        binding.choosePriority.setOnCheckedChangeListener{ _, checkID ->
            when (checkID) {
                R.id.chooseCritical ->{
                    priority = Priority.CRITICAL
                }
                R.id.chooseImportant ->{
                    priority = Priority.IMPORTANT
                }
                R.id.chooseLowPriority ->{
                    priority = Priority.LOW_PRIORITY
                }
            }
        }
        val list: MutableList<Subtask> = mutableListOf()
        var _subTaskList = MutableLiveData<List<Subtask>>()
        binding.addSubtask.setOnClickListener {
            val inputEditText = EditText(requireContext())
            AlertDialog.Builder(requireContext())
                .setTitle("Enter your subtask")
                .setView(inputEditText)
                .setPositiveButton("Confirm") { _, _ ->
                    list.add(Subtask(inputEditText.text.toString()))
                    _subTaskList.postValue(list)
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
                .show()
        }
        val subTaskList: LiveData<List<Subtask>> = _subTaskList
        subTaskList.observe(viewLifecycleOwner){tmpList->
            binding.listSubTask.adapter = SubtaskAdapter(tmpList,
                onStatusClick = {},
                onDeleteClick = {selectedTask->
                    val alertDialogBuilder = AlertDialog.Builder(requireContext())
                    alertDialogBuilder.setTitle("Confirm")
                    alertDialogBuilder.setMessage("You want to delete this subtask ?")
                    alertDialogBuilder.setPositiveButton("YES") { _, _ ->
                        list.remove(selectedTask)
                        _subTaskList.postValue(list)
                    }
                    alertDialogBuilder.setNegativeButton("NO") { _, _ -> }
                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()

                })
            binding.listSubTask.layoutManager = LinearLayoutManager(context)
        }
        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        binding.createButton.setOnClickListener {
            if(binding.chooseStartDate.text.isNullOrEmpty() || binding.chooseEndDate.text.isNullOrEmpty()
                || binding.chooseStartTime.text.isNullOrEmpty() || binding.chooseEndTime.text.isNullOrEmpty()
                || binding.chooseTitle.text.isNullOrEmpty()) return@setOnClickListener

            priorityTaskViewModel.addPriorityTask(PriorityTask(0,binding.chooseTitle.text.toString(),
                category,startDate,endDate,startTime,endTime,priority,list))
            parentFragmentManager.popBackStack()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        val bottomNavigationView = activity?.
        findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView?.visibility = View.VISIBLE
        _binding = null
    }
}