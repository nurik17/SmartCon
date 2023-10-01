package kz.cifron.smartcon.presentation.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File

class ResultViewModel(private val repository: ResultRepository) : ViewModel() {
    private val _uploadResponse = MutableLiveData<Response<ResponseBody>>()
    val uploadResponse: LiveData<Response<ResponseBody>> = _uploadResponse

    fun uploadData(imageFile: File, text: String, id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.uploadData(imageFile, text, id)
            _uploadResponse.postValue(response)
        }
    }
}
