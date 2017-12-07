package com.example.user.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.myapplication.CrudCenso.CensoCreate;
import com.example.user.myapplication.CrudCenso.CensoDelete;
import com.example.user.myapplication.CrudCenso.CensoRead;
import com.example.user.myapplication.CrudCenso.CensoUpdate;
import com.example.user.myapplication.CrudFiscalização.FiscCreate;
import com.example.user.myapplication.CrudFiscalização.FiscDelete;
import com.example.user.myapplication.CrudFiscalização.FiscRead;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import uk.me.jstott.jcoord.LatLng;
import uk.me.jstott.jcoord.UTMRef;

public class Main4Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,LocationListener {
    private Spinner Censo;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationManager locationManager;
    private AlertDialog alerta;
    private FloatingActionButton fab;
    private boolean apertou;
    private ProgressBar progressBar;
    private TextView local;
    private boolean isOn;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    /* Função para verificar existência de conexão com a internet
 */
    public  boolean verificaConexao() {
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            conectado = true;
        } else {
            conectado = false;
        }
        return conectado;
    }

    private boolean isEmptySpn(Spinner spn) {
        return spn.getSelectedItemId() == 0;
    }

    private void Mensagem(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public boolean LoadFromExternalPrivateStorage() {
        File dir = Environment.getExternalStorageDirectory();  //creates the directory named MyDir in External Files Directory
        //File file = new File(dir,"teste.txt");  //Creates a new file named MyFile4.txt in a folder "dir"
        String lstrNomeArq="teste.txt";
        File arq;
        String lstrlinha;
        try
        {

            verifyStoragePermissions(this);
            arq = new File(Environment.getExternalStorageDirectory(), lstrNomeArq);
            BufferedReader br = new BufferedReader(new FileReader(arq));
            while((lstrlinha=br.readLine())!=null){
                String[] textoSeparado = lstrlinha.split(" ");
                Censo novo=new Censo(Integer.parseInt(textoSeparado[0]),textoSeparado[1], textoSeparado[2],
                        textoSeparado[3],textoSeparado[4] ,textoSeparado[5] ,textoSeparado[6], textoSeparado[7]
                        ,textoSeparado[8], textoSeparado[9]);
                CensoUpdate a=new CensoUpdate(getApplicationContext());
                a.insertCenso(novo);

            }

            return true;


        }
        catch (Exception e)
        {
            e.printStackTrace();
            Mensagem("Erro : " + e.getMessage());
            return false;
        }   //Calling user defined method
    }

    public boolean existeArquivo() {
        File dir = Environment.getExternalStorageDirectory();  //creates the directory named MyDir in External Files Directory
        //File file = new File(dir,"teste.txt");  //Creates a new file named MyFile4.txt in a folder "dir"
        String lstrNomeArq="teste.txt";
        File arq;
        //String lstrlinha;
        try
        {

            verifyStoragePermissions(this);
            arq = new File(Environment.getExternalStorageDirectory(), lstrNomeArq);
            return true;


        }
        catch (Exception e)
        {
            e.printStackTrace();
            Mensagem("Erro : " + e.getMessage());
            return false;
        }   //Calling user defined method
    }

    public void SaveIdsDisponiveis() {
        FileOutputStream fileOutputStream=null;
        File dir = Environment.getExternalStorageDirectory();
        File file = new File(dir,"ids.txt");
        try {
            fileOutputStream = new FileOutputStream(file);
            //fileOutputStream=openFileOutput("MyFile1.txt", Context.MODE_PRIVATE);  //to open the file for writing text in private mode
            OutputStreamWriter osw = new OutputStreamWriter(fileOutputStream);
            CensoRead k=new CensoRead(getApplicationContext());
            ArrayList<Censo> cArray=k.getRegistros();

            ArrayList<Integer>intArray=new ArrayList<>();
            for(int a=0;a<cArray.size();a++){
                intArray.add(cArray.get(a).getId());
                System.out.println("Id: "+intArray.get(a));
            }

            FiscRead b=new FiscRead(getApplicationContext());
            ArrayList<Fiscalizacao> fArray=b.getRegistros();
            if(fArray!=null){
                for(int a=0;a<fArray.size();a++){
                    System.out.println("FiscId: "+fArray.get(a).getCensoId());
                    if(intArray.contains(fArray.get(a).getCensoId())){
                        intArray.remove((Object)fArray.get(a).getCensoId());
                    }
                }
            }
            System.out.println("tamanho: "+intArray.size());
            for(int a=0;a<intArray.size();a++){
                System.out.println("IdIddas: "+intArray);
            }

            if(intArray.size()!=0){
                osw.write(intArray.get(0)+ "\r\n");
                //System.out.println("\n "+intArray.get(0));
                for (int i=1;i< intArray.size();i++){
                    //System.out.println("\n "+intArray.get(i));
                    osw.append(intArray.get(i)+ "\r\n");

                }
            }
            else {
                osw.write("");
            }

            osw.flush();
            osw.close();
            fileOutputStream.close();

            //writing the text into the file in the form of char[] bytes ; getBytes will convert string to char[] bytes
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(fileOutputStream!=null)
            {
                try {
                    fileOutputStream.close();    //closing the file
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //Calling user defined method
        //Carregar();

    }


    public boolean LoadIds() {
        File dir = Environment.getExternalStorageDirectory();  //creates the directory named MyDir in External Files Directory
        //File file = new File(dir,"teste.txt");  //Creates a new file named MyFile4.txt in a folder "dir"
        String lstrNomeArq = "ids.txt";
        File arq;
        String lstrlinha;
        try {
            verifyStoragePermissions(Main4Activity.this);
            arq = new File(Environment.getExternalStorageDirectory(), lstrNomeArq);
            BufferedReader br = new BufferedReader(new FileReader(arq));
            variaveisGlobais.idDisponíveis = new ArrayList<>();
            variaveisGlobais.idDisponíveis.add("Selecione um Ponto para Fiscalizar...");
            variaveisGlobais.idDisponíveis.add("0");
            while ((lstrlinha = br.readLine()) != null) {
                variaveisGlobais.idDisponíveis.add(lstrlinha);
            }
            return true;


        } catch (Exception e) {
            //e.printStackTrace();
            Mensagem("Erro : " + e.getMessage());
            return false;
        }   //Calling user defined method
    }


    //x= array
    public void configSpinner(Spinner spn) {
        CensoRead k = new CensoRead(getApplicationContext());
        ArrayList<Censo> cArray = k.getRegistros();
        final List<String> CensoList = new ArrayList<String>();
        CensoList.add("Selecione um Censo para Fiscalizar...");
        for (int i = 0; i < cArray.size(); i++) {
            CensoList.add(Integer.toString(cArray.get(i).getId()));
        }

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, variaveisGlobais.idDisponíveis) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spn.setAdapter(spinnerArrayAdapter);

        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    // Notify the selected item text
                    Toast.makeText
                            (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        apertou = false;
        progressBar=(ProgressBar)findViewById(R.id.progressBar2);
        progressBar.setEnabled(false);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        local=(TextView)findViewById(R.id.obtendoLocal);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Censo = (Spinner) findViewById(R.id.Censo);
        Censo.setPrompt("Lista de IDs de Censos disponíveis...");
        SaveIdsDisponiveis();
        LoadIds();
        System.out.println(variaveisGlobais.idDisponíveis);
        configSpinner(Censo);

        if (existeArquivo() == true) {
            SaveIdsDisponiveis();
            CensoCreate c = new CensoCreate(getApplicationContext());
            c.createTable();
            FiscCreate a = new FiscCreate(getApplicationContext());
            a.createTable();
            configSpinner(Censo);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
            configure_button();
        }
        else {
            Mensagem("Arquivo com o Censo não está no dispositivo");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main4, menu);
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_list) {
            ListarFisc();
        } else if (id == R.id.nav_resumo) {
            Intent it=new Intent(Main4Activity.this,Main5Activity.class);
            startActivity(it);

        } else if (id == R.id.nav_att) {
            AttCenso();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    void configure_button() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 10);
            }
            return;

        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if(!isOn){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Main4Activity.this);
                    //define o titulo
                    builder.setTitle("Localização");
                    //define a mensagem
                    builder.setMessage("Você precisa ligar o gps, de acordo? ");
                    //define um botão como positivo
                    builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            startActivity(new Intent(Main4Activity.this,Main6Activity.class));
                            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(i);
                        }
                    });
                    //define um botão como negativo.
                    builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            startActivity(new Intent(Main4Activity.this,Main6Activity.class));
                        }
                    });
                    //cria o AlertDialog
                    alerta = builder.create();
                    //Exibe
                    alerta.show();

                }*/

                if (!isEmptySpn(Censo)){
                    progressBar.setVisibility(View.VISIBLE);
                    local.setVisibility(View.VISIBLE);
                    progressBar.setEnabled(true);
                    fab.setEnabled(false);
                    variaveisGlobais.IdCenso=Integer.parseInt((String)Censo.getSelectedItem());
                    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    apertou=true;

                }
                else {
                    Mensagem("Censo não pode ser vazio, por favor selecione um censo");
                }
            }
        });
    }
    public void ListarFisc(){
        Intent it=new Intent(Main4Activity.this,Main3Activity.class);
        startActivity(it);
    }
    public void AttCenso(){
        try {
            //locationManager.removeUpdates(listener);
            //locationManager = null;
        }catch (Exception e){
            e.printStackTrace();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Titulo");
        //define a mensagem
        builder.setMessage("Deseja atualizar o Censo? ");
        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                CensoDelete b=new CensoDelete(getApplicationContext());
                b.deleteTable();
                CensoCreate c=new CensoCreate(getApplicationContext());
                c.createTable();
                FiscDelete d=new FiscDelete(getApplicationContext());
                d.deleteTable();
                FiscCreate a=new FiscCreate(getApplicationContext());
                a.createTable();
                LoadFromExternalPrivateStorage();
                SaveIdsDisponiveis();
                LoadIds();
                Intent it =new Intent(Main4Activity.this,Main4Activity.class);
                startActivity(it);



            }
        });
        //define um botão como negativo.
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {


            }
        });
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();


    }

    @Override
    public void onLocationChanged(Location location) {
        if(apertou==true){
            double x=location.getLatitude();
            double y=location.getLongitude();
            fab.setEnabled(true);

            LatLng novo2=new LatLng(x,y);
            UTMRef novo3=novo2.toUTMRef();
            variaveisGlobais.UtmX=novo3.getEasting();
            variaveisGlobais.UtmY=novo3.getNorthing();
            variaveisGlobais.intZona=novo3.getLngZone();
            variaveisGlobais.Zona=novo3.getLatZone();
            //Mensagem(""+variaveisGlobais.intZona+"    "+variaveisGlobais.Zona);
            progressBar.setVisibility(View.INVISIBLE);
            local.setVisibility(View.INVISIBLE);
            progressBar.setEnabled(false);
            Intent it=new Intent(Main4Activity.this,Main2Activity.class);
            Mensagem("Localização Determinada");
            startActivity(it);
            apertou=false;
        }


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
