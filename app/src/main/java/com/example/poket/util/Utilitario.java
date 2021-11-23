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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Utilitario {

    public static void toast(Context context, String mensagem){
        Toast.makeText(context, mensagem, Toast.LENGTH_LONG).show();
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

    public static boolean comparaDatas(String dataRecebida, String dataHoje) {
        SimpleDateFormat dateFormat = new SimpleDateFormat ("dd/MM/yyyy");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = dateFormat.parse(dataRecebida);
            date2 = dateFormat.parse(dataHoje);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(date2.after(date1)) {
            return true;
        } else {
            return false;
        }
    }

    public static String ano(){
        String[] parts = Utilitario.dataAtual().split("/");
        return parts[2];
    }
}
