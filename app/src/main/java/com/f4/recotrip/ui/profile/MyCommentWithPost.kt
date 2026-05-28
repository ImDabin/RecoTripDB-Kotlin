package com.f4.recotrip.ui.profile

import com.f4.recotrip.ui.board.Comment

data class MyCommentWithPost(
    val comment: Comment,
    val postTitle: String,
    val postId: String // 🔥 추가
)
