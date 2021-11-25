package mx.tecnm.practica1_unidad4

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mx.tecnm.practica1_unidad4.Base.BaseViewHolder
import mx.tecnm.practica1_unidad4.Modelos.Contacto
import mx.tecnm.practica1_unidad4.databinding.ContactosRowBinding
import java.lang.IllegalArgumentException

class RecyclerAdapter (private val context: Context, val listaContacto:List<Contacto>, private val itemClickListener:onClickListener): RecyclerView.Adapter<BaseViewHolder<*>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return UsuariosViewHolder(LayoutInflater.from(context).inflate(R.layout.contactos_row,parent,false))
    }
    interface onClickListener{
        fun onItemLongClick(id: Int, usuario: String, itemView: View, position: Int):Boolean
        fun onClick(id: Int, nombre: String, itemView: View, position: Int):Boolean
    }

    //Para cada informacion
    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when(holder){
            is RecyclerAdapter.UsuariosViewHolder -> holder.bind(listaContacto[position],position)
            else -> throw IllegalArgumentException("No se paso el viewholder")
        }
    }

    override fun getItemCount(): Int = listaContacto.size
    //como tratar la informacion
    inner class UsuariosViewHolder(itemView: View): BaseViewHolder<Contacto>(itemView){
        val b= ContactosRowBinding.bind(itemView)
        override fun bind(item: Contacto, position: Int) {
            itemView.setOnLongClickListener { itemClickListener.onItemLongClick(item.id,item.usuario,itemView,position)
            }
            itemView.setOnClickListener { itemClickListener.onClick(item.id,item.usuario,itemView,position)
            }
            b.txtRowId.text = item.id.toString()
            b.imagen.setBackgroundResource(item.img)
            b.txtRowNombre.text = item.usuario
            b.txtRowNumero.text = item.numero
        }
    }
}