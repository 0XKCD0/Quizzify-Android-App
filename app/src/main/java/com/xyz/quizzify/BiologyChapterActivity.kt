package com.xyz.quizzify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xyz.quizzify.databinding.ActivityBiologyChapterBinding

class BiologyChapterActivity : AppCompatActivity() {

    lateinit var biologyChapterBinding: ActivityBiologyChapterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        biologyChapterBinding = ActivityBiologyChapterBinding.inflate(layoutInflater)
        setContentView(biologyChapterBinding.root)

        biologyChapterBinding.ch1B.setOnClickListener {

            val intent = Intent(this, QuestionActivity::class.java)
            startActivity(intent)
        }

        biologyChapterBinding.ch2B.setOnClickListener {

            val intent = Intent(this, ch2BActivity::class.java)
            startActivity(intent)
        }
    }
}