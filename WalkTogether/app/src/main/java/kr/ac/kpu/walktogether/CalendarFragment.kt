package kr.ac.kpu.walktogether


import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.text.SimpleDateFormat
import java.util.*


private lateinit var mCal: Calendar

class CalendarFragment : Fragment() {

    /** * 그리드뷰 어댑터 */
    private lateinit var gridAdapter: GridAdapter

    /** * 일 저장 할 리스트 */
    private lateinit var dayList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("CalendarFrag", "onCreate()")
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)



        return view
    }

    override fun onResume() {
        super.onResume()

        val now = System.currentTimeMillis();
        val date = Date(now) //연,월,일을 따로 저장

        val curYearFormat = SimpleDateFormat("yyyy", Locale.KOREA)
        val curMonthFormat = SimpleDateFormat("MM", Locale.KOREA)
        val curDayFormat = SimpleDateFormat("dd", Locale.KOREA) //현재 날짜 텍스트뷰에 뿌려줌

        tv_date?.setText(curYearFormat.format(date) + "/" + curMonthFormat.format(date)) //gridview 요일 표시
        dayList = ArrayList<String>()
        dayList.add("일")
        dayList.add("월")
        dayList.add("화")
        dayList.add("수")
        dayList.add("목")
        dayList.add("금")
        dayList.add("토")

        mCal = Calendar.getInstance() //이번달 1일 무슨요일인지 판단  mCal.set(Year,Month,Day)
        mCal.set(Integer.parseInt(curYearFormat.format(date)), Integer.parseInt(curMonthFormat.format(date)) - 1, 1)
        val dayNum = mCal.get(Calendar.DAY_OF_WEEK) //1일 - 요일 매칭 시키기 위해 공백 add
        for (i in 1..dayNum-1) {
            dayList.add("")
        }
        setCalendarDate(mCal.get(Calendar.MONTH) + 1)
        gridAdapter = GridAdapter(context!!, dayList)
        gridview?.setAdapter(gridAdapter)
    }

    /**
     * 해당 월에 표시할 일 수 구함
     *
     *  @param month
     *  */
    fun setCalendarDate(month: Int) {
        mCal.set(Calendar.MONTH, month - 1);
        for (i in 0..mCal.getActualMaximum(Calendar.DAY_OF_MONTH)-1) {
            dayList.add("" + (i + 1));
        }
    }


    /* 그리드뷰 어댑터 */
    private class GridAdapter(context: Context, list: List<String>) : BaseAdapter() {

        private var list: List<String>

        private var inflater: LayoutInflater

        /**
         * 생성자
         *
         *  @param context
         *  @param list
         */

        init {
            this.list = list
            this.inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }

        override fun getItem(position: Int): Any {
            return list.get(position)
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return list.size
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var holder: ViewHolder
            var convertview = convertView
            var day : String = ""
            if (convertview == null) {
                convertview = inflater.inflate(R.layout.item_calendar_gridview, parent, false)
                holder = ViewHolder()
                holder.tvItemGridView = convertview.findViewById(R.id.tv_item_gridview)
                holder.tvItemSmall = convertview.findViewById(R.id.tv_item_small)
                convertview.setTag(holder)
            } else {
                holder = convertview.getTag() as ViewHolder
            }
            holder.tvItemGridView?.setText("" + getItem(position))

            // 해당날짜를 스트링 형태로 저장
            day = getItem(position).toString()

            //스트링 형태의 날짜를 정수화하여 해당 날짜가 올바른지 파악
            if(day.toIntOrNull()!=null&&day.toInt()>0&&day.toInt()<32) {
                holder.tvItemSmall?.setText("" + getItem(position))
            }

            // 해당 날짜 텍스트 컬러 배경 변경
            mCal = Calendar.getInstance()
            val today: Int = mCal.get(Calendar.DAY_OF_MONTH)
            val sToday: String = today.toString()

            if (sToday.equals(getItem(position))) { //오늘 day 텍스트 컬러 변경
                holder.tvItemGridView?.setTextColor(Color.RED)
                holder.tvItemSmall?.setTextColor(Color.RED)
                //holder.tvItemGridView.setTextColor()
            }
            return convertview!!
        }

        private class ViewHolder {
            var tvItemSmall : TextView? = null
            var tvItemGridView: TextView? = null
        }

    }


}
