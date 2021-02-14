package com.escolafeliz.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.escolafeliz.dao.AlunoDao;
import com.escolafeliz.dao.MensagemDao;
import com.escolafeliz.entidade.Aluno;
import com.escolafeliz.entidade.Mensagem;

public class SMSReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {

        Object pdu[] = (Object[]) intent.getSerializableExtra("pdus");
        byte[]  pd1 = (byte[]) pdu[0];
        String formato = (String) intent.getSerializableExtra("format");

        SmsMessage sms = SmsMessage.createFromPdu(pd1,formato);

        String tel = sms.getDisplayOriginatingAddress();
        String msg = sms.getDisplayMessageBody();
        Long tempo = sms.getTimestampMillis();

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(tempo);

        String horario = formatter.format(calendar.getTime());

        criarHistorico(context,tel,msg,horario);

;    }

     public void criarHistorico(Context context, String tel, String msg, String horario){

         AlunoDao dao = new AlunoDao(context);

         List<Aluno> alunos = new ArrayList<>();

         alunos = dao.listarAlunos();

         MensagemDao msgDao = new MensagemDao(context);

         Mensagem novaMsg = new Mensagem();

         for(Aluno a: alunos){

             if(a.getTelefone().equals(tel)){
                 novaMsg.setNome(a.getNome());
                 novaMsg.setTelefone(tel);
                 novaMsg.setHorario(horario);
                 novaMsg.setConteudo(msg);
                 msgDao.salvar(novaMsg);
                 msgDao.close();
                 Toast.makeText(context, String.format(("Mensagem do contato "+a.getNome()+" salva.")),Toast.LENGTH_LONG).show();
                 break;
             }



         }

     }


}
