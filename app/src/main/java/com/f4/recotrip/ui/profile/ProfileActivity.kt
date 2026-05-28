package com.f4.recotrip.ui.profile

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.f4.recotrip.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {

    private lateinit var tvNickname: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvPassword: TextView
    private lateinit var btnChangePassword: Button

    // 🔥 Tab과 ViewPager 추가
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: ProfilePagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_activity)

        tvNickname = findViewById(R.id.tvNickname)
        tvEmail = findViewById(R.id.tvEmail)
        tvPassword = findViewById(R.id.tvPassword)
        btnChangePassword = findViewById(R.id.btnChangePassword)

        val currentUser = FirebaseAuth.getInstance().currentUser
        tvNickname.text = "닉네임: ${currentUser?.displayName ?: "없음"}"
        tvEmail.text = "이메일: ${currentUser?.email ?: "없음"}"
        tvPassword.text = "비밀번호: ******"


        btnChangePassword.setOnClickListener {
            // TODO: 비밀번호 변경 기능
        }

        // 🔥 여기 추가!!!
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)

        adapter = ProfilePagerAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "내 글"
                1 -> "내 댓글"
                2 -> "좋아요한 글"
                else -> ""
            }
        }.attach()
    }
}
