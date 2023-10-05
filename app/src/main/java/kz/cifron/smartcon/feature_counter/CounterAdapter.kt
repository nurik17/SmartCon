package kz.cifron.smartcon.feature_counter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kz.cifron.smartcon.databinding.CounterItemBinding
import kz.cifron.smartcon.feature_home.data.Tasks

class CounterAdapter(var razTipSchCount : Int): RecyclerView.Adapter<CounterAdapter.ViewHolder>(){

    private val values = MutableList(razTipSchCount) { "" }
    private var currentIndex = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CounterItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val value = values[position]
        holder.binding.itemCounter.text = value
    }


    override fun getItemCount(): Int {
        return razTipSchCount
    }

    fun updateValue(value : String){
        if (currentIndex < razTipSchCount) {
            values[currentIndex] = value
            currentIndex++
            notifyDataSetChanged()
        }
    }
    fun resetValues() {
        if(currentIndex > 0){
            currentIndex --
            values[currentIndex] = ""
            notifyDataSetChanged()
        }
    }

    fun getFilledValuesAsString(): String {
        val result = StringBuilder()
        for (value in values) {
            result.append(value)
        }
        return result.toString()
    }

    fun checkRight(){

    }


    inner class ViewHolder(val binding: CounterItemBinding) : RecyclerView.ViewHolder(binding.root)

}
