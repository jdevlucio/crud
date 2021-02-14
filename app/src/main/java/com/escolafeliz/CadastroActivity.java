package com.escolafeliz;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.escolafeliz.dao.AlunoDao;
import com.escolafeliz.entidade.Aluno;

public class CadastroActivity extends AppCompatActivity {

    private Aluno aluno;

    private TextInputEditText email;
    private TextInputEditText endereco;
    private TextInputEditText nome;
    private TextInputEditText site;
    private TextInputEditText telefone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        email = (TextInputEditText) findViewById(R.id.textEmail);
        endereco = (TextInputEditText) findViewById(R.id.textEndereco);
        nome = (TextInputEditText) findViewById(R.id.textNome);
        site = (TextInputEditText) findViewById(R.id.textSite);
        telefone = (TextInputEditText) findViewById(R.id.textTelefone);


        aluno = (Aluno) getIntent().getSerializableExtra("aluno");

        if(aluno == null)
            aluno = new Aluno();

        email.setText(aluno.getEmail());
        endereco.setText(aluno.getEndereco());
        nome.setText(aluno.getNome());
        site.setText(aluno.getSite());
        telefone.setText(aluno.getTelefone());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.menu_aluno:
                salvar();
                break;

            case R.id.menu_aluno_voltar:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void salvar(){

        AlunoDao dao = new AlunoDao(this);

        aluno.setEmail(email.getText().toString());
        aluno.setEndereco(endereco.getText().toString());
        aluno.setNome(nome.getText().toString());
        aluno.setSite(site.getText().toString());
        aluno.setTelefone(telefone.getText().toString());
        dao.salvar(aluno);
        dao.close();
        Toast.makeText(this, String.format(("Aluno salvo com sucesso")),Toast.LENGTH_SHORT).show();
        finish();

    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }
}
