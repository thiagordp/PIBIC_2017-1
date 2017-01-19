package br.ufsc.pibic.nfcrw;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Arrays;

public class NFCReadActivity extends AppCompatActivity {

    private final String TAG = "READ_ACT";

    /**
     * Visualizador de mensagens e leituras de NFC.
     */
    private TextView tvMessage;

    /**
     * Adaptador NFC do dispositivo.
     */
    private NfcAdapter nfcAdapter;

    /**
     *
     */
    private PendingIntent pendingIntent;
    /**
     *
     */
    private IntentFilter[] intentFilters;

    private String[][] nfcTechLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_read);

        // Configuração do botão de voltar.
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvMessage = (TextView) findViewById(R.id.tvMessage);        // Instanciação da textview que mostra informações de leitura
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);            // Atribui instância do adaptador NFC do dispositivo.

        // Caso o dispositivo não tenha suporte a NFC, aborta.
        if (nfcAdapter == null) {
            tvMessage.setText("Este dispositivo não tem suporte a NFC");
        } else {

            tvMessage.setText("Passe a TAG NFC");

            // Cria uma Intent que não poderá ser executada se já estiver no topo da pilha de histórico.
            pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

            // Criar um filtro para capturar Intents que indicam descobertas de tags NFC;
            IntentFilter ndefIntent = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);

            // TODO: Comentar e entender o código a partir deste ponto.
            try {
                ndefIntent.addDataType("*/*"); // Configura o tipo de dados que será aceito.
                intentFilters = new IntentFilter[]{ndefIntent};
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }

            nfcTechLists = new String[][]{new String[]{NfcF.class.getName()}};
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        String action = intent.getAction();
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        String s = action + "\n\n" + tag.toString();
        String valorSensor = "";

        Parcelable[] data = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (data != null) {
            try {
                for (int i = 0; i < data.length; i++) {
                    NdefRecord[] recs = ((NdefMessage) data[i]).getRecords();
                    for (int j = 0; j < recs.length; j++) {
                        if (recs[j].getTnf() == NdefRecord.TNF_WELL_KNOWN &&
                                Arrays.equals(recs[j].getType(), NdefRecord.RTD_TEXT)) {
                            byte[] payload = recs[j].getPayload();
                            String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
                            int langCodeLen = payload[0] & 0077;

                            s += ("\n\nNdefMessage[" + i + "], NdefRecord[" + j + "]:\n\"" +
                                    new String(payload, langCodeLen + 1, payload.length - langCodeLen - 1,
                                            textEncoding) + "\"");

                            valorSensor += new String(payload, langCodeLen + 1, payload.length - langCodeLen - 1, textEncoding);

                        }
                    }
                }
            } catch (Exception e) {
                Log.e("TagDispatch", e.toString());
            }
        }


        Log.e(TAG, s);

        tvMessage.setText(valorSensor);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null)
            nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null)
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, nfcTechLists);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
