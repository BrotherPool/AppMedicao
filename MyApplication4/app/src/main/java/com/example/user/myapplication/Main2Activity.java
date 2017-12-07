package com.example.user.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.user.myapplication.CrudFiscalização.FiscUpdate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class Main2Activity extends AppCompatActivity {
    private Spinner Lamp;
    private Spinner Acervo;
    private TextView UtmX;
    private TextView UtmY;
    private EditText Municipio;
    private EditText PotPlain;
    private EditText Endereco;
    private EditText Bairro;
    private EditText Medidor;
    private EditText Placa;



    private void Mensagem(String msg)
    {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
    private boolean isEmpty(EditText myeditText) {
        return myeditText.getText().toString().trim().length() == 0;
    }

    private boolean isEmptySpn(Spinner spn) {
        return spn.getSelectedItemId()==0;
    }

    private String ObterDiretorio()
    {
        File root = android.os.Environment.getExternalStorageDirectory();
        return root.toString();
    }

    /*public void Carregar()
    {
        File dir = Environment.getExternalStorageDirectory();  //creates the directory named MyDir in External Files Directory
        //File file = new File(dir,"teste2.txt");  //Creates a new file named MyFile4.txt in a folder "dir"
        String lstrNomeArq="teste2.txt";
        File arq;
        String lstrlinha;
        try
        {

            verifyStoragePermissions(Main2Activity.this);
            arq = new File(Environment.getExternalStorageDirectory(), lstrNomeArq);
            BufferedReader br = new BufferedReader(new FileReader(arq));
            while((lstrlinha=br.readLine())!=null){
                System.out.println(lstrlinha);

            }

            Mensagem("Texto Carregado com sucesso!");


        }
        catch (Exception e)
        {
            e.printStackTrace();
            Mensagem("Erro : " + e.getMessage());
        }
    }*/

    public void SaveInExternalPrivateStorage() {
        FileOutputStream fileOutputStream=null;
        FileOutputStream fileOutputStream2=null;
        File dir = Environment.getExternalStorageDirectory();
        File dir2 = Environment.getExternalStorageDirectory();
        File file = new File(dir,"teste2.txt");
        File file2=new File(dir2,"comparacao.txt");
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream2 = new FileOutputStream(file2);
            //fileOutputStream=openFileOutput("MyFile1.txt", Context.MODE_PRIVATE);  //to open the file for writing text in private mode
            OutputStreamWriter osw = new OutputStreamWriter(fileOutputStream);
            OutputStreamWriter osw2 = new OutputStreamWriter(fileOutputStream2);
            FiscRead k=new FiscRead(getApplicationContext());
            ArrayList<Fiscalizacao> fArray=k.getRegistros();
            CensoRead a=new CensoRead(getApplicationContext());
            Censo aux=a.getCenso(fArray.get(0).getCensoId());
            String Status="";
            String StatusTipo="";
            String StatusPotencia="";
            String StatusTipoPotencia="";
            String StatusMedicao="";
            String StatusNMedidor="";



            if(fArray.get(0).getCensoId()!=0){
                if(aux.getMedicao().equals("NAO")&&fArray.get(0).getMedicao().equals("SIM")){
                    Status="Divergência Estimado para Medido";
                }
                else if(aux.getMedicao().equals("SIM")&&fArray.get(0).getMedicao().equals("NAO")){
                    Status="Divergência Medido para Estimado";
                }
                else if(fArray.get(0).getMedicao().equals("NAO")&&(!aux.getTipo_lampada().equals(fArray.get(0).getTipo_lampada())
                        &&!aux.getPotencia().equals(fArray.get(0).getPotencia()))){
                    Status="Divergência de Tipo e Potência Estimada";
                }
                else if(fArray.get(0).getMedicao().equals("SIM")&&(!aux.getTipo_lampada().equals(fArray.get(0).getTipo_lampada())
                        &&!aux.getPotencia().equals(fArray.get(0).getPotencia()))){
                    Status="Divergência de Tipo e Potência Medida";
                }
                else if(fArray.get(0).getMedicao().equals("NAO")&&(!aux.getPotencia().equals(fArray.get(0).getPotencia()))){
                    Status="Divergência de Potência Estimada";

                }
                else if(fArray.get(0).getMedicao().equals("SIM")&&(!aux.getPotencia().equals(fArray.get(0).getPotencia()))){
                    Status="Divergência de Potência Medida";

                }
                else if(fArray.get(0).getMedicao().equals("NAO")&&(!aux.getTipo_lampada().equals(fArray.get(0).getTipo_lampada()))){
                    Status="Divergência de Tipo Estimada";
                }
                else if(fArray.get(0).getMedicao().equals("SIM")&&(!aux.getTipo_lampada().equals(fArray.get(0).getTipo_lampada()))){
                    Status="Divergência de Tipo Medida";
                }
                else{
                    Status="Ponto Normal";
                }

            }
            else if(fArray.get(0).getCensoId()==0){
                if(fArray.get(0).getMedicao().equals("NAO")){
                    Status="IP não cadastrada no Censo Estimado";
                }
                else if(fArray.get(0).getMedicao().equals("SIM")){
                    Status="IP não cadastrada no Censo Medido";
                }

            }

            if(fArray.get(0).getCensoId()!=0){
                //mediçao
                if(aux.getMedicao().equals("NAO")&&fArray.get(0).getMedicao().equals("SIM")){
                    StatusMedicao="Divergência Estimado para Medido";
                }
                if(aux.getMedicao().equals("SIM")&&fArray.get(0).getMedicao().equals("NAO")){
                    StatusMedicao="Divergência Medido para Estimado";
                }
                //potencia
                if(fArray.get(0).getMedicao().equals("NAO")&&(!aux.getPotencia().equals(fArray.get(0).getPotencia()))){
                    StatusTipoPotencia="Divergência de Potência Estimada";
                    StatusPotencia="Alterar potência de "+aux.getPotencia()+" para "+fArray.get(0).getPotencia();

                }
                if(fArray.get(0).getMedicao().equals("SIM")&&(!aux.getPotencia().equals(fArray.get(0).getPotencia()))){
                    StatusTipoPotencia="Divergência de Potência Medida";
                    StatusPotencia="Alterar potência de "+aux.getPotencia()+" para "+fArray.get(0).getPotencia();

                }
                //tipo
                if(fArray.get(0).getMedicao().equals("NAO")&&(!aux.getTipo_lampada().equals(fArray.get(0).getTipo_lampada()))){
                    StatusTipoPotencia="Divergência de Tipo Estimada";
                    StatusTipo="Alterar tipo de "+aux.getTipo_lampada()+" para "+fArray.get(0).getTipo_lampada();
                }
                if(fArray.get(0).getMedicao().equals("SIM")&&(!aux.getTipo_lampada().equals(fArray.get(0).getTipo_lampada()))){
                    StatusTipoPotencia="Divergência de Tipo Medida";
                    StatusTipo="Alterar tipo de "+aux.getTipo_lampada()+" para "+fArray.get(0).getTipo_lampada();
                }
                //tipo e potencia
                if(fArray.get(0).getMedicao().equals("NAO")&&(!aux.getTipo_lampada().equals(fArray.get(0).getTipo_lampada())
                        &&!aux.getPotencia().equals(fArray.get(0).getPotencia()))){
                    StatusTipoPotencia="Divergência de Tipo e Potência Estimada";
                }
                if(fArray.get(0).getMedicao().equals("SIM")&&(!aux.getTipo_lampada().equals(fArray.get(0).getTipo_lampada())
                        &&!aux.getPotencia().equals(fArray.get(0).getPotencia()))){
                    StatusTipoPotencia="Divergência de Tipo e Potência Medida";
                }
                //numero medidor
                if(!aux.getMedidor_nc().equals(fArray.get(0).getMedidor_nc())){
                    StatusNMedidor="Divergência NC na base do Censo, de "+aux.getMedidor_nc()+" para Fiscalizado "+fArray.get(0).getMedidor_nc();
                }

                if(aux.getTipo_lampada().equals(fArray.get(0).getTipo_lampada())){
                    StatusTipo="Tipo de Lâmpada Correta";
                }
                if(aux.getPotencia().equals(fArray.get(0).getPotencia())){
                    StatusPotencia="Potência de Lâmpada Correta";
                }
                if(aux.getTipo_lampada().equals(fArray.get(0).getTipo_lampada())
                        &&aux.getPotencia().equals(fArray.get(0).getPotencia())){
                    StatusTipoPotencia="Lâmpada Cadastrada Correta";
                }
                if(aux.getMedicao().equals(fArray.get(0).getMedicao())){
                    StatusMedicao="Cadastro de Medição Correto";
                }
                if(aux.getMedidor_nc().equals(fArray.get(0).getMedidor_nc())){
                    StatusNMedidor="Medidor Cadastrado Correto";
                }


            }
            osw.write(fArray.get(0).getString()+" Status Geral: "+Status+ "\r\n");
            osw2.write("ID: "+fArray.get(0).getId()+", ID Censo: "+fArray.get(0).getCensoId()
                    +", Status de Tipo: "+StatusTipo+
                    ", Status de Potência: "+StatusPotencia+
                    ", Status Tipo e Potência: "+StatusTipoPotencia+
                    ", Status Medição: "+StatusMedicao+
                    ", Status Nº Medidor: "+StatusNMedidor+
                    ", Status Geral: "+Status+ "\r\n");



            for (int i=1;i< fArray.size();i++){
                System.out.println("\n "+fArray.get(i).getString());
                a=new CensoRead(getApplicationContext());
                aux=a.getCenso(fArray.get(i).getCensoId());
                if(fArray.get(i).getCensoId()!=0){
                    if(aux.getMedicao().equals("NAO")&&fArray.get(i).getMedicao().equals("SIM")){
                        Status="Divergência Estimado para Medido";
                    }
                    else if(aux.getMedicao().equals("SIM")&&fArray.get(i).getMedicao().equals("NAO")){
                        Status="Divergência Medido para Estimado";
                    }
                    else if(fArray.get(i).getMedicao().equals("NAO")&&(!aux.getTipo_lampada().equals(fArray.get(i).getTipo_lampada())
                            &&!aux.getPotencia().equals(fArray.get(i).getPotencia()))){
                        Status="Divergência de Tipo e Potência Estimada";
                    }
                    else if(fArray.get(i).getMedicao().equals("SIM")&&(!aux.getTipo_lampada().equals(fArray.get(i).getTipo_lampada())
                            &&!aux.getPotencia().equals(fArray.get(i).getPotencia()))){
                        Status="Divergência de Tipo e Potência Medida";
                    }
                    else if(fArray.get(i).getMedicao().equals("NAO")&&(!aux.getPotencia().equals(fArray.get(i).getPotencia()))){
                        Status="Divergência de Potência Estimada";
                    }
                    else if(fArray.get(i).getMedicao().equals("SIM")&&(!aux.getPotencia().equals(fArray.get(i).getPotencia()))){
                        Status="Divergência de Potência Medida";

                    }
                    else if(fArray.get(i).getMedicao().equals("NAO")&&(!aux.getTipo_lampada().equals(fArray.get(i).getTipo_lampada()))){
                        Status="Divergência de Tipo Estimada";
                    }
                    else if(fArray.get(i).getMedicao().equals("SIM")&&(!aux.getTipo_lampada().equals(fArray.get(i).getTipo_lampada()))){
                        Status="Divergência de Tipo Medida";

                    }
                    else{
                        Status="Ponto Normal";
                    }

                }
                if(fArray.get(i).getCensoId()!=0){
                    //mediçao
                    if(aux.getMedicao().equals("NAO")&&fArray.get(i).getMedicao().equals("SIM")){
                        StatusMedicao="Divergência Estimado para Medido";
                    }
                    if(aux.getMedicao().equals("SIM")&&fArray.get(i).getMedicao().equals("NAO")){
                        StatusMedicao="Divergência Medido para Estimado";
                    }
                    //potencia
                    if(fArray.get(i).getMedicao().equals("NAO")&&(!aux.getPotencia().equals(fArray.get(i).getPotencia()))){
                        StatusTipoPotencia="Divergência de Potência Estimada";
                        StatusPotencia="Alterar potência de "+aux.getPotencia()+" para "+fArray.get(i).getPotencia();

                    }
                    if(fArray.get(i).getMedicao().equals("SIM")&&(!aux.getPotencia().equals(fArray.get(i).getPotencia()))){
                        StatusTipoPotencia="Divergência de Potência Medida";
                        StatusPotencia="Alterar potência de "+aux.getPotencia()+" para "+fArray.get(i).getPotencia();

                    }
                    //tipo
                    if(fArray.get(i).getMedicao().equals("NAO")&&(!aux.getTipo_lampada().equals(fArray.get(i).getTipo_lampada()))){
                        StatusTipoPotencia="Divergência de Tipo Estimada";
                        StatusTipo="Alterar tipo de "+aux.getTipo_lampada()+" para "+fArray.get(i).getTipo_lampada();
                    }
                    if(fArray.get(i).getMedicao().equals("SIM")&&(!aux.getTipo_lampada().equals(fArray.get(i).getTipo_lampada()))){
                        StatusTipoPotencia="Divergência de Tipo Medida";
                        StatusTipo="Alterar tipo de "+aux.getTipo_lampada()+" para "+fArray.get(i).getTipo_lampada();
                    }
                    //tipo e potencia
                    if(fArray.get(i).getMedicao().equals("NAO")&&(!aux.getTipo_lampada().equals(fArray.get(i).getTipo_lampada())
                            &&!aux.getPotencia().equals(fArray.get(i).getPotencia()))){
                        StatusTipoPotencia="Divergência de Tipo e Potência Estimada";
                    }
                    if(fArray.get(i).getMedicao().equals("SIM")&&(!aux.getTipo_lampada().equals(fArray.get(i).getTipo_lampada())
                            &&!aux.getPotencia().equals(fArray.get(i).getPotencia()))){
                        StatusTipoPotencia="Divergência de Tipo e Potência Medida";
                    }
                    //numero medidor
                    if(!aux.getMedidor_nc().equals(fArray.get(i).getMedidor_nc())){
                        StatusNMedidor="Divergência NC na base do Censo, de "+aux.getMedidor_nc()+" para Fiscalizado "+fArray.get(i).getMedidor_nc();
                    }

                    if(aux.getTipo_lampada().equals(fArray.get(i).getTipo_lampada())){
                        StatusTipo="Tipo de Lâmpada Correta";
                    }
                    if(aux.getPotencia().equals(fArray.get(i).getPotencia())){
                        StatusPotencia="Potência de Lâmpada Correta";
                    }
                    if(aux.getTipo_lampada().equals(fArray.get(i).getTipo_lampada())
                            &&aux.getPotencia().equals(fArray.get(i).getPotencia())){
                        StatusTipoPotencia="Lâmpada Cadastrada Correta";
                    }
                    if(aux.getMedicao().equals(fArray.get(i).getMedicao())){
                        StatusMedicao="Cadastro de Medição Correto";
                    }
                    if(aux.getMedidor_nc().equals(fArray.get(i).getMedidor_nc())){
                        StatusNMedidor="Medidor Cadastrado Correto";
                    }


                }
                if(fArray.get(i).getCensoId()==0){
                    if(fArray.get(i).getMedicao().equals("NAO")){
                        Status="IP não cadastrada no Censo Estimado";
                    }
                    else if(fArray.get(i).getMedicao().equals("SIM")){
                        Status="IP não cadastrada no Censo Medido";
                    }

                }
                osw.append(fArray.get(i).getString()+" Status Geral: "+Status+ "\r\n");
                osw2.append("ID: "+fArray.get(i).getId()+", ID Censo: "+fArray.get(i).getCensoId()
                        +", Status de Tipo: "+StatusTipo+
                        ", Status de Potência: "+StatusPotencia+
                        ", Status Tipo e Potência: "+StatusTipoPotencia+
                        ", Status Medição: "+StatusMedicao+
                        ", Status Nº Medidor: "+StatusNMedidor+
                        ", Status Geral: "+Status+ "\r\n");

            }
            osw.flush();
            osw.close();
            fileOutputStream.close();

            osw2.flush();
            osw2.close();
            fileOutputStream2.close();

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
            if(fileOutputStream2!=null)
            {
                try {
                    fileOutputStream2.close();    //closing the file
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //Calling user defined method
        //Carregar();

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

            if (intArray.isEmpty()){
                osw.write(""+ "\r\n");
                osw.flush();
                osw.close();
                fileOutputStream.close();

            }
            else {
                osw.write(intArray.get(0)+ "\r\n");
                for (int i=1;i< intArray.size();i++){
                    System.out.println("\n "+intArray.get(i));
                    osw.append(intArray.get(i)+ "\r\n");

                }
                osw.flush();
                osw.close();
                fileOutputStream.close();

            }





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


    //importat o censo para o bd do censo


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Lamp = (Spinner) findViewById(R.id.lamp);
        PotPlain=(EditText) findViewById(R.id.PotPlain);
        Acervo = (Spinner) findViewById(R.id.Acervo);
        UtmX=(TextView)findViewById(R.id.UtmX);
        UtmY=(TextView) findViewById(R.id.UtmY);
        Municipio=(EditText)findViewById(R.id.Municipio);
        Endereco=(EditText) findViewById(R.id.Endereco);
        Bairro=(EditText)findViewById(R.id.Bairro);
        Medidor=(EditText)findViewById(R.id.medidor);
        Placa=(EditText)findViewById(R.id.placa);
        CensoCreate c=new CensoCreate(getApplicationContext());
        c.createTable();
        FiscCreate a=new FiscCreate(getApplicationContext());
        a.createTable();





        StringBuilder sb = new StringBuilder();
        double numero1= variaveisGlobais.UtmX;
        DecimalFormat formato = new DecimalFormat(".#");
        String aux=formato.format(numero1);
        aux.replaceAll( "," , "." );
        sb.append(""+aux);
        String utmX = sb.toString();

        StringBuilder sb2 = new StringBuilder();
        double numero2= variaveisGlobais.UtmY;
        DecimalFormat formato2 = new DecimalFormat(".#");
        String aux2=formato2.format(numero2);
        aux2.replaceAll( "," , "." );
        sb2.append(""+aux2);
        String utmY = sb2.toString();

        UtmX.setText("UTM X: "+utmX);
        UtmY.setText("UTM Y: "+utmY);
        Lamp.setPrompt("Tipos de Lâmpada");
        Acervo.setPrompt("Acervo Fiscalização");
        configSpinner(Lamp,R.array.tipo_de_lampada);
        configSpinner(Acervo,R.array.Acervo);


    }

    public void configSpinner(Spinner spn,int x){

        String[] PotArray=getResources().getStringArray(x);
        final List<String> PotList = new ArrayList<>(Arrays.asList(PotArray));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item,PotList){
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
    public void salvarTxt(View view){
        variaveisGlobais.Municipio=Municipio.getText().toString();//notNull
        variaveisGlobais.Endereco=Endereco.getText().toString();//notNull
        variaveisGlobais.Bairro=Bairro.getText().toString();//notNull
        variaveisGlobais.Medidor=Medidor.getText().toString();//if Null seta pra SN
        variaveisGlobais.Placa=Placa.getText().toString();//pode ser Null
        //variaveisGlobais.Pot=(String)Pot.getSelectedItem();//notNull
        variaveisGlobais.Pot=PotPlain.getText().toString();
        variaveisGlobais.Lamp=(String)Lamp.getSelectedItem();//notNull
        variaveisGlobais.Acervo=(String)Acervo.getSelectedItem();//notNull
        CensoRead k=new CensoRead(getApplicationContext());
        ArrayList<Censo> cArray=k.getRegistros();
        for(int i=0;i<cArray.size();i++){
            System.out.println(cArray.get(i).getString());
        }



        FiscRead a=new FiscRead(getApplicationContext());
        ArrayList<Fiscalizacao> fArray=a.getRegistros();
        if(fArray!=null){
            for (int i=0;i< fArray.size();i++){
                System.out.println("\n "+fArray.get(i).getString());

            }
        }













        //Environment.getExternalStorageDirectory() memoria interna
        //Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) pasta especifica





        if(!(isEmpty(Municipio)||isEmpty(Endereco)||isEmpty(Bairro)||isEmpty(PotPlain)||isEmptySpn(Lamp)||isEmptySpn(Acervo))){
            //salvar

            String medidorNc=Medidor.getText().toString();
            if(medidorNc.equals("")){
                medidorNc="SN";
            }
            StringBuilder sb = new StringBuilder();
            sb.append(""+variaveisGlobais.UtmX);
            String utmX = sb.toString();

            StringBuilder sb2 = new StringBuilder();
            sb2.append(""+variaveisGlobais.UtmY);
            String utmY = sb2.toString();
            Fiscalizacao novo=new Fiscalizacao(variaveisGlobais.IdCenso,Municipio.getText().toString(),Endereco.getText().toString(),Bairro.getText().toString(),
                    Lamp.getSelectedItem().toString(),variaveisGlobais.Pot,Acervo.getSelectedItem().toString(),
                    medidorNc,Placa.getText().toString(),utmX,utmY,""+variaveisGlobais.intZona,""+variaveisGlobais.Zona);
            FiscUpdate u=new FiscUpdate(getApplicationContext());

            if(u.insertFisc(novo)){
                Toast.makeText(this, "Fiscalização foi inserida", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Erro,não inserido", Toast.LENGTH_SHORT).show();
            }
            SaveInExternalPrivateStorage();
            SaveIdsDisponiveis();
            //SaveInExternalPrivateStorage2();
            Intent it=new Intent(Main2Activity.this,Main4Activity.class);
            startActivity(it);


        }
        else{
            Mensagem("Existem campos ainda não preenchidos");
        }






    }
}
