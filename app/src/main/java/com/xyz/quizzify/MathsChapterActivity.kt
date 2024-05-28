package com.xyz.quizzify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xyz.quizzify.databinding.ActivityMathsChapterBinding

class MathsChapterActivity : AppCompatActivity() {

    lateinit var mathsChapterBinding: ActivityMathsChapterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mathsChapterBinding = ActivityMathsChapterBinding.inflate(layoutInflater)
        setContentView(mathsChapterBinding.root)

        mathsChapterBinding.ch1M.setOnClickListener {

            val intent = Intent(this, ch1MActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}