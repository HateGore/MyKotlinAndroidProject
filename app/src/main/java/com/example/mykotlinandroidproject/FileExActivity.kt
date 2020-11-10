package com.example.mykotlinandroidproject

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_file_ex.*
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.util.jar.Manifest

class FileExActivity : AppCompatActivity() {
    var filename = "data.txt"

    val MY_PERMISSION_REQUEST = 999

    var granted = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_ex)
        checkPermission()

        saveBtn.setOnClickListener {
            val text = editText.text.toString()
            when {
                TextUtils.isEmpty(text) -> {
                    Toast.makeText(applicationContext, "텍스트가 액션 빔", Toast.LENGTH_LONG).show()
                }
                !isExternalStorageWritable() -> {
                    Toast.makeText(applicationContext, "외부 저장장치가 액션 빔", Toast.LENGTH_LONG).show()
                }
                else -> {
                    //saveToInnerStorage(text, filename) // 내부 저장소 파일에 저장하는 함수 호출
                    saveToExternalCustomDirectory(text)
                }
            }
        }

        loadBtn.setOnClickListener() {
            try {
//                editText.setText(loadFromInnerStorage(filename)) // editText의 텍스트를 불러온 텍스트로 설정
                editText.setText(loadFromExternalCustomDirectory(filename))
            } catch (e: FileNotFoundException) {
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

    // 외부저장장치에서 앱 전용데이터로 사용할 파일 객체를 반환하는 함수
    private fun getAppDataFileFromExternalStorage(filename: String): File {
        val dir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        } else {
            // 하위 버전에서는 직접 디렉토리 이름 입력
            File(Environment.getExternalStorageDirectory().absolutePath + "/Documents")
        }

        dir?.mkdirs()
        return File("${dir?.absolutePath}${File.separator}${filename}");
    }

    // 외부 저장장치를 사용할 수 있는지 체크하는 함수
    private fun isExternalStorageWritable(): Boolean {
        when {
            // 외부저장장치 상태가 MEDIA_MOUNTED면 사용 가능
            Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED -> return true
            else -> return false
        }
    }

    // 외부저장소 앱 전용 디렉토리에 파일로 저장하는 함수
    private fun saveToExternalStorage(text:String, filename: String) {
        val fileOutputStream = FileOutputStream(getAppDataFileFromExternalStorage(filename))
        fileOutputStream.write(text.toByteArray())
        fileOutputStream.close()
    }

    // 외부저장소 앱 전용 디렉토리에서 파일 데이터를 불러오는 함수
    private fun loadFromExternalStorage(filename: String): String {
        return FileInputStream(getAppDataFileFromExternalStorage(filename)).reader().readText()
    }

    private fun checkPermission() {
        val permissionCheck = ContextCompat.checkSelfPermission(this@FileExActivity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        when {
            permissionCheck != PackageManager.PERMISSION_GRANTED -> {
                ActivityCompat.requestPermissions(
                    this@FileExActivity,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSION_REQUEST
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray)
    {
        when(requestCode) {
            MY_PERMISSION_REQUEST -> {
                when {
                    grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                        Toast.makeText(applicationContext, "권한 신청 성공.", Toast.LENGTH_SHORT).show()
                        granted = true
                    }
                    else -> {
                        Toast.makeText(applicationContext, "권한 신청 실패.", Toast.LENGTH_SHORT).show()
                        granted = false
                    }
                }
            }
        }
    }

    private fun saveToExternalCustomDirectory(text: String, filepath: String = "/sdcard/data.txt") {
        when {
            granted -> {
                val  fileOutputStream = FileOutputStream(File(filepath))
                fileOutputStream.write(text.toByteArray())
                fileOutputStream.close()
            }
            else -> {
                Toast.makeText(applicationContext, "권한이 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 임의의 경로에 파일에서 데이터를 읽는 함수
    private fun loadFromExternalCustomDirectory(filepath: String ="/sdcard/data.txt"):String {
        when {
            granted -> { return FileInputStream(File(filepath)).reader().readText()}
            else -> {
                Toast.makeText(applicationContext, "권한이 없습니다.", Toast.LENGTH_SHORT).show()
                return ""
            }
        }
    }

}

