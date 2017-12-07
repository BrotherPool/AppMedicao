package com.example.user.myapplication.CrudCenso;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.user.myapplication.Censo;

import java.util.ArrayList;

/**
 * Created by User on 26/07/2017.
 */

public class CensoRead extends SQLiteOpenHelper {
    public static final String NOME_BD="Meu_Db";
    public static final int VERSAO_BD=1;
    private Context nContext;
    private SQLiteDatabase db;
    private String PATH_BD="/data/user/0/com.example.user.myapplication/database/Meu_Db";
    private String nomeTabela="Censo";

    public CensoRead(Context context) {
        super(context,NOME_BD,null,VERSAO_BD);
        this.nContext=context;
        db=getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase bd) {


    }

    @Override
    public void onUpgrade(SQLiteDatabase bd, int oldVersion, int newVersion) {


    }

    public ArrayList<Censo> getRegistros(){
        openBD();
        ArrayList<Censo> cArray=new ArrayList<>();
        String getCenso="SELECT * FROM "+nomeTabela;
        try {
            Cursor c=db.rawQuery(getCenso,null);
            if(c.moveToFirst()){

                do {


                    Censo aux=new Censo(c.getInt(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6),c.getString(7)
                            ,c.getString(8),c.getString(9));
                    cArray.add(aux);

                }while (c.moveToNext());
                c.close();

            }
        }catch (Exception e){
            e.printStackTrace();
            return null;


        }finally {
            db.close();
        }

        return cArray;
    }

    public Censo getCenso(int id){
        openBD();
        Censo j=new Censo();
        String getCenso="SELECT * FROM "+nomeTabela+" WHERE (_id= "+id+")";
        try {
            Cursor c=db.rawQuery(getCenso,null);
            if(c.moveToFirst()){

                do {


                    Censo aux=new Censo(c.getInt(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6),c.getString(7)
                    ,c.getString(8),c.getString(9));
                    j=aux;

                }while (c.moveToNext());
                c.close();

            }
        }catch (Exception e){
            e.printStackTrace();
            return null;


        }finally {
            db.close();
        }

        return j;
    }

    private void openBD(){
        if(!db.isOpen()){
            db=nContext.openOrCreateDatabase(PATH_BD,SQLiteDatabase.OPEN_READWRITE,null);
        }
    }

}
