package com.example.mysec

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.appcompat.app.AlertDialog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {
    private val TAG = javaClass.simpleName
    lateinit var mContext: Context

    var pageIndex = 0
    lateinit var currentDate: Date

    lateinit var calendar_year_month_text: TextView
    lateinit var calendar_layout: LinearLayout
    lateinit var calendar_view: RecyclerView
    lateinit var calendarAdapter: CalendarAdapter
    lateinit var viewPager: ViewPager2

    companion object {
        var instance: HomeFragment? = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mContext = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View) {
        viewPager = view.findViewById(R.id.viewPager)
        calendar_year_month_text = view.findViewById(R.id.calendar_year_month_text)
        calendar_layout = view.findViewById(R.id.calendar_layout)
        calendar_view = view.findViewById(R.id.calendar_view)

        // 날짜 설정
        val date = Calendar.getInstance().run {
            add(Calendar.MONTH, pageIndex)
            time
        }
        currentDate = date
        Log.e(TAG, "Current Date: $date")

        // 포맷 적용
        val datetime: String = SimpleDateFormat(
            mContext.getString(R.string.calendar_year_month_format),
            Locale.KOREA
        ).format(date)
        calendar_year_month_text.text = datetime

        // 어댑터 설정
        calendarAdapter = CalendarAdapter(mContext, calendar_layout, date)
        calendar_view.layoutManager = GridLayoutManager(mContext, 7)
        calendar_view.adapter = calendarAdapter

        // 일정 클릭 시 다이얼로그 표시
        calendarAdapter.itemClick = object : CalendarAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                showAddEventDialog()
            }
        }
    }

    private fun showAddEventDialog() {
        val dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_event, null)
        val eventInput = dialogView.findViewById<EditText>(R.id.event_input)
        val addButton = dialogView.findViewById<Button>(R.id.add_event_button)

        val dialog = AlertDialog.Builder(mContext)
            .setTitle("일정 추가")
            .setView(dialogView)
            .setPositiveButton("완료") { dialog, _ ->
                val eventText = eventInput.text.toString()
                if (eventText.isNotEmpty()) {
                    // TODO: 일정 저장 및 UI 업데이트
                    Toast.makeText(mContext, "일정 추가됨: $eventText", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }
}
