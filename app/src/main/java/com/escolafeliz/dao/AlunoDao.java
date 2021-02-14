package com.escolafeliz.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



import java.util.ArrayList;
import java.util.List;

import com.escolafeliz.entidade.Aluno;

public class AlunoDao extends SQLiteOpenHelper {

    public AlunoDao(Context context) {
        super(context, "dbAluno", null, 9);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = " create table alunos ("+
                " id integer primary key , "+
                " nome TEXT NOT NULL , "+
                " endereco TEXT ,"+
                " telefone TEXT ,"+
                " site TEXT,"+
                " caminhofoto TEXT,"+
                " email TEXT);";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        String sql = "drop table if exists alunos";
        db.execSQL(sql);
        onCreate(db);


    }

    public void salvar (Aluno aluno){

        ContentValues values = new ContentValues();
        values.put("nome",aluno.getNome());
        values.put("endereco",aluno.getEndereco());
        values.put("telefone",aluno.getTelefone());
        values.put("site",aluno.getSite());
        values.put("email",aluno.getEmail());
        values.put("caminhofoto",aluno.getCaminhofoto());
        SQLiteDatabase db = getWritableDatabase();

        if(aluno.getId() == null)
            db.insert("alunos", null, values);

        else
            db.update("alunos",values,"id=?", new String[]{aluno.getId().toString()});

        db.close();
    }

    public List<Aluno> listarAlunos() {

        SQLiteDatabase db = getReadableDatabase();

        String sql;
        Cursor cursor;

        sql = "select * from alunos order by nome ";

        cursor = db.rawQuery(sql, null);


        List<Aluno> alunos = new ArrayList<>();
        while (cursor.moveToNext()) {
            Aluno aluno = new Aluno();
            aluno.setId(cursor.getLong(cursor.getColumnIndex("id")));
            aluno.setEndereco(cursor.getString(cursor.getColumnIndex("endereco")));
            aluno.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            aluno.setSite(cursor.getString(cursor.getColumnIndex("site")));
            aluno.setTelefone(cursor.getString(cursor.getColumnIndex("telefone")));
            aluno.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            aluno.setCaminhofoto(cursor.getString(cursor.getColumnIndex("caminhofoto")));
            alunos.add(aluno);
        }

        db.close();
        return alunos;
    }

    public void excluir(Aluno aluno){

        SQLiteDatabase db = getWritableDatabase();
        db.delete("alunos", "id=?", new String[]{aluno.getId().toString()});
        db.close();

    }

}



