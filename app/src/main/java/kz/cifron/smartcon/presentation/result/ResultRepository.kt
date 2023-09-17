package kz.cifron.smartcon.presentation.result

import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call

class ResultRepository(private val api: ResultApi) {

    fun uploadDataToServer(id: String, newInSch: String, imagePart: MultipartBody.Part): Call<ResultApiResponse> {
        return api.uploadDataToServer(
            id.toRequestBody(), // Определяйте это расширение
            newInSch.toRequestBody(), // Определяйте это расширение
            imagePart
        )
    }
}

