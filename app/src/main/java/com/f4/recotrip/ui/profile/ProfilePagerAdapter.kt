package com.f4.recotrip.ui.profile

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ProfilePagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 3  // 탭 3개 (내 글, 내 댓글, 좋아요한 글)

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MyPostsFragment()
            1 -> MyCommentsFragment()
            2 -> MyLikesFragment()
            else -> throw IllegalStateException("Invalid tab position")
        }
    }
}
