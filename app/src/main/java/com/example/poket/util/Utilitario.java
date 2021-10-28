package com.example.poket.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.poket.DTO.ContaDTO;
import com.example.poket.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Utilitario {

    public static void progresso(boolean e, ProgressBar load){
        load.setVisibility(e ? View.VISIBLE : View.GONE);
    }

    public static void toast(Context context, String mensagem){
        Toast.makeText(context, mensagem, Toast.LENGTH_LONG).show();
    }

    public static void semConta(){
        List<ContaDTO> listConta = new ArrayList<>();
        ContaDTO d = new ContaDTO();
        d.setId("0");
        d.setConta(".:Sem Conta:.");
        d.setValor("0");
        listConta.add(d);
    }

    public static void listaTipoRenda(Spinner spinner, String tipoPF, Context context) {
        List<String> listTipoGasto = Arrays.asList(
                "Salario", "Servicos", "Presente", "Aluguel", "Outros");

        ArrayAdapter<String> adapter_spinner = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1, listTipoGasto);
        spinner.setAdapter(adapter_spinner);

        if(!tipoPF.equals(""))
            spinner.setSelection(adapter_spinner.getPosition(tipoPF));
    }

    public static void listaTipoDespesa(Spinner spinner, String tipoPF, Context context) {
        List<String> listTipoGasto = Arrays.asList(
                "Alimentaçao", "Veiculo", "Moradia", "Lazer", "Outros");

        ArrayAdapter<String> adapter_spinner = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1, listTipoGasto);
        spinner.setAdapter(adapter_spinner);

        if(!tipoPF.equals(""))
            spinner.setSelection(adapter_spinner.getPosition(tipoPF));
    }

    public static void listaTipoPF(Spinner spinner, String tipoPF, Context context) {
        List<String> listTipoPF = Arrays.asList(
                "Curto Prazo", "Medio Prazo", "Longo Prazo");

        ArrayAdapter<String> adapter_spinner = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1, listTipoPF);
        spinner.setAdapter(adapter_spinner);

        if(!tipoPF.equals(""))
            spinner.setSelection(adapter_spinner.getPosition(tipoPF));
    }

    public static String dataAtual(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(new Date());
    }

    public static String convertBrToUsa(String dataConveter){
        String[] parts = dataConveter.split("/");
        return parts[2]+"-"+parts[1]+"-"+parts[0];
    }

    public static String convertUsaToBr(String dataConveter){
        String[] parts = dataConveter.split("-");
        return parts[2]+"/"+parts[1]+"/"+parts[0];
    }
}
