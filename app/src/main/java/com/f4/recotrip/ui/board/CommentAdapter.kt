package com.f4.recotrip.ui.board

import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.f4.recotrip.R
import com.google.firebase.auth.FirebaseAuth

class CommentAdapter(
    private val commentList: List<Comment>,
    private val onReply: (Comment) -> Unit,
    private val onEdit: (Comment) -> Unit,
    private val onDelete: (Comment) -> Unit,
    private val onReplySubmit: (Comment, String) -> Unit // 대댓글 등록 콜백 추가
) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layoutRoot: LinearLayout = itemView.findViewById(R.id.commentRoot)
        val authorText: TextView = itemView.findViewById(R.id.textCommentAuthor)
        val commentText: TextView = itemView.findViewById(R.id.textComment)
        val commentDate: TextView = itemView.findViewById(R.id.textCommentDate)
        val replyButton: TextView = itemView.findViewById(R.id.textReply)
        val moreButton: ImageButton = itemView.findViewById(R.id.buttonMore)
        val replyInputContainer: LinearLayout = itemView.findViewById(R.id.replyInputContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = commentList[position]
        holder.authorText.text = comment.userName
        holder.commentText.text = comment.content

        val formattedDate = comment.createdAt?.toDate()?.let {
            android.text.format.DateFormat.format("yyyy-MM-dd HH:mm", it)
        } ?: ""
        holder.commentDate.text = formattedDate.toString()

        val params = holder.layoutRoot.layoutParams as ViewGroup.MarginLayoutParams
        params.marginStart = if (comment.parentId != null) 60 else 0
        holder.layoutRoot.layoutParams = params

        holder.replyInputContainer.removeAllViews()

        holder.replyButton.setOnClickListener {
            onReply(comment)

            if (holder.replyInputContainer.childCount == 0) {
                val replyView = LayoutInflater.from(holder.itemView.context)
                    .inflate(R.layout.item_reply_input, holder.replyInputContainer, false)

                val editReply = replyView.findViewById<EditText>(R.id.editReply)
                val btnSubmit = replyView.findViewById<Button>(R.id.btnSubmitReply)

                btnSubmit.setOnClickListener {
                    val replyText = editReply.text.toString().trim()
                    if (replyText.isNotEmpty()) {
                        onReplySubmit(comment, replyText)
                        holder.replyInputContainer.removeAllViews()
                    }
                }

                holder.replyInputContainer.addView(replyView)
            } else {
                holder.replyInputContainer.removeAllViews()
            }
        }

        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        if (comment.userUid == currentUserUid) {
            holder.moreButton.visibility = View.VISIBLE
        } else {
            holder.moreButton.visibility = View.GONE
        }

        holder.moreButton.setOnClickListener {
            val popup = PopupMenu(holder.itemView.context, holder.moreButton)
            popup.menuInflater.inflate(R.menu.comment_options_menu, popup.menu)
            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_edit -> {
                        onEdit(comment)
                        true
                    }
                    R.id.menu_delete -> {
                        onDelete(comment)
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }

    override fun getItemCount(): Int = commentList.size
}