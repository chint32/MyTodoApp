package com.example.mytodoapp.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodoapp.R
import com.example.mytodoapp.model.todo.TodoEntity
import com.example.mytodoapp.view.TodoActivity
import java.text.SimpleDateFormat
import java.util.*


class ActiveTodoRecyclerAdapter internal constructor(context: Context): RecyclerView.Adapter<ActiveTodoRecyclerAdapter.TodoViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var listOfTodos = emptyList<TodoEntity>()


    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.active_title_textview)
        val description: TextView = itemView.findViewById(R.id.active_description_textview)
        var priority: TextView = itemView.findViewById(R.id.active_priority_textview)
        var dueDate: TextView = itemView.findViewById(R.id.active_duedate_textview)
        var dueTime: TextView = itemView.findViewById(R.id.active_duetime_textview)
        var timeLeft: TextView = itemView.findViewById(R.id.active_time_left_textview)
        var priorityImg: ImageView = itemView.findViewById(R.id.active_priority_imageview)
        var checkBox = itemView.findViewById<CheckBox>(R.id.active_todo_checkbox)
        var activeRv = itemView.findViewById<LinearLayout>(R.id.active_rv_item_LL)
        var reminder: TextView = itemView.findViewById(R.id.reminder)
        var completedAtTV: TextView = itemView.findViewById(R.id.completedAtTime)

        fun bind(todo: TodoEntity){

            if((activeRv.context as TodoActivity).mDefaultColorSecondary != 0) {
                activeRv.setBackgroundColor((activeRv.context as TodoActivity).mDefaultColorSecondary)
            }

            if((activeRv.context as TodoActivity).mDefaultColorTertiary != 0) {
                title.setTextColor((title.context as TodoActivity).mDefaultColorTertiary)
                description.setTextColor((description.context as TodoActivity).mDefaultColorTertiary)
                dueDate.setTextColor((dueDate.context as TodoActivity).mDefaultColorTertiary)
                dueTime.setTextColor((dueTime.context as TodoActivity).mDefaultColorTertiary)
                timeLeft.setTextColor((timeLeft.context as TodoActivity).mDefaultColorTertiary)
                checkBox.buttonTintList = ColorStateList.valueOf((activeRv.context as TodoActivity).mDefaultColorTertiary)

            }

            val bundle = Bundle()
            bundle.putString("title", title.text.toString())
            bundle.putString("description", description.text.toString())
            bundle.putString("priority", priority.text.toString())
            bundle.putString("due date", dueDate.text.toString())
            bundle.putString("time left", timeLeft.text.toString())
            bundle.putString("due time", dueTime.text.toString())
            bundle.putString("reminder1", reminder.text.toString())
            bundle.putString("category", todo.category)

            setPriority()

            checkBox.setOnClickListener{
                if(checkBox.isChecked) {
                    title.setPaintFlags(title.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
                    dueDate.setPaintFlags(dueDate.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
                    dueTime.setPaintFlags(dueTime.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
                    timeLeft.setPaintFlags(timeLeft.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)

                    val simpleDateFormat = SimpleDateFormat("MM/dd/yyyy @hh:mm")
                    val format: String = simpleDateFormat.format(Date())
                    completedAtTV.text = "Completed At: $format"
                    completedAtTV.visibility = View.VISIBLE

                } else {
                    title.setPaintFlags(title.getPaintFlags() and Paint.STRIKE_THRU_TEXT_FLAG.inv())
                    dueDate.setPaintFlags(dueDate.getPaintFlags() and Paint.STRIKE_THRU_TEXT_FLAG.inv())
                    dueTime.setPaintFlags(dueTime.getPaintFlags() and Paint.STRIKE_THRU_TEXT_FLAG.inv())
                    timeLeft.setPaintFlags(timeLeft.getPaintFlags() and Paint.STRIKE_THRU_TEXT_FLAG.inv())

                    completedAtTV.visibility = View.GONE
                }
            }

            itemView.setOnClickListener{
                it.findNavController().navigate(R.id.detailsEditFragment, bundle)
            }
        }

        fun setPriority(){
            if(priority.text == "5")
                priorityImg.setImageResource(R.drawable.priority5)
            else if(priority.text == "4")
                priorityImg.setImageResource(R.drawable.priority4)
            else if(priority.text == "3")
                priorityImg.setImageResource(R.drawable.priority3)
            else if(priority.text == "2")
                priorityImg.setImageResource(R.drawable.priority2)
            else
                priorityImg.setImageResource(R.drawable.priority1)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = inflater.inflate(R.layout.active_todo_list_item, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val current = listOfTodos[position]
        holder.title.text = current.title
        holder.description.text = current.description
        holder.priority.text = current.priority
        holder.dueDate.text = "Due Date: ${current.dueDate}"
        holder.dueTime.text = "Due Time: ${current.dueTime}"
        holder.timeLeft.text = "Timeleft: ${current.timeLeft}"
        holder.reminder.text = "Reminder: ${current.reminder}"

        holder.bind(current)
    }

    override fun getItemCount(): Int = listOfTodos.size

    fun updateTodos(todos: List<TodoEntity>  ){
        this.listOfTodos = todos
        notifyDataSetChanged()

    }
}