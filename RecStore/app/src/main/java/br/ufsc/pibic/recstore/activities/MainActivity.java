package br.ufsc.pibic.recstore.activities;

import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Arrays;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;

import br.ufsc.pibic.recstore.R;
import br.ufsc.pibic.recstore.fragments.OffersFragment;
import br.ufsc.pibic.recstore.fragments.PurchaseFragment;
import br.ufsc.pibic.recstore.fragments.SeenFragment;
import br.ufsc.pibic.recstore.fragments.SettingsFragment;
import br.ufsc.pibic.recstore.util.InteractionDefinition;
import br.ufsc.pibic.recstore.util.Util;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BeaconConsumer {

    private Beacon lastBeacon = null;
    private BeaconManager beaconManager;
    private boolean timePassed = false;
    private boolean timerStarted = false;
    private Class currentFragment = null;
    private double lastDistance = -1.0;
    private final String TAG = "MAIN_ACT";
    private int bounded = 0;
    private int detected = 0;
    private Integer userId = 1;
    private IntentFilter[] intentFilters;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private Region region = null;
    private String[][] nfcTechLists;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        beaconSetup();          // Runs setup commands for beacon
        nfcSetup();             // Runs setup commands for NFC.
        initializeFragment();   // Initializes a default starting fragment

        ////////////////////////////////////
        // userId = 3; // TODO: pegar o user_id da tela de login
        // TODO: pegar o id pelos Extras da intent.

        Intent intent = getIntent();
        this.userId = intent.getIntExtra("user_id", -1);

        Log.d(TAG, "User_id from intent: " + this.userId);
        /////////////////////////////
        timer = new Timer();
    }

    /**
     * Beacon settings setup
     */
    private void beaconSetup() {

        if (bounded == 0) {
            // Verificação de bluetooth ativo.
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            if (adapter != null && !adapter.isEnabled()) {
                Toast.makeText(getApplicationContext(), "Ligue o bluetooth para usar a aplicação!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Bluetooth ativado", Toast.LENGTH_SHORT).show();
            }

            // Configuração do bluetooth
            beaconManager = BeaconManager.getInstanceForApplication(this.getApplicationContext());
            beaconManager.setBackgroundMode(false);
            beaconManager.setBackgroundScanPeriod(1000L);
            beaconManager.setBackgroundBetweenScanPeriod(1000L);
            beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
            beaconManager.bind(this);
            bounded = 1;
        }

        Log.d("DEBUG", "Binded");
    }

    /**
     * NFC Settings Setup
     */
    private void nfcSetup() {

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            Toast.makeText(getApplicationContext(), "NFC não suportado", Toast.LENGTH_LONG).show();
        } else {
            if (nfcAdapter.isEnabled()) {
                Toast.makeText(getApplicationContext(), "NFC ativado", Toast.LENGTH_SHORT).show();
                pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

                IntentFilter ndefIntent = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
                try {
                    ndefIntent.addDataType("*/*");
                    intentFilters = new IntentFilter[]{ndefIntent};
                } catch (Exception e) {
//
                }

                nfcTechLists = new String[][]{new String[]{NfcF.class.getName()}};
            } else {
                Toast.makeText(getApplicationContext(), "NFC desativado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        try {
            Intent intentAct = new Intent(getApplicationContext(), InteractionActivity.class);

            String nfcContent = "";

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

                                nfcContent += new String(payload, langCodeLen + 1, payload.length - langCodeLen - 1, textEncoding);

                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e("TagDispatch", e.toString());
                }
            }


            String url = InteractionDefinition.buildURL(userId, InteractionDefinition.TYPE_URL_RECORD,
                    InteractionDefinition.DEVICE_NFC, nfcContent);
            intentAct.putExtra("url", url);

            startActivity(intentAct);
        } catch (Exception e) {
            Log.e("DEBUG", e.getMessage());
        }
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {
                if (detected == 0 && collection.size() > 0) {

                    Log.d("DEBUG", collection.size() + " Beacon(s) encontrado(s)!");

                    for (Beacon beacon : collection) {
                        Log.d("DEBUG_MAIN_ACT", "Beacon info:" + beacon.getBluetoothName() + "||" + beacon.getId1() + "||" + beacon.getId2() + "||" + beacon.getId3() + "||" + beacon.getBluetoothAddress() + "||" + beacon.getDistance() + "||" + beacon.getRssi());

                        if (timePassed &&
                                (beacon.getId1().equals(lastBeacon.getId1()))/* &&
                               (beacon.getDistance() > lastBeacon.getDistance() * 0.9 ||
                                        beacon.getDistance() < lastBeacon.getDistance() * 1.1)*/) {

                            Log.d(TAG, "Starting beacon act");

                            String url = Util.createURLBeacon(beacon, userId);
                            Intent intentAct = new Intent(getApplicationContext(), InteractionActivity.class);
                            //intentAct.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intentAct.putExtra("url", url);

                            timerStarted = false;


                            startActivity(intentAct);

                            timePassed = false;

                        } else if (!timerStarted) {
                            Log.d(TAG, "Starting timer");
                            lastBeacon = beacon;
                            lastDistance = beacon.getDistance();

                            TimerTask timerTask = new TimerTask() {
                                @Override
                                public void run() {
                                    try {
                                        timePassed = true;
                                        Log.d(TAG, "Timer finished");
                                    } catch (Exception e) {
                                        Log.d(TAG, e.getMessage());
                                        e.printStackTrace();
                                    }
                                }
                            };
                            Log.d("TIMER", "Time: " + InteractionDefinition.getTime());
                            timer.schedule(timerTask, InteractionDefinition.getTime()*1000); // Executará uma vez após X segundos
                            Log.d(TAG, "Timer started");
                            timerStarted = true;
                            timePassed = false;
                        }
                    }
                }
            }
        });

        try {
            // Inicia o monitoramento
            Log.d("DEBUG", "Iniciando monitoramento.");

            this.region = new Region("REGION", null, null, null);
            beaconManager.startRangingBeaconsInRegion(this.region);
            beaconManager.startMonitoringBeaconsInRegion(this.region);
        } catch (RemoteException e) {
            Log.d("DEBUG", e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


        if (nfcAdapter != null)
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, nfcTechLists);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        Class fragmentClass = null;

        // Handle l view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            fragmentClass = OffersFragment.class;
        } else if (id == R.id.nav_gallery) {
            fragmentClass = PurchaseFragment.class;
        } else if (id == R.id.nav_slideshow) {
            fragmentClass = SeenFragment.class;
        } else if (id == R.id.nav_manage) {
            fragmentClass = SettingsFragment.class;
        } else {
            fragmentClass = OffersFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }

        currentFragment = fragmentClass;
        Bundle bundle = new Bundle();
        bundle.putInt("user_id", userId);

        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fiContent, fragment).commit();
        item.setChecked(true);
        setTitle(item.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawers(/*GravityCompat.START*/);

        return true;
    }

    /**
     * Inicializa a fragment que será mostrada logo na inicialização do app.
     */
    private void initializeFragment() {
        Fragment fragment = null;
        Class fragmentClass = OffersFragment.class;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }

        Bundle bundle = new Bundle();
        bundle.putInt("user_id", userId);

        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fiContent, fragment).commit();

        currentFragment = OffersFragment.class;

        setTitle("Ofertas");

        DrawerLayout drawer2 = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer2.closeDrawers(/*GravityCompat.START*/);
    }
}