package com.f4.recotrip.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.f4.recotrip.R
import com.f4.recotrip.ui.home.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_activity_login)

        val auth = Firebase.auth
        val prefs = getSharedPreferences("loginPrefs", MODE_PRIVATE)
        val emailInput = findViewById<EditText>(R.id.email_input)
        val passwordInput = findViewById<EditText>(R.id.password_input)
        val nameInputLayout = findViewById<LinearLayout>(R.id.name_layout)
        nameInputLayout.visibility = View.GONE
        val signupButton = findViewById<Button>(R.id.signup_button)
        val loginButton = findViewById<Button>(R.id.login_button)
        val rememberCheckBox = findViewById<CheckBox>(R.id.checkbox_remember)
        val forgotPasswordText = findViewById<TextView>(R.id.forgot_password_text)

        var name = ""

        // 불러오기
        emailInput.setText(prefs.getString("email", ""))
        passwordInput.setText(prefs.getString("password", ""))
        rememberCheckBox.isChecked = prefs.getBoolean("remember", false)

        // 회원가입 버튼
        signupButton.setOnClickListener {
            if (nameInputLayout.visibility == View.VISIBLE) { // 가입 하기 버튼을 눌렀을 때
                val email = emailInput.text.toString()
                val password = passwordInput.text.toString()

                name = nameInputLayout.findViewById<EditText>(R.id.name_input).text.toString()
                if (email.isBlank() || password.isBlank() || name.isBlank()) {
                    Toast.makeText(this, "모든 입력란을 채워주세요.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            val profileUpdates = com.google.firebase.auth.UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build()
                            user?.updateProfile(profileUpdates)?.addOnCompleteListener { updateTask ->
                                if (updateTask.isSuccessful) {
                                    Toast.makeText(this, "회원가입 성공! 이름: $name", Toast.LENGTH_SHORT).show()
                                    loginUser(auth, email, password)
                                    startActivity(Intent(this, HomeActivity::class.java))
                                    finish()
                                } else {
                                    Toast.makeText(this, "이름 저장 실패", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(this, "회원가입 실패: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            else { // 회원가입 버튼을 눌렀을 때
                signupButton.text = "가입 하기"
                loginButton.text = "가입 취소"
                nameInputLayout.visibility = View.VISIBLE
                nameInputLayout.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left))
                return@setOnClickListener
            }
        }

        // 로그인 버튼
        loginButton.setOnClickListener {
            if (nameInputLayout.visibility == View.VISIBLE) { // 가입 취소 버튼을 눌렀을 때
                loginButton.text = "로그인"
                signupButton.text = "회원가입"
                name = ""
                nameInputLayout.visibility = View.GONE
                nameInputLayout.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right))
                return@setOnClickListener
            }
            else {
                val email = emailInput.text.toString()
                val password = passwordInput.text.toString()

                if (rememberCheckBox.isChecked) {
                    prefs.edit()
                        .putString("email", email)
                        .putString("password", password)
                        .putBoolean("remember", true)
                        .apply()
                } else {
                    prefs.edit().clear().apply()
                }

                loginUser(auth, email, password)
            }
        }

        forgotPasswordText.setOnClickListener {
            Toast.makeText(this, "비밀번호 재설정 기능은 아직 준비 중입니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loginUser(auth: FirebaseAuth, email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "로그인 실패: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}