package com.example.mytodoapp.view

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.mytodoapp.R
import com.example.mytodoapp.model.todo.TodoEntity
import com.example.mytodoapp.viewmodel.ActiveTodoViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.details_edit_fragment.*

class DetailsEditFragment : Fragment() {
    companion object {
        fun newInstance() = NewTodoFragment()
    }

    private lateinit var viewModel: ActiveTodoViewModel

    private lateinit var eventDate: String
    private lateinit var eventTime: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.details_edit_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ActiveTodoViewModel::class.java)

        (activity as TodoActivity).bottom_nav.visibility = View.GONE

        updateAppColors()

        edit_todo_detail.text = arguments?.getString("title")
        edit_todo_detail2.text = arguments?.getString("description")
        edit_todo_detail3.text = arguments?.getString("priority")
        edit_todo_detail4.text = arguments?.getString("due date")
        edit_todo_detail5.text = arguments?.getString("due time")
        timeLeftText.text = arguments?.getString("time left")
        reminderText.text = arguments?.getString("reminder1")
        editCategoryTV.text = arguments?.getString("category")

        eventDate = arguments?.getString("due date").toString()
        eventTime = arguments?.getString("due time").toString()

        val button = requireView().findViewById<CardView>(R.id.button_save_edit)
        button.setOnClickListener {
            val todo = TodoEntity(
                edit_todo_detail.text.toString(),
                edit_todo_detail2.text.toString(),
                false,
                edit_todo_detail3.text.toString(),
                edit_todo_detail4.text.toString(),
                edit_todo_detail5.text.toString(),
                timeLeftText.text.toString(),
                reminderText.text.toString(),
                editCategoryTV.text.toString()
            )
            todo.isCompleted = !todo.isCompleted
            viewModel.saveTodo(todo)
            it.findNavController().navigate(R.id.activeTodoFragment)
        }
    }

    fun updateAppColors(){
        if((activity as TodoActivity).mDefaultColorPrimary != 0) {
            descriptionIcon.imageTintList = ColorStateList.valueOf((activity as TodoActivity).mDefaultColorPrimary)
            priorityIcon.imageTintList = ColorStateList.valueOf((activity as TodoActivity).mDefaultColorPrimary)
            dueDateIcon.imageTintList = ColorStateList.valueOf((activity as TodoActivity).mDefaultColorPrimary)
            dueTimeIcon.imageTintList = ColorStateList.valueOf((activity as TodoActivity).mDefaultColorPrimary)
            timeLeftIcon.imageTintList = ColorStateList.valueOf((activity as TodoActivity).mDefaultColorPrimary)
            reminderIcon.imageTintList = ColorStateList.valueOf((activity as TodoActivity).mDefaultColorPrimary)
            editCategoryIcon.imageTintList = ColorStateList.valueOf((activity as TodoActivity).mDefaultColorPrimary)
        }

        if((activity as TodoActivity).mDefaultColorSecondary != 0) {
            detailsEditCard.backgroundTintList = ColorStateList.valueOf((activity as TodoActivity).mDefaultColorSecondary)
            edit_todo_detail.setBackgroundColor((activity as TodoActivity).mDefaultColorSecondary)
            detailsDescriptionLL.setBackgroundColor((activity as TodoActivity).mDefaultColorSecondary)
            detailsPriorityLL.setBackgroundColor((activity as TodoActivity).mDefaultColorSecondary)
            detailsDueDateLL.setBackgroundColor((activity as TodoActivity).mDefaultColorSecondary)
            detailsDueTimeLL.setBackgroundColor((activity as TodoActivity).mDefaultColorSecondary)
            detailsTimeLeftLL.setBackgroundColor((activity as TodoActivity).mDefaultColorSecondary)
            detailsExtraLL.setBackgroundColor((activity as TodoActivity).mDefaultColorSecondary)
            detailsExtra1LL.setBackgroundColor((activity as TodoActivity).mDefaultColorSecondary)
            button_save_edit.backgroundTintList = ColorStateList.valueOf((activity as TodoActivity).mDefaultColorSecondary)
        }

        if((activity as TodoActivity).mDefaultColorTertiary != 0){
            edit_todo_detail.setTextColor((activity as TodoActivity).mDefaultColorTertiary)
            edit_todo_detail.setHintTextColor((activity as TodoActivity).mDefaultColorTertiary)
            edit_todo_detail2.setTextColor((activity as TodoActivity).mDefaultColorTertiary)
            edit_todo_detail2.setHintTextColor((activity as TodoActivity).mDefaultColorTertiary)
            edit_todo_detail3.setTextColor((activity as TodoActivity).mDefaultColorTertiary)
            edit_todo_detail3.setHintTextColor((activity as TodoActivity).mDefaultColorTertiary)
            edit_todo_detail4.setTextColor((activity as TodoActivity).mDefaultColorTertiary)
            edit_todo_detail5.setTextColor((activity as TodoActivity).mDefaultColorTertiary)
            timeLeftText.setTextColor((activity as TodoActivity).mDefaultColorTertiary)
            timeLeftText.setHintTextColor((activity as TodoActivity).mDefaultColorTertiary)
            reminderText.setTextColor((activity as TodoActivity).mDefaultColorTertiary)
            reminderText.setHintTextColor((activity as TodoActivity).mDefaultColorTertiary)
            editCategoryTV.setTextColor((activity as TodoActivity).mDefaultColorTertiary)
            editCategoryTV.setHintTextColor((activity as TodoActivity).mDefaultColorTertiary)

            (activity as TodoActivity).supportActionBar?.title = Html.fromHtml("<font color=\"${(activity as TodoActivity).mDefaultColorTertiary}\">" + getString(R.string.app_name) + "</font>")
        }

        if((activity as TodoActivity).mDefaultColorBackground != 0) {
            detailsEditCard.backgroundTintList = ColorStateList.valueOf((activity as TodoActivity).mDefaultColorBackground)
            detailsEditFragment.setBackgroundColor((activity as TodoActivity).mDefaultColorBackground)
        }
    }
}