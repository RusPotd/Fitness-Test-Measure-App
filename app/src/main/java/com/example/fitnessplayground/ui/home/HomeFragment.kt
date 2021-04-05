package com.example.fitnessplayground.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessplayground.MyDBHelper
import com.example.fitnessplayground.R
import com.example.fitnessplayground.Test


class HomeFragment : Fragment(), TestAdapter.OnItemClickListener {
    private val names = ArrayList<TestExample>()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        var helper = MyDBHelper(root.context)
        var db = helper.readableDatabase

        var rs = db.rawQuery("SELECT * FROM TESTS", null)

        while(rs.moveToNext()) {
            var asa : String
            asa = rs.getString(1)
            val item = TestExample(asa)
            names += item
            //Toast.makeText(context, names.toString(), Toast.LENGTH_LONG).show()
        }

        var recyclerview = root.findViewById(R.id.recyclerview) as RecyclerView
        recyclerview.adapter = TestAdapter(names, this)
        recyclerview.layoutManager = LinearLayoutManager(this.context)
        recyclerview.setHasFixedSize(true)

        return root
    }

    override fun onItemClick(position: Int) {
        val clickedItem = names[position]
        //Toast.makeText(context, clickedItem.text, Toast.LENGTH_LONG).show()
        val myIntent = Intent(requireContext(), Test::class.java)
        myIntent.putExtra("testName", clickedItem.text)
        requireActivity().startActivity(myIntent)
    }
}