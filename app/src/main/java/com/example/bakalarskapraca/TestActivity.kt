package com.example.bakalarskapraca

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val backButton : ImageButton = findViewById(R.id.backToMainBtn2)
        backButton.setOnClickListener {
            finish()
        }

        // Creating the quiz list
        val quizList = (1..11).map { QuizItem(it) }

        // Instantiating the adapter with the quiz list
        val quizAdapter = QuizAdapter(quizList, this) // Changed variable name for clarity

        // Getting the RecyclerView from the layout and setting its adapter
        val recyclerView = findViewById<RecyclerView>(R.id.testListView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = quizAdapter

    }

}

data class QuizItem(val quizNumber: Int)

class QuizAdapter(private val quizList: List<QuizItem>, private val context: Context) : RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {

    class QuizViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val test_text: TextView = view.findViewById(R.id.item_text)
        var test_progress: TextView = view.findViewById(R.id.test_progress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.test_item_layout, parent, false)
        return QuizViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val quizItem = quizList[position]
        val resName = "Teoria_${position + 1}"
        val resId = holder.itemView.context.resources.getIdentifier(resName, "string", holder.itemView.context.packageName)

        val grade:String
        if(User.testy[position] > 91)
            grade = "A"
        else if (User.testy[position] > 81)
            grade = "B"
        else if (User.testy[position] > 71)
            grade = "C"
        else if (User.testy[position] > 61)
            grade = "D"
        else if (User.testy[position] > 51)
            grade = "E"
        else if (User.testy[position] == 0)
            grade = ""
        else
            grade = "FX"


        if (resId != 0) {
            holder.test_text.text = holder.itemView.context.getString(resId)
            holder.test_progress.text = grade
        } else {
            holder.test_text.text = "Test"
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, QuizActivity::class.java).apply {
                putExtra("test_ID", quizItem.quizNumber)
                putExtra("test_file", "test${quizItem.quizNumber}.json")
                putExtra("test_name", holder.itemView.context.getString(resId))
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = quizList.size
}
