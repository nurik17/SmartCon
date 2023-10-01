package kz.cifron.smartcon.presentation.result

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ResultApi {
    @Multipart
    @POST("addData")
    suspend fun uploadData(
        @Part image: MultipartBody.Part,
        @Query("NEW_IN_SCH") text: String,
        @Query("id") id: String
    ): Response<ResponseBody>
}
