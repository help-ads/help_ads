package kr.ac.kpu.walktogether

import android.content.Context
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_record.*
import java.util.*
import kotlin.concurrent.timer


/**
 * A simple [Fragment] subclass.
 *
 */

class RecordFragment : Fragment() {


    private var time = 0 //시간을 계산할 변수를 0으로 초기화
    private var timerTask: Timer? = null //Timer 타입의 timerTsk를 null을 허용하도록 선언(Timer 객체를 변수에 저장)
    private var isRunning = false //시작과 일시정지 이벤트를 구현하고 외부에 flag 역할을 할 isRunning 변수 선언
    private var lap = 1 //추가되는 랩 타임 번호
    private var count = 0 //발자국 수 세기 위한 변수
    private var arr = Array(10,{0.0})
    private var result : Double = 0.0
    private lateinit var vibrator : Vibrator

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("RecordFrag", "onCreate()")
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_record, container, false)
        vibrator = activity?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        return view
    }

    override fun onResume() {
        super.onResume()
        //시작 버튼(startFab)에 이벤트 연결
        startFab?.setOnClickListener{
            Log.d("RecordFrag", "start()")
            count++
            if(count == 1){
                start() //타이머 시간 흐르도록
            } else if(count < 11) {
                recordLapTime() //기록
            }
            else{ // 발자국 10개 입력시 -> 진동 울리도록
                recordLapTime() //기록
                endRecord()
            }
        }

        //초기화 버튼(resetFab)에 reset() 메서드 연결
        resetFab?.setOnClickListener{
            reset()
        }
    }

    private fun start(){ //추후에 timer를 취소하려면 timer를 실행하고 반환도는 값을 저장할 필요가 있음

        //startFab.setImageResource(R.drawable.ic_directions_walk_black_24dp) //일시정지로 이미지 변경


        timerTask = timer(period = 10) {
            //0.01초마다 작업하는 timer
            time++ //time 변수를 1씩 증가
            val sec = time / 100
            val milli = time % 100

            activity?.runOnUiThread {
                //UI 갱신
                secTextView.text = "$sec"
                milliTextView.text = "$milli"
            }
        }





    }

    //pause(): 일시정지 메소드
    private fun pause(){
        //startFab.setImageResource(R.drawable.ic_directions_run_black_24dp) //시작 이미지로 교체

        timerTask?.cancel() //타이머 취소
    }

    //recordLapTime(): 랩 타임을 기록하고 화면에 표시하는 메서드
    private fun recordLapTime(){
        val lapTime = this.time //현재 시간 저장
        val textView = TextView(activity) //텍스트 뷰 생성
        textView.text = "$lap 발자국 : ${lapTime / 100}.${lapTime % 100}" //1 발자국국 5.35초 처럼 출력되도록 설정
        arr[lap-1] = lapTime/100.toDouble()
        //맨 위에 랩타임 추가
        lapLayout.addView(textView, 0) //리니어 레이아웃에 랩타임 추가
        lap++ //랩 타임 번호 증가
    }

    //reset(): 타이머를 초기화 하는 메서드
    private fun reset(){
        Log.d("RecordFrag", "reset() $time, $isRunning, $count")
        timerTask?.cancel() //실행중인 타이머 취소

        //모든 변수 초기화
        time = 0
        count = 0
        //startFab.setImageResource(R.drawable.ic_directions_run_black_24dp)
        secTextView.text = "0"
        milliTextView.text = "00"
        //flag = false

        //모든 랩타임을 제거
        lapLayout.removeAllViews()
        lap = 1
        pause()
    }

    // endRecord() : 기록을 종료하고 내용을 다음프레그먼트로 보내는 메서드
    private fun endRecord() {
        pause() //타이머 중지   -> isRuning = true 상태 -> 안 먹힘
        vibrator.vibrate(500)
        for(i in 1..9){
            result += arr[i]-arr[i-1]
        }
        result = result/9

        val bundle =  Bundle()
        bundle.putDouble("result", result)
        Log.d("gkgkgk", "$result")

        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()

        //결과 프래그먼트 교체
        val walkingFragment = WalkingFragment()
        walkingFragment.setArguments(bundle)
        fragmentTransaction?.replace(R.id.main_content, walkingFragment)?.commit()


    }


}


