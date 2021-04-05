package com.example.fitnessplayground.ui.slideshow

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.fitnessplayground.MyDBHelper
import com.example.fitnessplayground.R
import com.example.fitnessplayground.ui.score.score


//All Atheletes
class SlideshowFragment : Fragment() {


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_slideshow, container, false)

        var search = root.findViewById<SearchView>(R.id.searchView)
        var user_list = root.findViewById<ListView>(R.id.listView)

        var names : ArrayList<String>
        names = java.util.ArrayList()

        //names.add("dsa")

        var helper = MyDBHelper(requireContext())
        var db = helper.readableDatabase

        var rs = db.rawQuery("SELECT * FROM USERS", null)

        while(rs.moveToNext()) {
            var asa : String
            asa = rs.getString(1)
            names.add(asa)
            //Toast.makeText(context, asa, Toast.LENGTH_LONG).show()
        }

        val adapter: ArrayAdapter<String> = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, names)

        user_list.adapter = adapter

        user_list.setOnItemClickListener(object : AdapterView.OnItemClickListener{
            override fun onItemClick(
                adapter: AdapterView<*>?,
                view: View?,
                position: Int,
                arg: Long
            ) {

                var bundle = Bundle()
                bundle.putString("username", names[position]);

                val fragment = score()
                fragment.arguments = bundle
                var transa = getFragmentManager()?.beginTransaction()
                transa?.addToBackStack(null)
                transa?.replace(R.id.nav_host_fragment, fragment)?.commit()
            }
        })

        user_list.setOnItemLongClickListener(object : AdapterView.OnItemLongClickListener{
            override fun onItemLongClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ): Boolean {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Are you sure!")
                builder.setMessage("Do you want to delete this user?")
                builder.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
                    db.execSQL("DELETE FROM USERS WHERE NAME=?", arrayOf(names[position]))
                    db.execSQL("DELETE FROM SCORE WHERE NAME=?", arrayOf(names[position]))
                    Toast.makeText(requireContext(),names[position]+" Deleted", Toast.LENGTH_SHORT).show()

                    val fragment = SlideshowFragment()
                    var transa = getFragmentManager()?.beginTransaction()
                    transa?.replace(R.id.nav_host_fragment, fragment)?.commit()
                }
                builder.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int ->
                    //Toast.makeText(requireContext(),"Item Not Deleted", Toast.LENGTH_LONG).show()
                }
                builder.show()
                return true
            }

        })

        search.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                search.clearFocus()
                if(names.contains(p0)){
                    adapter.filter.filter(p0)
                }
                else{
                    Toast.makeText(requireContext(), "Item Not Found", Toast.LENGTH_LONG).show()
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                adapter.filter.filter(p0)
                return false
            }

        })


        return root
    }
}