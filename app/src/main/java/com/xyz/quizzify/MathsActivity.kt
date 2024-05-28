package com.xyz.quizzify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xyz.quizzify.databinding.ActivityMathsBinding

class MathsActivity : AppCompatActivity() {

    lateinit var mathsBinding: ActivityMathsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mathsBinding = ActivityMathsBinding.inflate(layoutInflater)
        setContentView(mathsBinding.root)

        mathsBinding.cardViewChapterwise.setOnClickListener {

            val intent = Intent(this, MathsChapterActivity::class.java)
            startActivity(intent)
        }
    }
}