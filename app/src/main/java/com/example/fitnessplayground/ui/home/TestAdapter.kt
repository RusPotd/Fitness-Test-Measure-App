package com.example.fitnessplayground.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessplayground.R
import kotlinx.android.synthetic.main.test_example.view.*

class TestAdapter(private val testList: List<TestExample>,
private val listener: OnItemClickListener
                  ) :
    RecyclerView.Adapter<TestAdapter.TestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
       val itemView = LayoutInflater.from(parent.context).inflate(R.layout.test_example, parent, false)

        return TestViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        val currentItem = testList[position]

        holder.textView.text = currentItem.text
    }

    override fun getItemCount() = testList.size

    inner class TestViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview),
    View.OnClickListener{
        val textView: TextView = itemview.test_Name

        init {
            itemview.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if(position!=RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

}