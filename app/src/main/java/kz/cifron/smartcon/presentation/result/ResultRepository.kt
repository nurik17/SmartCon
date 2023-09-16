package kz.cifron.smartcon.presentation.result


import okhttp3.MultipartBody
import okhttp3.RequestBody

class ResultRepository(private val apiService: ResultApi) {

    fun uploadDataToServer(id: RequestBody, newInSch: RequestBody, image: MultipartBody.Part): String {
        try {
            val response = apiService.uploadDataToServer(id, newInSch, image).execute()
            if (response.isSuccessful) {
                val result = response.body()?.RESULT
                if (!result.isNullOrEmpty()) {
                    return "Data has been updated"
                }
            }
            return "This task not found"
        } catch (e: Exception) {
            return "FAILURE"
        }
    }
}
