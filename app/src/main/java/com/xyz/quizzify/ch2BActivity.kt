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
import com.xyz.quizzify.databinding.ActivityCh2BactivityBinding
import com.xyz.quizzify.databinding.ActivityQuestionBinding

class ch2BActivity : AppCompatActivity() {

    lateinit var ch2BactivityBinding: ActivityCh2BactivityBinding

    val database = FirebaseDatabase.getInstance()
    val databaseReference = database.reference.child("ch2")

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

//    val questions = HashSet<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ch2BactivityBinding = ActivityCh2BactivityBinding.inflate(layoutInflater)
        setContentView(ch2BactivityBinding.root)

//        do {
//            val number = Random.nextInt(1, 11)
//            questions.add(number)
//        }while (questions.size < 5)

        gameLogic()

        ch2BactivityBinding.buttonFinish.setOnClickListener {

            sendScore()

        }

        ch2BactivityBinding.buttonNext.setOnClickListener {

            resetTimer()

            gameLogic()

        }

        ch2BactivityBinding.textViewA.setOnClickListener {

            pauseTimer()

            userAnswer = "a"

            if (correctAnswer == userAnswer){
                ch2BactivityBinding.textViewA.setBackgroundColor(Color.GREEN)
                userCorrect++
                ch2BactivityBinding.textViewCorrect.text = userCorrect.toString()
            }else{
                ch2BactivityBinding.textViewA.setBackgroundColor(Color.RED)
                userWrong++
                ch2BactivityBinding.textViewWrong.text = userWrong.toString()
                findAnswer()
            }

            disableClickableOptions()

        }

        ch2BactivityBinding.textViewB.setOnClickListener {

            pauseTimer()

            userAnswer = "b"

            if (correctAnswer == userAnswer){
                ch2BactivityBinding.textViewB.setBackgroundColor(Color.GREEN)
                userCorrect++
                ch2BactivityBinding.textViewCorrect.text = userCorrect.toString()
            }else{
                ch2BactivityBinding.textViewB.setBackgroundColor(Color.RED)
                userWrong++
                ch2BactivityBinding.textViewWrong.text = userWrong.toString()
                findAnswer()
            }

            disableClickableOptions()

        }

        ch2BactivityBinding.textViewC.setOnClickListener {

            pauseTimer()

            userAnswer = "c"

            if (correctAnswer == userAnswer){
                ch2BactivityBinding.textViewC.setBackgroundColor(Color.GREEN)
                userCorrect++
                ch2BactivityBinding.textViewCorrect.text = userCorrect.toString()
            }else{
                ch2BactivityBinding.textViewC.setBackgroundColor(Color.RED)
                userWrong++
                ch2BactivityBinding.textViewWrong.text = userWrong.toString()
                findAnswer()
            }

            disableClickableOptions()

        }

        ch2BactivityBinding.textViewD.setOnClickListener {

            pauseTimer()

            userAnswer = "d"

            if (correctAnswer == userAnswer){
                ch2BactivityBinding.textViewD.setBackgroundColor(Color.GREEN)
                userCorrect++
                ch2BactivityBinding.textViewCorrect.text = userCorrect.toString()
            }else{
                ch2BactivityBinding.textViewD.setBackgroundColor(Color.RED)
                userWrong++
                ch2BactivityBinding.textViewWrong.text = userWrong.toString()
                findAnswer()
            }

            disableClickableOptions()

        }
    }

    private fun gameLogic(){

        restoreOptions()

        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                questionCount = snapshot.childrenCount.toInt()

                if (questionNumber <= questionCount){

                    question = snapshot.child(questionNumber.toString()).child("q").value.toString()
                    answerA = snapshot.child(questionNumber.toString()).child("a").value.toString()
                    answerB = snapshot.child(questionNumber.toString()).child("b").value.toString()
                    answerC = snapshot.child(questionNumber.toString()).child("c").value.toString()
                    answerD = snapshot.child(questionNumber.toString()).child("d").value.toString()
                    correctAnswer = snapshot.child(questionNumber.toString()).child("answer").value.toString()

                    ch2BactivityBinding.textViewQuestions.text = question
                    ch2BactivityBinding.textViewA.text = answerA
                    ch2BactivityBinding.textViewB.text = answerB
                    ch2BactivityBinding.textViewC.text = answerC
                    ch2BactivityBinding.textViewD.text = answerD

                    ch2BactivityBinding.progressBar4.visibility = View.INVISIBLE
                    ch2BactivityBinding.linearLayoutInfo.visibility = View.VISIBLE
                    ch2BactivityBinding.linearLayoutQuestions.visibility = View.VISIBLE
                    ch2BactivityBinding.linearLayoutButtons.visibility = View.VISIBLE

                    startTimer()

                }else{

                    val dialogMessage = AlertDialog.Builder(this@ch2BActivity)
                    dialogMessage.setTitle("Quizzify")
                    dialogMessage.setMessage("Congratulations!\nYou have answered all the questions. Do you want to see the result?")
                    dialogMessage.setCancelable(false)
                    dialogMessage.setPositiveButton("See Result"){dialogWindow, position ->

                        sendScore()
                    }
                    dialogMessage.setNegativeButton("Start Again"){dialogWindow, position ->

                        val intent = Intent(this@ch2BActivity, BiologyChapterActivity::class.java)
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

            "a" -> ch2BactivityBinding.textViewA.setBackgroundColor(Color.GREEN)
            "b" -> ch2BactivityBinding.textViewB.setBackgroundColor(Color.GREEN)
            "c" -> ch2BactivityBinding.textViewC.setBackgroundColor(Color.GREEN)
            "d" -> ch2BactivityBinding.textViewD.setBackgroundColor(Color.GREEN)

        }
    }

    fun disableClickableOptions(){

        ch2BactivityBinding.textViewA.isClickable = false
        ch2BactivityBinding.textViewB.isClickable = false
        ch2BactivityBinding.textViewC.isClickable = false
        ch2BactivityBinding.textViewD.isClickable = false


    }

    fun restoreOptions(){

        ch2BactivityBinding.textViewA.setBackgroundColor(Color.WHITE)
        ch2BactivityBinding.textViewB.setBackgroundColor(Color.WHITE)
        ch2BactivityBinding.textViewC.setBackgroundColor(Color.WHITE)
        ch2BactivityBinding.textViewD.setBackgroundColor(Color.WHITE)

        ch2BactivityBinding.textViewA.isClickable = true
        ch2BactivityBinding.textViewB.isClickable = true
        ch2BactivityBinding.textViewC.isClickable = true
        ch2BactivityBinding.textViewD.isClickable = true
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
                ch2BactivityBinding.textViewQuestions.text = "Sorry, Time is up! Continue with the next question."
                timerContinue = false
            }

        }.start()

        timerContinue = true
    }

    fun updateCountDownText(){

        val remainingTime : Int = (leftTime/1000).toInt()
        ch2BactivityBinding.textViewTime.text = remainingTime.toString()

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
                val intent = Intent(this@ch2BActivity, ResultActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }
}