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
import com.xyz.quizzify.databinding.ActivityMockTestBinding

class MockTestActivity : AppCompatActivity() {

    lateinit var mockTestBinding: ActivityMockTestBinding

    val database = FirebaseDatabase.getInstance()
    val databaseReference = database.reference.child("mtB")

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
        mockTestBinding = ActivityMockTestBinding.inflate(layoutInflater)
        setContentView(mockTestBinding.root)

        gameLogic()

        mockTestBinding.buttonFinish.setOnClickListener {

            sendScore()

        }

        mockTestBinding.buttonNext.setOnClickListener {

            resetTimer()

            gameLogic()

        }

        mockTestBinding.textViewA.setOnClickListener {

            pauseTimer()

            userAnswer = "a"

            if (correctAnswer == userAnswer){
                mockTestBinding.textViewA.setBackgroundColor(Color.GREEN)
                userCorrect++
                mockTestBinding.textViewCorrect.text = userCorrect.toString()
            }else{
                mockTestBinding.textViewA.setBackgroundColor(Color.RED)
                userWrong++
                mockTestBinding.textViewWrong.text = userWrong.toString()
                findAnswer()
            }

            disableClickableOptions()

        }

        mockTestBinding.textViewB.setOnClickListener {

            pauseTimer()

            userAnswer = "b"

            if (correctAnswer == userAnswer){
                mockTestBinding.textViewB.setBackgroundColor(Color.GREEN)
                userCorrect++
                mockTestBinding.textViewCorrect.text = userCorrect.toString()
            }else{
                mockTestBinding.textViewB.setBackgroundColor(Color.RED)
                userWrong++
                mockTestBinding.textViewWrong.text = userWrong.toString()
                findAnswer()
            }

            disableClickableOptions()

        }

        mockTestBinding.textViewC.setOnClickListener {

            pauseTimer()

            userAnswer = "c"

            if (correctAnswer == userAnswer){
                mockTestBinding.textViewC.setBackgroundColor(Color.GREEN)
                userCorrect++
                mockTestBinding.textViewCorrect.text = userCorrect.toString()
            }else{
                mockTestBinding.textViewC.setBackgroundColor(Color.RED)
                userWrong++
                mockTestBinding.textViewWrong.text = userWrong.toString()
                findAnswer()
            }

            disableClickableOptions()

        }

        mockTestBinding.textViewD.setOnClickListener {

            pauseTimer()

            userAnswer = "d"

            if (correctAnswer == userAnswer){
                mockTestBinding.textViewD.setBackgroundColor(Color.GREEN)
                userCorrect++
                mockTestBinding.textViewCorrect.text = userCorrect.toString()
            }else{
                mockTestBinding.textViewD.setBackgroundColor(Color.RED)
                userWrong++
                mockTestBinding.textViewWrong.text = userWrong.toString()
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

                    mockTestBinding.textViewQuestions.text = question
                    mockTestBinding.textViewA.text = answerA
                    mockTestBinding.textViewB.text = answerB
                    mockTestBinding.textViewC.text = answerC
                    mockTestBinding.textViewD.text = answerD

                    mockTestBinding.progressBar4.visibility = View.INVISIBLE
                    mockTestBinding.linearLayoutInfo.visibility = View.VISIBLE
                    mockTestBinding.linearLayoutQuestions.visibility = View.VISIBLE
                    mockTestBinding.linearLayoutButtons.visibility = View.VISIBLE

                    startTimer()

                }else{

                    val dialogMessage = AlertDialog.Builder(this@MockTestActivity)
                    dialogMessage.setTitle("Quizzify")
                    dialogMessage.setMessage("Congratulations!\nYou have answered all the questions. Do you want to see the result?")
                    dialogMessage.setCancelable(false)
                    dialogMessage.setPositiveButton("See Result"){dialogWindow, position ->

                        sendScore()
                    }
                    dialogMessage.setNegativeButton("Start Again"){dialogWindow, position ->

                        val intent = Intent(this@MockTestActivity, BiologyChapterActivity::class.java)
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

            "a" -> mockTestBinding.textViewA.setBackgroundColor(Color.GREEN)
            "b" -> mockTestBinding.textViewB.setBackgroundColor(Color.GREEN)
            "c" -> mockTestBinding.textViewC.setBackgroundColor(Color.GREEN)
            "d" -> mockTestBinding.textViewD.setBackgroundColor(Color.GREEN)

        }
    }

    fun disableClickableOptions(){

        mockTestBinding.textViewA.isClickable = false
        mockTestBinding.textViewB.isClickable = false
        mockTestBinding.textViewC.isClickable = false
        mockTestBinding.textViewD.isClickable = false


    }

    fun restoreOptions(){

        mockTestBinding.textViewA.setBackgroundColor(Color.WHITE)
        mockTestBinding.textViewB.setBackgroundColor(Color.WHITE)
        mockTestBinding.textViewC.setBackgroundColor(Color.WHITE)
        mockTestBinding.textViewD.setBackgroundColor(Color.WHITE)

        mockTestBinding.textViewA.isClickable = true
        mockTestBinding.textViewB.isClickable = true
        mockTestBinding.textViewC.isClickable = true
        mockTestBinding.textViewD.isClickable = true
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
                mockTestBinding.textViewQuestions.text = "Sorry, Time is up! Continue with the next question."
                timerContinue = false
            }

        }.start()

        timerContinue = true
    }

    fun updateCountDownText(){

        val remainingTime : Int = (leftTime/1000).toInt()
        mockTestBinding.textViewTime.text = remainingTime.toString()

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
                val intent = Intent(this@MockTestActivity, ResultActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}