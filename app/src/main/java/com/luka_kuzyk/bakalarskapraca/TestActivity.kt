package com.luka_kuzyk.bakalarskapraca

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// Aktivita zodpovedná za zobrazenie zoznamu testov
class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        // Návrat na hlavnú obrazovku
        val backButton : ImageButton = findViewById(R.id.backToMainBtn2)
        backButton.setOnClickListener {
            finish()
        }

        // Vytvorenie zoznamu testov
        val quizList = (1..11).map { QuizItem(it) }

        // Vytvorenie adaptéra s listom testov
        val quizAdapter = QuizAdapter(quizList, this)

        // Získanie RecyclerView z layoutu a nastavenie adaptéra
        val recyclerView = findViewById<RecyclerView>(R.id.testListView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = quizAdapter
    }
}

// Dátová trieda predstavujúca položku v zozname testov
data class QuizItem(val quizNumber: Int)

// Adaptér pre zoznam testov
class QuizAdapter(private val quizList: List<QuizItem>, private val context: Context) : RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {

    // ViewHolder pre každú položku testu
    class QuizViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val testText: TextView = view.findViewById(R.id.item_text)
        var testProgress: TextView = view.findViewById(R.id.test_progress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.test_item_layout, parent, false)
        return QuizViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val quizItem = quizList[position]
        val resName = "Teoria_${position + 1}"
        val resId = holder.itemView.context.resources.getIdentifier(resName, "string", holder.itemView.context.packageName)

        val grade: String
        grade = when {
            User.testy[position] > 91 -> "A"
            User.testy[position] > 81 -> "B"
            User.testy[position] > 71 -> "C"
            User.testy[position] > 61 -> "D"
            User.testy[position] > 51 -> "E"
            User.testy[position] == 0 -> ""
            else -> "FX"
        }

        if (resId != 0) {
            holder.testText.text = holder.itemView.context.getString(resId)
            holder.testProgress.text = grade
        } else {
            holder.testText.text = "Test"
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
