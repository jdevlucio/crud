package com.escolafeliz;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.escolafeliz.adapter.AlunoAdapter;
import com.escolafeliz.dao.AlunoDao;
import com.escolafeliz.entidade.Aluno;

public class MainActivity extends AppCompatActivity {


    private List<Aluno> alunos;
    private ListView listview;
    static final String STATE_FOTO = "user";
    private static Aluno aluno;
    private String caminhoFoto;
    static final int request_foto = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            caminhoFoto = savedInstanceState.getString(STATE_FOTO);
            aluno = (Aluno) savedInstanceState.getSerializable("aluno");
            initInstance();
        } else {
            initInstance();

        }

    }

    public void initInstance() {
        setContentView(R.layout.activity_main);
        listview = findViewById(R.id.listAlunos);
        registerForContextMenu(listview);
        carregaList();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_msgs, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_mensagem:

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                    if (!checkIfAlreadyhavePermission(0)) {
                        requestForSMSPermission();
                    } else {
                        abrirMensagens();
                    }
                } else {
                    abrirMensagens();
                }

                break;

            case R.id.menu_policy:
                Intent intentTelaPolicy = new Intent(this, activity_policy.class);
                startActivity(intentTelaPolicy);
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkIfAlreadyhavePermission(int tipo) {
        int result;

        if(tipo == 0)
         result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        else
         result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestForSMSPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.RECEIVE_SMS}, 1);
    }

    private void requestForCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 2);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {

            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    abrirMensagens();
                } else {
                    Toast.makeText(MainActivity.this, "É preciso fornecer permissão para SMS.", Toast.LENGTH_SHORT).show();
                }

            }

            case 2: {


                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    tirarFoto();
                } else {
                    Toast.makeText(MainActivity.this, "É preciso fornecer permissão para câmera.", Toast.LENGTH_SHORT).show();
                }

            }

        }
    }

    public void abrirMensagens() {
        Intent intentTelaMensagem = new Intent(this, MensagemActivity.class);
        startActivity(intentTelaMensagem);
    }

    public void abrirCadastro(View v) {

        Intent intentTelaCadastro = new Intent(this, CadastroActivity.class);
        startActivity(intentTelaCadastro);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {

        if (v.equals(listview)) {

            MenuItem menuFoto = menu.add("Foto");
            MenuItem menuAlterar = menu.add("Alterar");
            MenuItem menuExcluir = menu.add("Excluir");


            AdapterView.AdapterContextMenuInfo menuInfoListView = (AdapterView.AdapterContextMenuInfo) menuInfo;
            int pos = menuInfoListView.position;
            aluno = alunos.get(pos);

            menuExcluir.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    AlunoDao dao = new AlunoDao(MainActivity.this);
                    dao.excluir(aluno);
                    dao.close();
                    carregaList();
                    return false;
                }
            });

            menuAlterar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {


                    Intent intentCadastro = new Intent(MainActivity.this, CadastroActivity.class);
                    intentCadastro.putExtra("aluno", aluno);
                    startActivity(intentCadastro);
                    return false;
                }
            });

            menuFoto.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                        if (!checkIfAlreadyhavePermission(1)) {
                            requestForCameraPermission();
                        } else {
                            tirarFoto();
                        }
                    } else {
                        tirarFoto();
                    }


                    return false;
                }


            });
        }

    }

    public void tirarFoto() {

        Intent intentFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intentFoto.resolveActivity(getPackageManager()) != null) {

            File foto = null;
            try {
                foto = criarArquivo();
            } catch (IOException ex) {
                Toast.makeText(MainActivity.this, String.format(("Erro ao criar arquivo.")), Toast.LENGTH_SHORT).show();
            }

            if (foto != null) {
                Uri uri = FileProvider.getUriForFile(MainActivity.this,
                        "aluno.fcsl.MainActivity",
                        foto);

                intentFoto.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intentFoto, request_foto);

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        carregaList();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == request_foto && resultCode == RESULT_OK) {

            AlunoDao dao = new AlunoDao(this);
            aluno.setCaminhofoto(caminhoFoto);
            dao.salvar(aluno);
            dao.close();

        }
    }


    public void carregaList() {

        AlunoDao dao = new AlunoDao(this);
        alunos = dao.listarAlunos();
        dao.close();
        AlunoAdapter adapter = new AlunoAdapter(this, alunos);
        listview.setAdapter(adapter);

    }


    private File criarArquivo() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nomeArquivo = "JPEG_" + timeStamp + "_";
        File diretorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagem = File.createTempFile(nomeArquivo, ".jpg", diretorio);
        caminhoFoto = imagem.getAbsolutePath();
        return imagem;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable("aluno", aluno);
        savedInstanceState.putString(STATE_FOTO, caminhoFoto);
        super.onSaveInstanceState(savedInstanceState);
    }

}
