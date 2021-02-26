package com.sklagat46.gamingapp

import android.content.IntentSender
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.BuildCompat

@Suppress("UNREACHABLE_CODE")
class MainActivity : AppCompatActivity() {
    internal var score = 0;


    internal  var gameStarted =false
    internal lateinit var countDownTimer: CountDownTimer
    internal val initialCountDownTimer:Long = 60000
    internal val countDownInterval: Long= 1000
    internal  var timeLeftOnTimer: Long =60000
    internal lateinit var btnTapMe: Button
    internal lateinit var gameScoreTv: TextView
    internal lateinit var tvTimeLeft: TextView

    companion object{
        private val TAG  = MainActivity:: class.java.simpleName
        private  const val SCORE_KEY ="SCORE_KEY"
        private  const val TIME_LEFT_KEY ="TIME_LEFT_KEY"

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate Called. the score is: $score")

        btnTapMe =findViewById<Button>(R.id.btnTapMe)
        gameScoreTv = findViewById<TextView>(R.id.tvScore)
        tvTimeLeft = findViewById<TextView>(R.id.tvTimeLeft)


        btnTapMe!!.setOnClickListener {
            val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)
            btnTapMe!!.startAnimation(bounceAnimation)
            incrementScore();
        }

        if (savedInstanceState!= null){
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeftOnTimer = savedInstanceState.getLong(TIME_LEFT_KEY)
            restoreGame()
        }else {
            resetGame()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==R.id.actionAbout){
            showInfo()
        }
        return true
    }

    private fun showInfo() {
        val dialogTitle = getString(R.string.aboutTitle )
        val dialogMessage = getString(R.string.aboutMessage)

        val builder = AlertDialog.Builder(this)
        builder.setTitle(dialogTitle)
        builder.setMessage(dialogMessage)
        builder.create().show()

    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putInt(SCORE_KEY,score)
        outState.putLong(TIME_LEFT_KEY,timeLeftOnTimer)
        countDownTimer.cancel()
        Log.d(TAG,"onSavedInstanceState Called: Saving score: $score and time left: $timeLeftOnTimer")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestroy called")
    }
    private  fun resetGame(){
        score= 0
        gameScoreTv!!.text = getString(R.string.your_score_1d, score)
        val initialTimeLeft = initialCountDownTimer/1000
        tvTimeLeft!!.text = getString(R.string.time_left_1d, initialTimeLeft)

        countDownTimer = object: CountDownTimer(initialCountDownTimer,countDownInterval){
            override fun onTick(millisUntilFinished:  Long) {
                timeLeftOnTimer = millisUntilFinished
                val timeLeft =millisUntilFinished/1000
                tvTimeLeft.text = getString(R.string.time_left_1d, timeLeft)

            }

            override fun onFinish() {
                endGame()
            }
        }
        gameStarted = false

    }
    private fun restoreGame(){
        gameScoreTv!!.text =getString(R.string.your_score_1d,score)

        val restoreTime = timeLeftOnTimer /1000
        tvTimeLeft.text = getString(R.string.time_left_1d,score)

        countDownTimer = object : CountDownTimer(timeLeftOnTimer,countDownInterval){
            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = millisUntilFinished
                val timeLeft =millisUntilFinished/1000
                tvTimeLeft.text = getString(R.string.time_left_1d, timeLeft)
            }

            override fun onFinish() {
                endGame()
            }

        }
        startedGame()
    }

    private fun incrementScore() {
        if (!gameStarted){
            startedGame()
        }
        score += 1
        val newScore = getString(R.string.your_score_1d, score)
        gameScoreTv!!.text = newScore
        val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.blink)
        gameScoreTv!!.startAnimation(bounceAnimation)

  }
    private fun startedGame(){
        countDownTimer.start()
        gameStarted = true
    }
    private fun endGame(){
        Toast.makeText(this," GameOver, $score",Toast.LENGTH_LONG).show()
        resetGame()
    }
}