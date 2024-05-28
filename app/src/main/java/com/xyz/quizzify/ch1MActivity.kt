package com.xyz.quizzify

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xyz.quizzify.databinding.ActivityCh1MactivityBinding

class ch1MActivity : AppCompatActivity() {

    lateinit var ch1MactivityBinding: ActivityCh1MactivityBinding

    val database = FirebaseDatabase.getInstance()
    val databaseReference = database.reference.child("ch1M")

    var question = ""
    var answerA = ""
    var answerB = ""
    var answerC = ""
    var answerD = ""
    var correctAnswer = ""
    var questionCount = 0
    var questionNumber = 1

    var userAnswer = ""
    var userCorrect = 0
    var userWrong = 0

    lateinit var timer: CountDownTimer
    private val totalTime = 25000L
    var timerContinue = false
    var leftTime = totalTime

    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val scoreRef = database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ch1MactivityBinding = ActivityCh1MactivityBinding.inflate(layoutInflater)
        setContentView(ch1MactivityBinding.root)

        gameLogic()

        ch1MactivityBinding.buttonFinish.setOnClickListener {

            sendScore()

        }

        ch1MactivityBinding.buttonNext.setOnClickListener {

            resetTimer()

            gameLogic()

        }

        ch1MactivityBinding.textViewA.setOnClickListener {

            pauseTimer()

            userAnswer = "a"

            if (correctAnswer == userAnswer){
                ch1MactivityBinding.textViewA.setBackgroundColor(Color.GREEN)
                userCorrect++
                ch1MactivityBinding.textViewCorrect.text = userCorrect.toString()
            }else{
                ch1MactivityBinding.textViewA.setBackgroundColor(Color.RED)
                userWrong++
                ch1MactivityBinding.textViewWrong.text = userWrong.toString()
                findAnswer()
            }

            disableClickableOptions()

        }

        ch1MactivityBinding.textViewB.setOnClickListener {

            pauseTimer()

            userAnswer = "b"

            if (correctAnswer == userAnswer){
                ch1MactivityBinding.textViewB.setBackgroundColor(Color.GREEN)
                userCorrect++
                ch1MactivityBinding.textViewCorrect.text = userCorrect.toString()
            }else{
                ch1MactivityBinding.textViewB.setBackgroundColor(Color.RED)
                userWrong++
                ch1MactivityBinding.textViewWrong.text = userWrong.toString()
                findAnswer()
            }

            disableClickableOptions()

        }

        ch1MactivityBinding.textViewC.setOnClickListener {

            pauseTimer()

            userAnswer = "c"

            if (correctAnswer == userAnswer){
                ch1MactivityBinding.textViewC.setBackgroundColor(Color.GREEN)
                userCorrect++
                ch1MactivityBinding.textViewCorrect.text = userCorrect.toString()
            }else{
                ch1MactivityBinding.textViewC.setBackgroundColor(Color.RED)
                userWrong++
                ch1MactivityBinding.textViewWrong.text = userWrong.toString()
                findAnswer()
            }

            disableClickableOptions()

        }

