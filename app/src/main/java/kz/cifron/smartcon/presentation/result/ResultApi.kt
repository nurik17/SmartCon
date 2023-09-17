package kz.cifron.smartcon.presentation.result

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface ResultApi {
    @Multipart
    @POST("addData")
    fun uploadDataToServer(
        @Part("id") id: RequestBody,
        @Part("NEW_IN_SCH") newInSch: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<ResultApiResponse>
}