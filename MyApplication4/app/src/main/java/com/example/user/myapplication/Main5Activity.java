package com.example.user.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.user.myapplication.CrudCenso.CensoRead;
import com.example.user.myapplication.CrudFiscalização.FiscRead;

import java.util.ArrayList;

public class Main5Activity extends AppCompatActivity {
    private TextView tipoEstimadoTxt;
    private TextView tipoMedidoTxt;
    private TextView potenciaEstimadaTxt;
    private TextView potenciaMedidaTxt;
    private TextView ipEstimadoTxt;
    private TextView ipMedidoTxt;
    private TextView potTipoEstimadoTxt;
    private TextView potTipoMedidoTxt;
    private TextView estimadoMedidoTxt;
    private TextView medidoEstimadoTxt;
    private TextView pontosCertosTxt;
    private TextView pontosErradosTxt;


    public void alteraTipoEstimado(){
        int ipMedido=0;
        int ipEstimado=0;
        int tipoEstimado=0;
        int tipoMedido=0;
        int potenciaEstimada=0;
        int potenciaMedida=0;
        int potTipoEstimado=0;
        int potTipoMedido=0;
        int estimadoMedido=0;
        int medidoEstimado=0;
        int certo=0;
        int errado=0;
        boolean verificacao=true;
        CensoRead b=new CensoRead(getApplicationContext());

        FiscRead a=new FiscRead(getApplicationContext());
        ArrayList<Fiscalizacao> fArray=a.getRegistros();
        CensoRead c=new CensoRead(getApplicationContext());
        for(int i=0;i<fArray.size();i++){
            c=new CensoRead(getApplicationContext());
            Censo aux=c.getCenso(fArray.get(i).getCensoId());
            if(fArray.get(i).getCensoId()!=0){
                if(aux.getMedicao().equals("NAO")&&fArray.get(i).getMedicao().equals("SIM")){
                    estimadoMedido++;
                    verificacao=false;
                }
                else if(aux.getMedicao().equals("SIM")&&fArray.get(i).getMedicao().equals("NAO")){
                    medidoEstimado++;
                    verificacao=false;
                }
                else if(!(aux.getTipo_lampada().equals(fArray.get(i).getTipo_lampada()))&&(!aux.getPotencia().equals(fArray.get(i).getPotencia()))&&(fArray.get(i).getMedicao().equals("SIM"))){
                    potTipoMedido++;
                    //potenciaMedida--;
                    //tipoMedido--;
                    verificacao=false;
                }
                else if(!(aux.getTipo_lampada().equals(fArray.get(i).getTipo_lampada()))&&(!aux.getPotencia().equals(fArray.get(i).getPotencia()))&&(fArray.get(i).getMedicao().equals("NAO"))){
                    potTipoEstimado++;
                    //tipoEstimado--;
                    //potenciaEstimada--;
                    verificacao=false;
                }
                else if(!(aux.getTipo_lampada().equals(fArray.get(i).getTipo_lampada()))&&(fArray.get(i).getMedicao().equals("NAO"))){
                    tipoEstimado++;
                    verificacao=false;
                }
                else if(!(aux.getTipo_lampada().equals(fArray.get(i).getTipo_lampada()))&&(fArray.get(i).getMedicao().equals("SIM"))){
                    tipoMedido++;
                    verificacao=false;
                }
                else if((!aux.getPotencia().equals(fArray.get(i).getPotencia()))&&(fArray.get(i).getMedicao().equals("NAO"))){
                    potenciaEstimada++;
                    verificacao=false;
                }
                else if((!aux.getPotencia().equals(fArray.get(i).getPotencia()))&&(fArray.get(i).getMedicao().equals("SIM"))){
                    potenciaMedida++;
                    verificacao=false;
                }

                if(verificacao==true){
                    certo++;
                }
                if(verificacao==false){
                    errado++;
                    verificacao=true;
                }

            }
            else {
                if(fArray.get(i).getMedicao().equals("SIM")){
                    ipMedido++;
                }
                if(fArray.get(i).getMedicao().equals("NAO")){
                    ipEstimado++;
                }

            }




        }
        String s=""+tipoEstimado;
        tipoEstimadoTxt.setText(s);
        s=""+tipoMedido;
        tipoMedidoTxt.setText(s);
        s=""+potenciaEstimada;
        potenciaEstimadaTxt.setText(s);
        s=""+potenciaMedida;
        potenciaMedidaTxt.setText(s);
        s=""+ipEstimado;
        ipEstimadoTxt.setText(s);
        s=""+ipMedido;
        ipMedidoTxt.setText(s);
        s=""+potTipoMedido;
        potTipoMedidoTxt.setText(s);
        s=""+potTipoEstimado;
        potTipoEstimadoTxt.setText(s);
        s=""+estimadoMedido;
        estimadoMedidoTxt.setText(s);
        s=""+medidoEstimado;
        medidoEstimadoTxt.setText(s);
        s=""+certo;
        pontosCertosTxt.setText(s);
        s=""+errado;
        pontosErradosTxt.setText(s);

    }

    public float perdaReator(String lampada,int potencia){
        if((lampada.equals("LD")||(lampada.equals("LD")&&(potencia==5||potencia==9||potencia==17||potencia==35||potencia==50||potencia==53||potencia==70||potencia==120||potencia==128||potencia==150||potencia==125||potencia==290)))||
                (lampada.equals("AL")&&potencia==35)||
                (lampada.equals("HL")&&(potencia==50||potencia==150||potencia==300||potencia==400||potencia==500||potencia==2000))||
                (lampada.equals("IN")&&(potencia==15||potencia==25||potencia==35||potencia==40||potencia==50||potencia==60||potencia==70||potencia==100||potencia==150||potencia==200||potencia==240||potencia==250||potencia==300||potencia==500||potencia==1000||potencia==1500||potencia==2000))||
                (lampada.equals("MX")&&(potencia==160||potencia==250||potencia==300||potencia==400||potencia==500))){
            return 0;
        }
        else {
            return -1;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        tipoEstimadoTxt=(TextView)findViewById(R.id.divergenciaTipoEstimada);
        potenciaEstimadaTxt=(TextView)findViewById(R.id.divergenciaPotenciaEstimada);
        potenciaMedidaTxt=(TextView)findViewById(R.id.divergenciaPotenciaMedida);
        tipoMedidoTxt=(TextView)findViewById(R.id.divergenciaTipoMedida);
        ipEstimadoTxt=(TextView)findViewById(R.id.pontosIpNCadastradaCensoEstimado);
        ipMedidoTxt=(TextView)findViewById(R.id.pontosIpNCadastradaCensoMedido);
        potTipoEstimadoTxt=(TextView)findViewById(R.id.divergenciaTipoPotenciaEstimada);
        potTipoMedidoTxt=(TextView)findViewById(R.id.divergenciaTipoPotenciaMedida);
        estimadoMedidoTxt=(TextView)findViewById(R.id.divergenciaEstimadoMedido);
        medidoEstimadoTxt=(TextView)findViewById(R.id.divergenciaMedidoEstimado);
        pontosCertosTxt=(TextView)findViewById(R.id.pontosCertos);
        pontosErradosTxt=(TextView)findViewById(R.id.pontosErrados);
        alteraTipoEstimado();
        System.out.println(perdaReator("MX",160));
    }
    public void voltar(View v){
        Intent it= new Intent(Main5Activity.this,Main4Activity.class);
        startActivity(it);
    }
}
