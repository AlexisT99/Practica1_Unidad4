package mx.tecnm.practica1_unidad4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import mx.tecnm.practica1_unidad4.ModelosBD.CONTACTO
import mx.tecnm.practica1_unidad4.databinding.ActivityActualizarListaNegraBinding
import mx.tecnm.practica1_unidad4.databinding.ActivityMainBinding

class ActualizarListaNegra : AppCompatActivity() {
    private lateinit var b: ActivityActualizarListaNegraBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityActualizarListaNegraBinding.inflate(layoutInflater)
        setContentView(b.root)
        val id = intent.extras!!.getInt("id")
        val c = CONTACTO(this)
        var datos = c.consultaI(id)
        b.edtNombre.setText(datos.usuario)
        b.edtPhone.setText(datos.numero)
        b.btnActualizar.setOnClickListener {
            c.nombre = b.edtNombre.text.toString()
            c.numero = b.edtPhone.text.toString()
            if(c.actualizar(id.toString())){
                Toast.makeText(this,"Actualizado con exito",Toast.LENGTH_LONG).show()
            }
            finish()
        }
    }
}