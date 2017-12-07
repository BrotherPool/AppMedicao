package com.example.user.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

public class Maps extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FragmentManager fragmentManager;
    private static Context appContext;
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

            verifyStoragePermissions(this);
            arq = new File(Environment.getExternalStorageDirectory(), lstrNomeArq);
            BufferedReader br = new BufferedReader(new FileReader(arq));
            while((lstrlinha=br.readLine())!=null){
                String[] textoSeparado = lstrlinha.split(" ");
                Censo novo=new Censo(Integer.parseInt(textoSeparado[0]),textoSeparado[1], textoSeparado[2],
                        textoSeparado[3],textoSeparado[4] ,textoSeparado[5] ,textoSeparado[6], textoSeparado[7]
                        ,textoSeparado[8], textoSeparado[9]);
                CensoUpdate a=new CensoUpdate(Maps.getContext());
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
        Toast.makeText(Maps.getContext(), msg, Toast.LENGTH_SHORT).show();
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
            verifyStoragePermissions(this);
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
    public void SaveIdsDisponiveis() {
        FileOutputStream fileOutputStream=null;
        File dir = Environment.getExternalStorageDirectory();
        File file = new File(dir,"ids.txt");
        try {
            fileOutputStream = new FileOutputStream(file);
            //fileOutputStream=openFileOutput("MyFile1.txt", Context.MODE_PRIVATE);  //to open the file for writing text in private mode
            OutputStreamWriter osw = new OutputStreamWriter(fileOutputStream);
            CensoRead k=new CensoRead(Maps.getContext());
            ArrayList<Censo> cArray=k.getRegistros();

            ArrayList<Integer>intArray=new ArrayList<>();
            for(int a=0;a<cArray.size();a++){
                intArray.add(cArray.get(a).getId());
            }

            FiscRead b=new FiscRead(Maps.getContext());
            ArrayList<Fiscalizacao> fArray=b.getRegistros();
            if(fArray!=null){
                for(int a=0;a<fArray.size();a++){
                    if(intArray.contains(fArray.get(a).getCensoId())){
                        intArray.remove((Object)fArray.get(a).getCensoId());
                    }
                }
            }



            osw.write(intArray.get(0)+ "\r\n");
            System.out.println("\n "+intArray.get(0));
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
    public static Context getContext() {
        return appContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        appContext = getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.add(R.id.container,new MapsFragment(),"MapsFragment");
        transaction.commitAllowingStateLoss();
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
        getMenuInflater().inflate(R.menu.maps, menu);
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

        if (id == R.id.nav_exemplobasico) {

        }
        if (id == R.id.attCenso) {
            AttCenso();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                CensoDelete b=new CensoDelete(Maps.getContext());
                b.deleteTable();
                CensoCreate c=new CensoCreate(Maps.getContext());
                c.createTable();
                FiscDelete d=new FiscDelete(Maps.getContext());
                d.deleteTable();
                FiscCreate a=new FiscCreate(Maps.getContext());
                a.createTable();
                LoadFromExternalPrivateStorage();
                CensoRead k=new CensoRead(Maps.getContext());
                ArrayList<Censo> cArray=k.getRegistros();
                for(int i=0;i<cArray.size();i++){
                    System.out.println(cArray.get(i).getString());
                }
                SaveIdsDisponiveis();
                //LoadIds();

                Intent it=new Intent(Maps.this,Maps.class);
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
