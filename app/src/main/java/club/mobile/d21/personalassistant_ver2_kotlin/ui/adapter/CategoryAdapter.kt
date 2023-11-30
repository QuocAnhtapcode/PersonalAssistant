package club.mobile.d21.personalassistant_ver2_kotlin.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import club.mobile.d21.personalassistant_ver2_kotlin.databinding.ItemCategoryBinding

class CategoryAdapter(private val category: List<Pair<String,Int>>,
    private val onDetailClick:(Pair<String,Int>)->Unit
):RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    inner class ViewHolder(private var binding: ItemCategoryBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(category: Pair<String,Int>){
            binding.categoryText.text = category.first
            binding.categoryIcon.setImageResource(category.second)
            binding.category.setOnClickListener{
                onDetailClick.invoke(category)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCategoryBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(category[position])
    }
    override fun getItemCount(): Int {
        return category.size
    }
}