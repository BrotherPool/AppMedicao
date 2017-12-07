package com.example.user.myapplication;

/**
 * Created by User on 26/07/2017.
 */

public class Censo {
    private String tipo_lampada;
    private String potencia;
    private String acervo_poste;
    private String medidor_nc;
    private String medicao;
    private String latitude;
    private String intZona;
    private String Zona;
    private String longitude;
    private int id;

    public Censo(){

    }

    public Censo(int id,String tipo_lampada, String potencia, String acervo_poste,String medidor_nc ,
                 String medicao ,String latitude, String longitude,String intZona, String Zona){
        this.id=id;
        this.tipo_lampada=tipo_lampada;
        this.potencia=potencia;
        this.acervo_poste=acervo_poste;
        this.latitude=latitude;
        this.longitude=longitude;
        this.medidor_nc=medidor_nc;
        this.medicao=medicao;
        this.intZona=intZona;
        this.Zona=Zona;
        /*if(medidor_nc.isEmpty()){
            this.medidor_nc="SN";
            this.medicao="NAO";
        }*/
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
    public int getId(){
        return this.id  ;
    }
    public String getIntZona(){
        return this.intZona  ;
    }
    public String getZona(){
        return this.Zona  ;
    }
    public String getString(){
        String s="Id: "+this.getId()+" Tipo: "+this.getTipo_lampada()+" Pot: "+this.getPotencia()+" Acervo: "+this.getAcervo_poste()+" UtmX: "+this.getLatitude()+" UtmY: "+this.getLongitude()+" IntZona: "+this.getIntZona()
                +" Zona: "+ this.getZona()+" Medidor: "+this.getMedidor_nc()+" Medicao: "+this.getMedicao();
        return s;
    }



}
