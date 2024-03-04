package club.mobile.d21.personalassistant_ver2_kotlin.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import club.mobile.d21.personalassistant_ver2_kotlin.R
import club.mobile.d21.personalassistant_ver2_kotlin.data.Priority
import club.mobile.d21.personalassistant_ver2_kotlin.data.PriorityTask
import club.mobile.d21.personalassistant_ver2_kotlin.databinding.ItemPriorityTaskBinding
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class PriorityTaskAdapter (private var priorityTasks: List<PriorityTask>,
                           private val onDetailClick: (PriorityTask) -> Unit,
                           private val onDeleteClick: (PriorityTask) -> Unit
): RecyclerView.Adapter<PriorityTaskAdapter.ViewHolder>(){
    inner class ViewHolder(private var binding: ItemPriorityTaskBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(priorityTask: PriorityTask){
            binding.textTime.text = String.format(
                "%s %s - %s %s", priorityTask.startTime.customFormat(),
                priorityTask.startDate.customFormat(), priorityTask.endTime.customFormat(),
                priorityTask.endDate.customFormat()
            )
            binding.textTitle.text = priorityTask.name
            if (priorityTask.status) {
                binding.textTitle.setTextColor(ContextCompat.getColor(
                    itemView.context, R.color.main_color))
                binding.tickDone.visibility = View.VISIBLE
            } else {
                when (priorityTask.priority) {
                    Priority.LOW_PRIORITY -> {
                        binding.textTitle.setTextColor(
                            ContextCompat.getColor(itemView.context, R.color.green))
                    }
                    Priority.IMPORTANT -> {
                        binding.textTitle.setTextColor(
                            ContextCompat.getColor(itemView.context, R.color.yellow))
                    }
                    Priority.CRITICAL -> {
                        binding.textTitle.setTextColor(
                            ContextCompat.getColor(itemView.context, R.color.red))
                    }
                }
            }
            binding.priorityTask.setOnClickListener {
                onDetailClick.invoke(priorityTask)
            }
            binding.priorityTask.setOnLongClickListener{
                onDeleteClick.invoke(priorityTask)
                true
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPriorityTaskBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(priorityTasks[position])
    }
    override fun getItemCount(): Int {
        return priorityTasks.size
    }
    fun LocalDate.customFormat(): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return this.format(formatter)
    }

    fun LocalTime.customFormat(): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return this.format(formatter)
    }
}