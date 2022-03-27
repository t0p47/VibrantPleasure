package com.t0p47.vibrantpleasure.test

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.agilie.circularpicker.presenter.CircularPickerContract
import com.agilie.circularpicker.ui.view.CircularPickerView
import com.agilie.circularpicker.ui.view.PickerPagerTransformer
import com.t0p47.vibrantpleasure.R
import com.t0p47.vibrantpleasure.databinding.ActivitySampleBinding

class SampleActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySampleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sample)

        binding.viewPager.clipChildren = false
        binding.viewPager.setPageTransformer(false, PickerPagerTransformer(this, 300))

        binding.btnStartStop.setOnClickListener{
            val intent = Intent(this, Sample2Activity::class.java)
            startActivity(intent)
        }

        val typeface = Typeface.createFromAsset(this.assets, "OpenSans-ExtraBold.ttf")

        binding.viewPager.onAddView(CircularPickerView(this@SampleActivity).apply {
            colors = (intArrayOf(
                Color.parseColor("#00EDE9"),
                Color.parseColor("#0087D9"),
                Color.parseColor("#8A1CC3")))
            gradientAngle = 220
            maxLapCount = 1
            centeredTypeFace = typeface
            currentValue = 1300
            maxValue = 3000
            centeredTextSize = 60f
            centeredText = "Время работы"
            valueChangedListener = (object : CircularPickerContract.Behavior.ValueChangedListener {
                override fun onValueChanged(value: Int) {

                    binding.hoursTextView.text = value.toString()

                    /*when (value) {
                        0 -> binding.hoursTextView.text = value.toString() + "0"
                        else -> binding.hoursTextView.text = value.toString()
                    }*/
                }
            })
            colorChangedListener = (object : CircularPickerContract.Behavior.ColorChangedListener {
                override fun onColorChanged(r: Int, g: Int, b: Int) {
                    binding.hoursTextView.setTextColor(Color.rgb(r, g, b))
                }
            })
        })

        binding.viewPager.onAddView(CircularPickerView(this@SampleActivity).apply {
            colors = (intArrayOf(
                Color.parseColor("#FF8D00"),
                Color.parseColor("#FF0058"),
                Color.parseColor("#920084")))
            gradientAngle = 150
            maxValue = 3000
            currentValue = 2400
            centeredTypeFace = typeface
            maxLapCount = 1
            centeredTextSize = 60f
            centeredText = "Время задержки"
            valueChangedListener = object : CircularPickerContract.Behavior.ValueChangedListener {
                override fun onValueChanged(value: Int) {

                    binding.minutesTextView.text = value.toString()

                    /*when (value) {
                        0 -> binding.minutesTextView.text = "$value 0"
                        else -> binding.minutesTextView.text = "$value мс"
                    }*/
                }
            }
            colorChangedListener = (object : CircularPickerContract.Behavior.ColorChangedListener {
                override fun onColorChanged(r: Int, g: Int, b: Int) {
                    binding.minutesTextView.setTextColor(Color.rgb(r, g, b))
                }
            })
        })
        binding.hoursTextView.apply {
            this.typeface = typeface
            text = "1300"
        }

        binding.minutesTextView.apply {
            this.typeface = typeface
            text = "2400"

        }
        setupScale()
        addPageListener()

    }

    private fun addPageListener() {
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                // empty
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                val scaleFactor = 1 - positionOffset * 0.3f
                val scrollX = -positionOffset * (binding.hoursTextView.width + binding.colonTextView.width)
                if (positionOffset > 0) {
                    // Scale
                    scaleView(scaleFactor)
                    // Translation
                    translationView(scrollX)
                    // ColorZ
                    binding.hoursTextView.setTextColor(blendColors(resources.getColor(R.color.primaryColor), resources.getColor((R.color.primaryDarkColor)), positionOffset))

                    binding.minutesTextView.apply {
                        scaleX = 0.7f + positionOffset * 0.3f
                        scaleY = 0.7f + positionOffset * 0.3f
                        setTextColor(blendColors(resources.getColor(R.color.primaryColor), resources.getColor(R.color.primaryDarkColor), positionOffset))
                    }
                }
            }

            override fun onPageSelected(position: Int) {
                // empty
            }

        })
    }

    private fun setupScale() {
        binding.minutesTextView.apply {
            scaleX = 0.7f
            scaleY = 0.7f
        }

        binding.colonTextView.apply {
            scaleX = 0.7f
            scaleY = 0.7f
        }
    }

    private fun translationView(scrollX: Float) {
        binding.hoursTextView.translationX = scrollX
        binding.colonTextView.translationX = scrollX
        binding.minutesTextView.translationX = scrollX
    }

    private fun scaleView(scaleFactor: Float) {
        binding.hoursTextView.apply {
            scaleX = scaleFactor
            scaleY = scaleFactor
        }
    }

    private fun blendColors(from: Int, to: Int, ratio: Float): Int {
        val inverseRatio = 1f - ratio

        val r = Color.red(to) * ratio + Color.red(from) * inverseRatio
        val g = Color.green(to) * ratio + Color.green(from) * inverseRatio
        val b = Color.blue(to) * ratio + Color.blue(from) * inverseRatio

        return Color.rgb(r.toInt(), g.toInt(), b.toInt())
    }
}

