package com.example.user.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.example.user.myapplication.CrudCenso.CensoCreate;
import com.example.user.myapplication.CrudCenso.CensoDelete;
import com.example.user.myapplication.CrudCenso.CensoRead;
import com.example.user.myapplication.CrudCenso.CensoUpdate;
import com.example.user.myapplication.CrudFiscalização.FiscCreate;
import com.example.user.myapplication.CrudFiscalização.FiscDelete;
import com.example.user.myapplication.CrudFiscalização.FiscRead;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import uk.me.jstott.jcoord.UTMRef;

public class MapsFragment extends SupportMapFragment implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, LocationListener {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private AlertDialog alerta;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

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
    public boolean LoadFromExternalPrivateStorage() {
        File dir = Environment.getExternalStorageDirectory();  //creates the directory named MyDir in External Files Directory
        //File file = new File(dir,"teste.txt");  //Creates a new file named MyFile4.txt in a folder "dir"
        String lstrNomeArq="teste.txt";
        File arq;
        String lstrlinha;
        try
        {

            verifyStoragePermissions(getActivity());
            arq = new File(Environment.getExternalStorageDirectory(), lstrNomeArq);
            BufferedReader br = new BufferedReader(new FileReader(arq));
            while((lstrlinha=br.readLine())!=null){
                String[] textoSeparado = lstrlinha.split(" ");
                Censo novo=new Censo(Integer.parseInt(textoSeparado[0]),textoSeparado[1], textoSeparado[2],
                        textoSeparado[3],textoSeparado[4] ,textoSeparado[5] ,textoSeparado[6], textoSeparado[7]
                        ,textoSeparado[8], textoSeparado[9]);
                CensoUpdate a=new CensoUpdate(getContext());
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
    private void Mensagem(String msg)
    {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
    public void SaveIdsDisponiveis() {
        FileOutputStream fileOutputStream=null;
        File dir = Environment.getExternalStorageDirectory();
        File file = new File(dir,"ids.txt");
        try {
            fileOutputStream = new FileOutputStream(file);
            //fileOutputStream=openFileOutput("MyFile1.txt", Context.MODE_PRIVATE);  //to open the file for writing text in private mode
            OutputStreamWriter osw = new OutputStreamWriter(fileOutputStream);
            CensoRead k=new CensoRead(getContext());
            ArrayList<Censo> cArray=k.getRegistros();

            ArrayList<Integer>intArray=new ArrayList<>();
            for(int a=0;a<cArray.size();a++){
                intArray.add(cArray.get(a).getId());
            }

            FiscRead b=new FiscRead(getContext());
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
    public MarkerOptions criaPonto(int censo,double utmx,double utmy,int zonaInt,char zona){

        MarkerOptions ponto=new MarkerOptions();

        UTMRef novo3=new UTMRef(utmx,utmy,zona,zonaInt);
        uk.me.jstott.jcoord.LatLng aux=novo3.toLatLng();
        LatLng latLng = new LatLng(aux.getLat(), aux.getLng());
        ponto.position(latLng);
        ponto.title("Censo de número: "+censo);
        return ponto;
    }

    public boolean LoadIds() {
        File dir = Environment.getExternalStorageDirectory();  //creates the directory named MyDir in External Files Directory
        //File file = new File(dir,"teste.txt");  //Creates a new file named MyFile4.txt in a folder "dir"
        String lstrNomeArq="ids.txt";
        File arq;
        String lstrlinha;
        try
        {
            SaveIdsDisponiveis();
            verifyStoragePermissions(getActivity());
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
            //e.printStackTrace();
            //Mensagem("Erro : " + e.getMessage());
            return false;
        }   //Calling user defined method
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMapAsync(this);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        locationManager=(LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria=new Criteria();
        String Provider =locationManager.getBestProvider(criteria,true);


        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(this);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-3.8175, -38.4641);
        MarkerOptions marker=new MarkerOptions();
        marker.position(sydney);
        marker.title("Marcador em Sydney");*/

        Double lat;
        Double longi;
        int zona;
        char as;
        MarkerOptions novo;

        CensoRead censoRead= new CensoRead(Maps.getContext());
        ArrayList<Censo> cArray=censoRead.getRegistros();
        for(int i=0;i<cArray.size();i++){
            lat=Double.parseDouble(cArray.get(i).getLatitude());
            longi=Double.parseDouble(cArray.get(i).getLongitude());
            zona=Integer.parseInt(cArray.get(i).getIntZona());
            as=cArray.get(i).getZona().charAt(0);
            novo=criaPonto(cArray.get(i).getId(),lat,longi,zona,as);
            mMap.addMarker(novo);
            System.out.println("vez: "+i);
        }


        //mMap.addMarker(marker);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }

    @Override
    public void onMapClick(LatLng latLng) {
        Toast.makeText(getContext(),"Coordenadas: "+latLng.toString(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(getContext(),"apertou: ",Toast.LENGTH_SHORT).show();
        marker.showInfoWindow();
        //Intent it=new Intent(getActivity(),Main4Activity.class);
        //startActivity(it);
        /*CensoDelete teste=new CensoDelete(Maps.getContext());
        teste.deleteTable();
        CensoCreate censoCreate=new CensoCreate(Maps.getContext());
        censoCreate.createTable();
        CensoUpdate censoUpdate=new CensoUpdate(Maps.getContext());

        Censo aux=new Censo(12,"ME","350","B4B","SN","NAO","559499.09","9578005.57","24","M");
        censoUpdate.insertCenso(aux);
        CensoRead a=new CensoRead(Maps.getContext());
        ArrayList<Censo> cArray=a.getRegistros();
        int i;
        for(i=0;i<cArray.size();i++){
            System.out.println("\n"+cArray.get(0).getString());
        }*/
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude=location.getLatitude();
        double longitude=location.getLongitude();
        LatLng sydney = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
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
