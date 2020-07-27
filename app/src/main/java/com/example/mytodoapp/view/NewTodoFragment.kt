package com.example.mytodoapp.view

import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.mytodoapp.R
import com.example.mytodoapp.model.todo.TodoEntity
import com.example.mytodoapp.utils.MyNotificationPublisher
import com.example.mytodoapp.viewmodel.ActiveTodoViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.new_todo_fragment.*
import org.joda.time.DateTime
import org.joda.time.Period
import org.joda.time.format.DateTimeFormat
import java.lang.String.format


class NewTodoFragment : Fragment() {

    companion object {
        fun newInstance() = NewTodoFragment()
    }

    private lateinit var viewModel: ActiveTodoViewModel

    private lateinit var addTodoTitleEditText: EditText
    private lateinit var addTodoDescriptionEditText: EditText
    private lateinit var addTodoPriorityEditText: EditText
    private lateinit var addTodoDate: DatePicker
    private lateinit var addTodoTime: TimePicker
    private lateinit var eventDate: String
    private lateinit var eventTime: String
    var timeLeft = ""
    var reminderDate = ""
    var reminderTime = ""

    val NOTIFICATION_CHANNEL_ID = "10001"
    val default_notification_channel_id = "default"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.new_todo_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ActiveTodoViewModel::class.java)

        (activity as TodoActivity).bottom_nav.visibility = View.GONE

        val category = (activity as TodoActivity).updateCategory
        newCategory.text = category
        Toast.makeText(this.requireContext(), " $category ", Toast.LENGTH_SHORT).show()

        updateAppColors()

        addTodoTitleEditText = requireView().findViewById(R.id.edit_todo)
        addTodoDescriptionEditText = requireView().findViewById(R.id.edit_todo2)
        addTodoPriorityEditText = requireView().findViewById(R.id.edit_todo3)
        addTodoDate = requireView().findViewById(R.id.edit_todo4)
        addTodoTime = requireView().findViewById(R.id.edit_todo5)

        if(arguments?.getString("reminderDateTime") != null)
            edit_todo_6.text = requireArguments().getString("reminderDateTime")

        val button = requireView().findViewById<CardView>(R.id.button_save)
        button.setOnClickListener {
            val reminderDateTime: DateTime = DateTime.parse(
                format("%s %s", reminderDate, reminderTime),
                DateTimeFormat.forPattern("dd/MM/yyyy HH:mm")
            )

            val now: DateTime = DateTime.now()
            val period = Period(now, reminderDateTime)

            var delay: Long = 0L

            if (period.years == 0 && period.months == 0 && period.weeks == 0 && period.days == 0 && period.hours == 0 && period.minutes == 0 && period.seconds != 0)
                delay = (period.seconds * 1000).toLong()
            else if (period.years == 0 && period.months == 0 && period.weeks == 0 && period.days == 0 && period.hours == 0 && period.minutes != 0)
                delay = (period.seconds * 1000).toLong() + period.minutes * 60000
            else if (period.years == 0 && period.months == 0 && period.weeks == 0 && period.days == 0 && period.hours != 0)
                delay =
                    (period.seconds * 1000).toLong() + period.minutes * 60000 + period.hours * 3600000
            else if (period.years == 0 && period.months == 0 && period.weeks == 0 && period.days != 0)
                delay =
                    (period.seconds * 1000).toLong() + period.minutes * 60000 + period.hours * 3600000 + period.days * 86400000
            else if (period.years == 0 && period.months == 0 && period.weeks != 0)
                delay =
                    (period.seconds * 1000).toLong() + period.minutes * 60000 + period.hours * 3600000 + period.days * 86400000 + period.weeks * 604800000
            else if (period.years == 0 && period.months != 0)
                delay =
                    (period.seconds * 1000).toLong() + period.minutes * 60000 + period.hours * 3600000 + period.days * 86400000 + period.weeks * 604800000 + period.months * 2628000000
            else if (period.years != 0)
                delay =
                    (period.seconds * 1000).toLong() + period.minutes * 60000 + period.hours * 3600000 + period.days * 86400000 + period.weeks * 604800000 + period.months * 2628000000 + period.years * 31540000000

            scheduleNotification(getNotification("Todo: Pass in todo info"), 10000)
            Toast.makeText(this.requireContext(), "Reminder scheduled", Toast.LENGTH_SHORT).show()

            val todo = TodoEntity(
                addTodoTitleEditText.text.toString(),
                addTodoDescriptionEditText.text.toString(),
                false,
                addTodoPriorityEditText.text.toString(),
                "${addTodoDate.month + 1}/${addTodoDate.dayOfMonth}/${addTodoDate.year}",
                eventTime,
                "Time left: $timeLeft", edit_todo_6.text.toString(), category.toString()
            )
            viewModel.saveTodo(todo)

            val bundle = Bundle()
            bundle.putString("reminder", edit_todo_6.text.toString())
            bundle.putString("category", category)
            it.findNavController().navigate(R.id.activeTodoFragment, bundle)
        }

        edit_todo_6.setOnClickListener{
            cardView2.elevation = 0F
            darkenViews1.elevation = 2F
            saveReminderDateButton.elevation = 4F
            reminderDatePicker.elevation = 4F
            darkenViews1.background.alpha = 128
            darkenViews1.visibility = View.VISIBLE
            reminderDatePicker.visibility = View.VISIBLE
            saveReminderDateButton.visibility = View.VISIBLE
            button_save.visibility = View.GONE
        }

        saveReminderDateButton.setOnClickListener{
            reminderDatePicker.elevation = 0F
            reminderDatePicker.visibility = View.GONE
            reminderTimerPicker.elevation  = 4F
            reminderTimerPicker.visibility = View.VISIBLE
            saveReminderDateButton.visibility = View.GONE
            saveReminderTimeButton.visibility = View.VISIBLE
            reminderDate =
                "${reminderDatePicker.dayOfMonth}/${reminderDatePicker.month + 1}/${reminderDatePicker.year}"
        }

        saveReminderTimeButton.setOnClickListener{
            cardView2.elevation = 2F
            darkenViews1.elevation = 0F
            saveReminderTimeButton.elevation = 0F
            reminderTimerPicker.elevation = 0F
            darkenViews1.visibility = View.GONE
            reminderTimerPicker.visibility = View.GONE
            saveReminderTimeButton.visibility = View.GONE
            button_save.visibility = View.VISIBLE
            reminderTime = "${reminderTimerPicker.hour}:${reminderTimerPicker.minute}"
            edit_todo_6.text = "$reminderDate @ $reminderTime"
        }

        edit_todo_date.setOnClickListener{
            cardView2.elevation = 0F
            darkenViews1.elevation = 2F
            saveDateButton.elevation = 4F
            edit_todo4.elevation = 4F
            darkenViews1.background.alpha = 128
            darkenViews1.visibility = View.VISIBLE
            edit_todo4.visibility = View.VISIBLE
            saveDateButton.visibility = View.VISIBLE
            button_save.visibility = View.GONE
        }

        saveDateButton.setOnClickListener{
            saveDateButton.elevation = 0F
            edit_todo4.elevation = 0F
            saveTimeButton.elevation = 2F
            edit_todo5.elevation = 2F
            edit_todo4.visibility = View.GONE
            saveDateButton.visibility = View.GONE
            edit_todo5.visibility = View.VISIBLE
            saveTimeButton.visibility = View.VISIBLE
            eventDate = "${addTodoDate.dayOfMonth}/${addTodoDate.month + 1}/${addTodoDate.year}"
        }

        saveTimeButton.setOnClickListener{
            cardView2.elevation = 2F
            darkenViews1.elevation = 0F
            saveDateButton.elevation = 0F
            edit_todo5.elevation = 0F
            darkenViews1.background.alpha = 128
            darkenViews1.visibility = View.GONE
            edit_todo5.visibility = View.GONE
            saveTimeButton.visibility = View.GONE
            button_save.visibility = View.VISIBLE
            eventTime = "${addTodoTime.hour}:${addTodoTime.minute}"
            edit_todo_date.text = "Due: ${eventDate} @${eventTime}"

            val targetDateTime: DateTime = DateTime.parse(
                format("%s %s", eventDate, eventTime),
                DateTimeFormat.forPattern("dd/MM/yyyy HH:mm")
            )

            val now: DateTime = DateTime.now()
            val period = Period(now, targetDateTime)

            if (period.months == 0 && period.weeks == 0 && period.days == 0 && period.hours == 0 && period.minutes != 0)
                timeLeft = "${period.minutes} min"
            else if (period.months == 0 && period.weeks == 0 && period.days == 0 && period.hours != 0)
                timeLeft = "${period.hours} hours, ${period.minutes} min"
            else if (period.months == 0 && period.weeks == 0 && period.days != 0)
                timeLeft = "${period.days} days, ${period.hours} hours, ${period.minutes} min"
            else if (period.months == 0 && period.weeks != 0)
                timeLeft = "About ${period.weeks} week(s)"
            else if (period.months != 0)
                timeLeft = "About ${period} month(s)"

            newTodoTimeLeft.text = timeLeft
        }
        darkenViews1.setOnClickListener{}
    }

    fun updateAppColors(){
        if ((activity as TodoActivity).mDefaultColorPrimary != 0) {
            newDescriptionIcon.imageTintList = ColorStateList.valueOf((activity as TodoActivity).mDefaultColorPrimary)
            newPriorityIcon.imageTintList = ColorStateList.valueOf((activity as TodoActivity).mDefaultColorPrimary)
            newReminderIcon.imageTintList = ColorStateList.valueOf((activity as TodoActivity).mDefaultColorPrimary)
            newDueDateIcon.imageTintList = ColorStateList.valueOf((activity as TodoActivity).mDefaultColorPrimary)
            newTimeLeftIcon.imageTintList = ColorStateList.valueOf((activity as TodoActivity).mDefaultColorPrimary)
            newCategoryIcon.imageTintList = ColorStateList.valueOf((activity as TodoActivity).mDefaultColorPrimary)
        }

        if ((activity as TodoActivity).mDefaultColorSecondary != 0) {
            button_save.backgroundTintList = ColorStateList.valueOf((activity as TodoActivity).mDefaultColorSecondary)
            saveDateButton.backgroundTintList = ColorStateList.valueOf((activity as TodoActivity).mDefaultColorSecondary)
            saveTimeButton.backgroundTintList = ColorStateList.valueOf((activity as TodoActivity).mDefaultColorSecondary)
            saveReminderDateButton.backgroundTintList = ColorStateList.valueOf((activity as TodoActivity).mDefaultColorSecondary)
            saveReminderTimeButton.backgroundTintList = ColorStateList.valueOf((activity as TodoActivity).mDefaultColorSecondary)
            edit_todo4.background = ColorDrawable((activity as TodoActivity).mDefaultColorSecondary)
            edit_todo5.background = ColorDrawable((activity as TodoActivity).mDefaultColorSecondary)
            edit_todo.setBackgroundColor((activity as TodoActivity).mDefaultColorSecondary)
            newDescriptionLL.setBackgroundColor((activity as TodoActivity).mDefaultColorSecondary)
            newPriorityLL.setBackgroundColor((activity as TodoActivity).mDefaultColorSecondary)
            newReminderLL.setBackgroundColor((activity as TodoActivity).mDefaultColorSecondary)
            newDueDateLL.setBackgroundColor((activity as TodoActivity).mDefaultColorSecondary)
            newTimeLeftLL.setBackgroundColor((activity as TodoActivity).mDefaultColorSecondary)
            newCategoryLL.setBackgroundColor((activity as TodoActivity).mDefaultColorSecondary)
        }


        if ((activity as TodoActivity).mDefaultColorTertiary != 0) {
            linearLayout.setBackgroundColor((activity as TodoActivity).mDefaultColorTertiary)
            edit_todo.setTextColor((activity as TodoActivity).mDefaultColorTertiary)
            edit_todo.setHintTextColor((activity as TodoActivity).mDefaultColorTertiary)
            edit_todo2.setTextColor((activity as TodoActivity).mDefaultColorTertiary)
            edit_todo2.setHintTextColor((activity as TodoActivity).mDefaultColorTertiary)
            edit_todo3.setTextColor((activity as TodoActivity).mDefaultColorTertiary)
            edit_todo3.setHintTextColor((activity as TodoActivity).mDefaultColorTertiary)
            edit_todo_6.setTextColor((activity as TodoActivity).mDefaultColorTertiary)
            edit_todo_6.setHintTextColor((activity as TodoActivity).mDefaultColorTertiary)
            edit_todo_date.setTextColor((activity as TodoActivity).mDefaultColorTertiary)
            edit_todo_date.setHintTextColor((activity as TodoActivity).mDefaultColorTertiary)
            newCategory.setTextColor((activity as TodoActivity).mDefaultColorTertiary)
            newCategory.setHintTextColor((activity as TodoActivity).mDefaultColorTertiary)
            newTodoTimeLeft.setTextColor((activity as TodoActivity).mDefaultColorTertiary)
            newTodoTimeLeft.setHintTextColor((activity as TodoActivity).mDefaultColorTertiary)

            (activity as TodoActivity).supportActionBar?.title = Html.fromHtml("<font color=\"${(activity as TodoActivity).mDefaultColorTertiary}\">" + getString(R.string.app_name) + "</font>")
        }

        if ((activity as TodoActivity).mDefaultColorBackground != 0) {
            newTodoFragment.setBackgroundColor((activity as TodoActivity).mDefaultColorBackground)
        }
    }

        @RequiresApi(Build.VERSION_CODES.M)
    private fun scheduleNotification(notification: Notification, delay: Long) {
        val notificationIntent = Intent(this.requireContext(), MyNotificationPublisher::class.java)
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID, 1)
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION, notification)
        val pendingIntent = PendingIntent.getBroadcast(
            this.requireContext(),
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        var futureInMillis = SystemClock.elapsedRealtime() + delay
        val alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        assert(alarmManager != null)
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent)
    }

    private fun getNotification(content: String): Notification {

        val resultIntent = Intent(this.requireContext(), TodoActivity::class.java)
        val resultPendingIntent = PendingIntent.getActivity(
            this.requireContext(),
            1,
            resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder =
            NotificationCompat.Builder(this.requireContext(), default_notification_channel_id)
        builder.setContentTitle("Scheduled Notification")
        builder.setContentText(content)
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
        builder.setAutoCancel(true)
        builder.setChannelId(NOTIFICATION_CHANNEL_ID)
        builder.setContentIntent(resultPendingIntent)
        return builder.build()
    }

}
