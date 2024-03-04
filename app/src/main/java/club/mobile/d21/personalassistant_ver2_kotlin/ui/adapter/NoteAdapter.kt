package club.mobile.d21.personalassistant_ver2_kotlin.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import club.mobile.d21.personalassistant_ver2_kotlin.data.Note
import club.mobile.d21.personalassistant_ver2_kotlin.databinding.ItemNoteBinding
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class NoteAdapter(private val notes: List<Note>,
                  private val onDeleteClick:(Note)->Unit
):RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemNoteBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(note: Note){
            binding.note.text = note.title
            binding.dateTime.text = note.date.customFormat() + " " + note.time.customFormat()
            binding.note.setOnLongClickListener{
                onDeleteClick.invoke(note)
                true
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNoteBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(notes[position])
    }

    override fun getItemCount(): Int {
        return notes.size
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