package kz.cifron.smartcon.presentation.home

import retrofit2.Response
import retrofit2.http.GET

interface TaskApi {

    @GET("v5/getTasks")
    suspend fun getTasks() : Response<List<Tasks>>
}