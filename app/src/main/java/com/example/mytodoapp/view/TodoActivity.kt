package com.example.mytodoapp.view

import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.telecom.Call
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.mytodoapp.R
import com.example.mytodoapp.model.category.CategoryEntity
import com.example.mytodoapp.utils.MyNotificationPublisher
import com.example.mytodoapp.viewmodel.ActiveTodoViewModel
import kotlinx.android.synthetic.main.active_todo_fragment.*
import kotlinx.android.synthetic.main.active_todo_list_item.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.category_fragment.*
import kotlinx.android.synthetic.main.category_list_item.view.*
import kotlinx.android.synthetic.main.completed_todo_fragment.*
import kotlinx.android.synthetic.main.completed_todo_list_item.*
import kotlinx.android.synthetic.main.details_edit_fragment.*
import kotlinx.android.synthetic.main.new_todo_fragment.*
import org.w3c.dom.Text

import yuku.ambilwarna.AmbilWarnaDialog
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener


class TodoActivity : AppCompatActivity() {

    lateinit var navController: NavController
    private lateinit var viewModel: ActiveTodoViewModel

    var mLayout: ConstraintLayout? = null
    var mDefaultColorPrimary = 0
    var mDefaultColorSecondary = 0
    var mDefaultColorTertiary = 0
    var mDefaultColorBackground = 0
    var updateCategory = ""


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mLayout = findViewById(R.id.main_layout)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        bottom_nav.setupWithNavController(navController)
        NavigationUI.setupActionBarWithNavController(this, navController)
        viewModel = ViewModelProviders.of(this).get(ActiveTodoViewModel::class.java)

        loadSavedColors()
        deleteCategoryButton.backgroundTintList = ColorStateList.valueOf(mDefaultColorSecondary)

        getSupportActionBar()?.setBackgroundDrawable(ColorDrawable(mDefaultColorPrimary))
        supportActionBar?.title =
            Html.fromHtml("<font color=\"$mDefaultColorTertiary\">" + getString(R.string.app_name) + "</font>")

        bottom_nav.setBackgroundColor(mDefaultColorPrimary)
        bottom_nav.itemTextColor = ColorStateList.valueOf(mDefaultColorTertiary)
        bottom_nav.itemIconTintList = ColorStateList.valueOf(mDefaultColorTertiary)

        deleteCategoryButton.setOnClickListener {

            viewModel.listOfTodos.observe(this, Observer {

                val sortedList = it.sortedWith(compareBy { it.priority })
                var sortedListByCategory = sortedList?.toMutableList()
                sortedListByCategory?.removeIf {
                    it.category != selectedCategory.text
                }

                if (sortedListByCategory != null) {
                    for (item in sortedListByCategory) {
                        viewModel.deleteTodo(item)
                    }
                }
            })

            viewModel.deleteCategory(CategoryEntity(selectedCategory.text.toString()))
            selectedCategory.visibility = View.GONE
            deleteCatFromList.visibility = View.GONE
            deleteCategoryButton.visibility = View.GONE
        }

    }

    fun savePrimaryColor() {
        val sharedPreferences: SharedPreferences =
            this.getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("primary color", mDefaultColorPrimary)
        editor.apply()
    }

    fun saveSecondaryColor() {
        val sharedPreferences: SharedPreferences =
            this.getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("secondary color", mDefaultColorSecondary)
        editor.apply()
    }

    fun saveFontColor() {
        val sharedPreferences: SharedPreferences =
            this.getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("font color", mDefaultColorTertiary)
        editor.apply()
    }

    fun saveBgColor() {
        val sharedPreferences: SharedPreferences =
            this.getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("bg color", mDefaultColorBackground)
        editor.apply()
    }

    fun loadSavedColors() {
        val sharedPreferences: SharedPreferences = this.getPreferences(Context.MODE_PRIVATE)
        mDefaultColorPrimary = sharedPreferences.getInt("primary color", Color.BLUE)
        mDefaultColorSecondary = sharedPreferences.getInt("secondary color", Color.CYAN)
        mDefaultColorTertiary = sharedPreferences.getInt("font color", Color.BLACK)
        mDefaultColorBackground = sharedPreferences.getInt("bg color", Color.WHITE)

    }

    fun openColorPickerPrimary() {
        val colorPicker =
            AmbilWarnaDialog(this, mDefaultColorPrimary, object : OnAmbilWarnaListener {
                override fun onCancel(dialog: AmbilWarnaDialog) {}
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onOk(dialog: AmbilWarnaDialog, color: Int) {
                    mDefaultColorPrimary = color
                    savePrimaryColor()
                    finish()
                    startActivity(getIntent())
                }
            })
        colorPicker.show()
    }

    fun openColorPickerSecondary() {
        val colorPicker =
            AmbilWarnaDialog(this, mDefaultColorSecondary, object : OnAmbilWarnaListener {
                override fun onCancel(dialog: AmbilWarnaDialog) {}
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onOk(dialog: AmbilWarnaDialog, color: Int) {
                    mDefaultColorSecondary = color
                    saveSecondaryColor()
                    finish()
                    startActivity(getIntent())
                }
            })
        colorPicker.show()
    }

    fun openColorPickerTertiary() {
        val colorPicker =
            AmbilWarnaDialog(this, mDefaultColorTertiary, object : OnAmbilWarnaListener {
                override fun onCancel(dialog: AmbilWarnaDialog) {}
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onOk(dialog: AmbilWarnaDialog, color: Int) {
                    mDefaultColorTertiary = color
                    saveFontColor()
                    finish()
                    startActivity(getIntent())
                }
            })
        colorPicker.show()
    }

    fun openColorPickerBackground() {
        val colorPicker =
            AmbilWarnaDialog(this, mDefaultColorBackground, object : OnAmbilWarnaListener {
                override fun onCancel(dialog: AmbilWarnaDialog) {}
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onOk(dialog: AmbilWarnaDialog, color: Int) {
                    mDefaultColorBackground = color
                    saveBgColor()
                    finish()
                    startActivity(getIntent())
                }
            })
        colorPicker.show()
    }


    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.top_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.deleteTodo -> {
                viewModel.deleteAllCompletedTasks()
            }
            R.id.Primary -> {
                openColorPickerPrimary()
            }
            R.id.Secondary -> {
                openColorPickerSecondary()
            }
            R.id.Tertiary -> {
                openColorPickerTertiary()
            }
            R.id.Background -> {
                openColorPickerBackground()
            }
            R.id.deleteCategory -> {
                deleteCategory()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun deleteCategory() {

        var index = 0
        var data = Array<String>(categoryRV.touchables.size) { "" }
        Toast.makeText(this, "${categoryRV.touchables.size}", Toast.LENGTH_SHORT).show()
        for (item in categoryRV.touchables) {
            data[index] = item.categoryTitle.text.toString()
            index++
        }
        selectedCategory.text = data[0]

        val onValueChangeListener =
            NumberPicker.OnValueChangeListener { numberPicker, i, i1 ->
                selectedCategory.setText("${data[i1]}")
            }

        deleteCatFromList.setMinValue(0)
        deleteCatFromList.setMaxValue(data.size - 1)
        deleteCatFromList.setDisplayedValues(data)
        deleteCatFromList.visibility = View.VISIBLE
        deleteCategoryButton.visibility = View.VISIBLE
        selectedCategory.visibility = View.VISIBLE
        deleteCatFromList.setOnValueChangedListener(onValueChangeListener)
    }
}
