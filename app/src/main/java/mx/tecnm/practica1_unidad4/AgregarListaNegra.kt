package mx.tecnm.practica1_unidad4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import mx.tecnm.practica1_unidad4.Modelos.Contacto
import mx.tecnm.practica1_unidad4.ModelosBD.CONTACTO
import mx.tecnm.practica1_unidad4.databinding.ActivityAgregarListaNegraBinding
import mx.tecnm.practica1_unidad4.databinding.ActivityMainBinding

class AgregarListaNegra : AppCompatActivity() {
    private lateinit var b: ActivityAgregarListaNegraBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityAgregarListaNegraBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.btnAgregar.setOnClickListener {
            val c = CONTACTO(this)
            c.numero = b.edtPhone.text.toString()
            c.nombre = b.edtNombre.text.toString()
            c.bloqueado = 1
            if(c.insertar()){
                Toast.makeText(this,"Agregado con exito",Toast.LENGTH_LONG).show()
            }

            finish()
        }
    }
}