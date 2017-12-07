package com.example.user.myapplication.CrudCenso;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.user.myapplication.Censo;

/**
 * Created by User on 26/07/2017.
 */

public class CensoUpdate extends SQLiteOpenHelper {
    public static final String NOME_BD="Meu_Db";
    public static final int VERSAO_BD=1;
    private Context nContext;
    private SQLiteDatabase db;
    private String PATH_BD="/data/user/0/com.example.user.myapplication/database/Meu_Db";
    private String nomeTabela="Censo";

    public CensoUpdate(Context context) {
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

    public boolean insertCenso(Censo censo){

        openBD();
        try {
            ContentValues cv=new ContentValues();
            cv.put("_id",censo.getId());
            cv.put("tipo_lampada",censo.getTipo_lampada());
            cv.put("potencia",censo.getPotencia());
            cv.put("acervo_poste",censo.getAcervo_poste());
            cv.put("medidor_nc",censo.getMedidor_nc());
            cv.put("medicao",censo.getMedicao());
            cv.put("latitude",censo.getLatitude());
            cv.put("longitude",censo.getLongitude());
            cv.put("intZona",censo.getIntZona());
            cv.put("zona",censo.getZona());
            db.insert(nomeTabela,null,cv);
            System.out.println("Censo inserido");
            return true;


        }catch (Exception e){
            e.printStackTrace();
            System.out.println("erro");
            return false;

        }finally {
            db.close();
        }

    }

    public boolean updateCenso(Censo censo){

        openBD();
        try {
            String where="_id= "+censo.getId()+"";
            ContentValues cv=new ContentValues();
            cv.put("_id",censo.getId());
            cv.put("tipo_lampada",censo.getTipo_lampada());
            cv.put("potencia",censo.getPotencia());
            cv.put("acervo_poste",censo.getAcervo_poste());
            cv.put("medidor_nc",censo.getMedidor_nc());
            cv.put("medicao",censo.getMedicao());
            cv.put("latitude",censo.getLatitude());
            cv.put("longitude",censo.getLongitude());
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
