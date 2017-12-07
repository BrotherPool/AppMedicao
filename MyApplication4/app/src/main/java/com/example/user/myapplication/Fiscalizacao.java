package com.example.user.myapplication;

import com.example.user.myapplication.CrudCenso.CensoRead;

import java.util.Locale;

/**
 * Created by User on 26/07/2017.
 */

public class Fiscalizacao {
    private int id;
    private int CensoId;
    private String Municipio;
    private String Endereco;
    private String Bairro;
    private String tipo_lampada;
    private String potencia;
    private String acervo_poste;
    private String medidor_nc;
    private String medicao;
    private String PlacaDeIndentificacao;
    private String latitude;
    private String longitude;
    private String intZona;
    private String Zona;


    public Fiscalizacao(){

    }

    public Fiscalizacao(int id,int CensoId,String Municipio,String Endereco,String Bairro,String tipo_lampada, String potencia,
                        String acervo_poste, String medidor_nc ,
                        String PlacaDeIndentificacao, String latitude, String longitude,String intZona, String Zona){
        this.id=id;
        this.CensoId=CensoId;
        this.Municipio=Municipio;
        this.Endereco=Endereco;
        this.Bairro=Bairro;
        this.PlacaDeIndentificacao=PlacaDeIndentificacao;
        this.tipo_lampada=tipo_lampada;
        this.potencia=potencia;
        this.acervo_poste=acervo_poste;
        this.latitude=latitude;
        this.longitude=longitude;
        this.medidor_nc=medidor_nc;
        this.medicao="SIM";
        this.intZona=intZona;
        this.Zona=Zona;
        if(medidor_nc.equals("SN")){
            this.medidor_nc="SN";
            this.medicao="NAO";
        }

    }
    public Fiscalizacao(int CensoId,String Municipio,String Endereco,String Bairro,String tipo_lampada, String potencia,
                         String acervo_poste, String medidor_nc ,
                         String PlacaDeIndentificacao, String latitude, String longitude,String intZona, String Zona){
        this.CensoId=CensoId;
        this.Municipio=Municipio;
        this.Endereco=Endereco;
        this.Bairro=Bairro;
        this.PlacaDeIndentificacao=PlacaDeIndentificacao;
        this.tipo_lampada=tipo_lampada;
        this.potencia=potencia;
        this.acervo_poste=acervo_poste;
        this.latitude=latitude;
        this.longitude=longitude;
        this.medidor_nc=medidor_nc;
        this.intZona=intZona;
        this.Zona=Zona;
        this.medicao="SIM";
        if(medidor_nc.equals("SN")){
            this.medidor_nc="SN";
            this.medicao="NAO";
        }
    }
    public int getId(){
        return this.id;
    }
    public int getCensoId(){ return this.CensoId; }
    public String getMunicipio(){
        return this.Municipio;
    }
    public String getEndereco(){
        return this.Endereco;
    }
    public String getBairro(){
        return this.Bairro;
    }
    public String getPlacaDeIndentificacao(){
        return this.PlacaDeIndentificacao;
    }
    public String getTipo_lampada(){
        return this.tipo_lampada;
    }
    public String getAcervo_poste(){
        return this.acervo_poste;
    }
    public String getMedidor_nc(){
        return this.medidor_nc;
    }
    public String getMedicao(){
        return this.medicao;
    }
    public String getLatitude(){
        return this.latitude;
    }
    public String getLongitude(){
        return this.longitude;
    }
    public String getPotencia(){
        return this.potencia;
    }

    public String getIntZona() {
        return this.intZona;
    }

    public String getZona() {
        return this.Zona;
    }

    public String getString(){
        /*Double valor = Double.valueOf(this.getLatitude());
        valor = Double.valueOf(String.format(Locale.US, "%.0f", valor));
        System.out.println(valor);*/
        String s="ID: "+this.getId()+" CensoID: "+this.getCensoId()+
                " Municipio: "+this.getMunicipio()+" Endereco: "+this.getEndereco()+" Bairro: "+this.getBairro()+
                " Tipo: "+this.getTipo_lampada()+" Pot: "+this.getPotencia()+" Acervo: "+this.getAcervo_poste()+
                " Medidor: "+this.getMedidor_nc()+" Medicao: "+this.getMedicao()+
                " Placa de Indentificação: "+this.getPlacaDeIndentificacao()+
                " UtmX: "+this.getLatitude()+" UtmY: "+this.getLongitude()+" IntZona: "+this.getIntZona()
                +" Zona: "+ this.getZona();
        return s;
    }
    public String getStringLista(){
        /*Double valor = Double.valueOf(this.getLatitude());
        valor = Double.valueOf(String.format(Locale.US, "%.0f", valor));
        System.out.println(valor);*/
        String s="ID: "+this.getId()+" CensoID: "+this.getCensoId();
        if(this.getCensoId()==0){
            s="ID: "+this.getId()+" CensoID: Novo Ponto!";
        }

        return s;
    }



}
