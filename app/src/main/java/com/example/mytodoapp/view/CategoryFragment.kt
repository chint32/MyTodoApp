package com.example.mytodoapp.view

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodoapp.R
import com.example.mytodoapp.model.category.CategoryEntity
import com.example.mytodoapp.utils.CategoryRecyclerAdapter
import com.example.mytodoapp.viewmodel.ActiveTodoViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.category_fragment.*


class CategoryFragment : Fragment() {

    companion object {
        fun newInstance() = CategoryFragment()
    }

    private lateinit var viewModel: ActiveTodoViewModel
    private lateinit var categoryRecycler: RecyclerView
    private lateinit var categoryRecyclerAdapter: CategoryRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.category_fragment, container, false)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ActiveTodoViewModel::class.java)

        (activity as TodoActivity).bottom_nav.visibility = View.GONE

        updateAppColors()

        categoryRecycler = requireView().findViewById(R.id.categoryRV)
        categoryRecycler.layoutManager = GridLayoutManager(this.context, 2)
        categoryRecyclerAdapter =  CategoryRecyclerAdapter(this.requireContext())
        categoryRecycler.adapter = categoryRecyclerAdapter

        viewModel.listOfCategories.observe(viewLifecycleOwner, Observer {
            categoryRecyclerAdapter.updateCategories(it)
        })

        categoryFab.setOnClickListener{
            cardView3.elevation = 0F
            darkenViews.elevation = 2F
            darkenViews.background.alpha = 126
            darkenViews.visibility = View.VISIBLE
            newCategoryET.elevation = 4F
            newCategoryET.bringToFront()
            newCategoryET.visibility = View.VISIBLE
            saveCatButton.elevation = 4F
            saveCatButton.bringToFront()
            saveCatButton.visibility = View.VISIBLE
        }

        saveCatButton.setOnClickListener{
            viewModel.saveCategory(CategoryEntity(newCategoryET.text.toString()))
            darkenViews.visibility = View.GONE
            darkenViews.elevation = 0F
            newCategoryET.visibility = View.GONE
            saveCatButton.visibility = View.GONE
        }

        darkenViews.setOnClickListener{

        }
    }
    fun updateAppColors(){
        if ((activity as TodoActivity).mDefaultColorPrimary != 0) {
            cardView3.backgroundTintList = ColorStateList.valueOf((activity as TodoActivity).mDefaultColorPrimary)
            saveCatButton.backgroundTintList = ColorStateList.valueOf((activity as TodoActivity).mDefaultColorPrimary)
        }

        if ((activity as TodoActivity).mDefaultColorSecondary != 0) {
            saveCatButton.backgroundTintList = ColorStateList.valueOf((activity as TodoActivity).mDefaultColorSecondary)
            categoryFab.backgroundTintList = ColorStateList.valueOf((activity as TodoActivity).mDefaultColorSecondary)
        }

        if ((activity as TodoActivity).mDefaultColorTertiary != 0) {
            (activity as TodoActivity).supportActionBar?.title = Html.fromHtml("<font color=\"${(activity as TodoActivity).mDefaultColorTertiary}\">" + getString(R.string.app_name) + "</font>")
            categoryFab.setColorFilter((activity as TodoActivity).mDefaultColorTertiary)
        }

        if ((activity as TodoActivity).mDefaultColorBackground != 0) {
            categoryFragment.setBackgroundColor((activity as TodoActivity).mDefaultColorBackground)
        }
    }

}
