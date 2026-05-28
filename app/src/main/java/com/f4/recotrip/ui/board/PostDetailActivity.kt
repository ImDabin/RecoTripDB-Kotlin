package com.f4.recotrip.ui.board

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.f4.recotrip.R
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class PostDetailActivity : AppCompatActivity() {

    private lateinit var commentAdapter: CommentAdapter
    private val allComments = mutableListOf<Comment>()
    private val displayComments = mutableListOf<Comment>()
    private lateinit var postId: String
    private lateinit var city: String
    private var replyingTo: Comment? = null
    private lateinit var btnLike: ImageButton
    private var liked = false
    private lateinit var textLikeCount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.community_post_comment_activity)

        postId = intent.getStringExtra("postId") ?: return
        city = intent.getStringExtra("city") ?: ""

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewComments)
        val inputComment = findViewById<EditText>(R.id.editTextComment)
        val btnAddComment = findViewById<Button>(R.id.buttonAddComment)
        val btnEdit = findViewById<Button>(R.id.buttonEditPost)
        val btnDelete = findViewById<Button>(R.id.buttonDeletePost)
        btnLike = findViewById(R.id.buttonLike)
        textLikeCount = findViewById(R.id.textLikeCount)

        findViewById<TextView>(R.id.detail_title).text = intent.getStringExtra("title") ?: ""
        findViewById<TextView>(R.id.detail_content).text = intent.getStringExtra("content") ?: ""
        findViewById<TextView>(R.id.detail_author).text = "작성자: ${intent.getStringExtra("author") ?: "익명"}"

        recyclerView.layoutManager = LinearLayoutManager(this)
        commentAdapter = CommentAdapter(
            displayComments,
            this::replyToComment,
            this::editCommentDialog,
            this::confirmDeleteComment,
            this::submitReply // 대댓글 등록 처리 함수
        )
        recyclerView.adapter = commentAdapter

        btnAddComment.setOnClickListener {
            val text = inputComment.text.toString().trim()
            if (text.isNotEmpty()) {
                val parentId = replyingTo?.id
                val user = FirebaseAuth.getInstance().currentUser
                val userName = user?.displayName ?: "익명"
                addComment(text, userName, parentId)
                inputComment.text.clear()
                replyingTo = null
            }
        }

        btnEdit.setOnClickListener {
            val intent = Intent(this, PostWriteActivity::class.java).apply {
                putExtra("mode", "edit")
                putExtra("postId", postId)
                putExtra("title", findViewById<TextView>(R.id.detail_title).text.toString())
                putExtra("content", findViewById<TextView>(R.id.detail_content).text.toString())
                putExtra("city", city)
            }
            startActivity(intent)
        }

        btnDelete.setOnClickListener {
            FirebaseFirestore.getInstance().collection("posts").document(postId)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "삭제되었습니다", Toast.LENGTH_SHORT).show()
                    finish()
                }
        }

        val currentUid = FirebaseAuth.getInstance().currentUser?.uid
        val likeRef = FirebaseFirestore.getInstance()
            .collection("posts").document(postId)
            .collection("likes").document(currentUid!!)

        likeRef.get().addOnSuccessListener { doc ->
            liked = doc.exists()
            updateLikeIcon()
        }

        btnLike.setOnClickListener {
            if (liked) {
                likeRef.delete().addOnSuccessListener {
                    liked = false
                    updateLikeIcon()
                    loadLikeCount()
                }
            } else {
                likeRef.set(mapOf("likedAt" to Timestamp.now())).addOnSuccessListener {
                    liked = true
                    updateLikeIcon()
                    loadLikeCount()
                }
            }
        }

        loadComments()
    }

    override fun onResume() {
        super.onResume()
        liked = false
        reloadPost()
        reloadLikeState()
        loadLikeCount()
    }

    private fun reloadPost() {
        FirebaseFirestore.getInstance()
            .collection("posts").document(postId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    findViewById<TextView>(R.id.detail_title).text = document.getString("title") ?: ""
                    findViewById<TextView>(R.id.detail_content).text = document.getString("content") ?: ""
                    findViewById<TextView>(R.id.detail_author).text = "작성자: ${document.getString("author") ?: "익명"}"

                    val timestamp = document.getTimestamp("createdAt")
                    val formattedDate = timestamp?.toDate()?.let {
                        android.text.format.DateFormat.format("yyyy-MM-dd HH:mm", it)
                    } ?: "작성일 없음"
                    findViewById<TextView>(R.id.detail_date).text = "작성일: $formattedDate"

                    val currentUid = FirebaseAuth.getInstance().currentUser?.uid
                    val authorUid = document.getString("authorUid")
                    val btnEdit = findViewById<Button>(R.id.buttonEditPost)
                    val btnDelete = findViewById<Button>(R.id.buttonDeletePost)
                    if (authorUid == currentUid) {
                        btnEdit.visibility = View.VISIBLE
                        btnDelete.visibility = View.VISIBLE
                    } else {
                        btnEdit.visibility = View.GONE
                        btnDelete.visibility = View.GONE
                    }
                }
            }
    }

    private fun reloadLikeState() {
        val currentUid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val likeRef = FirebaseFirestore.getInstance()
            .collection("posts").document(postId)
            .collection("likes").document(currentUid)
        likeRef.get().addOnSuccessListener { doc ->
            liked = doc.exists()
            updateLikeIcon()
        }
    }

    private fun loadLikeCount() {
        FirebaseFirestore.getInstance()
            .collection("posts").document(postId)
            .collection("likes")
            .get()
            .addOnSuccessListener { result ->
                val count = result.size()
                textLikeCount.text = "좋아요 ${count}개"
            }
    }

    private fun updateLikeIcon() {
        if (liked) {
            btnLike.setImageResource(R.drawable.ic_favorite_filled)
        } else {
            btnLike.setImageResource(R.drawable.ic_favorite_border)
        }
    }

    private fun loadComments() {
        FirebaseFirestore.getInstance()
            .collection("posts").document(postId)
            .collection("comments")
            .orderBy("createdAt", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { result ->
                allComments.clear()
                for (doc in result) {
                    val comment = doc.toObject(Comment::class.java)
                    comment.id = doc.id
                    allComments.add(comment)
                }
                displayComments.clear()
                allComments.filter { it.parentId == null }.forEach { root ->
                    displayComments.add(root)
                    addRepliesRecursively(root.id)
                }
                commentAdapter.notifyDataSetChanged()
            }
    }

    private fun addComment(content: String, userName: String, parentId: String?) {
        val user = FirebaseAuth.getInstance().currentUser
        val userNameFinal = user?.displayName ?: "익명"
        val userUid = user?.uid ?: "unknown"
        val comment = hashMapOf(
            "content" to content,
            "userName" to userNameFinal,
            "userUid" to userUid,
            "createdAt" to Timestamp.now(),
            "parentId" to parentId
        )
        FirebaseFirestore.getInstance()
            .collection("posts").document(postId)
            .collection("comments")
            .add(comment)
            .addOnSuccessListener {
                loadComments()
            }
    }

    private fun addRepliesRecursively(parentId: String) {
        val children = allComments.filter { it.parentId == parentId }
        for (child in children) {
            displayComments.add(child)
            addRepliesRecursively(child.id)
        }
    }

    private fun submitReply(parentComment: Comment, replyText: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val userName = user?.displayName ?: "익명"
        val userUid = user?.uid ?: "unknown"
        val reply = hashMapOf(
            "content" to replyText,
            "userName" to userName,
            "userUid" to userUid,
            "createdAt" to Timestamp.now(),
            "parentId" to parentComment.id
        )
        FirebaseFirestore.getInstance()
            .collection("posts").document(postId)
            .collection("comments")
            .add(reply)
            .addOnSuccessListener {
                loadComments()
                replyingTo = null
            }
    }

    private fun replyToComment(comment: Comment) {
        replyingTo = comment
        Toast.makeText(this, "답글 입력 중...", Toast.LENGTH_SHORT).show()
    }

    private fun editCommentDialog(comment: Comment) {
        val input = EditText(this)
        input.setText(comment.content)
        AlertDialog.Builder(this)
            .setTitle("댓글 수정")
            .setView(input)
            .setPositiveButton("저장") { _, _ ->
                val newText = input.text.toString().trim()
                if (newText.isNotEmpty()) {
                    FirebaseFirestore.getInstance()
                        .collection("posts").document(postId)
                        .collection("comments").document(comment.id)
                        .update("content", newText)
                        .addOnSuccessListener {
                            loadComments()
                        }
                }
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun confirmDeleteComment(comment: Comment) {
        AlertDialog.Builder(this)
            .setTitle("댓글 삭제")
            .setMessage("이 댓글을 삭제하시겠습니까?")
            .setPositiveButton("삭제") { _, _ ->
                FirebaseFirestore.getInstance()
                    .collection("posts").document(postId)
                    .collection("comments").document(comment.id)
                    .delete()
                    .addOnSuccessListener {
                        loadComments()
                    }
            }
            .setNegativeButton("취소", null)
            .show()
    }
}