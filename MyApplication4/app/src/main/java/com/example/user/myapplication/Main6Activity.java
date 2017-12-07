package com.example.user.myapplication;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.myapplication.CrudCenso.CensoCreate;
import com.example.user.myapplication.CrudCenso.CensoDelete;
import com.example.user.myapplication.CrudFiscalização.FiscCreate;
import com.example.user.myapplication.CrudFiscalização.FiscDelete;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import uk.me.jstott.jcoord.LatLng;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class Main6Activity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    TextView latitudeText;
    TextView longitudeText;
    private boolean apertou;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Double myLatitude;
    private Double myLongitude;
    private static final int MY_PERMISSION_REQUEST_FINE_LOCATION = 101;
    private static final int MY_PERMISSION_REQUEST_COARSE_LOCATION = 102;
    private boolean permissionIsGranted = false;
    Button settingButton;
    private AlertDialog alerta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);
        apertou=false;
        latitudeText = (TextView) findViewById(R.id.tvLatitude);
        longitudeText = (TextView) findViewById(R.id.tvLongitude);
        settingButton = (Button) findViewById(R.id.btSettings);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10 * 1000);
        locationRequest.setFastestInterval(15 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        final boolean isOn = manager.isProviderEnabled( LocationManager.GPS_PROVIDER);


        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_FINE_LOCATION);
                } else {
                    permissionIsGranted = true;
                }*/
                if(!isOn){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Main6Activity.this);
                    //define o titulo
                    builder.setTitle("Localização");
                    //define a mensagem
                    builder.setMessage("Você precisa ligar o gps, de acordo? ");
                    //define um botão como positivo
                    builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            startActivity(new Intent(Main6Activity.this,Main6Activity.class));
                            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(i);
                        }
                    });
                    //define um botão como negativo.
                    builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            startActivity(new Intent(Main6Activity.this,Main6Activity.class));
                        }
                    });
                    //cria o AlertDialog
                    alerta = builder.create();
                    //Exibe
                    alerta.show();

                }
                else{
                    latitudeText.setText("Latitude");
                    longitudeText.setText("Longitude");
                    settingButton.setEnabled(false);
                    apertou=true;

                }


            }
        });


    }

    @Override
    public void onConnected(Bundle bundle) {
        requestLocationUpdates();
    }

    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_FINE_LOCATION);
            } else {
                permissionIsGranted = true;
            }*/
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        myLatitude = location.getLatitude();
        myLongitude = location.getLongitude();
        latitudeText.setText("Latitude : " + String.valueOf(myLatitude));
        longitudeText.setText("Longitude : " + String.valueOf(myLongitude));
        settingButton.setEnabled(true);
        if (apertou==true){
            //passo pra outra tela

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (permissionIsGranted) {
            //if (googleApiClient.isConnected()) {
                requestLocationUpdates();
            //}
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //if (permissionIsGranted)
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //if (permissionIsGranted)
            googleApiClient.disconnect();
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                    permissionIsGranted = true;
                } else {
                    //permission denied
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    //define o titulo
                    builder.setTitle("Permisão Negada");
                    //define a mensagem
                    builder.setMessage("Sem a permissão de localização você não pode determinar a localização do seu telefone, " +
                            "você deseja realmente fazer isso?");
                    //define um botão como positivo
                    builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {

                            Toast.makeText(getApplicationContext(), "Este aplicativo precisa da permissão para usar a sua localização", Toast.LENGTH_SHORT).show();
                            latitudeText.setText("Latitude : permission not granted");
                            longitudeText.setText("Longitude : permission not granted");
                        }
                    });
                    //define um botão como negativo.
                    builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            //startActivity(new Intent(Main6Activity.this,Main6Activity.class));
                            Toast.makeText(getApplicationContext(), "Aperte Allow da proxima vez", Toast.LENGTH_SHORT).show();
                        }
                    });
                    //cria o AlertDialog
                    alerta = builder.create();
                    //Exibe
                    alerta.show();
                    permissionIsGranted = false;

                }
                break;
            case MY_PERMISSION_REQUEST_COARSE_LOCATION:
                // do something for coarse location
                break;
        }
    }*/
}