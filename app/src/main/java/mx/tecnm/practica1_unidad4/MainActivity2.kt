package mx.tecnm.practica1_unidad4

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import mx.tecnm.practica1_unidad4.Modelos.Contacto
import mx.tecnm.practica1_unidad4.ModelosBD.CONTACTO
import mx.tecnm.practica1_unidad4.databinding.ActivityMain2Binding
import mx.tecnm.practica1_unidad4.databinding.ActivityMainBinding
import java.util.*

class MainActivity2 : AppCompatActivity(),RecyclerAdapter.onClickListener, SearchView.OnQueryTextListener {
    private lateinit var b: ActivityMain2Binding
    lateinit var listaContacto:MutableList<Contacto>
    var listaContactoOriginal:MutableList<Contacto>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(b.root)
        b.btnAgregarListaBlanca.setOnClickListener {
            val intento = Intent( this, AgregarListaBlanca::class.java)
            startActivity(intento)
        }
        b.btnListaNegra.setOnClickListener {
            val intento = Intent( this, MainActivity::class.java)
            startActivity(intento)
        }
        setupRecyclerView()
        b.search.setOnQueryTextListener(this)
    }

    private fun setupRecyclerView(){
        b.listaContactos.layoutManager = LinearLayoutManager(this)
        b.listaContactos.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
        val c = CONTACTO(this)
        listaContacto = c.consulta(0)
        listaContactoOriginal = c.consulta(0)
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
                builder.setMessage("??Estas seguro que quieres quitar a $usuario de la lista blanca?")
                builder.setPositiveButton("Si"){ d,w->
                    c.eliminar(id)
                    listaContacto.removeAt(position)
                    listaContactoOriginal?.removeAt(position)
                    RecargarRecycler()
                }
                builder.setNegativeButton("No"){ d,w->
                    d.cancel()
                }
            }
            else if (it.title.equals("Editar")) {
                val intento = Intent( this, ActualizarListaBlanca::class.java)
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
}