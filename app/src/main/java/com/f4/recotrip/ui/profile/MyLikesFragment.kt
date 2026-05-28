package com.f4.recotrip.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.f4.recotrip.R
import com.f4.recotrip.ui.board.Post
import com.f4.recotrip.ui.board.PostAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyLikesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostAdapter
    private val likedPosts = mutableListOf<Post>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_fragment_my_likes, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewLikes)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = PostAdapter(likedPosts)
        recyclerView.adapter = adapter

        loadLikedPosts()

        return view
    }

    private fun loadLikedPosts() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        likedPosts.clear()

        db.collection("posts")
            .get()
            .addOnSuccessListener { postsSnapshot ->
                if (postsSnapshot.isEmpty) {
                    adapter.notifyDataSetChanged()
                    return@addOnSuccessListener
                }

                var checkedPosts = 0

                postsSnapshot.forEach { postDoc ->
                    val postId = postDoc.id

                    db.collection("posts").document(postId)
                        .collection("likes")
                        .document(uid)
                        .get()
                        .addOnSuccessListener { likeDoc ->
                            if (likeDoc.exists()) {
                                val post = postDoc.toObject(Post::class.java)
                                post?.id = postDoc.id

                                val postRef = db.collection("posts").document(post.id)

                                // 좋아요 수 가져오기
                                postRef.collection("likes")
                                    .get()
                                    .addOnSuccessListener { likesSnapshot ->
                                        post.likeCount = likesSnapshot.size()

                                        // 댓글 수 가져오기
                                        postRef.collection("comments")
                                            .get()
                                            .addOnSuccessListener { commentsSnapshot ->
                                                post.commentCount = commentsSnapshot.size()
                                                likedPosts.add(post)
                                                checkedPosts++
                                                if (checkedPosts == postsSnapshot.size()) {
                                                    adapter.notifyDataSetChanged()
                                                }
                                            }
                                            .addOnFailureListener {
                                                checkedPosts++
                                                if (checkedPosts == postsSnapshot.size()) {
                                                    adapter.notifyDataSetChanged()
                                                }
                                            }
                                    }
                                    .addOnFailureListener {
                                        checkedPosts++
                                        if (checkedPosts == postsSnapshot.size()) {
                                            adapter.notifyDataSetChanged()
                                        }
                                    }
                            } else {
                                checkedPosts++
                                if (checkedPosts == postsSnapshot.size()) {
                                    adapter.notifyDataSetChanged()
                                }
                            }
                        }
                        .addOnFailureListener {
                            checkedPosts++
                            if (checkedPosts == postsSnapshot.size()) {
                                adapter.notifyDataSetChanged()
                            }
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "좋아요한 글 불러오기 실패", Toast.LENGTH_SHORT).show()
            }
    }

}
