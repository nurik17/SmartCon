package kz.cifron.smartcon.presentation.result

import kz.cifron.smartcon.presentation.home.Tasks
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ResultApi {

    @Multipart
    @POST("addData")
    fun uploadDataToServer(
        @Part text : MultipartBody.Part,
        @Part image : MultipartBody.Part,
    ) : Call<ResponseBody>

}