package com.f4.recotrip.ui.board

import com.google.firebase.Timestamp

data class Post(
    var id: String = "",
    val title: String = "",
    val content: String = "",
    val author: String = "",
    val authorUid: String = "",
    val city: String = "",
    val createdAt: Timestamp? = null, // 작성일 추가
    var likeCount: Int = 0,   // 추가
    var commentCount: Int = 0 // 추가
)