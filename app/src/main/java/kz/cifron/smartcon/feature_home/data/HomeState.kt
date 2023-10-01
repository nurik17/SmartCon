package kz.cifron.smartcon.feature_home.data

sealed class TaskState {
    object Loading : TaskState()
    data class Success(val tasks: List<Tasks>) : TaskState()
    data class Error(val errorMessage: String) : TaskState()
}
