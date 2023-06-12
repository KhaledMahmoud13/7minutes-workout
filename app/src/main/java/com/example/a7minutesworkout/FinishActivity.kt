package com.example.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.a7minutesworkout.databinding.ActivityFinishBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class FinishActivity : AppCompatActivity() {
    lateinit var binding: ActivityFinishBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_finish)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_finish)

        setSupportActionBar(binding.toolbarFinishActivity)

        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding.toolbarFinishActivity.setNavigationOnClickListener{
            onBackPressed()
        }

        binding.btnFinish.setOnClickListener {
            finish()
        }

        val historyDao = (application as WorkOutApp).db.historyDao()
        addDate(historyDao)
    }

    private fun addDate(historyDao: HistoryDao){
        val c = Calendar.getInstance()
        val dateTime = c.time

        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
        val date = sdf.format(dateTime)

        lifecycleScope.launch {
            historyDao.insert(HistoryEntity(date))
        }
    }
}