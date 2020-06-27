package com.example.sagliklibeslen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sagliklibeslen.Model.Besinler;
import com.example.sagliklibeslen.Model.Su;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SuAdapter extends BaseAdapter {
    private Context context;
    private List<Su> liste;

    public SuAdapter(Context context, List<Su> liste){
        this.context=context;
        this.liste=liste;
    }

    @Override
    public int getCount() {
        return liste.size();

    }

    @Override
    public Object getItem(int position) {
        return liste.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.su_layout,parent,false);
        TextView suAdi=convertView.findViewById(R.id.suAdi);
        TextView toplam=convertView.findViewById(R.id.toplam);
        TextView tarih=convertView.findViewById(R.id.tarih);
        String tarihx = liste.get(position).Date.toString().substring(8,10) + "/" + liste.get(position).Date.toString().substring(5,7) + "/" + liste.get(position).Date.toString().substring(0,4);
        Date date1= null;
        try {
            date1 = new SimpleDateFormat("dd/MM/yyyy").parse(tarihx);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat format = new SimpleDateFormat("dd MMMM yyyy");
        tarih.setText(format.format(date1) + " " + liste.get(position).Date.toString().substring(11,16));

        suAdi.setText(liste.get(position).getBardakSayisi() + " Bardak");
        toplam.setText(liste.get(position).getBardakSayisi() * 0.250 +" Litre");
        return convertView;
    }
}
