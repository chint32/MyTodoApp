package com.example.mytodoapp.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodoapp.R
import com.example.mytodoapp.model.category.CategoryEntity
import com.example.mytodoapp.model.todo.TodoEntity
import com.example.mytodoapp.view.TodoActivity
import kotlinx.android.synthetic.main.category_list_item.view.*

class CategoryRecyclerAdapter internal constructor(context: Context): RecyclerView.Adapter<CategoryRecyclerAdapter.CategoryViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var listOfCategories = emptyList<CategoryEntity>()

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var categoryCard = itemView.findViewById<CardView>(R.id.categoryCard)
        var categoryTitle = itemView.findViewById<TextView>(R.id.categoryTitle)

        fun bind(category: CategoryEntity){

            if((categoryCard.context as TodoActivity).mDefaultColorSecondary != 0) {
                categoryCard.backgroundTintList = ColorStateList.valueOf((categoryCard.context as TodoActivity).mDefaultColorSecondary)
            }

            if((categoryTitle.context as TodoActivity).mDefaultColorTertiary != 0) {
                categoryTitle.setTextColor((categoryTitle.context as TodoActivity).mDefaultColorTertiary)
            }

            (categoryTitle.context as TodoActivity).updateCategory = categoryTitle.text.toString()
            val bundle = Bundle()
            (categoryTitle.context as TodoActivity).updateCategory = category.category
            bundle.putString("category", categoryTitle.text.toString())

            itemView.setOnClickListener{
                (categoryTitle.context as TodoActivity).updateCategory = itemView.categoryTitle.text.toString()
                it.findNavController().navigate(R.id.activeTodoFragment, bundle)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder{
        val view = inflater.inflate(R.layout.category_list_item, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val current = listOfCategories[position]
        holder.categoryTitle.text = current.category

        holder.bind(current)
    }

    override fun getItemCount(): Int = listOfCategories.size

    fun updateCategories(todos: List<CategoryEntity>  ){
        this.listOfCategories = todos
        notifyDataSetChanged()
    }
}


