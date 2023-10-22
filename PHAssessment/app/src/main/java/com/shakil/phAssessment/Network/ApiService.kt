package com.shakil.phAssessment.Network

import com.shakil.phAssessment.Model.QuizListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiService {

    /** Get all the quiz list **/
    @Headers("Accept: application/json")
    @GET("quiz.json")
    suspend fun getCoinsList(): Response<QuizListResponse>

}