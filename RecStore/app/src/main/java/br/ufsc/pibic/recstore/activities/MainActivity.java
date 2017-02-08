package br.ufsc.pibic.recstore.activities;

import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Bundle;
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
import org.json.JSONArray;

import java.util.Collection;

import br.ufsc.pibic.recstore.R;
import br.ufsc.pibic.recstore.fragments.OffersFragment;
import br.ufsc.pibic.recstore.fragments.PurchaseFragment;
import br.ufsc.pibic.recstore.fragments.SeenFragment;
import br.ufsc.pibic.recstore.fragments.SettingsFragment;
import br.ufsc.pibic.recstore.util.Util;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BeaconConsumer {

    private BeaconManager beaconManager;
    private PendingIntent pendingIntent;
    private int detected = 0;
    private NfcAdapter nfcAdapter;
    private String[][] nfcTechLists;
    private IntentFilter[] intentFilters;
    private Class currentFragment = null;
    private Integer userId = -1;

    private int bounded = 0;

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
        userId = 1; // TODO: pegar o user_id da tela de login
        /////////////////////////////
    }

    /**
     * Beacon settings setup
     */
    private void beaconSetup() {

        // Verificação de bluetooth ativo.
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null && !adapter.isEnabled()) {
            Toast.makeText(getApplicationContext(), "Ligue o bluetooth para usar a aplicação!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Bluetooth ativado", Toast.LENGTH_SHORT).show();
        }

        // Configuração do bluetooth
        if (bounded == 0) {
            beaconManager = BeaconManager.getInstanceForApplication(this.getApplicationContext());
            beaconManager.setBackgroundMode(true);
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

        Toast.makeText(getApplicationContext(), "Tag identificada!", Toast.LENGTH_LONG).show();


        String x = "{products : [{ \"_id\" : { \"$oid\" : \"5897d24e2385721f682a73d2\" }, " +
                "\"product_id\" : 2, \"product_name\" : \"Watch\", \"product_price\" : 50.0 }, " +
                "{ \"_id\" : { \"$oid\" : \"5897d24d2385721f682a73d1\" }, " +
                "\"product_id\" : 1, \"product_name\" : \"Caneta\", \"product_price\" : 2.5 } ]}";

        JSONArray json = Util.parserJason(x);

        try {
            Intent intentAct = new Intent(getApplicationContext(), InteractionActivity.class);
            //intentAct.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
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
                        Log.d("DEBUG", beacon.getBluetoothName() + "||" + beacon.getId1() + "||" + beacon.getRssi());


                        String url = Util.createURLBeacon(beacon, userId);
                        Intent intentAct = new Intent(getApplicationContext(), InteractionActivity.class);
                        //intentAct.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intentAct.putExtra("url", url);

                        startActivity(intentAct);
                        break;
                    }
                    detected = 1;
                }
            }
        });

        try {
            // Inicia o monitoramento
            Log.d("DEBUG", "Iniciando monitoramento.");
            beaconManager.startRangingBeaconsInRegion(new Region("REGION", null, null, null));
            beaconManager.startMonitoringBeaconsInRegion(new Region("REGION", null, null, null));
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

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fiContent, fragment).commit();
        item.setChecked(true);
        setTitle(item.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawers(/*GravityCompat.START*/);


        return true;
    }


    /*********************************
     * Criação de URL de interação
     * <p>
     * TODO: Substituir por Asynctasks
     *********************************/


    private void initializeFragment() {
        Fragment fragment = null;
        Class fragmentClass = OffersFragment.class;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fiContent, fragment).commit();

        currentFragment = OffersFragment.class;

        setTitle("Ofertas");

        DrawerLayout drawer2 = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer2.closeDrawers(/*GravityCompat.START*/);
    }
}
