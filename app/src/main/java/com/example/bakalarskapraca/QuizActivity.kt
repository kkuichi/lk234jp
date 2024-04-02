package com.example.bakalarskapraca

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.TypedValue
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.util.Collections

class QuizActivity : AppCompatActivity() {

    private lateinit var quiztext: TextView
    private lateinit var answerA: TextView
    private lateinit var answerB: TextView
    private lateinit var answerC: TextView
    private lateinit var answerD: TextView
    private lateinit var questionItems: List<QuestionsItem>
    private var currentQuestion: Int = 0
    private var correctAnswered: Int = 0
    private var wrongAnswered: Int = 0
    private var test_ID: Int = 0
    private lateinit var nextBtn: Button
    private lateinit var test_title: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val test_file = intent.getStringExtra("test_file") ?: return
        val testName = intent.getStringExtra("test_name") ?: return
        test_ID = intent.getIntExtra("test_ID", 0)

        quiztext = findViewById(R.id.quizText)
        answerA = findViewById(R.id.aanswer)
        answerB = findViewById(R.id.banswer)
        answerC = findViewById(R.id.canswer)
        answerD = findViewById(R.id.danswer)

        test_title = findViewById(R.id.test_header)
        test_title.title = testName.substring(3)


        loadAllQuestions(test_file)

        val doShuffleTest = loadShuffleTestsSettings()
        if(doShuffleTest) {
            Collections.shuffle(questionItems)
        }
        setQuestionScreen(currentQuestion)

        answerA.setOnClickListener {
            buttonOptionClick(answerA)
        }
        answerB.setOnClickListener {
            buttonOptionClick(answerB)
        }
        answerC.setOnClickListener {
            buttonOptionClick(answerC)
        }
        answerD.setOnClickListener {
            buttonOptionClick(answerD)
        }

    }
    fun loadShuffleTestsSettings():Boolean {
        val sharedPreferences = getSharedPreferences("UserSettings", MODE_PRIVATE)
        return sharedPreferences.getBoolean("ShuffleQuestions", false)
    }

    private fun buttonOptionClick(answer: TextView){

        highlightSelectedAnswer(answer)

        val cardView = answer.parent.parent as MaterialCardView
        val isCorrect = questionItems[currentQuestion].correct == answer.text.toString()

        nextBtn = findViewById(R.id.next_quiz_button)
        nextBtn.setOnClickListener {

            if (isCorrect) {
                correctAnswered++
                cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.correctAnswer))
            } else {
                wrongAnswered++
                cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.wrongAnswer))
            }

            if(currentQuestion<questionItems.size-1){
                val handler = Handler()
                handler.postDelayed( Runnable {
                    run {
                        currentQuestion++
                        setQuestionScreen(currentQuestion)
                        resetAnswerStyles()
                    }
                },1000)
            }else{
                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra("correct", correctAnswered)
                intent.putExtra("wrong", wrongAnswered)
                intent.putExtra("testID", test_ID)
                startActivity(intent)
                finish()
            }
        }

    }

    private fun resetAnswerStyles() {
        val defaultBackground = TypedValue()
        theme.resolveAttribute(androidx.appcompat.R.attr.colorControlNormal, defaultBackground, true)

        val cardViews = listOf(answerA, answerB, answerC, answerD).map { it.parent.parent as MaterialCardView }
        cardViews.forEach { cardView ->
            cardView.setCardBackgroundColor(defaultBackground.data)
        }
    }
    private fun highlightSelectedAnswer(selectedAnswer: TextView) {
        val selectedCardView = selectedAnswer.parent.parent as MaterialCardView
        val highlightColor = TypedValue()
        theme.resolveAttribute(androidx.appcompat.R.attr.colorAccent, highlightColor, true)
        resetAnswerStyles()
        selectedCardView.setCardBackgroundColor(highlightColor.data)
    }

    private fun loadAllQuestions(fileName:String) {
        questionItems = ArrayList()
        val jsonquiz: String = loadJsonFromFile(fileName)
        try {
            val jsonObject = JSONObject(jsonquiz)
            val questions = jsonObject.getJSONArray("test")
            for (i in 0 until questions.length()) {
                val question = questions.getJSONObject(i)
                val questionsString = question.getString("question")
                val answer1String = question.getString("answer1")
                val answer2String = question.getString("answer2")
                val answer3String = question.getString("answer3")
                val answer4String = question.getString("answer4")
                val correctString = question.getString("correct")
                (questionItems as ArrayList<QuestionsItem>).add(
                    QuestionsItem(
                        questionsString,
                        answer1String,
                        answer2String,
                        answer3String,
                        answer4String,
                        correctString
                    )
                )
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun loadJsonFromFile(fileName: String):String{
        var json = ""
        try {
            val file = File(getExternalFilesDir(null), fileName)
            json = file.readText(Charsets.UTF_8)
        }catch (e: IOException){
            Log.d("LoadJsonFile", e.toString())
        }
        return json
    }
    private fun loadJsonFromAsset(s: String): String {
        var json = ""
        try {
            val inputStream = assets.open(s)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, charset("UTF-8"))
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return json
    }
    private fun setQuestionScreen(currentQuestions: Int) {
        quiztext.text = questionItems[currentQuestions].question
        answerA.text = questionItems[currentQuestions].answer1
        answerB.text = questionItems[currentQuestions].answer2
        answerC.text = questionItems[currentQuestions].answer3
        answerD.text = questionItems[currentQuestions].answer4
    }
    class QuestionsItem(
        var question:String,
        var answer1:String,
        var answer2:String,
        var answer3: String,
        var answer4:String,
        var correct:String)
}

