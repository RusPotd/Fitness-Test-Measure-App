package com.example.fitnessplayground

import android.content.ContentValues
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.animation.Animation
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.content_test.*
import java.text.SimpleDateFormat
import java.util.*


class Test : AppCompatActivity() {

    private var username: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        setSupportActionBar(findViewById(R.id.toolbar))

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()?.setDisplayShowHomeEnabled(true);

        var btnStart : Button
        var btnStop : Button
        var btnStart2 : Button
        var btnStop2 : Button
        var btnStart3 : Button
        var btnStop3 : Button
        var chronometer : Chronometer
        var chronometer2 : Chronometer
        var chronometer3 : Chronometer
        var rotate: Animation
        var circle: CircleImageView
        var circle2: CircleImageView
        var circle3: CircleImageView
        var pause : Long
        pause = 0
        var pause2 : Long
        pause2 = 0
        var pause3 : Long
        pause3 = 0

        btnStart = findViewById(R.id.btnStart)
        btnStop = findViewById(R.id.btnStop)
        btnStart2 = findViewById(R.id.btnStart2)
        btnStop2 = findViewById(R.id.btnStop2)
        btnStart3 = findViewById(R.id.btnStart3)
        btnStop3 = findViewById(R.id.btnStop3)

        chronometer = findViewById(R.id.timer)
        chronometer2 = findViewById(R.id.timer2)
        chronometer3 = findViewById(R.id.timer3)
        circle = findViewById(R.id.undo)
        circle2 = findViewById(R.id.undo2)
        circle3 = findViewById(R.id.undo3)


        btnStart.setOnClickListener {
            //circle.startAnimation(rotate)
            chronometer.format = "%s"
            chronometer.base = SystemClock.elapsedRealtime()-pause
            chronometer.start()
            btnStart.visibility = View.GONE
            btnStop.visibility = View.VISIBLE
        }

        btnStart2.setOnClickListener {
            //circle.startAnimation(rotate)
            chronometer2.format = "%s"
            chronometer2.base = SystemClock.elapsedRealtime()-pause2
            chronometer2.start()
            btnStart2.visibility = View.GONE
            btnStop2.visibility = View.VISIBLE
        }

        btnStart3.setOnClickListener {
            //circle.startAnimation(rotate)
            chronometer3.format = "%s"
            chronometer3.base = SystemClock.elapsedRealtime()-pause3
            chronometer3.start()
            btnStart3.visibility = View.GONE
            btnStop3.visibility = View.VISIBLE
        }

        btnStop.setOnClickListener {
            chronometer.stop()
            //chronometer.base = SystemClock.elapsedRealtime()
            pause = SystemClock.elapsedRealtime() - chronometer.base
            var milisec = (pause%1000).toInt() //81985 985
            var sec = (pause/1000).toInt()  //81
            var min = (sec/60).toInt()  //1
            var fsec = (sec%60).toInt()    //21
            output_time.setText(min.toString()+":"+fsec.toString()+":"+milisec.toString())
            btnStart.visibility = View.VISIBLE
            btnStop.visibility = View.GONE
        }

        btnStop2.setOnClickListener {
            chronometer2.stop()
            //chronometer.base = SystemClock.elapsedRealtime()
            pause2 = SystemClock.elapsedRealtime() - chronometer2.base
            var milisec2 = (pause2%1000).toInt() //81985 985
            var sec2 = (pause2/1000).toInt()  //81
            var min2 = (sec2/60).toInt()  //1
            var fsec2 = (sec2%60).toInt()    //21
            output_time2.setText(min2.toString()+":"+fsec2.toString()+":"+milisec2.toString())
            btnStart2.visibility = View.VISIBLE
            btnStop2.visibility = View.GONE
        }

        btnStop3.setOnClickListener {
            chronometer3.stop()
            //chronometer.base = SystemClock.elapsedRealtime()
            pause3 = SystemClock.elapsedRealtime() - chronometer3.base
            var milisec3 = (pause3%1000).toInt() //81985 985
            var sec3 = (pause3/1000).toInt()  //81
            var min3 = (sec3/60).toInt()  //1
            var fsec3 = (sec3%60).toInt()    //21
            output_time3.setText(min3.toString()+":"+fsec3.toString()+":"+milisec3.toString())
            btnStart3.visibility = View.VISIBLE
            btnStop3.visibility = View.GONE
        }

        circle.setOnClickListener {
            chronometer.stop()
            chronometer.base = SystemClock.elapsedRealtime()
            pause = 0
            output_time.setText("00:00:00")
        }

        circle2.setOnClickListener {
            chronometer2.stop()
            chronometer2.base = SystemClock.elapsedRealtime()
            pause2 = 0
            output_time2.setText("00:00:00")
        }

        circle3.setOnClickListener {
            chronometer3.stop()
            chronometer3.base = SystemClock.elapsedRealtime()
            pause3 = 0
            output_time3.setText("00:00:00")
        }

