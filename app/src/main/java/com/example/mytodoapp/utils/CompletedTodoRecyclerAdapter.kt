package com.example.mytodoapp.utils

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodoapp.R
import com.example.mytodoapp.model.todo.TodoEntity
import com.example.mytodoapp.view.TodoActivity

class CompletedTodoRecyclerAdapter internal constructor(context: Context): RecyclerView.Adapter<CompletedTodoRecyclerAdapter.TodoViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var listOfTodos = emptyList<TodoEntity>()

    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.completed_title_textview)
        val description: TextView = itemView.findViewById(R.id.completed_description_textview)
        var priority: TextView = itemView.findViewById(R.id.completed_priority_textview)
        var dueDate: TextView = itemView.findViewById(R.id.completed_duedate_textview)
        var dueTime: TextView = itemView.findViewById(R.id.completed_duetime_textview)
        var timeLeft: TextView = itemView.findViewById(R.id.completed_time_left_textview)
        var priorityImg: ImageView = itemView.findViewById(R.id.completed_priority_imageview)
        var comprv = itemView.findViewById<LinearLayout>(R.id.completed_rv_item_LL)
        var reminder: TextView = itemView.findViewById(R.id.reminder1)

        fun bind(todo: TodoEntity){

            updateAppColors()

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

            itemView.setOnClickListener{
                it.findNavController().navigate(R.id.detailsEditFragment, bundle)
            }

        }

        fun updateAppColors(){
            if((comprv.context as TodoActivity).mDefaultColorSecondary != 0) {
                comprv.setBackgroundColor((comprv.context as TodoActivity).mDefaultColorSecondary)
            }

            if((comprv.context as TodoActivity).mDefaultColorTertiary != 0) {
                title.setTextColor((title.context as TodoActivity).mDefaultColorTertiary)
                description.setTextColor((description.context as TodoActivity).mDefaultColorTertiary)
                dueDate.setTextColor((dueDate.context as TodoActivity).mDefaultColorTertiary)
                dueTime.setTextColor((dueTime.context as TodoActivity).mDefaultColorTertiary)
                timeLeft.setTextColor((timeLeft.context as TodoActivity).mDefaultColorTertiary)
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
        val view = inflater.inflate(R.layout.completed_todo_list_item, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val current = listOfTodos[position]
        holder.title.text = current.title
        holder.description.text = current.description
        holder.priority.text = current.priority
        holder.dueDate.text = "${current.dueDate}"
        holder.dueTime.text = "${current.dueTime}"
        holder.timeLeft.text = "${current.timeLeft}"
        holder.reminder.text = "${current.reminder}"

        holder.bind(current)
    }

    override fun getItemCount(): Int = listOfTodos.size

    fun updateTodos(todos: List<TodoEntity>  ){
        this.listOfTodos = todos
        notifyDataSetChanged()
    }
}