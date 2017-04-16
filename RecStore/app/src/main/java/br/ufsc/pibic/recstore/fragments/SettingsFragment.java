package br.ufsc.pibic.recstore.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import br.ufsc.pibic.recstore.R;
import br.ufsc.pibic.recstore.util.InteractionDefinition;
import br.ufsc.pibic.recstore.util.Util;

import static android.R.attr.x;


public class SettingsFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        final SeekBar seekBar = (SeekBar) v.findViewById(R.id.sbTempo);
        final EditText editText = (EditText) v.findViewById(R.id.edtIP);
        final TextView textView = (TextView) v.findViewById(R.id.tvTempo);
        final FloatingActionButton button = (FloatingActionButton) v.findViewById(R.id.btn_save_cfg);
        seekBar.setProgress(InteractionDefinition.getTime());

        String x = seekBar.getProgress() + " segundo(s)";
        textView.setText(x);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    progress = 1;
                }

                String x = progress + " segundo(s)";
                textView.setText(x);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InteractionDefinition.setIp(editText.getText().toString());
                InteractionDefinition.setTime(seekBar.getProgress());

                String ip = InteractionDefinition.getIp();
                Integer time = InteractionDefinition.getTime();

                String x = ip + "|" + time;
                Toast.makeText(v.getContext(), x, Toast.LENGTH_LONG).show();
            }
        });


        return v;
    }

    private void getConfigPath(View v, TextView textView) {
        try {
            FileInputStream fileInputStream = new FileInputStream(Util.CONFIG_PATH);

            byte[] dados = new byte[fileInputStream.available()];

            fileInputStream.read(dados, 0, dados.length);

            String text = new String(dados, 0, dados.length, "UTF-8");
            JSONObject json = new JSONObject(text);
            Log.d("JSON", json.toString());

            textView.setText(json.getInt("time"));

            fileInputStream.close();

        } catch (JSONException e) {
            Toast.makeText(v.getContext(), "Erro ao abrir o arquivo.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(Util.CONFIG_PATH);
                fileOutputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            Toast.makeText(v.getContext(), "Erro ao abrir o arquivo.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}