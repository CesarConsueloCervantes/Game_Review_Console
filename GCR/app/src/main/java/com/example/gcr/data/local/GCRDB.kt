package com.example.gcr.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class GCRDB(
    context: Context,
): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val crearTablaUsuario = """
            CREATE TABLE $TABLE_USER (
                $ID TEXT PRIMARY KEY
            )
        """.trimIndent()
        db.execSQL(crearTablaUsuario)
    }

    fun insertUser(bd: SQLiteDatabase, user_id: String){
        val values = ContentValues().apply {
            put(ID, user_id)
        }
        bd.insert(TABLE_USER, null, values)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        onCreate(db)
    }

    companion object{
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "GCR.db"

        const val TABLE_USER = "user"
        const val ID = "id"
        const val user_name = "username"
        const val password = "password"
        const val image = "image"

    }
}