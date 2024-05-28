package com.xyz.quizzify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xyz.quizzify.databinding.ActivityHomepageBinding

class HomepageActivity : AppCompatActivity() {

    lateinit var homepageBinding: ActivityHomepageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homepageBinding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(homepageBinding.root)

        homepageBinding.cardViewMaths.setOnClickListener {

            val intent = Intent(this, MathsActivity::class.java)
            startActivity(intent)

        }

        homepageBinding.cardViewPhysics.setOnClickListener {


        }

        homepageBinding.cardViewChemistry.setOnClickListener {


        }


        homepageBinding.cardViewBiology.setOnClickListener {

            val intent = Intent(this, BiologyActivity::class.java)
            startActivity(intent)
        }

    }
}