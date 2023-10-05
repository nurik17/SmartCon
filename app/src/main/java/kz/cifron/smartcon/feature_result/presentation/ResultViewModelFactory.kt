package kz.cifron.smartcon.feature_result.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kz.cifron.smartcon.feature_result.data.TaskDao
import kz.cifron.smartcon.feature_result.domain.ResultRepository

@Suppress("UNCHECKED_CAST")
class ResultViewModelFactory(private val repository: ResultRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultViewModel::class.java)) {
            return ResultViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
