package com.xyz.quizzify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.xyz.quizzify.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    lateinit var signUpBinding: ActivitySignUpBinding
    val auth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpBinding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = signUpBinding.root
        setContentView(view)


        signUpBinding.buttonSignIn.setOnClickListener {

            val email = signUpBinding.textEditEmailSignIn.text.toString()
            val password = signUpBinding.textEditPasswordSignIn.text.toString()

            signUpWithFirebase(email, password)

        }
    }

    fun signUpWithFirebase(email: String, password: String){

        signUpBinding.progressBarSignUp.visibility = View.VISIBLE
        signUpBinding.buttonSignIn.isClickable = false

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->

            if (task.isSuccessful){
                Toast.makeText(applicationContext, "Your account has been created", Toast.LENGTH_LONG).show()
                finish()
                signUpBinding.progressBarSignUp.visibility = View.INVISIBLE
                signUpBinding.buttonSignIn.isClickable = true

            }else{

                Toast.makeText(applicationContext, task.exception?.localizedMessage, Toast.LENGTH_LONG).show()

            }
        }

    }
}