package com.example.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minutesworkout.databinding.ActivityHistoryBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_history)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_history)

        setSupportActionBar(binding.toolbarHistory)

        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "History"
        }
        binding.toolbarHistory.setNavigationOnClickListener {
            onBackPressed()
        }
        val historyDao = (application as WorkOutApp).db.historyDao()
        getAllCompletedDates(historyDao)
    }

    private fun getAllCompletedDates(historyDao: HistoryDao){
        lifecycleScope.launch {
            historyDao.fetchAllDates().collect{ allCompletedDates ->
                if(allCompletedDates.isNotEmpty()){
                    binding.tvHistory.visibility = View.VISIBLE
                    binding.rvHistory.visibility = View.VISIBLE
                    binding.tvNoDataAvailable.visibility = View.GONE

                    binding.rvHistory.layoutManager = LinearLayoutManager(this@HistoryActivity)

                    val dates = ArrayList<String>()
                    for (date in allCompletedDates){
                        dates.add(date.date)
                    }

                    val historyAdapter = HistoryAdapter(dates)

                    binding.rvHistory.adapter = historyAdapter
                }else{
                    binding.tvHistory.visibility = View.GONE
                    binding.rvHistory.visibility = View.GONE
                    binding.tvNoDataAvailable.visibility = View.VISIBLE
                }
            }
        }
    }
}