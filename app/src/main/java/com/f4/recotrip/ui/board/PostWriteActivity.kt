package com.f4.recotrip.ui.board

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.f4.recotrip.R
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth // import 추가


class PostWriteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.community_post_write_activity)

        val titleEditText = findViewById<EditText>(R.id.editTextTitle)
        val contentEditText = findViewById<EditText>(R.id.editTextContent)
        val cityEditText = findViewById<EditText>(R.id.editTextCity)
        val submitButton = findViewById<Button>(R.id.buttonSubmit)

        val mode = intent.getStringExtra("mode") ?: "write"
        val postId = intent.getStringExtra("postId")
        val title = intent.getStringExtra("title") ?: ""
        val content = intent.getStringExtra("content") ?: ""
        val city = intent.getStringExtra("city") ?: ""

        titleEditText.setText(title)
        contentEditText.setText(content)
        cityEditText.setText(city)
        cityEditText.isEnabled = false

        submitButton.setOnClickListener {
            val updatedTitle = titleEditText.text.toString().trim()
            val updatedContent = contentEditText.text.toString().trim()

            if (updatedTitle.isEmpty() || updatedContent.isEmpty()) {
                Toast.makeText(this, "제목과 내용을 입력하세요", Toast.LENGTH_SHORT).show()
            } else {
                if (mode == "edit" && postId != null) {
                    updatePost(postId, updatedTitle, updatedContent)
                } else {
                    savePost(updatedTitle, updatedContent, city)
                }
            }
        }
    }

    private fun updatePost(postId: String, title: String, content: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("posts").document(postId)
            .update(mapOf("title" to title, "content" to content))
            .addOnSuccessListener {
                Toast.makeText(this, "수정되었습니다", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "수정 실패: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun savePost(title: String, content: String, city: String) {
        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        val userName = user?.displayName ?: "익명"
        val authorUid = user?.uid ?: "unknown"

        val post = hashMapOf(
            "title" to title,
            "content" to content,
            "author" to userName,
            "authorUid" to authorUid,
            "city" to city,
            "createdAt" to Timestamp.now()
        )

        db.collection("posts").add(post)
            .addOnSuccessListener {
                Toast.makeText(this, "게시물이 등록되었습니다", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "저장 실패: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}