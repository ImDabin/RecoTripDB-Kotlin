package com.f4.recotrip.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.f4.recotrip.R

class MyCommentAdapter(
    private val commentList: List<MyCommentWithPost>,
    private val onItemClick: (MyCommentWithPost) -> Unit
) : RecyclerView.Adapter<MyCommentAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val commentTextView: TextView = view.findViewById(R.id.itemText)
        val postTitleTextView: TextView = view.findViewById(R.id.itemSubText)

        init {
            view.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(commentList[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment_with_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = commentList[position]
        holder.commentTextView.text = item.comment.content
        holder.postTitleTextView.text = "게시글: ${item.postTitle}"
    }

    override fun getItemCount(): Int = commentList.size
}
