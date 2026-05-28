package com.f4.recotrip.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.f4.recotrip.R
import com.f4.recotrip.ui.board.Comment
import com.f4.recotrip.ui.board.PostDetailActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyCommentsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyCommentAdapter
    private val commentsWithPost = mutableListOf<MyCommentWithPost>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_fragment_my_comments, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewComments)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = MyCommentAdapter(commentsWithPost) { selectedComment ->
            goToPostDetail(selectedComment.postId)
        }
        recyclerView.adapter = adapter

        loadMyComments()

        return view
    }

    private fun goToPostDetail(postId: String) {
        val intent = Intent(requireContext(), PostDetailActivity::class.java)
        intent.putExtra("postId", postId)
        startActivity(intent)
    }

    private fun loadMyComments() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        commentsWithPost.clear()

        db.collection("posts")
            .get()
            .addOnSuccessListener { postsSnapshot ->
                val postDocuments = postsSnapshot.documents

                if (postDocuments.isEmpty()) {
                    adapter.notifyDataSetChanged()
                    return@addOnSuccessListener
                }

                var loadedPosts = 0

                postDocuments.forEach { postDoc ->
                    val postId = postDoc.id
                    val postTitle = postDoc.getString("title") ?: "제목 없음"

                    db.collection("posts")
                        .document(postId)
                        .collection("comments")
                        .whereEqualTo("userUid", uid)
                        .get()
                        .addOnSuccessListener { commentsSnapshot ->
                            for (commentDoc in commentsSnapshot) {
                                val comment = commentDoc.toObject(Comment::class.java)
                                comment.id = commentDoc.id
                                commentsWithPost.add(MyCommentWithPost(comment, postTitle, postId))
                            }
                            loadedPosts++
                            if (loadedPosts == postDocuments.size) {
                                adapter.notifyDataSetChanged()
                            }
                        }
                        .addOnFailureListener {
                            loadedPosts++
                            if (loadedPosts == postDocuments.size) {
                                adapter.notifyDataSetChanged()
                            }
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "내 댓글 불러오기 실패", Toast.LENGTH_SHORT).show()
            }
    }
}
