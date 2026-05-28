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

class MyPostsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostAdapter
    private val posts = mutableListOf<Post>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_fragment_my_posts, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewPosts)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = PostAdapter(posts)
        recyclerView.adapter = adapter

        loadMyPosts()

        return view
    }

    private fun loadMyPosts() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        posts.clear()

        db.collection("posts")
            .whereEqualTo("authorUid", uid)
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    adapter.notifyDataSetChanged()
                    return@addOnSuccessListener
                }

                val documents = result.documents
                var loadedCount = 0

                for ((index, doc) in documents.withIndex()) {
                    val post = doc.toObject(Post::class.java) ?: continue
                    post.id = doc.id
                    posts.add(post)

                    val postRef = db.collection("posts").document(post.id)

                    // 좋아요 갯수 세기
                    postRef.collection("likes")
                        .get()
                        .addOnSuccessListener { likesSnapshot ->
                            posts[index].likeCount = likesSnapshot.size()

                            // 댓글 갯수 세기
                            postRef.collection("comments")
                                .get()
                                .addOnSuccessListener { commentsSnapshot ->
                                    posts[index].commentCount = commentsSnapshot.size()
                                    loadedCount++

                                    if (loadedCount == documents.size) {
                                        adapter.notifyDataSetChanged()
                                    }
                                }
                                .addOnFailureListener {
                                    loadedCount++
                                    if (loadedCount == documents.size) {
                                        adapter.notifyDataSetChanged()
                                    }
                                }
                        }
                        .addOnFailureListener {
                            loadedCount++
                            if (loadedCount == documents.size) {
                                adapter.notifyDataSetChanged()
                            }
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "내 글 불러오기 실패", Toast.LENGTH_SHORT).show()
            }
    }
}
