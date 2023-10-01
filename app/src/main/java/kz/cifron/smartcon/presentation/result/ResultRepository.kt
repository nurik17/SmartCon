package kz.cifron.smartcon.presentation.result

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import java.io.File
import retrofit2.Response


class ResultRepository(private val api: ResultApi) {
    suspend fun uploadData(
        imageFile: File,
        text: String,
        id: String
    ): Response<ResponseBody> {
        val imagePart = MultipartBody.Part.createFormData(
            "image",
            imageFile.name,
            imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        )

        return api.uploadData(imagePart, text, id)
    }
}
