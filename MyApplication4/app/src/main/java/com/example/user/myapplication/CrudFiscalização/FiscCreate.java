package com.example.user.myapplication.CrudFiscalização;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by User on 26/07/2017.
 */

public class FiscCreate extends SQLiteOpenHelper {
    public static final String NOME_BD="Meu_Db";
    public static final int VERSAO_BD=1;
    private Context nContext;
    private SQLiteDatabase db;
    private String PATH_BD="/data/user/0/com.example.user.myapplication/database/Meu_Db";
    private String nomeTabela="Fiscalizacao";

    public FiscCreate(Context context) {
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

    public boolean createTable(){
        openBD();
        String createTable="CREATE TABLE IF NOT EXISTS "+nomeTabela+"(_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE," +
                "Censo_id integer NOT NULL, Municipio text not null,Endereco text not null,Bairro text not null," +
                "tipo_lampada text not null,potencia text not null, " +
                "acervo_poste text not null,medidor_nc text not null,medicao text not null,placa text, " +
                "latitude text not null, longitude text not null,intZona text not null, zona text not null," +
                "FOREIGN KEY(Censo_id) REFERENCES Censo(_id));";
        try{
            db.execSQL(createTable);
            System.out.println("fisc criado");
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
