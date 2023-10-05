package kz.cifron.smartcon.feature_result.domain

import kz.cifron.smartcon.feature_result.data.ResultApiResponse
import okhttp3.MultipartBody
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
        @Query("id") id: Int
    ): ResultApiResponse
}
