package br.ufsc.pibic.nfcrw;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class NFCWriteActivity extends AppCompatActivity {

    /**
     * Tag para mensagens de Log
     */
    private static final String TAG = "NFC_WRITE_ACT";

    /**
     * Mensagem de sucesso para a intent.
     */
    private static final String WRISUCESS = "WSUC";

    /**
     * Visualizador de textos
     */
    private TextView tvMessage;

    /**
     * Caixa de texto para escrita do novo conteúdo das tags
     */
    private EditText edtWriteText;

    /**
     * Adaptador NFC do dispositivo.
     */
    private NfcAdapter nfcAdapter;

    /**
     * Filtragem de Intent's recebidas.
     */
    private IntentFilter[] intentFilters;

    /**
     * Activity pendentente para ser executada.
     */
    private PendingIntent nfcPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_write);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Filtragem do tipo de Intent que será capturada.
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        intentFilters = new IntentFilter[]{tagDetected};

        // Instanciação dos itens de layout.
        tvMessage = (TextView) findViewById(R.id.tvMessage);
        edtWriteText = (EditText) findViewById(R.id.edtWriteText);

        // Recebe o adaptador padrão de NFC.
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            tvMessage.setText("Este dispositivo não tem suporte a NFC");
        } else {
            tvMessage.setText("Aguardando Gravação de Tag");

            // Ativa activity, mas configura para que não seja executada caso já o esteja.
            nfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if (rawMsgs != null) {
                NdefMessage[] msgs = new NdefMessage[rawMsgs.length];

                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
                for (NdefMessage tmpMsg : msgs) {
                    for (NdefRecord tmpRecord : tmpMsg.getRecords()) {
                        Log.e(TAG, "\n" + new String(tmpRecord.getPayload()));
                    }
                }
            }
        }

        if (nfcAdapter != null) {
            // Ativa o envio em primeiro plano da Activity.
            nfcAdapter.enableForegroundDispatch(this, nfcPendingIntent, intentFilters, null);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
//      super.onNewIntent(intent);

        // Caso seja uma descoberta de uma TAG.
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            // Caso a caixa de texto não esja vazia.
            if (!TextUtils.isEmpty(edtWriteText.getText().toString())) {

                // Cria uma registro e uma mensagem NDEF a partir da String.
                NdefRecord ndefRecord = stringToNdefRecord(edtWriteText.getText().toString());
                NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{ndefRecord});

                // Caso a escrita na tag ocorra, coloca a MainActivity em primeiro plano.
                if (writeNFCTag(ndefMessage, detectedTag)) {
                    Intent main = new Intent(this, MainActivity.class);

                    main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    main.putExtra(WRISUCESS, true);

                    this.startActivity(main);
                } else {
                    Snackbar.make(tvMessage, "Falha na Gravação", Snackbar.LENGTH_LONG).show();
                }
            } else {
                Snackbar.make(tvMessage, "Tag não definida!", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Método recebe uma cadeia de caracteres e cria um registro NDEF contendo a cadeia como conteúdo.
     *
     * @param paylod Cadeia de caracteres
     * @return Registro NDEF
     */
    public NdefRecord stringToNdefRecord(String paylod) {

        byte[] textBytes = paylod.getBytes();
        byte[] data = new byte[1 + textBytes.length];

        data[0] = (byte) 0;

        System.arraycopy(textBytes, 0, data, 1, textBytes.length);

        NdefRecord ndefRecord = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);

        return ndefRecord;
    }

    /**
     * Escreve o conteúdo de uma mensagem NDEF e uma tag
     *
     * @param ndefMessage Mensagem NDEF
     * @param tag         Tag que será escrita
     * @return true caso tenha efetuado a escrita com sucesso e, false, caso contrário.
     */
    public static boolean writeNFCTag(NdefMessage ndefMessage, Tag tag) {

        int sizeNdefMessage = ndefMessage.toByteArray().length; // Número de bytes da mensagem NDEF

        try {
            Ndef ndef = Ndef.get(tag); // Cria um registro NDEF a partir da tag.
            if (ndef != null) {
                ndef.connect(); // Habilita operações de leitura e escrita.

                if (!ndef.isWritable())
                    return false; // Caso a tag não seja escrevível, retorna falso.

                Log.d(TAG, "MAX size: " + ndef.getMaxSize() + "-- Size: " + sizeNdefMessage);
                if (ndef.getMaxSize() < sizeNdefMessage)
                    return false; // Caso o tamanho da msg seja maior que o tamanho da tag, retorna falso.

                ndef.writeNdefMessage(ndefMessage);

                return true;
            } else {
                NdefFormatable ndefFormatable = NdefFormatable.get(tag);
                if (ndefFormatable == null) {
                    try {
                        ndefFormatable.connect();
                        ndefFormatable.format(ndefMessage);
                        return true;
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
    }
}
