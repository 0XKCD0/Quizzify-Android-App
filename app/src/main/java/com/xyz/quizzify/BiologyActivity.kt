package com.xyz.quizzify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xyz.quizzify.databinding.ActivityBiologyBinding

class BiologyActivity : AppCompatActivity() {

    lateinit var biologyBinding: ActivityBiologyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        biologyBinding = ActivityBiologyBinding.inflate(layoutInflater)
        setContentView(biologyBinding.root)

        biologyBinding.cardViewChapterwise.setOnClickListener {

            val intent = Intent(this, BiologyChapterActivity::class.java)
            startActivity(intent)
        }

        biologyBinding.cardViewMockTest.setOnClickListener {

            val intent = Intent(this, MockTestActivity::class .java)
            startActivity(intent)
        }
    }
}