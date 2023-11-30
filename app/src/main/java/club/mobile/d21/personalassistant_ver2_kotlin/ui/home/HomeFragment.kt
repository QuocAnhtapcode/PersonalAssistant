package club.mobile.d21.personalassistant_ver2_kotlin.ui.home

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
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import club.mobile.d21.personalassistant_ver2_kotlin.R
import club.mobile.d21.personalassistant_ver2_kotlin.databinding.FragmentHomeBinding
import club.mobile.d21.personalassistant_ver2_kotlin.service.UpdateDailyTaskWorker
import club.mobile.d21.personalassistant_ver2_kotlin.ui.adapter.CategoryAdapter
import club.mobile.d21.personalassistant_ver2_kotlin.ui.adapter.DailyTaskAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        val categoryView: RecyclerView = binding.priorityTask
        val categoryList: List<Pair<String,Int>> = listOf(
            Pair("PERSONAL",R.drawable.ic_category_personal),
            Pair("HEALTH",R.drawable.ic_category_health),
            Pair("WORK",R.drawable.ic_category_work),
            Pair("EDUCATION",R.drawable.ic_category_education),
            Pair("FAMILY",R.drawable.ic_category_family),
            Pair("ENTERTAINMENT",R.drawable.ic_category_entertainment),
            Pair("FINANCIAL INDEPENDENCE",R.drawable.ic_category_financial_independence),
            Pair("SKILL DEVELOPMENT",R.drawable.ic_category_skill_development),
            Pair("OTHER",R.drawable.ic_category_other)
        )
        categoryView.adapter = CategoryAdapter(categoryList,
            onDetailClick = { selectedCategory ->
                val bundle = Bundle()
                bundle.putString("selectedCategory", selectedCategory.first)
                bundle.putInt("selectedCategoryIcon", selectedCategory.second)
                findNavController().navigate(
                    R.id.action_homeFragment_to_priorityTaskFragment,
                    bundle
                )
            }
        )
        categoryView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val dailyTaskList: RecyclerView = binding.dailyTask
        homeViewModel.dailyTask.observe(viewLifecycleOwner){list->
            dailyTaskList.adapter = DailyTaskAdapter(list,
                onStatusClick = {selectedTask->
                    if(selectedTask.status) {
                        homeViewModel.undone(selectedTask.id)
                    }else{
                        homeViewModel.done(selectedTask.id)
                    }
                }, onDetailClick = {selectedTask->
                    val alertDialogBuilder = AlertDialog.Builder(requireContext())
                    alertDialogBuilder.setTitle("Description")
                    alertDialogBuilder.setMessage(selectedTask.description)
                    alertDialogBuilder.setPositiveButton("DELETE") { _, _ ->
                        homeViewModel.delete(selectedTask)
                    }
                    alertDialogBuilder.setNegativeButton("BACK") { _, _ -> }
                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()
                })
        }
        dailyTaskList.layoutManager = LinearLayoutManager(context)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleEvents()
    }
    private fun handleEvents(){
        val updateDailyTaskRequest = PeriodicWorkRequestBuilder<UpdateDailyTaskWorker>(
            1, // Thời gian lặp lại: 1 ngày
            TimeUnit.DAYS
        ).build()
        WorkManager.getInstance(requireContext()).enqueue(updateDailyTaskRequest)
        binding.addButton.setOnClickListener {
            val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
            bottomNavigationView?.visibility = View.GONE
            findNavController().navigate(R.id.action_homeFragment_to_addDailyTaskFragment)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}