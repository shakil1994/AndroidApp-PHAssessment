package com.shakil.phAssessment.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shakil.phAssessment.Common.DataStatus
import com.shakil.phAssessment.Common.SingleLiveEvent
import com.shakil.phAssessment.Model.QuizListResponse
import com.shakil.phAssessment.Repo.QuizListRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizListViewModel @Inject constructor(private val repo: QuizListRepo) : ViewModel() {

    //getQuizList
    private val _quizListResponse = MutableLiveData<SingleLiveEvent<DataStatus<QuizListResponse>>>()
    val quizListResponse: LiveData<SingleLiveEvent<DataStatus<QuizListResponse>>> get() = _quizListResponse

    fun getQuizList() = viewModelScope.launch {
        repo.getQuizList().collect {
            _quizListResponse.value = SingleLiveEvent(it)
        }
    }
}