package com.example.user.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.myapplication.CrudCenso.CensoRead;
import com.example.user.myapplication.CrudFiscalização.FiscDelete;
import com.example.user.myapplication.CrudFiscalização.FiscRead;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static android.graphics.Color.GRAY;

public class Main3Activity extends AppCompatActivity {
    private LinearLayout vertical;
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    private AlertDialog alerta;

    /**
     * Generate a value suitable for use in .
     * This value will not collide with ID values generated at build time by aapt for R.id.
     *
     * @return a generated ID value
     */
    public static int generateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    private void Mensagem(String msg)
    {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private RelativeLayout teste(final Fiscalizacao dados){
        final RelativeLayout conteudo=new RelativeLayout(Main3Activity.this);
        RelativeLayout.LayoutParams conteudoParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        Random rand = new Random();
        int r = rand.nextInt();
        int g = rand.nextInt();
        int b = rand.nextInt();
        conteudo.setBackgroundColor(new Color().rgb(r, g, b));
        conteudo.setLayoutParams(conteudoParams);

        Button Erase=new Button(Main3Activity.this);
        RelativeLayout.LayoutParams EraseParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        EraseParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        EraseParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        EraseParams.addRule(RelativeLayout.CENTER_VERTICAL);
        Erase.setText("Apagar");
        Erase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Main3Activity.this);
                //define o titulo
                builder.setTitle("Titulo");
                //define a mensagem
                builder.setMessage("Deseja apagar a fiscalização? ");
                //define um botão como positivo
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        FiscDelete a=new FiscDelete(getApplicationContext());
                        a.deleteFisc(dados);
                        Mensagem("Apagado");
                        vertical.removeView(conteudo);
                        SaveIdsDisponiveis();

                    }
                });
                //define um botão como negativo.
                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Mensagem("Não Apagado");

                    }
                });
                //cria o AlertDialog
                alerta = builder.create();
                //Exibe
                alerta.show();


                //
            }
        });
        Erase.setLayoutParams(EraseParams);
        conteudo.addView(Erase);
        /*novo.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.HORIZONTAL));*/




        TextView Fisc=new TextView(Main3Activity.this);
        //setar id
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Fisc.setId(View.generateViewId());
            Erase.setId(View.generateViewId());
        }else {
            Fisc.setId(generateViewId());
            Erase.setId(generateViewId());
        }
        RelativeLayout.LayoutParams FiscParams= new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        FiscParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        FiscParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        FiscParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        FiscParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        FiscParams.addRule(RelativeLayout.LEFT_OF, Erase.getId());
        FiscParams.addRule(RelativeLayout.START_OF,Erase.getId());
        Fisc.setText(dados.getStringLista());
        Fisc.setTextSize(20);

        Fisc.setLayoutParams(FiscParams);


        conteudo.addView(Fisc);
        return conteudo;

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
        Toast.makeText(this,"Data saved to "+file.getAbsolutePath(),Toast.LENGTH_LONG).show();
        //Calling user defined method
        //Carregar();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        //começa
        vertical=(LinearLayout)findViewById(R.id.vertical);
        //novo.addView(novo2);
        FiscRead b=new FiscRead(getApplicationContext());
        ArrayList<Fiscalizacao> fArray=b.getRegistros();

        for(int a=0;a<fArray.size();a++){
            vertical.addView(teste(fArray.get(a)));
        }
    }
    public void voltar(View v){
        Intent it=new Intent(Main3Activity.this,Main4Activity.class);
        startActivity(it);

    }
}
