package com.example.mytodoapp.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavArgument
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodoapp.R
import com.example.mytodoapp.model.todo.TodoEntity
import com.example.mytodoapp.utils.ActiveTodoRecyclerAdapter
import com.example.mytodoapp.utils.CompletedTodoRecyclerAdapter
import com.example.mytodoapp.viewmodel.ActiveTodoViewModel
import kotlinx.android.synthetic.main.active_todo_fragment.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.completed_todo_fragment.*
import kotlinx.android.synthetic.main.completed_todo_list_item.*
import org.joda.time.DateTime
import org.joda.time.Period
import org.joda.time.format.DateTimeFormat
import java.lang.String

class CompletedTodoFragment: Fragment(){
    companion object {
        fun newInstance() = CompletedTodoFragment()
    }

    private lateinit var viewModel: ActiveTodoViewModel
    private lateinit var todoRecycler: RecyclerView
    private lateinit var completedTodoRecyclerAdapter: CompletedTodoRecyclerAdapter
    private var initialEmptyList = true
    var sortedListByCategory = mutableListOf<TodoEntity>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.completed_todo_fragment, container, false)
        return rootView
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ActiveTodoViewModel::class.java)

        (activity as TodoActivity).bottom_nav.visibility = View.VISIBLE

        updateAppColors()

        var category = (activity as TodoActivity).updateCategory

        todoRecycler = requireView().findViewById(R.id.completed_recycler_view)
        todoRecycler.layoutManager = LinearLayoutManager(this.context)
        completedTodoRecyclerAdapter =  CompletedTodoRecyclerAdapter(this.requireContext())
        todoRecycler.adapter = completedTodoRecyclerAdapter

        viewModel.listOfCOmpletedTodos.observe(viewLifecycleOwner, Observer {
            val sortedList = it.sortedWith(compareBy({it.priority}))
            sortedListByCategory = sortedList.toMutableList()

            showPromptIfEmptyList(sortedListByCategory)

            sortedListByCategory.removeIf {
                it.category != category
            }
            sortedList?.let { completedTodoRecyclerAdapter.updateTodos(it) }
        })
    }

    fun updateAppColors(){
        if((activity as TodoActivity).mDefaultColorTertiary != 0) {
            (activity as TodoActivity).supportActionBar?.title = Html.fromHtml(
                "<font color=\"${(activity as TodoActivity).mDefaultColorTertiary}\">" + getString(R.string.app_name) + "</font>"
            )
        }

        if((activity as TodoActivity).mDefaultColorBackground != 0) {
            completedTodoFragment.setBackgroundColor((activity as TodoActivity).mDefaultColorBackground)
        }
    }

    fun showPromptIfEmptyList(sortedList: List<TodoEntity>){
        if (sortedList.isEmpty()) {
            noCompletedTodos.height = 500
            if(initialEmptyList)
                initialEmptyList = false
            if(!initialEmptyList)
                noCompletedTodos.setTextColor(Color.BLACK)
        } else {
            noCompletedTodos.setTextColor(Color.WHITE)
            noCompletedTodos.height = 10
        }
    }
}