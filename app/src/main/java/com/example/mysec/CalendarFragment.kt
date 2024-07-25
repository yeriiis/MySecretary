package com.example.mysec

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CalendarFragment : Fragment() {

    private var pageIndex: Int = 0
    private lateinit var calendarAdapter: CalendarAdapter

    companion object {
        fun newInstance(pageIndex: Int): CalendarFragment {
            val fragment = CalendarFragment()
            val args = Bundle().apply {
                putInt("pageIndex", pageIndex)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pageIndex = it.getInt("pageIndex", 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.calendar_view)
        val calendarLayout: LinearLayout = view.findViewById(R.id.calendar_layout)

        // 초기 날짜 설정
        val date = Calendar.getInstance().apply {
            add(Calendar.MONTH, pageIndex)
            set(Calendar.DAY_OF_MONTH, 1)
        }.time

        calendarAdapter = CalendarAdapter(requireContext(), calendarLayout, date)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 7) // 7 columns for a week
        recyclerView.adapter = calendarAdapter

        populateCalendar()
        return view
    }

    private fun populateCalendar() {
        val startDate = Calendar.getInstance().apply {
            add(Calendar.MONTH, pageIndex)
            set(Calendar.DAY_OF_MONTH, 1)
        }
        val daysInMonth = startDate.getActualMaximum(Calendar.DAY_OF_MONTH)
        val dataList = ArrayList<Int>()

        for (day in 1..daysInMonth) {
            dataList.add(day)
        }
        calendarAdapter.dataList = dataList
        calendarAdapter.notifyDataSetChanged()
    }
}
