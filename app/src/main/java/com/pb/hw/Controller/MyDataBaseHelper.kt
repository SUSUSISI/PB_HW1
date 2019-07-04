package com.pb.hw.Controller

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.pb.hw.Retrofit.RetrofitModel

class MyDataBaseHelper(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) :
    SQLiteOpenHelper(context, name, factory, version) {
    val DATABASE_NAME = "History.db"
    var TABLE_NAME = ""
    val COL_1 = "ID"
    val COL_2 = "NAME"
    val COL_3 = "ADDRESS"
    val COL_4 = "LATITUDE"
    val COL_5 = "LONGITUDE"

    constructor(context: Context ) : this(context,"History.db", null, 1)
    constructor(context: Context, tableName : String ) : this(context,"History.db", null, 1) { TABLE_NAME = tableName }
    fun setTableName(name : String){TABLE_NAME = name}

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table ${TABLE_NAME} (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, ADDRESS TEXT, LATITUDE DOUBLE, LONGITUDE DOUBLE )")
    }

    fun dropTable(){
        val db = writableDatabase
        db.execSQL("DROP TABLE $TABLE_NAME")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {}

    fun insertData(place : RetrofitModel.Place) : Boolean{
        val item = ContentValues()
        item.put(COL_2,place.name)
        item.put(COL_3,place.add)
        item.put(COL_4,place.latitude)
        item.put(COL_5,place.longitude)

        val address = place.add
        val cursor = writableDatabase.rawQuery("select * from $TABLE_NAME where ADDRESS = '$address'",null)

        Log.d("cursorSize", cursor.count.toString())
        if(cursor.moveToNext()) {
            Log.d("db","이미 data 있음")
            Log.d("Name",cursor.getString(1))
            Log.d("Add",cursor.getString(2))
            Log.d("Lat",cursor.getString(3))
            Log.d("Long",cursor.getString(4))
            return true
        }

        return when(writableDatabase.insert(TABLE_NAME,null, item).toInt()){
            -1 -> false
            else -> true
        }
    }


    fun getAllData() : ArrayList<RetrofitModel.Place> {
        val cursor = writableDatabase.rawQuery("select * from ${TABLE_NAME}", null)
        var list = ArrayList <RetrofitModel.Place>()
        Log.d("cursorSize", cursor.count.toString())
        while(cursor.moveToNext()) {
            Log.d("Name",cursor.getString(1))
            Log.d("Add",cursor.getString(2))
            Log.d("Lat",cursor.getString(3))
            Log.d("Long",cursor.getString(4))
            list.add(
                RetrofitModel.Place(
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getDouble(4),
                    cursor.getDouble(3)
                )
            )
        }

        return list
    }
    fun deleteAllData() { writableDatabase.execSQL("delete from ${TABLE_NAME}")}
}