/*
 *   Copyright (C) 2015, 2016 - Samuel Peregrina Morillas <gaedr@correo.ugr.es>, Nieves V. Velásquez Díaz <nievesvvd@correo.ugr.es>
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.nieves.puntonfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    Tag tag;
    private TextView textView;
    private NfcAdapter nfcAdapter;
    private String key = "\u0002eshola";


    /**
     * Metodo con el que inicializamos la aplicacion y comprobamos tanto si el dispositivo soporta NFC
     * asi como que si esta activado.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tag = null;
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        textView = (TextView) findViewById(R.id.texto);

        //comprobamos si el usuario tiene ncf
        if (nfcAdapter == null) {
            textView.setText(R.string.tieneNFC);
            finish();
            return;
        }
        //vemos si esta activado o no el NFC
        if (!nfcAdapter.isEnabled()) {
            textView.setText(R.string.NFCnoActivo);
        } else {
            textView.setText(R.string.NFCactivo);
        }

        //creamos un nuevo intent con los datos del tag y lo enviamos a esta actividad
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // creamos un intetn filtro con los datos tipo MIME
        IntentFilter tagDetectada = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ndefIntent = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter[] intentFilters = new IntentFilter[]{tagDetectada, ndefIntent};
    }

    /**
     * Metodo que es llamado cuando el usuario aproxima un tag al dispositivo, de forma que evitamos
     * que se abra la aplicacion de nuevo
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        textView.setText(R.string.FindTag);
        // if (getIntent().getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
        tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        setIntent(intent);
        leerTag(intent);
        //}
    }

    /**
     * Metodo con el que leemos el contenido de una tag NFC
     *
     * @param intent
     */
    public void leerTag(Intent intent) {
        Ndef ndef = Ndef.get(tag);
        try {
            ndef.connect();

            Parcelable[] mensaje = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (mensaje != null) {

                NdefMessage[] textPaso = new NdefMessage[mensaje.length];
                for (int j = 0; j < mensaje.length; j++) {
                    textPaso[j] = (NdefMessage) mensaje[j];
                }
                NdefRecord record = textPaso[0].getRecords()[0];

                byte[] payload = record.getPayload();
                String textoFinal = new String(payload);

                textView.setText(getString(R.string.mensajeLeido) + textoFinal);
                //una vez leido el texto, veremos si el contenido del mensaje es el que queremos
                if (textoFinal.toLowerCase().equals(key)) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));
                    startActivity(browserIntent);
                } else {
                    Log.d(TAG, "Contenido NFC: " + textoFinal.toLowerCase() + "\nContenido KEY: " + key);
                    textView.setText(R.string.mensajeFallido);
                }

                ndef.close();
            }

        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "No se ha podido leer el mensaje.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Metodo con el que interceptamos el que hacer cuando se detecta una nueva tag de NFC
     *
     * @param activity actividad en la que nos encontramos
     * @param adapter  adaptador que vamos a usar, en este caso NFC
     */

    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};
        
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType("text/plain");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Revisa en tipo de Mime.");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    /**
     * Metodo con el que finalizamos el interceptar una nueva señal de la tag de NFC
     *
     * @param activity La peticion correspondiente  para detener foreground dispatch.
     * @param adapter  The {@link NfcAdapter} usado para el foreground dispatch.
     */
    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    /**
     * Metodo con el que indicamos que hacer cuando vuelva la aplicacion del segundo plano.
     */
    @Override
    protected void onResume() {
        super.onResume();
        setupForegroundDispatch(this, nfcAdapter);
    }

    /**
     * Metodo con el que le decimos a la aplicacion que hacer cuando se envia a segundo plano.
     */
    @Override
    protected void onPause() {
        stopForegroundDispatch(this, nfcAdapter);
        super.onPause();
    }
}
