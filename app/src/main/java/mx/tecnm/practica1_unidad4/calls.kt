package mx.tecnm.practica1_unidad4

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.TelephonyManager
import android.widget.Toast
import android.telecom.TelecomManager
import android.telephony.SmsManager
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.FirebaseFirestore
import mx.tecnm.practica1_unidad4.ModelosBD.CONTACTO
import java.text.SimpleDateFormat
import java.util.*


class calls : BroadcastReceiver() {
    private var ring = false
    private var callReceived = false
    private val db = FirebaseFirestore.getInstance()

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onReceive(context: Context, intent: Intent) {
        ring = true
        val c: Date = Calendar.getInstance().getTime()
        val df = SimpleDateFormat("dd-MMM-yyyy 'at' hh:mm:ss")
        val formattedDate: String = df.format(c)
        val telecomManager: TelecomManager? = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager?
        if(intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)){
            val bundle = intent.extras
            var phoneNumber = bundle!!.getString("incoming_number")
            if (phoneNumber != null) {
                phoneNumber = phoneNumber.replace("[-+.^:,]".toRegex(), "")
                val c = MyApplication.appContext?.let { CONTACTO(it) }
                if (c != null) {
                    if(c.consultaN(phoneNumber)){
                        telecomManager?.endCall ()
                        Toast.makeText(MyApplication.appContext,"llamada entrante de $phoneNumber, enviando a buzon...",Toast.LENGTH_LONG).show()
                        SmsManager.getDefault().sendTextMessage(phoneNumber,null,"No contestare, Deja de molestar",null,null)
                        db.collection("lista Negra").document().set(
                            hashMapOf("Telefono" to phoneNumber,"Hora" to formattedDate )
                        )
                    }

                }
            }
        }
        if(intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
            callReceived=true
        }
        if(intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_IDLE)){
            if(ring && !callReceived)
            {
                ring=false;
                val bundle = intent.extras
                var phoneNumber = bundle!!.getString("incoming_number")
                if (phoneNumber != null) {
                    phoneNumber = phoneNumber.replace("[-+.^:,]".toRegex(), "")
                    val c = MyApplication.appContext?.let { CONTACTO(it) }
                    if (c != null) {
                        if(c.consultaB(phoneNumber)){
                            SmsManager.getDefault().sendTextMessage(phoneNumber,null,"Estoy un poco ocupado en este momento, te llamo mas tarde :D",null,null)
                            db.collection("lista Blanca").document().set(
                                hashMapOf("Telefono" to phoneNumber,"Hora" to formattedDate )
                            )
                        }

                    }
                }
            }
            callReceived=false;
        }
    }
}