package kz.cifron.smartcon.feature_home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.cifron.smartcon.feature_home.domain.TaskRepository

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    val taskLiveData = repository.taskStateLiveData

    fun getTasks() {
        viewModelScope.launch {
            repository.getTasks()
        }
    }

}
