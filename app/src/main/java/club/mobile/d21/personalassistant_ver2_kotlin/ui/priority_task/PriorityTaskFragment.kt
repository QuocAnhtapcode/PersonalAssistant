package club.mobile.d21.personalassistant_ver2_kotlin.ui.priority_task

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import club.mobile.d21.personalassistant_ver2_kotlin.R
import club.mobile.d21.personalassistant_ver2_kotlin.databinding.FragmentPriorityTaskBinding
import club.mobile.d21.personalassistant_ver2_kotlin.ui.adapter.PriorityTaskAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

class PriorityTaskFragment: Fragment() {
    private var _binding: FragmentPriorityTaskBinding? = null
    private val binding get() = _binding!!
    private val priorityTaskViewModel: PriorityTaskViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPriorityTaskBinding.inflate(inflater,container,false)
        val priorityTaskList: RecyclerView = binding.priorityTask
        val selectedCategory = arguments?.getString("selectedCategory")
        val selectedCategoryIcon = arguments?.getInt("selectedCategoryIcon")
        var priorityTask = priorityTaskViewModel.priorityTask
        when(selectedCategory){
            "PERSONAL"->{
                priorityTask = priorityTaskViewModel.personalTask
                binding.titlePriorityTask.text = " PERSONAL TASK"
                binding.titlePriorityTask.setCompoundDrawablesWithIntrinsicBounds(selectedCategoryIcon!!,0,0,0)
            }
            "HEALTH"->{
                priorityTask = priorityTaskViewModel.healthTask
                binding.titlePriorityTask.text = " HEALTH TASK"
                binding.titlePriorityTask.setCompoundDrawablesWithIntrinsicBounds(selectedCategoryIcon!!,0,0,0)
            }
            "WORK"->{
                priorityTask = priorityTaskViewModel.workTask
                binding.titlePriorityTask.text = " WORK TASK"
                binding.titlePriorityTask.setCompoundDrawablesWithIntrinsicBounds(selectedCategoryIcon!!,0,0,0)
            }
            "EDUCATION"->{
                priorityTask = priorityTaskViewModel.educationTask
                binding.titlePriorityTask.text = " EDUCATION TASK"
                binding.titlePriorityTask.setCompoundDrawablesWithIntrinsicBounds(selectedCategoryIcon!!,0,0,0)
            }
            "FAMILY"->{
                priorityTask = priorityTaskViewModel.familyTask
                binding.titlePriorityTask.text = " FAMILY TASK"
                binding.titlePriorityTask.setCompoundDrawablesWithIntrinsicBounds(selectedCategoryIcon!!,0,0,0)
            }
            "FRIEND"->{
                priorityTask = priorityTaskViewModel.friendTask
                binding.titlePriorityTask.text = " FRIEND TASK"
                binding.titlePriorityTask.setCompoundDrawablesWithIntrinsicBounds(selectedCategoryIcon!!,0,0,0)
            }
            "ENTERTAINMENT"->{
                priorityTask = priorityTaskViewModel.entertainmentTask
                binding.titlePriorityTask.text = " ENTERTAINMENT TASK"
                binding.titlePriorityTask.setCompoundDrawablesWithIntrinsicBounds(selectedCategoryIcon!!,0,0,0)
            }
            "FINANCIAL INDEPENDENCE"->{
                priorityTask = priorityTaskViewModel.financialIndependenceTask
                binding.titlePriorityTask.text = "FINANCIAL INDEPENDENCE"
                binding.titlePriorityTask.setCompoundDrawablesWithIntrinsicBounds(selectedCategoryIcon!!,0,0,0)
            }
            "SKILL DEVELOPMENT"->{
                priorityTask = priorityTaskViewModel.skillDevelopmentTask
                binding.titlePriorityTask.text = " SKILL DEVELOPMENT"
                binding.titlePriorityTask.setCompoundDrawablesWithIntrinsicBounds(selectedCategoryIcon!!,0,0,0)
            }
            "OTHER"->{
                priorityTask = priorityTaskViewModel.otherTask
                binding.titlePriorityTask.text = " OTHER"
                binding.titlePriorityTask.setCompoundDrawablesWithIntrinsicBounds(selectedCategoryIcon!!,0,0,0)
            }
        }
        priorityTask.observe(viewLifecycleOwner){list->
            priorityTaskList.adapter = PriorityTaskAdapter(
                list,
                onDetailClick ={selectedTask->
                    val bottomSheet = SubtaskBottomSheet(selectedTask)
                    bottomSheet.show(childFragmentManager, bottomSheet.tag)
                },
                onDeleteClick = {selectedTask->
                    val alertDialogBuilder = AlertDialog.Builder(requireContext())
                    alertDialogBuilder.setTitle(selectedTask.name)
                    alertDialogBuilder.setMessage("You want to delete this task ?")
                    alertDialogBuilder.setPositiveButton("YES") { _, _ ->
                        priorityTaskViewModel.delete(selectedTask)
                    }
                    alertDialogBuilder.setNegativeButton("NO") { _, _ -> }
                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()
                }
            )
        }
        priorityTaskList.layoutManager = LinearLayoutManager(context)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleEvents()
    }
    private fun handleEvents(){
        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        binding.addButton.setOnClickListener {
            val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
            bottomNavigationView?.visibility = View.GONE
            findNavController().navigate(R.id.action_priorityTaskFragment_to_addPriorityTaskFragment)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}