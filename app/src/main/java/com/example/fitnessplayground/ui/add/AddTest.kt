package com.example.fitnessplayground.ui.add

import android.animation.Animator
import android.content.ContentValues
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.example.fitnessplayground.MyDBHelper
import com.example.fitnessplayground.R
import kotlinx.android.synthetic.main.fragment_addtest.*

class AddTest : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_addtest, container, false)

        var No_of_readings : Spinner
        var test_parameters : Spinner
        var saveBtn : Button
        No_of_readings = root.findViewById(R.id.spinner_readings)
        test_parameters = root.findViewById(R.id.spinner_parameters)
        saveBtn = root.findViewById(R.id.save_test_Btn)

        var Readings : Int
        Readings = 1
        var Param : Int
        Param = 1

        val readingsOption = arrayOf(1, 3)
        val parametersOption = arrayOf("Score", "Time", "Both")

        No_of_readings.adapter = ArrayAdapter<Int>(root.context, android.R.layout.simple_list_item_1, readingsOption)

        No_of_readings.onItemSelectedListener = object : AdapterView.OnItemClickListener,
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
                Readings = readingsOption.get(position)
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

        test_parameters.adapter = ArrayAdapter<String>(root.context, android.R.layout.simple_list_item_1, parametersOption)

        test_parameters.onItemSelectedListener = object : AdapterView.OnItemClickListener,
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
                var P = parametersOption.get(position)
                if(P=="Score"){
                    Param = 1
                }
                else if (P=="Time"){
                    Param = 2
                }
                else {
                    Param = 3
                }
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

        //var doneAnimation : LottieAnimationView = root.findViewById(R.id.ok_animation)

        saveBtn.setOnClickListener {
            var cv = ContentValues()
            cv.put("TESTNAME", testName.text.toString())
            cv.put("READINGS", Readings)
            cv.put("PARAMETERS", Param)
            cv.put("INFO", para.text.toString())
            cv.put("NEED", requiredScore.text.toString())
            db.insert("TESTS", null, cv)
            Toast.makeText(activity, "Insert Success", Toast.LENGTH_SHORT).show()
            testName.setText("")
            para.setText("")

            var rs = db.rawQuery("SELECT * FROM TESTS", null)

            if(rs.moveToNext()) {
                var asa : String
                asa = rs.getString(1) +" "+ rs.getString(2) +" "+ rs.getString(3)
                Toast.makeText(context, asa, Toast.LENGTH_LONG).show()
            }

            container_basic!!.visibility = View.GONE
            animationLayout!!.visibility = View.VISIBLE
            ok_animation.playAnimation()

            ok_animation.addAnimatorListener(object : Animator.AnimatorListener{
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    //Add your code here for animation end
                    animationLayout!!.visibility = View.GONE
                    container_basic!!.visibility = View.VISIBLE

                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }

            })


        }

        return root
    }
}