package com.example.fitnessplayground

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDBHelper(context: Context) : SQLiteOpenHelper(context, "USERDB", null, 1){
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE USERS(ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, NAME VARCHAR(50), GENDER VARCHAR(5))")
        db?.execSQL("CREATE TABLE TESTS(ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, TESTNAME VARCHAR(50), READINGS INT, PARAMETERS INT, INFO VARCHAR(1500), NEED VARCHAR(50))")
        db?.execSQL("CREATE TABLE SCORE(ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, NAME VARCHAR(50), TEST VARCHAR(50), SCORE1 INT, SCORE2 INT, SCORE3 INT, TIME1 INT, TIME2 INT, TIME3 INT, DATE VARCHAR(50))")

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}