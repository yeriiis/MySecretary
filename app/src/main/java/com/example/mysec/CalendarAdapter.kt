package com.example.mysec

import DateCalendar
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CalendarAdapter(
    private val context: Context,
    private val calendarLayout: LinearLayout,
    private val date: Date
) : RecyclerView.Adapter<CalendarAdapter.CalendarItemHolder>() {

    private val TAG = javaClass.simpleName
    var dataList: ArrayList<Int> = arrayListOf()
    private val dateCalendar: DateCalendar = DateCalendar(date)

    init {
        dateCalendar.initBaseCalendar()
        dataList = dateCalendar.dateList
    }

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarItemHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_calendar, parent, false)
        return CalendarItemHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarItemHolder, position: Int) {
        val height = calendarLayout.height / 6 // 6행으로 나누어 높이 설정
        holder.itemView.layoutParams.height = height
        holder.bind(dataList[position], position)
        holder.itemView.setOnClickListener {
            itemClick?.onClick(it, position)
        }
    }

    override fun getItemCount(): Int = dataList.size

    inner class CalendarItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemCalendarDateText: TextView = itemView.findViewById(R.id.item_calendar_date_text)

        fun bind(data: Int, position: Int) {
            val firstDateIndex = dateCalendar.prevTail
            val lastDateIndex = dataList.size - dateCalendar.nextHead - 1

            itemCalendarDateText.text = data.takeIf { it > 0 }?.toString() ?: ""

            val currentDate = SimpleDateFormat("dd", Locale.KOREA).format(date).toIntOrNull() ?: 0
            if (data == currentDate) {
                itemCalendarDateText.setTypeface(itemCalendarDateText.typeface, Typeface.BOLD)
                itemCalendarDateText.setTextColor(context.getColor(R.color.light_green))
            } else {
                itemCalendarDateText.setTypeface(itemCalendarDateText.typeface, Typeface.NORMAL)
                if (position < firstDateIndex || position > lastDateIndex) {
                    itemCalendarDateText.setTextColor(context.getColor(R.color.light_gray))
                } else {
                    itemCalendarDateText.setTextColor(context.getColor(R.color.black))
                }
            }
        }
    }
}


