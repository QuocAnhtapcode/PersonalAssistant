package club.mobile.d21.personalassistant_ver2_kotlin.ui.priority_task

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import club.mobile.d21.personalassistant_ver2_kotlin.data.PriorityTask
import club.mobile.d21.personalassistant_ver2_kotlin.data.Subtask
import club.mobile.d21.personalassistant_ver2_kotlin.databinding.BottomSheetSubtaskBinding
import club.mobile.d21.personalassistant_ver2_kotlin.ui.adapter.SubtaskAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SubtaskBottomSheet(selectedPriorityTask: PriorityTask): BottomSheetDialogFragment() {
    private lateinit var binding:BottomSheetSubtaskBinding
    private val priorityTaskViewModel: PriorityTaskViewModel by activityViewModels()
    private val priorityTask = selectedPriorityTask
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
        binding = BottomSheetSubtaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val subtaskList: RecyclerView = binding.subtask
        priorityTaskViewModel.getSubtasks(priorityTask.id)
        priorityTaskViewModel.subTask.observe(viewLifecycleOwner){list->
            subtaskList.adapter = SubtaskAdapter(list,
                onStatusClick = {selectedSubtask->
                    if(selectedSubtask.status) {
                        priorityTaskViewModel.undoneSubtask(priorityTask.id, selectedSubtask)
                    }else{
                        priorityTaskViewModel.doneSubtask(priorityTask.id,selectedSubtask)
                    }
                },
                onDeleteClick = {selectedTask->
                    val alertDialogBuilder = AlertDialog.Builder(requireContext())
                    alertDialogBuilder.setTitle("Confirm")
                    alertDialogBuilder.setMessage("You want to delete this subtask ?")
                    alertDialogBuilder.setPositiveButton("YES") { _, _ ->
                        priorityTaskViewModel.deleteSubtask(priorityTask.id,selectedTask)
                    }
                    alertDialogBuilder.setNegativeButton("NO") { _, _ -> }
                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()
                })

        }
        binding.addButton.setOnClickListener {
            if(binding.subtaskEditText.text.isNullOrEmpty())
                return@setOnClickListener
            priorityTaskViewModel.addSubtask(priorityTask.id, Subtask(binding.subtaskEditText.text.toString()))
            binding.subtaskEditText.setText("")
        }
        subtaskList.layoutManager = LinearLayoutManager(context)
    }
}