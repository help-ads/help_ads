package kr.ac.kpu.walktogether


import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_walking.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class WalkingFragment : Fragment() {

    var startFlag = true
    var interval : Long = 2000
    var result : Double = 0.0
    val intervalChange = interval/10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_walking, container, false)
        val bundle = getArguments();
        if (bundle != null) {
            result = bundle.getDouble("result")
            interval = (result*1000).toLong()
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        val vibrator = activity?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        tvSec.setText("보폭 : ${interval/1000.toDouble()}초")


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
            tvSec.setText("보폭 : ${interval/1000.toDouble()}초")
        }

        btnMinus.setOnClickListener {
            interval -= intervalChange
            tvSec.setText("보폭 : ${interval/1000.toDouble()}초")
        }
    }
}
