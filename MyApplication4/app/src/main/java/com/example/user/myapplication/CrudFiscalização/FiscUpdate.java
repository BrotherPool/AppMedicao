package com.example.user.myapplication.CrudFiscalização;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.user.myapplication.Censo;
import com.example.user.myapplication.Fiscalizacao;

/**
 * Created by User on 26/07/2017.
 */

public class FiscUpdate extends SQLiteOpenHelper {
    public static final String NOME_BD="Meu_Db";
    public static final int VERSAO_BD=1;
    private Context nContext;
    private SQLiteDatabase db;
    private String PATH_BD="/data/user/0/com.example.user.myapplication/database/Meu_Db";
    private String nomeTabela="Fiscalizacao";

    public FiscUpdate(Context context) {
        super(context,NOME_BD,null,VERSAO_BD);
        this.nContext=context;
        db=getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase bd) {


    }

    @Override
    public void onUpgrade(SQLiteDatabase bd, int oldVersion, int newVersion) {


    }

    public boolean insertFisc(Fiscalizacao fiscalizacao){

        openBD();
        try {
            ContentValues cv=new ContentValues();
            //cv.put("_id",fiscalizacao.getId());
            cv.put("Censo_id",fiscalizacao.getCensoId());
            cv.put("Municipio",fiscalizacao.getMunicipio());
            cv.put("Endereco",fiscalizacao.getEndereco());
            cv.put("Bairro",fiscalizacao.getBairro());
            cv.put("tipo_lampada",fiscalizacao.getTipo_lampada());
            cv.put("potencia",fiscalizacao.getPotencia());
            cv.put("acervo_poste",fiscalizacao.getAcervo_poste());
            cv.put("medidor_nc",fiscalizacao.getMedidor_nc());
            cv.put("medicao",fiscalizacao.getMedicao());
            cv.put("placa",fiscalizacao.getPlacaDeIndentificacao());
            cv.put("latitude",fiscalizacao.getLatitude());
            cv.put("longitude",fiscalizacao.getLongitude());
            cv.put("intZona",fiscalizacao.getIntZona());
            cv.put("zona",fiscalizacao.getZona());
            db.insert(nomeTabela,null,cv);
            return true;


        }catch (Exception e){
            e.printStackTrace();
            return false;

        }finally {
            db.close();
        }

    }

    public boolean updateFisc(Fiscalizacao fiscalizacao){

        openBD();
        try {
            String where="_id= "+fiscalizacao.getId()+"";
            ContentValues cv=new ContentValues();
            //cv.put("_id",fiscalizacao.getId());
            cv.put("Censo_id",fiscalizacao.getCensoId());
            cv.put("Municipio",fiscalizacao.getMunicipio());
            cv.put("Endereco",fiscalizacao.getEndereco());
            cv.put("Bairro",fiscalizacao.getBairro());
            cv.put("tipo_lampada",fiscalizacao.getTipo_lampada());
            cv.put("potencia",fiscalizacao.getPotencia());
            cv.put("acervo_poste",fiscalizacao.getAcervo_poste());
            cv.put("medidor_nc",fiscalizacao.getMedidor_nc());
            cv.put("medicao",fiscalizacao.getMedicao());
            cv.put("placa",fiscalizacao.getPlacaDeIndentificacao());
            cv.put("latitude",fiscalizacao.getLatitude());
            cv.put("longitude",fiscalizacao.getLongitude());
            db.update(nomeTabela,cv,where,null);
            return true;


        }catch (Exception e){
            e.printStackTrace();
            return false;

        }finally {
            db.close();
        }

    }


    private void openBD(){
        if(!db.isOpen()){
            db=nContext.openOrCreateDatabase(PATH_BD,SQLiteDatabase.OPEN_READWRITE,null);
        }
    }
}
