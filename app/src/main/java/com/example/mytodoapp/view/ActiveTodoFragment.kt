package com.example.mytodoapp.view

import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavArgument
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodoapp.R
import com.example.mytodoapp.model.todo.TodoEntity
import com.example.mytodoapp.utils.ActiveTodoRecyclerAdapter
import com.example.mytodoapp.viewmodel.ActiveTodoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.active_todo_fragment.*
import kotlinx.android.synthetic.main.activity_main.*
import org.joda.time.DateTime
import org.joda.time.Period
import org.joda.time.format.DateTimeFormat
import java.lang.String


class ActiveTodoFragment : Fragment(){

    companion object {
        fun newInstance() = ActiveTodoFragment()
    }

    private lateinit var viewModel: ActiveTodoViewModel
    private lateinit var todoRecycler: RecyclerView
    private lateinit var activeTodoRecyclerAdapter: ActiveTodoRecyclerAdapter
    private var initialEmptyList = true
    var sortedListByCategory = mutableListOf<TodoEntity>()
    lateinit var checkBox: CheckBox

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setRetainInstance(true)
        val rootView = inflater.inflate(R.layout.active_todo_fragment, container, false)
        return rootView
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @ExperimentalStdlibApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ActiveTodoViewModel::class.java)

        (activity as TodoActivity).bottom_nav.visibility = View.VISIBLE
        updateAppColors()


        todoRecycler = requireView().findViewById(R.id.active_recycler_view)
        todoRecycler.layoutManager = LinearLayoutManager(this.context)
        activeTodoRecyclerAdapter =  ActiveTodoRecyclerAdapter(this.requireContext())
        todoRecycler.adapter = activeTodoRecyclerAdapter

        var category = (activity as TodoActivity).updateCategory
        Toast.makeText(this.requireContext(), " $category ", Toast.LENGTH_SHORT).show()

        viewModel.listOfTodos.observe(viewLifecycleOwner, Observer { it ->

            val sortedList = it.sortedWith(compareBy {it.priority})
            sortedListByCategory = sortedList.toMutableList()
            sortedListByCategory.removeIf {
                it.category != category
            }
            showPromptIfEmptyList(sortedListByCategory)
            calculateTimeLeft()
            sortedListByCategory?.let { activeTodoRecyclerAdapter.updateTodos(it) }

        })

        val bundle = Bundle()
        bundle.putString("category", arguments?.getString("category"))

        val fab = requireView().findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener{
            it.findNavController().navigate(R.id.newTodoFragment, bundle)
        }
    }

    fun updateAppColors(){
        if((activity as TodoActivity).mDefaultColorSecondary != 0)
            fab.backgroundTintList = ColorStateList.valueOf((activity as TodoActivity).mDefaultColorSecondary)

        if((activity as TodoActivity).mDefaultColorTertiary != 0)
            (activity as TodoActivity).supportActionBar?.title = Html.fromHtml("<font color=\"${(activity as TodoActivity).mDefaultColorTertiary}\">" + getString(R.string.app_name) + "</font>")
        fab.imageTintList = ColorStateList.valueOf((activity as TodoActivity).mDefaultColorTertiary)

        if((activity as TodoActivity).mDefaultColorBackground != 0)
            activeTodoFragment.setBackgroundColor((activity as TodoActivity).mDefaultColorBackground)
    }

    fun calculateTimeLeft(){
        for(item in sortedListByCategory){
            val update: DateTime = DateTime.parse(
                String.format("%s %s", "${item.dueDate}", item.dueTime),
                DateTimeFormat.forPattern("MM/dd/yyyy HH:mm")
            )
            val now: DateTime = DateTime.now()
            val period = Period(now, update)

            if(period.months == 0 && period.weeks == 0 && period.days == 0 && period.hours == 0 && period.minutes != 0)
                item.timeLeft = "${period.minutes} min"
            else if(period.months == 0 && period.weeks == 0 && period.days == 0 && period.hours != 0)
                item.timeLeft = "${period.hours} hours, ${period.minutes} min"
            else if(period.months == 0 && period.weeks == 0 && period.days != 0)
                item.timeLeft = "${period.days} days, ${period.hours} hours, ${period.minutes} min"
            else if(period.months == 0 && period.weeks != 0)
                item.timeLeft = "About ${period.weeks} week(s)"
            else if(period.months != 0)
                item.timeLeft = "About ${period.months} month(s)"
        }
    }

    fun showPromptIfEmptyList(sortedList: List<TodoEntity>){
        if (sortedList.isEmpty()) {
            noActiveTodos.height = 500
            if(initialEmptyList)
                initialEmptyList = false
            if(!initialEmptyList)
                noActiveTodos.setTextColor(Color.BLACK)
        } else {
            noActiveTodos.setTextColor(Color.WHITE)
            noActiveTodos.height = 10
        }
    }
}
