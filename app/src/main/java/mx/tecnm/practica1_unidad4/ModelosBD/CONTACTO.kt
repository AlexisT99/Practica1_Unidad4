package mx.tecnm.practica1_unidad4.ModelosBD

import android.content.ContentValues
import android.content.Context
import mx.tecnm.practica1_unidad4.BaseDatos
import mx.tecnm.practica1_unidad4.Modelos.Contacto
import mx.tecnm.practica1_unidad4.R
import java.util.*

class CONTACTO(p: Context) {
    val p = p
    var nombre = ""
    var numero = ""
    var bloqueado = 0

    fun insertar(): Boolean {
        val tabla = BaseDatos(p, "Usuarios", null, 1).writableDatabase
        val dato = ContentValues()
        dato.put("USUARIO", nombre)
        dato.put("NUMERO",numero)
        dato.put("BLOQUEADO", bloqueado)
        val resultado = tabla.insert("CONTACTO", null, dato)
        tabla.close()
        if (resultado == -1L) return false
        return true
    }

    fun consulta(bloqueo:Int): MutableList<Contacto> {
        val tabla = BaseDatos(p, "Usuarios", null, 1).writableDatabase
        val listaUsuarios = mutableListOf<Contacto>()
        val cursor = tabla.query("CONTACTO", arrayOf("*"), "BLOQUEADO=?", arrayOf(bloqueo.toString()), null, null, null)
        if (cursor.moveToFirst()&&bloqueo==1) {
            do {
                listaUsuarios.add(Contacto(cursor.getInt(0),cursor.getString(1),cursor.getString(2), R.drawable.devil))
            } while (cursor.moveToNext())
        }
        else if(cursor.moveToFirst()&&bloqueo==0){
            do {
                listaUsuarios.add(Contacto(cursor.getInt(0),cursor.getString(1),cursor.getString(2), R.drawable.friendship))
            } while (cursor.moveToNext())
        }
        tabla.close()
        return listaUsuarios
    }

    fun eliminar(id: Int): Boolean {
        val tabla = BaseDatos(p, "Usuarios", null, 1).writableDatabase
        val resultado = tabla.delete("CONTACTO", "ID=?", arrayOf(id.toString()))
        if (resultado == 0) return false
        return true
    }

    fun actualizar(idActualizar:String): Boolean{
        val tabla = BaseDatos (p, "Usuarios", null,  1).writableDatabase
        val datos = ContentValues()
        datos.put("USUARIO", nombre)
        datos.put("NUMERO",numero)
        val resultado = tabla.update( "CONTACTO", datos,  "ID=?", arrayOf(idActualizar))
        if(resultado == 0) return false
        return true
    }

    fun consultaI(id:Int): Contacto {
        val tabla = BaseDatos(p, "Usuarios", null, 1).writableDatabase
        lateinit var contacto:Contacto
        val cursor = tabla.query("CONTACTO", arrayOf("*"), "ID=?", arrayOf(id.toString()), null, null, null)
        if (cursor.moveToFirst()) {
            contacto =  Contacto(cursor.getInt(0),cursor.getString(1),cursor.getString(2), R.drawable.friendship)
        }
        tabla.close()
        return  contacto
    }
    fun consultaN(numerot:String): Boolean {
        val tabla = BaseDatos(p, "Usuarios", null, 1).writableDatabase
        val cursor = tabla.query("CONTACTO", arrayOf("*"), "NUMERO=? AND BLOQUEADO=1", arrayOf(numerot), null, null, null)
        if (cursor.moveToFirst()) {
          return true
        }
        tabla.close()
        return false
    }
    fun consultaB(numerot:String): Boolean {
        val tabla = BaseDatos(p, "Usuarios", null, 1).writableDatabase
        val cursor = tabla.query("CONTACTO", arrayOf("*"), "NUMERO=? AND BLOQUEADO=0", arrayOf(numerot), null, null, null)
        if (cursor.moveToFirst()) {
            return true
        }
        tabla.close()
        return false
    }
}