        /********************************************************************/
        var helper = MyDBHelper(applicationContext)
        var db = helper.readableDatabase
        var readings : Int = 0
        var parameters : Int = 0
        var videoLink : String = ""

        val b = intent.extras
        val Test_name = b!!.getString("testName")
        Toast.makeText(applicationContext, Test_name, Toast.LENGTH_LONG).show()

        chronometer.base = SystemClock.elapsedRealtime()
        chronometer2.base = SystemClock.elapsedRealtime()
        chronometer3.base = SystemClock.elapsedRealtime()
        output_time.setText("")
        output_time2.setText("")
        output_time3.setText("")
        output_score.setText("")
        output_score2.setText("")
        output_score3.setText("")

        var rs = db.rawQuery("SELECT * FROM TESTS WHERE TESTNAME=?", arrayOf(Test_name))

        if(rs.moveToNext()) {
            var asa : String
            readings = rs.getString(2).toInt()
            parameters = rs.getString(3).toInt()
            videoLink = rs.getString(4).toString()
        }

        var videoPlayer = findViewById<YouTubePlayerView>(R.id.videoPlayer)
        lifecycle.addObserver(videoPlayer)

        videoPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                //app:videoId="qzcGfN9S_QY"
                youTubePlayer.cueVideo(videoLink, 0f)
            }
        })

        if(readings==3){
            layout2.visibility = View.VISIBLE
            layout3.visibility = View.VISIBLE
        }
        else{
            layout2.visibility = View.GONE
            layout3.visibility = View.GONE
        }

        if(parameters==1){
            output_time.isEnabled = false
            output_time2.isEnabled = false
            output_time3.isEnabled = false
            btnStart.isEnabled = false
            btnStart2.isEnabled = false
            btnStart3.isEnabled = false
            undo.isEnabled = false
            undo2.isEnabled = false
            undo3.isEnabled = false

            output_score.isEnabled = true
            output_score2.isEnabled = true
            output_score3.isEnabled = true
        }
        else if(parameters==2){
            output_score.isEnabled = false
            output_score2.isEnabled = false
            output_score3.isEnabled = false

            output_time.isEnabled = true
            output_time2.isEnabled = true
            output_time3.isEnabled = true
            btnStart.isEnabled = true
            btnStart2.isEnabled = true
            btnStart3.isEnabled = true
            undo.isEnabled = true
            undo2.isEnabled = true
            undo3.isEnabled = true
        }
        else{
            output_score.isEnabled = true
            output_score2.isEnabled = true
            output_score3.isEnabled = true

            output_time.isEnabled = true
            output_time2.isEnabled = true
            output_time3.isEnabled = true
            btnStart.isEnabled = true
            btnStart2.isEnabled = true
            btnStart3.isEnabled = true
            undo.isEnabled = true
            undo2.isEnabled = true
            undo3.isEnabled = true
        }
        /*****************************************************************************************/

        //var Test_name : String
        //Test_name = ""
        var select_test : Spinner = findViewById(R.id.select_test)
        var names : ArrayList<String>
        names = java.util.ArrayList()

        rs = db.rawQuery("SELECT * FROM USERS", null)
        while(rs.moveToNext()) {
            var asa : String
            asa = rs.getString(1)
            names.add(asa)
            //Toast.makeText(context, asa, Toast.LENGTH_LONG).show()
        }

        select_test.adapter = ArrayAdapter<String>(applicationContext, android.R.layout.simple_list_item_1, names)

        select_test.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
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
                username = names.get(position)
            }

        }

        val sdf = SimpleDateFormat("dd/M/yyyy")
        val currentDate = sdf.format(Date())

        val save_score_Btn: Button = findViewById(R.id.save_score_Btn)
        save_score_Btn.setOnClickListener {
            var cv = ContentValues()
            cv.put("NAME", username)
            cv.put("TEST", Test_name)
            cv.put("SCORE1", output_score.text.toString())
            cv.put("SCORE2", output_score2.text.toString())
            cv.put("SCORE3", output_score3.text.toString())
            cv.put("TIME1", pause.toString())
            cv.put("TIME2", pause2.toString())
            cv.put("TIME3", pause3.toString())
            cv.put("DATE", currentDate.toString())
            db.insert("SCORE", null, cv)

            Toast.makeText(applicationContext, "Inserted", Toast.LENGTH_SHORT).show()

            chronometer.base = SystemClock.elapsedRealtime()
            chronometer2.base = SystemClock.elapsedRealtime()
            chronometer3.base = SystemClock.elapsedRealtime()
            output_time.setText("")
            output_time2.setText("")
            output_time3.setText("")
            output_score.setText("")
            output_score2.setText("")
            output_score3.setText("")
            pause = 0
            pause2 = 0
            pause3 = 0

        }
    }
}