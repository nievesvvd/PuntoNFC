package com.nieves.puntonfc;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

//import android.hardware.Camera.Parameters;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "NfcDemo";
    Tag tag;
    private TextView textView;
    private NfcAdapter nfcAdapter;
    private String key = "hola";
    Intent intent = getIntent();
    private CameraManager linterna;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        mTextView = (TextView) findViewById(R.id.textView_explanation);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        //comprobamos si el usuario tiene ncf
        if (nfcAdapter == null) {
            Toast.makeText(this, "Este dispositivo no es compatible con NFC.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        //vemos si esta activado o no el NFC
        if (!nfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC esta desactivado.", Toast.LENGTH_SHORT).show();
        }
        leerTag(intent);
    }

    /**
     * Metodo con el que manejaremos que pasa cuando el telefono lee varias tag con la aplicacion
     * abierta buscando evitar que la abra de nuevo
     *
     * @param i
     */
    public void leerTag(Intent i) {
        Ndef ndef = Ndef.get(tag);

        try {
            ndef.connect();
            //interface for classes whose instances can be written to and restored from a Parcel.
            Parcelable[] mensaje = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            //vemos si el mensaje de la tag esta o no vacio
            if (mensaje != null) {
                NdefMessage[] textPaso = new NdefMessage[mensaje.length];
                for (int j = 0; j < mensaje.length; j++) {
                    textPaso[j] = (NdefMessage) mensaje[j];
                }
                NdefRecord record = textPaso[0].getRecords()[0];

                byte[] payload = record.getPayload();
                String textoFinal = new String(payload);

                textView.setText("Mensaje leido!! Su contenido es: " + textoFinal);
                //una vez leido el texto, veremos si el contenido del mensaje es el que queremos
                if (textoFinal.toLowerCase() == key) {
                    encenderLinterna();
                } else
                    textView.setText("El contenido del mensaje no es correcto");

                ndef.close();
            }
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "No se ha podido leer el mensaje.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Metodo que enciende la linterna del dispositivo, para ello vemos si esta o no disponible y
     * trabajamos en consecuencia a partir de la ID de la camara correcta
     */
    private void encenderLinterna() {
        String id;

        //si la linterna esta disponible, vemos la camara que la tiene (id) para poder encenderla
        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            id = obtenerIDCamara();
            linterna.setTorchMode(id, true);
        }
    }

    /**
     * Metodo con el que obtenemos el ID de la camara posterior para poder encender la linterna
     *
     * @return
     */
    public String obtenerIDCamara() {
        final String id;

        for (id:
             linterna.getCameraIdList()) {
            CameraCharacteristics caracter = linterna.getCameraCharacteristics(id);
            int camara = caracter.get(CameraCharacteristics.LENS_FACING);
            if (camara == CameraCharacteristics.LENS_FACING_BACK)
                return id;
        }
    }

    /**
     * Metodo que es llamado cuando el usuario aproxima un tag al dispositivo, de forma que evitamos
     * que se habra la aplicacion de nuevo
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        if (getIntent().getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED))
            tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        setIntent(intent);
        leerTag(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        /**
         * It's important, that the activity is in the foreground (resumed). Otherwise
         * an IllegalStateException is thrown.
         */
//        setupForegroundDispatch(this, nfcAdapter);
    }

    @Override
    protected void onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
         */
//        stopForegroundDispatch(this, nfcAdapter);

        super.onPause();
    }
}
