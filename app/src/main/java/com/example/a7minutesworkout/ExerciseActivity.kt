package com.example.a7minutesworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.example.a7minutesworkout.databinding.ActivityExerciseBinding
import com.example.a7minutesworkout.databinding.DialogBinding
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var binding: ActivityExerciseBinding

    private var restTimer: CountDownTimer? = null
    private var restProgress = 0

    private var restTimeDuration: Long = 1

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0

    private var exerciseTimeDuration: Long = 1

    private lateinit var exerciseList : ArrayList<ExerciseModel>
    private var currentExercisePosition = -1

    private lateinit var textToSpeech: TextToSpeech

    private lateinit var player: MediaPlayer

    private lateinit var exerciseAdapter : ExerciseStatusAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_exercise)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_exercise)

        setSupportActionBar(binding.toolbarEX)

        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        exerciseList = Constants.defaultExerciseList()

        textToSpeech = TextToSpeech(this, this)

        binding.toolbarEX.setNavigationOnClickListener {
            customDialogForBackButton()
        }

        setupRestView()
        setupExerciseStatusRecyclerView()
    }

    override fun onBackPressed() {
        customDialogForBackButton()
//        super.onBackPressed()
    }

    private fun customDialogForBackButton(){
        val customDialog = Dialog(this)
        val dialogBinding = DialogBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.btnYes.setOnClickListener {
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }
        dialogBinding.btnNo.setOnClickListener {
            customDialog.dismiss()
        }

        customDialog.show()
    }

    private fun setupExerciseStatusRecyclerView(){
        binding.rvEx.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        exerciseAdapter = ExerciseStatusAdapter(exerciseList)
        binding.rvEx.adapter = exerciseAdapter
    }

    private fun setupRestView(){

        try {
            val soundURI = Uri.parse("android.resource://com.example.a7minutesworkout/" + R.raw.rest)
            player = MediaPlayer.create(applicationContext, soundURI)
            player.isLooping = false
            player.start()
        } catch (e: Exception){
            e.printStackTrace()
        }

        binding.fl.visibility = View.VISIBLE
        binding.tvTitle.visibility = View.VISIBLE
        binding.tvExercise.visibility = View.INVISIBLE
        binding.flExercise.visibility = View.INVISIBLE
        binding.ivImage.visibility = View.INVISIBLE
        binding.tvUpcoming.visibility = View.VISIBLE
        binding.tvUpcomingExercise.visibility = View.VISIBLE
        if (restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }

        speakOut("Rest Now")
        binding.tvUpcomingExercise.text = exerciseList[currentExercisePosition + 1].getName()
        setRestProgressBar()
    }

    private fun setupExerciseView(){
        binding.fl.visibility = View.INVISIBLE
        binding.tvTitle.visibility = View.INVISIBLE
        binding.tvExercise.visibility = View.VISIBLE
        binding.flExercise.visibility = View.VISIBLE
        binding.ivImage.visibility = View.VISIBLE
        binding.tvUpcoming.visibility = View.INVISIBLE
        binding.tvUpcomingExercise.visibility = View.INVISIBLE

        if (exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        speakOut(exerciseList[currentExercisePosition].getName())

        binding.ivImage.setImageResource(exerciseList[currentExercisePosition].getImage())
        binding.tvExercise.text = exerciseList[currentExercisePosition].getName()

        setExerciseProgressBar()
    }

    private fun setRestProgressBar(){
        binding.progressBar.progress = restProgress
        restTimer = object : CountDownTimer(restTimeDuration * 10000, 1000){
            override fun onTick(p0: Long) {
                restProgress++
                binding.progressBar.progress = 10 - restProgress
                binding.tvTimer.text = (10 - restProgress).toString()
            }

            override fun onFinish() {
                currentExercisePosition++
                exerciseList[currentExercisePosition].setIsSelected(true)
                exerciseAdapter.notifyDataSetChanged()
                setupExerciseView()
            }
        }.start()
    }

    private fun setExerciseProgressBar(){
        binding.progressBarExercise.progress = exerciseProgress
        exerciseTimer = object : CountDownTimer(exerciseTimeDuration * 30000, 1000){
            override fun onTick(p0: Long) {
                exerciseProgress++
                binding.progressBarExercise.progress = 30 - exerciseProgress
                binding.tvTimerExercise.text = (30 - exerciseProgress).toString()
            }

            override fun onFinish() {
                if (currentExercisePosition < exerciseList.size - 1){
                    exerciseList[currentExercisePosition].setIsSelected(false)
                    exerciseList[currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter.notifyDataSetChanged()
                    setupRestView()
                }else{
                    finish()
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)
                }
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }

        if (exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        player.stop()
        textToSpeech.shutdown()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS){
            val result = textToSpeech.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){

            }
        }else{

        }
    }

    private fun speakOut(text: String){
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }
}