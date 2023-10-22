package com.shakil.phAssessment.View

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import coil.load
import com.shakil.phAssessment.Common.Constants
import com.shakil.phAssessment.Common.DataStatus
import com.shakil.phAssessment.Common.NetWorkChecking
import com.shakil.phAssessment.Common.SharedPref
import com.shakil.phAssessment.Common.isLoading
import com.shakil.phAssessment.Model.QuizListResponse
import com.shakil.phAssessment.R
import com.shakil.phAssessment.ViewModel.QuizListViewModel
import com.shakil.phAssessment.databinding.ActivityQuizAnsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuizAnsActivity : AppCompatActivity() {

    private var _binding: ActivityQuizAnsBinding? = null
    private val binding get() = _binding

    private val viewModel: QuizListViewModel by viewModels()

    private var currentPosition: Int = 1
    private var questionList: List<QuizListResponse.Question>? = null
    private var correctAnswerScore: Int = 0
    private var isSelectedAnswer: Boolean = false
    private var selectedItemName: String? = null
    private var selectedOptionPosition: Int = 0
    private var preferenceManager: SharedPref? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityQuizAnsBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        preferenceManager = SharedPref(this)

        getQuizList()
        defaultOptionsView()
        selectedAnswer()
    }

    // TODO: Get Quiz List=========================
    private fun getQuizList() {
        lifecycleScope.launch {
            binding.apply {
                if (!NetWorkChecking.isNetworkAvailable(this@QuizAnsActivity)) {
                    viewModel.getQuizList()
                    viewModel.quizListResponse.observe(this@QuizAnsActivity) { response ->
                        response.getContentIfNotHandled()?.let {
                            when (it.status) {
                                DataStatus.Status.LOADING -> {
                                    binding?.progressBar?.isLoading(true, binding!!.constantLayout)
                                }
                                DataStatus.Status.SUCCESS -> {
                                    binding?.progressBar?.isLoading(false, binding!!.constantLayout)
                                    questionList = it.data?.questions
                                    setQuestionList()
                                }
                                DataStatus.Status.ERROR -> {
                                    binding?.progressBar?.isLoading(false, binding!!.constantLayout)
                                    Toast.makeText(this@QuizAnsActivity, "There is something wrong!!", Toast.LENGTH_LONG).show()
                                }
                            }
                        }

                    }
                }
                else{
                    binding?.progressBar?.isLoading(false, binding!!.constantLayout)
                    Toast.makeText(baseContext, "Please check your internet connection", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // TODO: Set Question===================
    private fun setQuestionList() {
        defaultOptionsView()
        this.questionList.let {
            binding.apply {
                val questionsList = it
                var currentPosition = currentPosition
                val question: QuizListResponse.Question? = questionsList?.get(currentPosition - 1)
                binding?.txtPoints?.text = StringBuilder().append(question?.score).append(" Point")
                Log.e("QUIZANSACTIVITY", "IMAGE: " + question?.questionImageUrl)
                if (question?.questionImageUrl.toString() == "null"){
                    binding?.imgQuestion?.isVisible = false
                }else {
                    binding?.imgQuestion?.isVisible = true
                    binding?.imgQuestion?.load(question?.questionImageUrl) {
                        crossfade(true)
                        crossfade(500)
                        // TODO:  App logo set as placeholder & error photo============
                        placeholder(R.drawable.ic_logo)
                        error(R.drawable.ic_logo)
                    }
                }
                binding?.txtQuestion?.text = question?.question.toString()

                // TODO: Question null check and set value and invisible the other view=============
                if (question?.answers?.a.toString() == "null") {
                    binding?.txtQusOne?.isVisible = false
                } else {
                    binding?.txtQusOne?.isVisible = true
                    binding?.txtQusOne?.text = question?.answers?.a.toString()
                }

                if (question?.answers?.b.toString() == "null") {
                    binding?.txtQusTwo?.isVisible = false
                } else {
                    binding?.txtQusTwo?.isVisible = true
                    binding?.txtQusTwo?.text = question?.answers?.b.toString()
                }

                if (question?.answers?.c.toString() == "null") {
                    binding?.txtQusThree?.isVisible = false
                } else {
                    binding?.txtQusThree?.isVisible = true
                    binding?.txtQusThree?.text = question?.answers?.c.toString()
                }

                if (question?.answers?.d.toString() == "null") {
                    binding?.txtQusFour?.isVisible = false
                } else {
                    binding?.txtQusFour?.isVisible = true
                    binding?.txtQusFour?.text = question?.answers?.d.toString()
                }

                // TODO: Set question current value and the total question===============
                binding?.txtQuesCurrentValue?.text = StringBuilder().append("Question: ").append(currentPosition).append("/")
                binding?.txtQuesTotalValue?.text = StringBuilder().append(questionsList?.size)
            }
        }
    }

    private fun defaultOptionsView() {
        val options = ArrayList<AppCompatTextView>()
        binding!!.txtQusOne.let {
            options.add(0, it)
        }
        binding!!.txtQusTwo.let {
            options.add(1, it)
        }
        binding!!.txtQusThree.let {
            options.add(2, it)
        }
        binding!!.txtQusFour.let {
            options.add(3, it)
        }
        for (option in options) {
            option.setTextColor(Color.parseColor("#000000"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(this, R.drawable.button_background)
        }
    }

    // TODO: Select Answer====================
    private fun selectedAnswer() {
        binding?.apply {
            txtQusOne.setOnClickListener {
                if (!isSelectedAnswer) {
                    selectedOptionView(1)
                }
            }
            txtQusTwo.setOnClickListener {
                if (!isSelectedAnswer) {
                    selectedOptionView(2)
                }
            }
            txtQusThree.setOnClickListener {
                if (!isSelectedAnswer) {
                    selectedOptionView(3)
                }
            }
            txtQusFour.setOnClickListener {
                if (!isSelectedAnswer) {
                    selectedOptionView(4)
                }
            }
        }
    }

    // TODO: Select Options============
    private fun selectedOptionView(selectedOptionNum: Int) {
        defaultOptionsView()
        selectedOptionPosition = selectedOptionNum
        isSelectedAnswer = true

        val question = questionList?.get(currentPosition - 1)
        question?.let {

            // TODO: Selected item number check and assign value to corresponding number========
            when (selectedOptionNum) {
                1 -> {
                    selectedItemName = "A"
                }

                2 -> {
                    selectedItemName = "B"
                }

                3 -> {
                    selectedItemName = "C"
                }

                4 -> {
                    selectedItemName = "D"
                }
            }

            // TODO: Answer check with the response & take action according to the answer
            if (it.correctAnswer != selectedItemName) {
                when (it.correctAnswer) {
                    "A" -> {
                        answerView(1, R.drawable.right_answer_background)
                    }

                    "B" -> {
                        answerView(2, R.drawable.right_answer_background)
                    }

                    "C" -> {
                        answerView(3, R.drawable.right_answer_background)
                    }

                    "D" -> {
                        answerView(4, R.drawable.right_answer_background)
                    }
                }
                answerView(selectedOptionPosition, R.drawable.wrong_answer_background)
            } else {
                correctAnswerScore += question.score
                binding?.txtScore?.text = StringBuilder().append("Score: ").append(correctAnswerScore)
                answerView(selectedOptionPosition, R.drawable.right_answer_background)

                // TODO: Check the current score with the pref highScore
                val highScoreFromPreferences = preferenceManager?.getInt(Constants.HIGHSCORE) ?: 0
                if (highScoreFromPreferences <= correctAnswerScore) {
                    preferenceManager?.saveInt(Constants.HIGHSCORE, correctAnswerScore)
                    Log.e("QUIZANSACTIVITY", "CURRENTSCORE: " + correctAnswerScore)
                }
            }
            selectedOptionPosition = 0
            currentPosition++
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    when {
                        currentPosition <= questionList!!.size -> {
                            setQuestionList()
                        }

                        else -> {
                            binding?.constantLayout?.isVisible = false
                            // TODO: Show the popup
                            showPopUpConfirmationWindow()
                        }
                    }
                    isSelectedAnswer = false
                },
                2000 // TODO: Automatically skip the question after 2 seconds
            )
        }
    }

    // TODO: Answer VIEW
    private fun answerView(answer: Int, drawableView: Int) {
        when (answer) {
            1 -> {
                binding?.txtQusOne?.background = ContextCompat.getDrawable(this, drawableView)
            }

            2 -> {
                binding?.txtQusTwo?.background = ContextCompat.getDrawable(this, drawableView)
            }

            3 -> {
                binding?.txtQusThree?.background = ContextCompat.getDrawable(this, drawableView)
            }

            4 -> {
                binding?.txtQusFour?.background = ContextCompat.getDrawable(this, drawableView)
            }
        }
    }

    // TODO: Show Confirmation Dialog
    private fun showPopUpConfirmationWindow() {
        // TODO: Create the popup window
        val popupView = layoutInflater.inflate(R.layout.dialog_window_confirmation, null)
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        // TODO: Set score value in the popup window
        val txtScore: AppCompatTextView = popupView.findViewById(R.id.txtScore)

        /** Assuming currentHighScore is your score value **/
        txtScore.text = StringBuilder().append("Your Score is: ").append(correctAnswerScore)

        // TODO: Set button click listener inside the popup window
        val backButton: AppCompatButton = popupView.findViewById(R.id.btnGoMainMenu)
        backButton.setOnClickListener {
            /** Handle button click (for example, navigate back home) **/
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            popupWindow.dismiss() /** Dismiss the popup window after handling the click **/
        }
        /** Show the popup window middle of the screen **/
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)
    }
}