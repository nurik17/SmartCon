package kz.cifron.smartcon.feature_home.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kz.cifron.smartcon.databinding.MainItemBinding

class TaskAdapter : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {


    inner class TaskViewHolder(val binding : MainItemBinding) : RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Tasks>(){
        override fun areItemsTheSame(oldItem: Tasks, newItem: Tasks): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Tasks, newItem: Tasks): Boolean {
            return  oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallBack)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MainItemBinding.inflate(inflater, parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = differ.currentList[position]
        holder.itemView.apply {
            holder.binding.textViewTitle.text = task.ADDR
            holder.binding.textViewDescription.text = task.LS.toString()
            setOnClickListener {
                onItemClickListener?.invoke(task)
            }
        }
    }
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Tasks) -> Unit)? = null

    fun setOnItemClickListener(listener: (Tasks) -> Unit) {
        onItemClickListener = listener
    }

}
