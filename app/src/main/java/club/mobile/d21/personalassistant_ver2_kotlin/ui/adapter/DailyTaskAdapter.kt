package club.mobile.d21.personalassistant_ver2_kotlin.ui.adapter

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import club.mobile.d21.personalassistant_ver2_kotlin.R
import club.mobile.d21.personalassistant_ver2_kotlin.data.DailyTask
import club.mobile.d21.personalassistant_ver2_kotlin.databinding.ItemDailyTaskBinding

class DailyTaskAdapter(private val dailyTasks: List<DailyTask>,
                       private val onStatusClick: (DailyTask) -> Unit,
                       private val onDetailClick: (DailyTask) -> Unit
):RecyclerView.Adapter<DailyTaskAdapter.ViewHolder>() {

    inner class ViewHolder(private var binding: ItemDailyTaskBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(dailyTask: DailyTask) {
            binding.taskName.text = dailyTask.time.toString() + " - " + dailyTask.name
            if(dailyTask.status) {
                binding.taskName.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.main_color))
                binding.statusButton.setImageResource(R.drawable.ic_done)
            }else{
                binding.taskName.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.black))
                binding.statusButton.setImageResource(R.drawable.ic_undone)
            }
            binding.statusButton.setOnClickListener {
                onStatusClick.invoke(dailyTask)
            }
            binding.taskName.setOnLongClickListener {
                onDetailClick.invoke(dailyTask)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDailyTaskBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dailyTasks[position])
    }

    override fun getItemCount(): Int {
        return dailyTasks.size
    }
}