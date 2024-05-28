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
import com.xyz.quizzify.databinding.ActivityQuestionBinding
import kotlin.random.Random

class QuestionActivity : AppCompatActivity() {

    lateinit var questionBinding: ActivityQuestionBinding

    val database = FirebaseDatabase.getInstance()
    val databaseReference = database.reference.child("questions")

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
        questionBinding = ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(questionBinding.root)

//        do {
//            val number = Random.nextInt(1, 11)
//            questions.add(number)
//        }while (questions.size < 5)

        gameLogic()

        questionBinding.buttonFinish.setOnClickListener {

            sendScore()

        }

        questionBinding.buttonNext.setOnClickListener {

            resetTimer()

            gameLogic()

        }

        questionBinding.textViewA.setOnClickListener {

            pauseTimer()

            userAnswer = "a"

            if (correctAnswer == userAnswer){
                questionBinding.textViewA.setBackgroundColor(Color.GREEN)
                userCorrect++
                questionBinding.textViewCorrect.text = userCorrect.toString()
            }else{
                questionBinding.textViewA.setBackgroundColor(Color.RED)
                userWrong++
                questionBinding.textViewWrong.text = userWrong.toString()
                findAnswer()
            }

            disableClickableOptions()

        }

        questionBinding.textViewB.setOnClickListener {

            pauseTimer()

            userAnswer = "b"

            if (correctAnswer == userAnswer){
                questionBinding.textViewB.setBackgroundColor(Color.GREEN)
                userCorrect++
                questionBinding.textViewCorrect.text = userCorrect.toString()
            }else{
                questionBinding.textViewB.setBackgroundColor(Color.RED)
                userWrong++
                questionBinding.textViewWrong.text = userWrong.toString()
                findAnswer()
            }

            disableClickableOptions()

        }

        questionBinding.textViewC.setOnClickListener {

            pauseTimer()

            userAnswer = "c"

            if (correctAnswer == userAnswer){
                questionBinding.textViewC.setBackgroundColor(Color.GREEN)
                userCorrect++
                questionBinding.textViewCorrect.text = userCorrect.toString()
            }else{
                questionBinding.textViewC.setBackgroundColor(Color.RED)
                userWrong++
                questionBinding.textViewWrong.text = userWrong.toString()
                findAnswer()
            }

            disableClickableOptions()

        }

        questionBinding.textViewD.setOnClickListener {

            pauseTimer()

            userAnswer = "d"

            if (correctAnswer == userAnswer){
                questionBinding.textViewD.setBackgroundColor(Color.GREEN)
                userCorrect++
                questionBinding.textViewCorrect.text = userCorrect.toString()
            }else{
                questionBinding.textViewD.setBackgroundColor(Color.RED)
                userWrong++
                questionBinding.textViewWrong.text = userWrong.toString()
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

                    questionBinding.textViewQuestions.text = question
                    questionBinding.textViewA.text = answerA
                    questionBinding.textViewB.text = answerB
                    questionBinding.textViewC.text = answerC
                    questionBinding.textViewD.text = answerD

                    questionBinding.progressBar4.visibility = View.INVISIBLE
                    questionBinding.linearLayoutInfo.visibility = View.VISIBLE
                    questionBinding.linearLayoutQuestions.visibility = View.VISIBLE
                    questionBinding.linearLayoutButtons.visibility = View.VISIBLE

                    startTimer()

                }else{

                    val dialogMessage = AlertDialog.Builder(this@QuestionActivity)
                    dialogMessage.setTitle("Quizzify")
                    dialogMessage.setMessage("Congratulations!\nYou have answered all the questions. Do you want to see the result?")
                    dialogMessage.setCancelable(false)
                    dialogMessage.setPositiveButton("See Result"){dialogWindow, position ->

                        sendScore()
                    }
                    dialogMessage.setNegativeButton("Start Again"){dialogWindow, position ->

                        val intent = Intent(this@QuestionActivity, BiologyChapterActivity::class.java)
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

            "a" -> questionBinding.textViewA.setBackgroundColor(Color.GREEN)
            "b" -> questionBinding.textViewB.setBackgroundColor(Color.GREEN)
            "c" -> questionBinding.textViewC.setBackgroundColor(Color.GREEN)
            "d" -> questionBinding.textViewD.setBackgroundColor(Color.GREEN)

        }
    }

    fun disableClickableOptions(){

        questionBinding.textViewA.isClickable = false
        questionBinding.textViewB.isClickable = false
        questionBinding.textViewC.isClickable = false
        questionBinding.textViewD.isClickable = false


    }

    fun restoreOptions(){

        questionBinding.textViewA.setBackgroundColor(Color.WHITE)
        questionBinding.textViewB.setBackgroundColor(Color.WHITE)
        questionBinding.textViewC.setBackgroundColor(Color.WHITE)
        questionBinding.textViewD.setBackgroundColor(Color.WHITE)

        questionBinding.textViewA.isClickable = true
        questionBinding.textViewB.isClickable = true
        questionBinding.textViewC.isClickable = true
        questionBinding.textViewD.isClickable = true
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
                questionBinding.textViewQuestions.text = "Sorry, Time is up! Continue with the next question."
                timerContinue = false
            }

        }.start()

        timerContinue = true
    }

    fun updateCountDownText(){

        val remainingTime : Int = (leftTime/1000).toInt()
        questionBinding.textViewTime.text = remainingTime.toString()

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
                val intent = Intent(this@QuestionActivity, ResultActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }
}