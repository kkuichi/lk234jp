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
            startActivity(Intent(this, MainActivity::class.java))
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
        val textView: TextView = view.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return QuizViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val quizItem = quizList[position]
        holder.textView.text = "Test ${quizItem.quizNumber}"
        holder.itemView.setOnClickListener {
            val intent = Intent(context, QuizActivity::class.java).apply {
                putExtra("test_file", "test${quizItem.quizNumber}.json")
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = quizList.size
}

