package mx.tecnm.practica1_unidad4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import mx.tecnm.practica1_unidad4.ModelosBD.CONTACTO
import mx.tecnm.practica1_unidad4.databinding.ActivityAgregarListaBlancaBinding

class AgregarListaBlanca : AppCompatActivity() {
    private lateinit var b: ActivityAgregarListaBlancaBinding
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityAgregarListaBlancaBinding.inflate(layoutInflater)
        setContentView(b.root)
        b.btnAgregar.setOnClickListener {
            val c = CONTACTO(this)
            c.numero = b.edtPhone.text.toString()
            c.nombre = b.edtNombre.text.toString()
            c.bloqueado = 0
            if(c.insertar()){
                Toast.makeText(this,"Agregado con exito", Toast.LENGTH_LONG).show()
                db.collection("Contactos lista blanca").document().set(
                    hashMapOf("Telefono" to c.numero,"nombre" to c.nombre )
                )
            }
            finish()
        }
    }
}