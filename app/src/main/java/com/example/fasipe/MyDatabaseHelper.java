package com.example.fasipe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "fasipe_cuiaba.db";
    private static final int DATABASE_VERSION = 4;

    private static final String TABLE_NAME = "ocorrencia";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NOME = "nome";
    private static final String COLUMN_CPF = "cpf";
    private static final String COLUMN_TIPO_OCORRENCIA = "tipo_ocorrencia";
    private static final String COLUMN_TIPO_OCORRENCIA_DESCRICAO = "tipo_ocorrencia_descricao";
    private static final String COLUMN_ESTAGIARIO = "estagiario";
    private static final String COLUMN_COORDENADOR = "coordenador";

    MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOME + " TEXT, " +
                COLUMN_CPF + " TEXT, " +
                COLUMN_TIPO_OCORRENCIA + " TEXT, " +
                COLUMN_TIPO_OCORRENCIA_DESCRICAO + " TEXT, " +
                COLUMN_ESTAGIARIO + " TEXT, " +
                COLUMN_COORDENADOR + " TEXT);";
        db.execSQL(query);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void addRecord(String nome, String cpf, String tipoOcorrencia, String tipoOcorrenciaDescricao, String estagiario, String coordenador) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NOME, nome);
        cv.put(COLUMN_CPF, cpf);
        cv.put(COLUMN_TIPO_OCORRENCIA, tipoOcorrencia);
        cv.put(COLUMN_TIPO_OCORRENCIA_DESCRICAO, tipoOcorrenciaDescricao);
        cv.put(COLUMN_ESTAGIARIO, estagiario);
        cv.put(COLUMN_COORDENADOR, coordenador);

        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Falha ao adicionar ocorrencia", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Ocorrencia adicionada com sucesso!", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void updateRecord(String row_id, String nome, String cpf, String tipoOcorrencia, String tipoOcorrenciaDescricao, String estagiario, String coordenador) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NOME, nome);
        cv.put(COLUMN_CPF, cpf);
        cv.put(COLUMN_TIPO_OCORRENCIA, tipoOcorrencia);
        cv.put(COLUMN_TIPO_OCORRENCIA_DESCRICAO, tipoOcorrenciaDescricao);
        cv.put(COLUMN_ESTAGIARIO, estagiario);
        cv.put(COLUMN_COORDENADOR, coordenador);

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Falha ao atualizar ocorrencia", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Ocorrencia atualizada com sucesso!", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteOneRow(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Falha ao deletar ocorrencia", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Ocorrencia deletada com sucesso!", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }
}
