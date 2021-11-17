package com.resurrection.popuptranslator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.resurrection.popuptranslator.Language.ENGLISH
import com.resurrection.popuptranslator.Language.TURKISH

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var translateAPI = TranslateAPI(ENGLISH, TURKISH, "Hi",
            object : TranslateAPI.TranslateListener {
                override fun onSuccess(translatedText: String?) {
                    Toast.makeText(this@MainActivity, translatedText, Toast.LENGTH_LONG).show()
                }
                override fun onFailure(ErrorText: String?) {
                    Toast.makeText(this@MainActivity, ErrorText, Toast.LENGTH_LONG).show()
                }
            })


    }
}