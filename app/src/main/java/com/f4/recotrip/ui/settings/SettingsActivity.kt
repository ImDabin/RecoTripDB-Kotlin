package com.f4.recotrip.ui.settings

import android.content.DialogInterface
import android.os.Bundle
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.f4.recotrip.R
import java.io.BufferedReader
import java.io.InputStreamReader

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val prefs = getSharedPreferences("app_settings", MODE_PRIVATE)
        val isDarkMode = prefs.getBoolean("dark_mode", false)
        val mode = if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(mode)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity_main)

        // 다크모드 설정
        findViewById<TextView>(R.id.text_dark_mode).setOnClickListener {
            showDarkModeDialog()
        }

        // 알림 설정
        findViewById<TextView>(R.id.text_notifications).setOnClickListener {
            showNotificationDialog()
        }

        // 앱 버전 표시
        val versionText = findViewById<TextView>(R.id.text_app_version)
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        val versionName = packageInfo.versionName
        val versionCode = packageInfo.versionCode
        versionText.text = "버전 $versionName($versionCode)"

        // 서비스 이용약관
        findViewById<TextView>(R.id.text_terms).setOnClickListener {
            val content = loadAssetTextFile("policy_service.txt")
            val fragment = PolicyFragment.newInstance("서비스 이용약관", content)
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, fragment)  // add → replace
                .addToBackStack(null)
                .commit()
        }

        // 개인정보 처리방침
        findViewById<TextView>(R.id.text_privacy).setOnClickListener {
            val content = loadAssetTextFile("policy_personal.txt")
            val fragment = PolicyFragment.newInstance("개인정보 처리방침", content)
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, fragment)  // add → replace
                .addToBackStack(null)
                .commit()
        }
    }

    private fun showDarkModeDialog() {
        val dialogView = layoutInflater.inflate(R.layout.settings_dialog_toggle, null)
        val toggle = dialogView.findViewById<Switch>(R.id.dialog_switch)

        val prefs = getSharedPreferences("app_settings", MODE_PRIVATE)
        val isDarkMode = prefs.getBoolean("dark_mode", false)

        toggle.isChecked = isDarkMode
        toggle.text = if (isDarkMode) "ON" else "OFF"
        toggle.setOnCheckedChangeListener { _, isChecked ->
            toggle.text = if (isChecked) "ON" else "OFF"
        }

        AlertDialog.Builder(this)
            .setTitle("다크모드 설정")
            .setView(dialogView)
            .setPositiveButton("확인") { _: DialogInterface, _: Int ->
                prefs.edit().putBoolean("dark_mode", toggle.isChecked).apply()
                val mode = if (toggle.isChecked)
                    AppCompatDelegate.MODE_NIGHT_YES
                else
                    AppCompatDelegate.MODE_NIGHT_NO
                AppCompatDelegate.setDefaultNightMode(mode)
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun showNotificationDialog() {
        val dialogView = layoutInflater.inflate(R.layout.settings_dialog_toggle, null)
        val toggle = dialogView.findViewById<Switch>(R.id.dialog_switch)

        val prefs = getSharedPreferences("app_settings", MODE_PRIVATE)
        val isEnabled = prefs.getBoolean("notifications_enabled", true)

        toggle.isChecked = isEnabled
        toggle.text = if (isEnabled) "ON" else "OFF"
        toggle.setOnCheckedChangeListener { _, isChecked ->
            toggle.text = if (isChecked) "ON" else "OFF"
        }

        AlertDialog.Builder(this)
            .setTitle("알림 설정")
            .setView(dialogView)
            .setPositiveButton("확인") { _: DialogInterface, _: Int ->
                prefs.edit().putBoolean("notifications_enabled", toggle.isChecked).apply()
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun loadAssetTextFile(filename: String): String {
        val inputStream = assets.open(filename)
        val reader = BufferedReader(InputStreamReader(inputStream))
        return reader.readText()
    }
}