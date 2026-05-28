package com.f4.recotrip.ui.board

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.f4.recotrip.R

class PostAdapter(private val postList: List<Post>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.textTitle)
        val content: TextView = view.findViewById(R.id.textContent)
        val date: TextView = view.findViewById(R.id.textDate)
        val author: TextView = view.findViewById(R.id.textAuthor)
        val likeCount: TextView = view.findViewById(R.id.textLikeCount)
        val commentCount: TextView = view.findViewById(R.id.textCommentCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postList[position]
        holder.title.text = post.title

        // 🔥 게시글 내용 앞부분만 표시 (50자 + ...)
        val preview = if (post.content.length > 50) {
            post.content.substring(0, 50) + "..."
        } else {
            post.content
        }
        holder.content.text = preview

        holder.author.text = post.author

        val formattedDate = post.createdAt?.toDate()?.let {
            android.text.format.DateFormat.format("yyyy-MM-dd HH:mm", it)
        } ?: "작성일 없음"
        holder.date.text = formattedDate.toString()

        holder.likeCount.text = "좋아요 ${post.likeCount}"
        holder.commentCount.text = "댓글 ${post.commentCount}"

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, PostDetailActivity::class.java).apply {
                putExtra("postId", post.id)
                putExtra("title", post.title)
                putExtra("content", post.content)
                putExtra("author", post.author)
                putExtra("city", post.city)
                putExtra("createdAt", post.createdAt)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = postList.size
}
