package com.example.user.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.audiofx.BassBoost;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.me.jstott.jcoord.LatLng;
import uk.me.jstott.jcoord.UTMRef;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private Spinner Censo;
    private TextView longitude;
    private TextView latitude;
    private TextView txtUtm;
    private String lat="Latitude: Obtendo Dados...";
    private String longi="Longitude: Obtendo Dados...";
    private String utm="Utm: Obtendo Dados...";
    private LocationManager locationManager;
    private LocationListener listener;
    private AlertDialog alerta;

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

    private boolean isEmptySpn(Spinner spn) {
        return spn.getSelectedItemId()==0;
    }

    private void Mensagem(String msg)
    {
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

            verifyStoragePermissions(MainActivity.this);
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
            }

            FiscRead b=new FiscRead(getApplicationContext());
            ArrayList<Fiscalizacao> fArray=b.getRegistros();

            for(int a=0;a<fArray.size();a++){
                if(intArray.contains(fArray.get(a).getCensoId())){
                    intArray.remove((Object)fArray.get(a).getCensoId());
                }
            }


            osw.write(intArray.get(0)+ "\r\n");
            for (int i=1;i< intArray.size();i++){
                System.out.println("\n "+intArray.get(i));
                osw.append(intArray.get(i)+ "\r\n");

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
        String lstrNomeArq="ids.txt";
        File arq;
        String lstrlinha;
        try
        {

            verifyStoragePermissions(MainActivity.this);
            arq = new File(Environment.getExternalStorageDirectory(), lstrNomeArq);
            BufferedReader br = new BufferedReader(new FileReader(arq));
            variaveisGlobais.idDisponíveis=new ArrayList<>();
            variaveisGlobais.idDisponíveis.add("Selecione um Ponto para Fiscalizar...");
            variaveisGlobais.idDisponíveis.add("0");
            while((lstrlinha=br.readLine())!=null){
                variaveisGlobais.idDisponíveis.add(lstrlinha);

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


                                           //x= array
    public void configSpinner(Spinner spn){
        CensoRead k=new CensoRead(getApplicationContext());
        ArrayList<Censo> cArray=k.getRegistros();
        final List<String> CensoList = new ArrayList<String>();
        CensoList.add("Selecione um Censo para Fiscalizar...");
        for (int i=0;i< cArray.size();i++){
            CensoList.add(Integer.toString(cArray.get(i).getId()));
        }

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item,variaveisGlobais.idDisponíveis){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
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
                if(position > 0){
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
        setContentView(R.layout.activity_main);



        latitude = (TextView) findViewById(R.id.latitude);
        longitude = (TextView) findViewById(R.id.longitude);
        button = (Button) findViewById(R.id.button);
        txtUtm=(TextView)findViewById(R.id.utm);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Censo=(Spinner)findViewById(R.id.Censo);
        Censo.setPrompt("Lista de IDs de Censos disponíveis...");
        LoadIds();


        if(LoadFromExternalPrivateStorage()==true){
            CensoCreate c=new CensoCreate(getApplicationContext());
            c.createTable();
            FiscCreate a=new FiscCreate(getApplicationContext());
            a.createTable();
            configSpinner(Censo);


            listener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double x=location.getLatitude();
                    double y=location.getLongitude();
                    lat="Latitude: "+x;
                    longi="Longitude: "+y;

                    LatLng novo2=new LatLng(x,y);
                    UTMRef novo3=novo2.toUTMRef();
                    StringBuilder sb = new StringBuilder();
                    sb.append("Zona: ");
                    sb.append(novo3.getLngZone());
                    sb.append(novo3.getLatZone());
                    sb.append(" E: ");
                    variaveisGlobais.UtmX=novo3.getEasting();
                    sb.append(variaveisGlobais.UtmX);
                    variaveisGlobais.UtmY=novo3.getNorthing();
                    sb.append(" N: ");
                    sb.append(variaveisGlobais.UtmY);
                    utm = sb.toString();

                    variaveisGlobais.UtmCompleto=utm;


                    txtUtm.setText("UTM: "+utm);
                    latitude.setText(lat);
                    longitude.setText(longi);
                    Intent it=new Intent(MainActivity.this,Main2Activity.class);
                    locationManager.removeUpdates(listener);
                    locationManager = null;
                    Mensagem("Localização Determinada");
                    startActivity(it);

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {


                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {
                    Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(i);
                }
            };
            configure_button();
        }
        else {
            Mensagem("Arquivo com o Censo não está no dispositivo");
        }


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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if (!isEmptySpn(Censo)){
                    latitude.setText("Latitude: Obtendo Dados...");
                    longitude.setText("Longitude: Obtendo Dados...");
                    txtUtm.setText("UTM: Obtendo Dados...");
                    variaveisGlobais.IdCenso=Integer.parseInt((String)Censo.getSelectedItem());
                    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    locationManager.requestLocationUpdates("gps", 5000, 0, listener);

                }
                else {
                    Mensagem("Censo não pode ser vazio, por favor selecione um censo");
                }



            }
        });
    }
    public void ListarFisc(View v){
        locationManager.removeUpdates(listener);
        locationManager = null;
        Intent it=new Intent(MainActivity.this,Main3Activity.class);
        startActivity(it);
    }
    public void AttCenso(View v){
        locationManager.removeUpdates(listener);
        locationManager = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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

                Intent it=new Intent(MainActivity.this,MainActivity.class);
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
}
