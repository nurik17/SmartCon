package kz.cifron.smartcon.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class TaskRepository(private val itemApi: TaskApi) {

    private val _taskStateLiveData = MutableLiveData<TaskState>()
    val taskStateLiveData: LiveData<TaskState> = _taskStateLiveData

    suspend fun getTasks() {
        try {
            _taskStateLiveData.postValue(TaskState.Loading)
            val response = itemApi.getTasks()
            if (response.isSuccessful) {
                val tasks = response.body()
                _taskStateLiveData.postValue(tasks?.let { TaskState.Success(it) })
            } else {
                _taskStateLiveData.postValue(TaskState.Error("Error fetching tasks"))
            }
        } catch (e: Exception) {
            _taskStateLiveData.postValue(TaskState.Error(e.message ?: "An error occurred"))
        }
    }
}
