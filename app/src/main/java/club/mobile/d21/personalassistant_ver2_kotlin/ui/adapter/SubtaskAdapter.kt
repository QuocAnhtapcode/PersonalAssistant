package club.mobile.d21.personalassistant_ver2_kotlin.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import club.mobile.d21.personalassistant_ver2_kotlin.R
import club.mobile.d21.personalassistant_ver2_kotlin.data.Subtask
import club.mobile.d21.personalassistant_ver2_kotlin.databinding.ItemSubtaskBinding

class SubtaskAdapter(private val subtasks: List<Subtask>,
                     private val onStatusClick: (Subtask)->Unit,
                     private val onDeleteClick:(Subtask)->Unit
): RecyclerView.Adapter<SubtaskAdapter.ViewHolder>() {
    inner class ViewHolder(private var binding: ItemSubtaskBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(subtask: Subtask){
            binding.taskName.text = subtask.title
            if(subtask.status) {
                binding.taskName.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.main_color))
                binding.statusButton.setImageResource(R.drawable.ic_done)
            }else{
                binding.taskName.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.black))
                binding.statusButton.setImageResource(R.drawable.ic_undone)
            }
            binding.statusButton.setOnClickListener {
                onStatusClick.invoke(subtask)
            }
            binding.taskName.setOnLongClickListener {
                onDeleteClick.invoke(subtask)
                true
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSubtaskBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(subtasks[position])
    }
    override fun getItemCount(): Int {
        return subtasks.size
    }
}