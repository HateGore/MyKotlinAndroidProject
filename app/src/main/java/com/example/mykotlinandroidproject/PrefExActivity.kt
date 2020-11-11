package com.example.mykotlinandroidproject

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_pref_ex.*

class PrefExActivity : AppCompatActivity() {
    // nameField 의 데이터를 저장할 key
    val namefieldKey = "nameField";
    val pushCheckboxKey = "pushCheckBox"
    val preference by lazy { getSharedPreferences("PrefExActivity", Context.MODE_PRIVATE)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pref_ex)
        pref_save.setOnClickListener() {
            preference.edit().putString(namefieldKey, pref_edit.text.toString()).apply()
            preference.edit().putBoolean(pushCheckboxKey, pref_checkBox.isChecked).apply()
        }

        pref_load.setOnClickListener() {
            pref_edit.setText(preference.getString(namefieldKey, ""))
            pref_checkBox.isChecked = preference.getBoolean(pushCheckboxKey, false)
        }
    }
}