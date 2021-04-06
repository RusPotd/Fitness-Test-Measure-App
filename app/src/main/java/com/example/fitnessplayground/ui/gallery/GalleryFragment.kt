package com.example.fitnessplayground.ui.gallery

import android.content.ContentValues
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.fitnessplayground.MyDBHelper
import com.example.fitnessplayground.R
import kotlinx.android.synthetic.main.fragment_gallery.*

//Add Athelete
class GalleryFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_gallery, container, false)

        var gender : Spinner
        var saveBtn : Button
        gender = root.findViewById(R.id.gender)
        saveBtn = root.findViewById(R.id.addUserSaveBtn)

        var selectedGender : String
        selectedGender = " "

        val genderOption = arrayOf("Male", "Female")

        gender.adapter = ArrayAdapter<String>(root.context, android.R.layout.simple_list_item_1, genderOption)

        gender.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedGender = genderOption.get(position)
            }

            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                TODO("Not yet implemented")
            }

        }

        var helper = MyDBHelper(requireContext())
        var db = helper.readableDatabase

        saveBtn.setOnClickListener {

            var rs = db.rawQuery("SELECT * FROM USERS", null)

            var count = 0
            var userName = uname.text.toString()

            while(rs.moveToNext()) {
                var asa : String
                asa = rs.getString(1)
                if(userName==asa){
                    Toast.makeText(context, "Username already exist", Toast.LENGTH_LONG).show()
                }
                else{
                    count+=1
                }
                //Toast.makeText(context, asa, Toast.LENGTH_LONG).show()
            }

            if(count==rs.count){
                var cv = ContentValues()
                cv.put("NAME", uname.text.toString())
                cv.put("GENDER", selectedGender)
                db.insert("USERS", null, cv)
                Toast.makeText(activity, "Insert Success", Toast.LENGTH_SHORT).show()
                uname.setText("")
            }

        }

        return root
    }
}