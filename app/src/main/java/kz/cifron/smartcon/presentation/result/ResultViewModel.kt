package kz.cifron.smartcon.presentation.result

import android.util.Log
import okhttp3.MultipartBody
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ResultViewModel(private val repository: ResultRepository) : ViewModel() {

    fun uploadData(id: String, newInSch: String, imagePart: MultipartBody.Part) {
        repository.uploadDataToServer(id, newInSch, imagePart).enqueue(object : Callback<ResultApiResponse> {
            override fun onResponse(call: Call<ResultApiResponse>, response: Response<ResultApiResponse>) {
                if (response.isSuccessful) {
                    val resultApiResponse = response.body()
                    if (resultApiResponse != null) {
                        Log.d("ResultFragment", "Успешный ответ от сервера: $resultApiResponse")
                    } else {
                        Log.d("ResultFragment", "Ошибка: ответ от сервера пустой")
                    }
                } else {
                    Log.e("ResultFragment", "Ошибка: сервер вернул неуспешный статус код ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResultApiResponse>, t: Throwable) {
                // Ошибка при выполнении запроса
            }
        })
    }
}
