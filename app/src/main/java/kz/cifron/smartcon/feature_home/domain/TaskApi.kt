package kz.cifron.smartcon.feature_home.domain

import kz.cifron.smartcon.feature_home.data.Tasks
import retrofit2.Response
import retrofit2.http.GET

interface TaskApi {
    @GET("v5/getTasks")
    suspend fun getTasks() : Response<List<Tasks>>
}