package com.example.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.a7minutesworkout.databinding.ActivityExerciseBinding

class ExerciseActivity : AppCompatActivity() {
    lateinit var binding: ActivityExerciseBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_exercise)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_exercise)

        setSupportActionBar(binding.toolbarEX)

        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding.toolbarEX.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}