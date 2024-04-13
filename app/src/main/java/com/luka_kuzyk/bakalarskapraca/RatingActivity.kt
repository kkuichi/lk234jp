package com.luka_kuzyk.bakalarskapraca

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObjects

class RatingActivity : AppCompatActivity() {

    private lateinit var usersRecyclerView: RecyclerView
    private lateinit var adapter: UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)

        val backToMain: ImageButton = findViewById(R.id.backToMainBtn)
        backToMain.setOnClickListener {
            finish()
        }


        usersRecyclerView = findViewById(R.id.usersRecyclerView)
        usersRecyclerView.layoutManager = LinearLayoutManager(this)

        fetchTopUsers() // Завантаження даних користувачів і оновлення RecyclerView
    }
    fun fetchTopUsers(){
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .orderBy("progress", Query.Direction.DESCENDING)
            .limit(15)
            .get()
            .addOnSuccessListener { documents ->
                var topUsers = documents.toObjects<getUser>()
                updateRecyclerView(topUsers)
            }
            .addOnFailureListener {exeption ->
                println("make rating problem: $exeption")
            }
    }

    private fun updateRecyclerView(topUsers: List<getUser>) {
        adapter = UsersAdapter(topUsers)
        usersRecyclerView.adapter = adapter
    }

}


data class getUser(
    val uid: String = "",
    val name: String = "",
    val progress: Int = 0
)

class UsersAdapter(val users: List<getUser>) : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val nameTextView: TextView = view.findViewById(R.id.user_name)
        private val progressTextView: TextView = view.findViewById(R.id.user_progress)

        fun bind(user: getUser) {
            nameTextView.text = user.name
            progressTextView.text = user.progress.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rating_item_layout, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount() = users.size
}

