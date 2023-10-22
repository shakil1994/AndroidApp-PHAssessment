package com.shakil.phAssessment.Model

import com.google.gson.annotations.SerializedName

data class QuizListResponse(
    @SerializedName("questions")
    var questions: List<Question>
) {
    data class Question(
        @SerializedName("answers")
        var answers: Answers,
        @SerializedName("correctAnswer")
        var correctAnswer: String,
        @SerializedName("question")
        var question: String,
        @SerializedName("questionImageUrl")
        var questionImageUrl: String,
        @SerializedName("score")
        var score: Int
    ) {
        data class Answers(
            @SerializedName("A")
            var a: String,
            @SerializedName("B")
            var b: String,
            @SerializedName("C")
            var c: String,
            @SerializedName("D")
            var d: String
        )
    }
}