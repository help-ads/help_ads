package kr.ac.kpu.walk3

import android.content.Context
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity() {

    var startFlag = true
    var interval : Long = 2000
    val intervalChange = interval/10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        tvSec.setText("걸음 사이 간격 : ${interval/1000.toDouble()}초")


        btnWalk.setOnClickListener {
            if(startFlag) {
                btnMinus.visibility = View.INVISIBLE
                btnPlus.visibility = View.INVISIBLE
                btnWalk.setText("STOP")

                // 안드로이드 API 26 이상일 때
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1){
                    vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(interval, 200),0))
                }
                else {
                    vibrator.vibrate(longArrayOf(interval, 200), 0)
                }


                startFlag = false
            }
            else {
                btnMinus.visibility = View.VISIBLE
                btnPlus.visibility = View.VISIBLE

                btnWalk.setText("START")
                vibrator.cancel()
                startFlag = true
            }
        }

        btnPlus.setOnClickListener{
            interval += intervalChange
            tvSec.setText("걸음 사이 간격 : ${interval/1000.toDouble()}초")
        }

        btnMinus.setOnClickListener {
            interval -= intervalChange
            tvSec.setText("걸음 사이 간격 : ${interval/1000.toDouble()}초")
        }


    }


}
