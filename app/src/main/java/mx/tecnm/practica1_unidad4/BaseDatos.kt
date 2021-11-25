package mx.tecnm.practica1_unidad4

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BaseDatos(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(p0: SQLiteDatabase) {
        p0.execSQL("CREATE TABLE CONTACTO(ID INTEGER PRIMARY KEY AUTOINCREMENT, USUARIO VARCHAR(200),NUMERO VARCHAR(200),BLOQUEADO INTEGER)")
    }

    override fun onUpgrade(p0: SQLiteDatabase, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

}