        ch1MactivityBinding.textViewD.setOnClickListener {

            pauseTimer()

            userAnswer = "d"

            if (correctAnswer == userAnswer){
                ch1MactivityBinding.textViewD.setBackgroundColor(Color.GREEN)
                userCorrect++
                ch1MactivityBinding.textViewCorrect.text = userCorrect.toString()
            }else{
                ch1MactivityBinding.textViewD.setBackgroundColor(Color.RED)
                userWrong++
                ch1MactivityBinding.textViewWrong.text = userWrong.toString()
                findAnswer()
            }

            disableClickableOptions()

        }
    }

    private fun gameLogic(){

        restoreOptions()

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                questionCount = snapshot.childrenCount.toInt()

                if (questionNumber <= questionCount){

                    question = snapshot.child(questionNumber.toString()).child("q").value.toString()
                    answerA = snapshot.child(questionNumber.toString()).child("a").value.toString()
                    answerB = snapshot.child(questionNumber.toString()).child("b").value.toString()
                    answerC = snapshot.child(questionNumber.toString()).child("c").value.toString()
                    answerD = snapshot.child(questionNumber.toString()).child("d").value.toString()
                    correctAnswer = snapshot.child(questionNumber.toString()).child("answer").value.toString()

                    ch1MactivityBinding.textViewQuestions.text = question
                    ch1MactivityBinding.textViewA.text = answerA
                    ch1MactivityBinding.textViewB.text = answerB
                    ch1MactivityBinding.textViewC.text = answerC
                    ch1MactivityBinding.textViewD.text = answerD

                    ch1MactivityBinding.progressBar4.visibility = View.INVISIBLE
                    ch1MactivityBinding.linearLayoutInfo.visibility = View.VISIBLE
                    ch1MactivityBinding.linearLayoutQuestions.visibility = View.VISIBLE
                    ch1MactivityBinding.linearLayoutButtons.visibility = View.VISIBLE

                    startTimer()

                }else{

                    val dialogMessage = AlertDialog.Builder(this@ch1MActivity)
                    dialogMessage.setTitle("Quizzify")
                    dialogMessage.setMessage("Congratulations!\nYou have answered all the questions. Do you want to see the result?")
                    dialogMessage.setCancelable(false)
                    dialogMessage.setPositiveButton("See Result"){dialogWindow, position ->

                        sendScore()
                    }
                    dialogMessage.setNegativeButton("Start Again"){dialogWindow, position ->

                        val intent = Intent(this@ch1MActivity, BiologyChapterActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    dialogMessage.create().show()

                }

                questionNumber++
            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()


            }

        })
    }

    fun findAnswer(){

        when (correctAnswer){

            "a" -> ch1MactivityBinding.textViewA.setBackgroundColor(Color.GREEN)
            "b" -> ch1MactivityBinding.textViewB.setBackgroundColor(Color.GREEN)
            "c" -> ch1MactivityBinding.textViewC.setBackgroundColor(Color.GREEN)
            "d" -> ch1MactivityBinding.textViewD.setBackgroundColor(Color.GREEN)

        }
    }

    fun disableClickableOptions(){

        ch1MactivityBinding.textViewA.isClickable = false
        ch1MactivityBinding.textViewB.isClickable = false
        ch1MactivityBinding.textViewC.isClickable = false
        ch1MactivityBinding.textViewD.isClickable = false


    }

    fun restoreOptions(){

        ch1MactivityBinding.textViewA.setBackgroundColor(Color.WHITE)
        ch1MactivityBinding.textViewB.setBackgroundColor(Color.WHITE)
        ch1MactivityBinding.textViewC.setBackgroundColor(Color.WHITE)
        ch1MactivityBinding.textViewD.setBackgroundColor(Color.WHITE)

        ch1MactivityBinding.textViewA.isClickable = true
        ch1MactivityBinding.textViewB.isClickable = true
        ch1MactivityBinding.textViewC.isClickable = true
        ch1MactivityBinding.textViewD.isClickable = true
    }

    private fun startTimer(){

        timer = object : CountDownTimer(leftTime, 1000){
            override fun onTick(millisUntilFinished: Long) {

                leftTime=millisUntilFinished
                updateCountDownText()
            }

            override fun onFinish() {

                disableClickableOptions()
                resetTimer()
                updateCountDownText()
                ch1MactivityBinding.textViewQuestions.text = "Sorry, Time is up! Continue with the next question."
                timerContinue = false
            }

        }.start()

        timerContinue = true
    }

    fun updateCountDownText(){

        val remainingTime : Int = (leftTime/1000).toInt()
        ch1MactivityBinding.textViewTime.text = remainingTime.toString()

    }

    fun pauseTimer(){

        timer.cancel()
        timerContinue = false

    }

    fun resetTimer(){

        pauseTimer()
        leftTime = totalTime
        updateCountDownText()

    }

    fun sendScore(){

        user?.let {
            val userUID = it.uid
            scoreRef.child("scores").child(userUID).child("correct").setValue(userCorrect)
            scoreRef.child("scores").child(userUID).child("wrong").setValue(userWrong).addOnCompleteListener {

                Toast.makeText(applicationContext, "Score sent to database successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@ch1MActivity, ResultActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }
}
