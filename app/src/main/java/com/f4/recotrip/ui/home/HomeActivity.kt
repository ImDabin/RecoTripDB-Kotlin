package com.f4.recotrip.ui.home
import androidx.activity.viewModels
import com.f4.recotrip.ui.plan.PlanViewModel

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import com.f4.recotrip.R
import com.f4.recotrip.ui.auth.LoginActivity
import com.f4.recotrip.ui.board.CitySelectActivity
import com.f4.recotrip.ui.plan.MyPlanActivity
import com.f4.recotrip.ui.plan.PlanActivity
import com.f4.recotrip.ui.profile.ProfileActivity
import com.f4.recotrip.ui.settings.SettingsActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class HomeActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var btnMenu: ImageButton
    private lateinit var btnStart: Button
    private lateinit var btnCheckPlan: Button
    private var isLogin = FirebaseAuth.getInstance().currentUser != null
    private val viewModel: PlanViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)
        btnMenu = findViewById(R.id.btnMenu)
        btnStart = findViewById(R.id.btnStart)


        // 햄버거 메뉴 열기
        btnMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.END)
        }

        // 출발하기 버튼
        btnStart.setOnClickListener {
            startActivity(Intent(this, PlanActivity::class.java))
        }


        // 네비게이션 메뉴 클릭 처리
        navigationView.setNavigationItemSelectedListener { menuItem ->
            handleNavigationItem(menuItem, isLogin)
            true
        }

        // 로그인 상태에 따라 로그아웃 버튼 visible or not
        val logoutButton = navigationView.menu.findItem(R.id.nav_logout)
        logoutButton.isVisible = isLogin

        // 헤더 뷰 안의 로그인 버튼 클릭 이벤트
        val headerView = navigationView.getHeaderView(0)
        val loginButton = headerView.findViewById<LinearLayout>(R.id.login_button)
        val tvLoginStatus = headerView.findViewById<TextView>(R.id.tvLoginStatus)

        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            val name = user.displayName ?: user.email ?: "사용자"
            tvLoginStatus.text = "$name 님"

            loginButton.setOnClickListener {
                // 🔥 로그인 상태 → 프로필 화면으로 이동
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            }
        } else {
            tvLoginStatus.text = "로그인"

            loginButton.setOnClickListener {
                // 🔥 비로그인 상태 → 로그인 화면으로 이동
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }

    }

    private fun handleNavigationItem(item: MenuItem, isLogin: Boolean) {
        if (isLogin) {
            when (item.itemId) {
                R.id.nav_plan -> {
                    startActivity(Intent(this, MyPlanActivity::class.java))
                }

                R.id.nav_board -> {
                    startActivity(Intent(this, CitySelectActivity::class.java))
                }

                R.id.nav_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                }

                R.id.nav_logout -> {
                    Firebase.auth.signOut()

                    if (Firebase.auth.currentUser == null) {
                        Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "로그아웃 실패!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        else { //로그인 안 했을 때
            when (item.itemId) {
                R.id.nav_plan -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                }

                R.id.nav_board -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                }

                R.id.nav_settings -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            }
        }
        drawerLayout.closeDrawer(GravityCompat.END)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END)
        } else {
            super.onBackPressed()
        }
        viewModel.cachedCityList = null
    }
}