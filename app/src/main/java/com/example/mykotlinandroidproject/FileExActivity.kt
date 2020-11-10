package com.example.mykotlinandroidproject

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_file_ex.*
import java.io.FileNotFoundException

class FileExActivity : AppCompatActivity() {
    var filename = "data.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_ex)

        saveBtn.setOnClickListener {
            val text = editText.text.toString()
            when {
                TextUtils.isEmpty(text) -> {
                    Toast.makeText(applicationContext, "텍스트가 액션 빔", Toast.LENGTH_LONG).show()
                }
                else -> {
                    saveToInnerStorage(text, filename)
                }
            }
        }

        loadBtn.setOnClickListener() {
            try
            {
                editText.setText(loadFromInnerStorage(filename))
            }
            catch (e: FileNotFoundException)
            {
                Toast.makeText(applicationContext, "파일의 온기가 처음부터 없었습니다.", Toast.LENGTH_LONG).show()
            }
        }
    }

    // 내부 저장소 파일 저장
    private fun saveToInnerStorage(text: String, filename: String) {
        val fileOutputStream = openFileOutput(filename, Context.MODE_PRIVATE)

        fileOutputStream.write(text.toByteArray())

        fileOutputStream.close()
    }

    // 내부 저장소 파일 불러옴
    private fun loadFromInnerStorage(filename: String): String {
        val fileInputStream = openFileInput(filename)

        return fileInputStream.reader().readText()
    }
}