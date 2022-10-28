package com.example.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.a7minutesworkout.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    companion object{
        private const val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW"
        private const val US_UNITS_VIEW = "US_UNITS_VIEW"
    }

    private var currentVisibleView : String = METRIC_UNITS_VIEW

    private lateinit var binding: ActivityBmiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bmi)
//        setContentView(R.layout.activity_bmi)

        setSupportActionBar(binding.toolbarBMI)

        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "BMI"
        }
        binding.toolbarBMI.setNavigationOnClickListener {
            onBackPressed()
        }

        makeVisibleMetricUnitsView()

        binding.rgUnits.setOnCheckedChangeListener{ _ , checkedId : Int ->
            if (checkedId == R.id.rbMetricUnits){
                makeVisibleMetricUnitsView()
            }else{
                makeVisibleUSUnitsView()
            }
        }

        binding.btnCalc.setOnClickListener {
            calculateUnits()
        }
    }

    private fun makeVisibleMetricUnitsView(){
        currentVisibleView = METRIC_UNITS_VIEW
        binding.tilWeight.visibility = View.VISIBLE
        binding.tilHeight.visibility = View.VISIBLE
        binding.tilUsMetricUnitWeight.visibility = View.GONE
        binding.tilMetricUsUnitHeightFeet.visibility = View.GONE
        binding.tilMetricUsUnitHeightInch.visibility = View.GONE

        binding.etWeight.text!!.clear()
        binding.etHeight.text!!.clear()

        binding.BMIResult.visibility = View.INVISIBLE
    }
    private fun makeVisibleUSUnitsView(){
        currentVisibleView = US_UNITS_VIEW
        binding.tilWeight.visibility = View.INVISIBLE
        binding.tilHeight.visibility = View.INVISIBLE
        binding.tilUsMetricUnitWeight.visibility = View.VISIBLE
        binding.tilMetricUsUnitHeightFeet.visibility = View.VISIBLE
        binding.tilMetricUsUnitHeightInch.visibility = View.VISIBLE

        binding.etUsMetricUnitWeight.text!!.clear()
        binding.etUsMetricUnitHeightFeet.text!!.clear()
        binding.etUsMetricUnitHeightInch.text!!.clear()

        binding.BMIResult.visibility = View.INVISIBLE
    }

    private fun displayBMIResult(bmi: Float) {

        val bmiLabel: String
        val bmiDescription: String

        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0
        ) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops!You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0
        ) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0
        ) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0
        ) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0
        ) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0
        ) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        //Use to set the result layout visible
        binding.BMIResult.visibility = View.VISIBLE

        // This is used to round the result value to 2 decimal values after "."
        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        binding.tvBMIValue.text = bmiValue // Value is set to TextView
        binding.tvBMIType.text = bmiLabel // Label is set to TextView
        binding.tvBMIDescription.text = bmiDescription // Description is set to TextView
    }

    private fun validateMetricUnits(): Boolean{
        var isValid = true

        if (binding.etWeight.text.toString().isEmpty()){
            isValid = false
        }
        if (binding.etHeight.text.toString().isEmpty()){
            isValid = false
        }

        return isValid
    }

    private fun calculateUnits(){
        if (currentVisibleView == METRIC_UNITS_VIEW){
            if (validateMetricUnits()){
                val heightValue: Float = binding.etHeight.text.toString().toFloat() / 100
                val weightValue: Float = binding.etWeight.text.toString().toFloat()

                val bmi = weightValue / (heightValue * heightValue)

                displayBMIResult(bmi)
            }else{
                binding.etWeight.error = "Required"
                binding.etHeight.error = "Required"
            }
        }else{
            if (validateUsUnits()){
                val usUnitHeightValueFeet: String = binding.etUsMetricUnitHeightFeet.text.toString()
                val usUnitHeightValueInch: String = binding.etUsMetricUnitHeightInch.text.toString()
                val usUnitWeightValue: Float = binding.etUsMetricUnitWeight.text.toString().toFloat()
                val heightValue = usUnitHeightValueInch.toFloat() + usUnitHeightValueFeet.toFloat() * 12
                val bmi = 703 * (usUnitWeightValue / (heightValue * heightValue))
                displayBMIResult(bmi)
            }else{
                binding.etUsMetricUnitWeight.error = "Required"
                binding.etUsMetricUnitHeightFeet.error = "Required"
                binding.etUsMetricUnitHeightInch.error = "Required"
            }
        }
    }

    private fun validateUsUnits(): Boolean{
        var isValid = true

        if (binding.etUsMetricUnitWeight.text.toString().isEmpty()){
            isValid = false
        }
        if (binding.etUsMetricUnitHeightFeet.toString().isEmpty()){
            isValid = false
        }
        if (binding.etUsMetricUnitHeightInch.toString().isEmpty()){
            isValid = false
        }
        return isValid
    }
}