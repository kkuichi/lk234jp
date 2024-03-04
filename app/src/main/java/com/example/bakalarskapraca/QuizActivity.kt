package com.example.bakalarskapraca

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.TextView
import org.json.JSONException
import org.json.JSONObject
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
    private lateinit var nextBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val quizFile = intent.getStringExtra("test_file") ?: return
        quiztext = findViewById(R.id.quizText)
        answerA = findViewById(R.id.aanswer)
        answerB = findViewById(R.id.banswer)
        answerC = findViewById(R.id.canswer)
        answerD = findViewById(R.id.danswer)

        loadAllQuestions(quizFile);
        Collections.shuffle(questionItems);
        setQuestionScreen(currentQuestion);

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


    private fun paintBtnToRegular(){
        answerA.setBackgroundColor(resources.getColor(R.color.background))
        answerB.setBackgroundColor(resources.getColor(R.color.background))
        answerC.setBackgroundColor(resources.getColor(R.color.background))
        answerD.setBackgroundColor(resources.getColor(R.color.background))
        answerA.setTextColor(resources.getColor(R.color.text_secondary_color))
        answerB.setTextColor(resources.getColor(R.color.text_secondary_color))
        answerC.setTextColor(resources.getColor(R.color.text_secondary_color))
        answerD.setTextColor(resources.getColor(R.color.text_secondary_color))

    }
    private fun buttonOptionClick(answer: TextView){
        var db_answer = "none"
        when(answer){
            answerA -> db_answer = questionItems[currentQuestion].answer1
            answerB -> db_answer = questionItems[currentQuestion].answer2
            answerC -> db_answer = questionItems[currentQuestion].answer3
            answerD -> db_answer = questionItems[currentQuestion].answer4
            else -> println("buttonOptionClick: Error in matching answerType")
        }
        paintBtnToRegular()
        answer.setBackgroundColor(Color.WHITE)

        nextBtn = findViewById(R.id.next_quiz_button)
        nextBtn.setOnClickListener {

            if(db_answer.equals(questionItems[currentQuestion].correct)){
                correctAnswered++
                answer.setBackgroundColor(Color.GREEN)
            }else{
                wrongAnswered++
                answer.setBackgroundColor(Color.RED)
            }
            answer.setTextColor(Color.WHITE)
            if(currentQuestion<questionItems.size-1){
                val handler = Handler()
                handler.postDelayed( Runnable {
                    run {
                        paintBtnToRegular()
                        currentQuestion++
                        setQuestionScreen(currentQuestion)
                    }
                },1000)
            }else{
                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra("correct", correctAnswered)
                intent.putExtra("wrong", wrongAnswered)
                startActivity(intent)
                finish()
            }
        }

    }
    private fun loadAllQuestions(quizFile:String) {
        questionItems = ArrayList()
        val jsonquiz: String = loadJsonFromAsset(quizFile)
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
//        quiztext.apply {
//            text = (questionItems.get(currentQuestions).question)
//        }
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

