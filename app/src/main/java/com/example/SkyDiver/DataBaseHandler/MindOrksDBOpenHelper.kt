package com.example.SkyDiver.DataBaseHandler

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MindOrksDBOpenHelper(context: Context,
                           factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME,
        factory, DATABASE_VERSION) {


    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_PRODUCTS_TABLE = (
                "CREATE TABLE " + TABLE_NAME +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME + " TEXT" +
                COLUMN_WEIGHT + "TEXT"+
                COLUMN_EQUIPMENT + "TEXT"+
                COLUMN_CANOPY + "TEXT" + ")"
            )
            db.execSQL(CREATE_PRODUCTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }
    fun addName(name: ListItem) {
        val values = ContentValues()
        values.put(COLUMN_NAME, name.userName)
        values.put(COLUMN_WEIGHT, name.userWeight)
        values.put(COLUMN_EQUIPMENT, name.userEquipment)
        values.put(COLUMN_CANOPY, name.userCanopy)
        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }


    fun getAllName(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    fun deletItemFromDB( title :String )
    {
        val db = this.readableDatabase
        db.delete(TABLE_NAME, "$COLUMN_NAME=?", arrayOf(title))
    }

    fun deleteDB()
    {
        val db = this.writableDatabase
        db.execSQL("delete from "+ TABLE_NAME);
    }

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "mindorksName.db"
        val TABLE_NAME = "name"
        val COLUMN_ID = "_id"
        val COLUMN_NAME = "username"
        val COLUMN_WEIGHT = "weight"
        val COLUMN_EQUIPMENT = "equipment"
        val COLUMN_CANOPY = "canopy"
    }
}