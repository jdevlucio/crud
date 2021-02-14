package com.escolafeliz.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import com.escolafeliz.R;
import com.escolafeliz.entidade.Aluno;

public class AlunoAdapter extends BaseAdapter {

    private List<Aluno> alunos;
    private Context context;

    public AlunoAdapter(Context context, List<Aluno> alunos){
       this.alunos = alunos;
       this.context = context;
    }



    @Override
    public int getCount() {
        return alunos.size();
    }

    @Override
    public Object getItem(int position) {
        return alunos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return alunos.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Aluno aluno = alunos.get(position);
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.list_item, null);
        TextView txtNome = convertView.findViewById(R.id.textViewNome);
        TextView txtTelefone = convertView.findViewById(R.id.textViewTelefone);
        txtNome.setText(aluno.getNome());
        txtTelefone.setText(aluno.getTelefone());

        if(aluno.getCaminhofoto()!=null) {

            File arquivo = new File(aluno.getCaminhofoto());

            if (arquivo.exists()) {
                Bitmap bmp = BitmapFactory.decodeFile(arquivo.getAbsolutePath());
                ImageView imagem = convertView.findViewById(R.id.imageView);
                imagem.setImageBitmap(bmp);
            }
        }

        return convertView;
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<Aluno> alunos) {
        this.alunos = alunos;
    }
}
