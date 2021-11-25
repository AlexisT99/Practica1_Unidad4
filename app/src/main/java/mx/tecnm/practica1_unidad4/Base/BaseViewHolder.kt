package mx.tecnm.practica1_unidad4.Base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import mx.tecnm.practica1_unidad4.Modelos.Contacto

abstract class BaseViewHolder<T>(itemView: View):RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: Contacto, position:Int)
}