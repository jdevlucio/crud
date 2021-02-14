package com.escolafeliz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import com.escolafeliz.dao.MensagemDao;
import com.escolafeliz.entidade.Mensagem;

public class MensagemActivity extends AppCompatActivity {

    private List<Mensagem> mensagens;
    private ListView listview;
    private ArrayAdapter<Mensagem> adapter;
    private Mensagem mensagem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagem);

        listview = findViewById(R.id.listMensagens);

        registerForContextMenu(listview);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View v, int arg2,
                                    long arg3) {
                if(adapter != null) {
                    mensagem = new Mensagem();
                    mensagem = adapter.getItem(arg2);
                    String detalhes;
                    detalhes = "Nome: "+mensagem.getNome()+"\nTelefone: "+mensagem.getTelefone()+"\nHorário: "+
                    mensagem.getHorario()+"\nMensagem: "+mensagem.getConteudo();
                    Toast.makeText(MensagemActivity.this, String.format((detalhes)), Toast.LENGTH_LONG).show();
                }
            }
        });


        carregaListMensagens();
    }

    public void carregaListMensagens(){

        MensagemDao dao = new MensagemDao(this);
        mensagens = dao.listarMensagens();
        dao.close();

        adapter = new ArrayAdapter<Mensagem>(this, android.R.layout.simple_list_item_1, mensagens);
        listview.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_listagem,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.listagem_mensagem:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
