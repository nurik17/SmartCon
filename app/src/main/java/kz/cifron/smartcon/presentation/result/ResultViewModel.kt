package kz.cifron.smartcon.presentation.result


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ResultViewModel(private val repository: ResultRepository) : ViewModel() {

    private val _resultLiveData = MutableLiveData<String>()
    val resultLiveData: LiveData<String>
        get() = _resultLiveData

    fun uploadDataToServer(id: RequestBody, newInSch: RequestBody, image: MultipartBody.Part) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.uploadDataToServer(id, newInSch, image)
            _resultLiveData.postValue(result)
        }
    }
}
