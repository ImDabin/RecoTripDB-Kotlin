package com.f4.recotrip.ui.board

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.f4.recotrip.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class PostListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostAdapter
    private val posts = mutableListOf<Post>()

    private var currentSort = "좋아요순"  // 현재 정렬 기준

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.community_post_list_activity)

        val cityName = intent.getStringExtra("cityName") ?: "Unknown"
        findViewById<TextView>(R.id.textViewSelectedCity).text = "$cityName 게시판"
        updateSortText()

        recyclerView = findViewById(R.id.recyclerViewPosts)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PostAdapter(posts)
        recyclerView.adapter = adapter

        findViewById<FloatingActionButton>(R.id.fabAddPost).setOnClickListener {
            val intent = Intent(this, PostWriteActivity::class.java)
            intent.putExtra("city", cityName)
            startActivity(intent)
        }

        findViewById<FloatingActionButton>(R.id.fabSort).setOnClickListener {
            showSortBottomSheet()
        }

        loadPosts(cityName)
    }

    override fun onResume() {
        super.onResume()
        val cityName = intent.getStringExtra("cityName") ?: "Unknown"
        loadPosts(cityName)
    }

    private fun loadPosts(city: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("posts")
            .whereEqualTo("city", city)
            .get()
            .addOnSuccessListener { result ->
                posts.clear()

                val documents = result.documents
                if (documents.isEmpty()) {
                    adapter.notifyDataSetChanged()
                    return@addOnSuccessListener
                }

                for ((index, document) in documents.withIndex()) {
                    val post = document.toObject(Post::class.java) ?: continue
                    post.id = document.id
                    posts.add(post)

                    db.collection("posts").document(post.id)
                        .collection("likes")
                        .get()
                        .addOnSuccessListener { likesSnapshot ->
                            post.likeCount = likesSnapshot.size()
                            adapter.notifyItemChanged(index)
                        }

                    db.collection("posts").document(post.id)
                        .collection("comments")
                        .get()
                        .addOnSuccessListener { commentsSnapshot ->
                            post.commentCount = commentsSnapshot.size()
                            adapter.notifyItemChanged(index)
                        }
                }

                applyCurrentSort()
            }
            .addOnFailureListener {
                Toast.makeText(this, "불러오기 실패: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun applyCurrentSort() {
        when (currentSort) {
            "좋아요순" -> posts.sortByDescending { it.likeCount }
            "댓글 많은 순" -> posts.sortByDescending { it.commentCount }
            "오래된 순" -> posts.sortBy { it.createdAt?.toDate() }
            "최신순" -> posts.sortByDescending { it.createdAt?.toDate() }
        }
        adapter.notifyDataSetChanged()
    }

    private fun updateSortText() {
        findViewById<TextView>(R.id.textCurrentSort).text = "정렬: $currentSort"
    }

    private fun showSortBottomSheet() {
        val dialogView = layoutInflater.inflate(R.layout.bottom_sheet_sort, null)
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(dialogView)

        val like = dialogView.findViewById<TextView>(R.id.optionLike)
        val comment = dialogView.findViewById<TextView>(R.id.optionComment)
        val newest = dialogView.findViewById<TextView>(R.id.optionNewest)
        val oldest = dialogView.findViewById<TextView>(R.id.optionOldest)

        // 강조 스타일
        val map = mapOf(
            "좋아요순" to like,
            "댓글 많은 순" to comment,
            "최신순" to newest,
            "오래된 순" to oldest
        )
        map[currentSort]?.setTypeface(null, Typeface.BOLD)

        like.setOnClickListener {
            currentSort = "좋아요순"
            updateSortText()
            applyCurrentSort()
            dialog.dismiss()
        }

        comment.setOnClickListener {
            currentSort = "댓글 많은 순"
            updateSortText()
            applyCurrentSort()
            dialog.dismiss()
        }

        newest.setOnClickListener {
            currentSort = "최신순"
            updateSortText()
            applyCurrentSort()
            dialog.dismiss()
        }

        oldest.setOnClickListener {
            currentSort = "오래된 순"
            updateSortText()
            applyCurrentSort()
            dialog.dismiss()
        }

        dialog.show()
    }
}
