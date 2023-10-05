package kz.cifron.smartcon.feature_result.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.cifron.smartcon.feature_result.data.ResultEntity
import kz.cifron.smartcon.feature_result.domain.ResultRepository

class ResultViewModel(private val repository: ResultRepository) : ViewModel() {

    fun insertTask(taskEntity: ResultEntity){
        viewModelScope.launch {
            repository.insertTask(taskEntity)
        }
    }

    fun getAllTasks(){
        viewModelScope.launch {
            repository.getAllTasks()
        }
    }
}
