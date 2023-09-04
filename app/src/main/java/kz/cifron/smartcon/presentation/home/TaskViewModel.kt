package kz.cifron.smartcon.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.Locale

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    val taskLiveData = repository.taskStateLiveData



    fun getTasks() {
        viewModelScope.launch {
            repository.getTasks()
        }
    }

}
