package mx.tecnm.practica1_unidad4

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import mx.tecnm.practica1_unidad4.Modelos.Contacto
import mx.tecnm.practica1_unidad4.ModelosBD.CONTACTO
import mx.tecnm.practica1_unidad4.databinding.ActivityMainBinding
import java.util.*
import android.content.SharedPreferences

import android.preference.PreferenceManager




class MainActivity : AppCompatActivity(),RecyclerAdapter.onClickListener, SearchView.OnQueryTextListener {
    private lateinit var b: ActivityMainBinding
    lateinit var listaContacto:MutableList<Contacto>
    var listaContactoOriginal:MutableList<Contacto>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
        val PERMISSION_ALL = 1
        val PERMISSIONS = arrayOf(
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.ANSWER_PHONE_CALLS,
            Manifest.permission.SEND_SMS
        )

        if (!hasPermissions(this, *PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL)
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Advertencia")
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        if (!prefs.getBoolean("firstTime", false)) {
            builder.setMessage("Esta aplicación se ejecuta siempre y cuando esté abierta.\n Al agregar números escribe los puros números por favor.\n Enviara a buzón a los de lista negra con mensaje y a los de lista blanca al no contestarle les dirá que les llamara luego por SMS.\n El botón arriba del '+'(Agregar) es para cambiar entre listas")
            builder.setPositiveButton("OK"){ d,w->
                d.dismiss()
            }
            builder.show()
            // mark first time has ran.
            val editor = prefs.edit()
            editor.putBoolean("firstTime", true)
            editor.commit()
        }


        b.btnAgregarListaNegra.setOnClickListener {
            val intento = Intent( this, AgregarListaNegra::class.java)
            startActivity(intento)
        }
        b.btnListaBlanca.setOnClickListener {
            val intento = Intent( this, MainActivity2::class.java)
            startActivity(intento)
        }
        setupRecyclerView()
        b.search.setOnQueryTextListener(this)
    }

    private fun setupRecyclerView(){
        b.listaContactos.layoutManager = LinearLayoutManager(this)
        b.listaContactos.addItemDecoration(
            DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        val c = CONTACTO(this)
        listaContacto = c.consulta(1)
        listaContactoOriginal = c.consulta(1)
        b.listaContactos.adapter = RecyclerAdapter(this,listaContacto,this)
    }

    private fun RecargarRecycler(){
        b.listaContactos.adapter = RecyclerAdapter(this,listaContacto,this)
    }

    override fun onItemLongClick(id: Int, usuario: String, itemView: View, position: Int): Boolean {
       return true
    }

    override fun onClick(id: Int, usuario: String, itemView: View, position: Int): Boolean {
        val menu = PopupMenu(this,itemView)
        val builder = AlertDialog.Builder(this)
        val c = CONTACTO(this)
        menu.menu.add("Borrar")
        menu.menu.add("Editar")
        menu.setOnMenuItemClickListener {
            menu.dismiss()
            if (it.title.equals("Borrar")) {
                builder.setTitle("Advertencia")
                builder.setMessage("¿Estas seguro que quieres quitar a $usuario de la lista negra?")
                builder.setPositiveButton("Si"){ d,w->
                    c.eliminar(id)
                    listaContacto.removeAt(position)
                    listaContactoOriginal?.removeAt(position)
                    RecargarRecycler()
                    d.dismiss()
                }
                builder.setNegativeButton("No"){ d,w->
                    d.cancel()
                }
            }
            else if (it.title.equals("Editar")) {
                val intento = Intent( this, ActualizarListaNegra::class.java)
                intento.putExtra("id",id)
                menu.dismiss()
                startActivity(intento)
            }
            builder.show()
            true
        }
        menu.show()
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            filtrado(newText)
        }
        return false
    }
    fun filtrado(txtBuscar: String) {
        val longitud = txtBuscar.length
        if(longitud == 0){
            listaContacto.clear()
            listaContacto.addAll(listaContactoOriginal!!)
        }else{
            listaContacto.clear()
            listaContacto.addAll(listaContactoOriginal!!)
            val collecion = listaContacto.filter {
                it.usuario.lowercase(Locale.getDefault()).contains(txtBuscar.lowercase(Locale.getDefault()))
                    .or(it.numero.lowercase(Locale.getDefault()).contains(txtBuscar.lowercase(Locale.getDefault())))
            }
            listaContacto.clear()
            listaContacto.addAll(collecion)
        }
        RecargarRecycler()
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerView()
    }
    fun hasPermissions(context: Context, vararg permissions: String): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
}