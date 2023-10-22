package com.shakil.phAssessment.Repo

import com.shakil.phAssessment.Common.DataStatus
import com.shakil.phAssessment.Network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class QuizListRepo @Inject constructor(private val apiService: ApiService) {

    /** getQuizList **/
    suspend fun getQuizList() = flow {
        emit(DataStatus.loading())
        val result = apiService.getCoinsList()
        when (result.code()) {
            200 -> {
                emit(DataStatus.success(result.body()))
            }

            400 -> {
                emit(DataStatus.error(result.message()))
            }

            404 -> {
                emit(DataStatus.error(result.message()))
            }

            500 -> {
                emit(DataStatus.error(result.message()))
            }
        }
    }.catch {
        emit(DataStatus.error(it.message.toString()))
    }.flowOn(Dispatchers.IO)
}