package com.example.fitnessplayground.ui.score

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.fitnessplayground.MyDBHelper
import com.example.fitnessplayground.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_showscore.*
import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.hssf.usermodel.HSSFRow
import org.apache.poi.hssf.usermodel.HSSFSheet
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.text.DecimalFormat

//score
class score : Fragment(), OnChartValueSelectedListener{

    var uname: String = ""
    var test: String = ""

    private lateinit var mChart: LineChart
    var required : String = ""

    var s1 = 0
    var s2 = 0
    var s3 = 0
    var Param = 0
    var readings = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_showscore, container, false)

        mChart = root.findViewById(R.id.lineChart)
        mChart.setOnChartValueSelectedListener(this)

        var bundle : Bundle? = this.arguments
        uname = bundle?.getString("username").toString()

        Toast.makeText(context, uname, Toast.LENGTH_LONG).show()

        var usernameSpinner : Spinner
        usernameSpinner = root.findViewById(R.id.username)


        var helper = MyDBHelper(requireContext())
        var db = helper.readableDatabase

        var names : ArrayList<String>
        names = java.util.ArrayList()

        var rs = db.rawQuery("SELECT * FROM USERS", null)
        while(rs.moveToNext()) {
            var asa : String
            asa = rs.getString(1)
            names.add(asa)
            //Toast.makeText(context, asa, Toast.LENGTH_LONG).show()
        }


        usernameSpinner.adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, names)
        if(uname!=""){
            usernameSpinner.setSelection(names.indexOf(uname))
        }

        usernameSpinner.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                TODO("Not yet implemented")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                uname = names.get(position)
            }

        }

        /*********************************************************************************************/

        var TestNames : ArrayList<String>
        TestNames = java.util.ArrayList()

        rs = db.rawQuery("SELECT * FROM TESTS", null)
        while(rs.moveToNext()) {
            var asa : String
            asa = rs.getString(1)
            TestNames.add(asa)
            //Toast.makeText(context, asa, Toast.LENGTH_LONG).show()
        }

        var TestNameSpinner : Spinner
        TestNameSpinner = root.findViewById(R.id.test_Name)

        TestNameSpinner.adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, TestNames)

        TestNameSpinner.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                TODO("Not yet implemented")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                test = TestNames.get(position)

                mChart.invalidate()

                var scoreOrTime : Spinner = root.findViewById(R.id.scoreOrTime)
                val parametersOption = arrayOf("Score", "Time")
                scoreOrTime.adapter = ArrayAdapter<String>(root.context, android.R.layout.simple_list_item_1, parametersOption)

                scoreOrTime.onItemSelectedListener = object : AdapterView.OnItemClickListener,  AdapterView.OnItemSelectedListener {
                    override fun onItemClick(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                       //
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        //
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
                            s1 = 3
                            s2 = 4
                            s3 = 5
                        }
                        else if (P=="Time"){
                            Param = 2
                            s1 = 6
                            s2 = 7
                            s3 = 8
                        }

                        rs = db.rawQuery("SELECT * FROM TESTS WHERE TESTNAME=?", arrayOf(test))

                        if(rs!=null && rs.count>0) {
                            while(rs.moveToNext()){
                                readings = rs.getInt(2)
                            }
                        }

                        mChart.invalidate()

                        mChart.isDragEnabled = true
                        mChart.setScaleEnabled(false)

                        var yValues : ArrayList<Entry>
                        yValues = ArrayList()

                        rs = db.rawQuery("SELECT * FROM SCORE WHERE NAME=? AND TEST=?", arrayOf(uname, test))

                        if(rs!=null && rs.count>0) {

                            var i = 1
                            var min = 10000f

                            while (rs.moveToNext()) {
                                yValues.add(
                                    Entry(
                                        i.toFloat(),
                                        rs.getInt(s1).toFloat()
                                    )
                                )
                                i += 1
                                if (rs.getInt(s1) < min) {
                                    min = rs.getInt(s1).toFloat()
                                }
                            }

                            var set1 = LineDataSet(yValues, "Set 1")

                            var dataSet: ArrayList<ILineDataSet>
                            dataSet = ArrayList()

                            if(readings==3) {

                                var zValues: ArrayList<Entry>
                                zValues = ArrayList()

                                rs = db.rawQuery(
                                    "SELECT * FROM SCORE WHERE NAME=? AND TEST=?",
                                    arrayOf(uname, test)
                                )

                                i = 1
                                while (rs.moveToNext()) {
                                    zValues.add(
                                        Entry(
                                            i.toFloat(),
                                            rs.getInt(s2).toFloat()
                                        )
                                    )
                                    i += 1
                                    if (rs.getInt(s2) < min) {
                                        min = rs.getInt(s2).toFloat()
                                    }
                                }

                                var set2 = LineDataSet(zValues, "Set 2")

                                var wValues: ArrayList<Entry>
                                wValues = ArrayList()

                                rs = db.rawQuery(
                                    "SELECT * FROM SCORE WHERE NAME=? AND TEST=?",
                                    arrayOf(uname, test)
                                )

                                i = 1
                                while (rs.moveToNext()) {
                                    wValues.add(
                                        Entry(
                                            i.toFloat(),
                                            rs.getInt(s3).toFloat()
                                        )
                                    )
                                    i += 1
                                    if (rs.getInt(s3) < min) {
                                        min = rs.getInt(s3).toFloat()
                                    }
                                }

                                var set3 = LineDataSet(wValues, "Set 3")

                                set2.fillAlpha = 110
                                set2.setColor(Color.BLUE)
                                set2.lineWidth = 3f
                                set2.valueTextSize = 10f

                                set3.fillAlpha = 110
                                set3.setColor(Color.WHITE)
                                set3.lineWidth = 3f
                                set3.valueTextSize = 10f


                                dataSet.add(set1)
                                dataSet.add(set2)
                                dataSet.add(set3)
                            }
                            else{
                                dataSet.add(set1)
                            }

                            set1.fillAlpha = 110
                            set1.setColor(Color.RED)
                            set1.lineWidth = 3f
                            set1.valueTextSize = 10f


                            var data = LineData(dataSet)
                            mChart.data = data

                            mChart.data.isHighlightEnabled = true
                            mChart.setPinchZoom(true)

                            if (Param == 2) {
                                minScore.setText((min / 1000).toString())
                            } else {
                                minScore.setText(min.toString())
                            }
                        }
                        else{
                            //player not given that test
                            mChart.data = LineData()
                            mChart.invalidate()

                            requiredScore.setText("")
                            average.setText("")
                            minScore.setText("")

                            date.setText("")
                            score_1_out.setText("")
                            score_2_out.setText("")
                            score_3_out.setText("")

                        }
                    }
                }


                rs = db.rawQuery("SELECT * FROM TESTS WHERE TESTNAME=?", arrayOf(test))
                if(rs!=null && rs.count>0) {

                    while (rs.moveToNext()) {
                        required = rs.getString(5)
                    }
                    requiredScore.setText(required)
                }
            }

        }

        /*********************************************************************************************/
        //set line

        var MaxRange = root.findViewById<EditText>(R.id.MaxRange)

        MaxRange.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                //
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(MaxRange.text.toString()!="") {
                    Toast.makeText(requireContext(), mChart.visibleXRange.toString(), Toast.LENGTH_SHORT).show()
                    if(mChart.visibleXRange.toInt() > MaxRange.text.toString().toInt()){
                        //Log.i("Max", "Max -> visible : "+mChart.visibleXRange.toString()+" got : "+MaxRange.text.toString())
                        mChart.setVisibleXRangeMaximum(MaxRange.text.toString().toFloat()) //decide minimum to show
                    }
                    else{
                        //Log.i("Min", "Min -> visible : "+mChart.visibleXRange.toString()+" got : "+MaxRange.text.toString())
                        mChart.setVisibleXRangeMinimum(MaxRange.text.toString().toFloat()) //decide minimum to show
                    }
                    mChart.invalidate()
                    mChart.moveViewToX(mChart.xChartMax)

                }
            }

        })

        /******************************************************************************************/

        var share : CircleImageView = root.findViewById(R.id.share)
        share.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Export to Excel")
            builder.setMessage("Do you want to export this data?")
            builder.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->

                var filePath = File(Environment.getExternalStorageDirectory().toString() + "/"+uname+"_data.xls");

                val hssfWorkbook = HSSFWorkbook()
                var hssfSheet: HSSFSheet = hssfWorkbook.createSheet("Training Data "+uname)

                val hssfRow: HSSFRow = hssfSheet.createRow(0)

                hssfRow.createCell(0).setCellValue("Name")
                hssfRow.createCell(1).setCellValue("Test")
                hssfRow.createCell(2).setCellValue("Date")
                hssfRow.createCell(3).setCellValue("Score 1")
                hssfRow.createCell(4).setCellValue("Score 2")
                hssfRow.createCell(5).setCellValue("Score 3")
                hssfRow.createCell(6).setCellValue("Time 1")
                hssfRow.createCell(7).setCellValue("Time 2")
                hssfRow.createCell(8).setCellValue("Time 3")

                rs = db.rawQuery("SELECT * FROM SCORE WHERE NAME=?", arrayOf(uname))

                var i = 1
                if(rs!=null && rs.count>0) {
                    while (rs.moveToNext()) {

                        var hssfRow2: HSSFRow = hssfSheet.createRow(i)

                        hssfRow2.createCell(0).setCellValue(rs.getString(1))
                        hssfRow2.createCell(1).setCellValue(rs.getString(2))
                        hssfRow2.createCell(2).setCellValue(rs.getString(9))
                        hssfRow2.createCell(3).setCellValue(rs.getString(3))
                        hssfRow2.createCell(4).setCellValue(rs.getString(4))
                        hssfRow2.createCell(5).setCellValue(rs.getString(5))
                        hssfRow2.createCell(6).setCellValue(rs.getString(6))
                        hssfRow2.createCell(7).setCellValue(rs.getString(7))
                        hssfRow2.createCell(8).setCellValue(rs.getString(8))

                        i+=1
                    }
                }



                try {
                    if (!filePath.exists()) {
                        filePath.createNewFile()
                    }
                    val fileOutputStream = FileOutputStream(filePath)
                    hssfWorkbook.write(fileOutputStream)
                    if (fileOutputStream != null) {
                        fileOutputStream.flush()
                        fileOutputStream.close()
                    }
                    Toast.makeText(requireContext(), "File saved to : "+filePath.toString(), Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            builder.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int ->
                //Toast.makeText(requireContext(),"Item Not Deleted", Toast.LENGTH_LONG).show()
            }
            builder.show()
        }

        return root
    }

    override fun onNothingSelected() {
        Log.d("Nothing selected", "Nothing selected")
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        var dataset = mChart.data.getDataSetForEntry(e)
        var index = dataset.getEntryIndex(e)

        var set1 = mChart.data.dataSets.get(0)

        var numberFormat : DecimalFormat = DecimalFormat("#.000")

        if(readings==3) {
            var set2 = mChart.data.dataSets.get(1)
            var set3 = mChart.data.dataSets.get(2)

            if(Param==2){
                score_1_out.setText(((set1.getEntryForIndex(index).y)/1000).toString())
                score_2_out.setText(((set2.getEntryForIndex(index).y)/1000).toString())
                score_3_out.setText(((set3.getEntryForIndex(index).y)/1000).toString())
            }else{
                score_1_out.setText(set1.getEntryForIndex(index).y.toString())
                score_2_out.setText(set2.getEntryForIndex(index).y.toString())
                score_3_out.setText(set3.getEntryForIndex(index).y.toString())
            }

            var avg = set1.getEntryForIndex(index).y + set2.getEntryForIndex(index).y + set3.getEntryForIndex(index).y

            if(Param==2){
                var avgS = (avg/3)/1000
                avgS = numberFormat.format(avgS).toFloat()
                average.setText(avgS.toString())

            }
            else{
                var avgS = (avg/3)
                avgS = numberFormat.format(avgS).toFloat()
                average.setText(avgS.toString())

            }
        }
        else{
            if(Param==2){
                score_1_out.setText(((set1.getEntryForIndex(index).y)/1000).toString())
            }else{
                score_1_out.setText(set1.getEntryForIndex(index).y.toString())

            }
            var avg = set1.getEntryForIndex(index).y


            if(Param==2){
                var avgS = (avg)/1000
                avgS = numberFormat.format(avgS).toFloat()
                average.setText(avgS.toString())

            }
            else{
                var avgS = (avg)
                avgS = numberFormat.format(avgS).toFloat()
                average.setText(avgS.toString())
            }
        }



        var helper = MyDBHelper(requireContext())
        var db = helper.readableDatabase

        var rs = db.rawQuery("SELECT * FROM SCORE WHERE NAME=? AND TEST=?", arrayOf(uname, test))
        if(rs!=null && rs.count>0) {

            var i = 0
            while (rs.moveToNext()) {
                if (i == index) {
                    var DBDate = rs.getString(9)
                    date.setText(DBDate)
                    break
                }
                i += 1
            }
        }


    }
}