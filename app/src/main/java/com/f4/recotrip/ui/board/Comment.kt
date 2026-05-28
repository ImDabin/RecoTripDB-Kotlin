package com.f4.recotrip.ui.board

import com.google.firebase.Timestamp

data class Comment(
    var id: String = "",
    val content: String = "",
    val userName: String = "",
    val userUid: String = "",
    val createdAt: Timestamp? = null,
    val parentId: String? = null // null이면 일반 댓글, 아니면 대댓글
)