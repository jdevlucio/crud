package com.escolafeliz.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



import java.util.ArrayList;
import java.util.List;

import com.escolafeliz.entidade.Mensagem;

public class MensagemDao extends SQLiteOpenHelper {

    public MensagemDao(Context context) {
        super(context, "dbMensagem", null, 9);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = " create table mensagens ("+
                " id integer primary key , "+
                " nome TEXT NOT NULL , "+
                " telefone TEXT ,"+
                " conteudo TEXT ,"+
                " horario TEXT);";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        String sql = "drop table if exists mensagens";
        db.execSQL(sql);
        onCreate(db);


    }

    public void salvar (Mensagem mensagem){

        ContentValues values = new ContentValues();
        values.put("nome",mensagem.getNome());
        values.put("telefone",mensagem.getTelefone());
        values.put("conteudo",mensagem.getConteudo());
        values.put("horario",mensagem.getHorario());
        SQLiteDatabase db = getWritableDatabase();
        db.insert("mensagens", null, values);
        db.close();
    }

    public List<Mensagem> listarMensagens() {

        SQLiteDatabase db = getReadableDatabase();

        String sql;
        Cursor cursor;

        sql = "select * from mensagens order by horario desc ";

        cursor = db.rawQuery(sql, null);


        List<Mensagem> mensagens = new ArrayList<>();
        while (cursor.moveToNext()) {
            Mensagem mensagem = new Mensagem();
            mensagem.setId(cursor.getLong(cursor.getColumnIndex("id")));
            mensagem.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            mensagem.setTelefone(cursor.getString(cursor.getColumnIndex("telefone")));
            mensagem.setConteudo(cursor.getString(cursor.getColumnIndex("conteudo")));
            mensagem.setHorario(cursor.getString(cursor.getColumnIndex("horario")));
            mensagens.add(mensagem);
        }

        db.close();
        return mensagens;
    }

    public void excluir(Mensagem mensagem){

        SQLiteDatabase db = getWritableDatabase();
        db.delete("mensagens", "id=?", new String[]{mensagem.getId().toString()});
        db.close();

    }

}



