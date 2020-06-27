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
import com.example.sagliklibeslen.Model.BesinlerModel;
import com.example.sagliklibeslen.Model.UserBesinModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OzetAdapter extends BaseAdapter {

    private Context context;
    private List<BesinlerModel> liste;

    public OzetAdapter(Context context, List<BesinlerModel> liste){
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
        convertView = LayoutInflater.from(context).inflate(R.layout.ozet_layout,parent,false);
        TextView besinAdi=convertView.findViewById(R.id.besinAdi);
        TextView besinKalori=convertView.findViewById(R.id.besin_kalori);
        TextView besinKarbon=convertView.findViewById(R.id.besin_karbon);
        TextView besinProtein=convertView.findViewById(R.id.besin_protein);
        TextView besinYag=convertView.findViewById(R.id.besin_yag);
        TextView barkodNo=convertView.findViewById(R.id.barkodNo);
        TextView tarih=convertView.findViewById(R.id.tarih);
        TextView porsiyon=convertView.findViewById(R.id.porsiyon);
        TextView toplam=convertView.findViewById(R.id.toplam);

        besinAdi.setText(liste.get(position).besinAdi);
        besinKalori.setText(String.format("%.2f", liste.get(position).getBesinKalori())+"");
        besinKarbon.setText(String.format("%.2f", liste.get(position).getBesinKarbonhidrat())+"");
        besinProtein.setText(String.format("%.2f", liste.get(position).getBesinProtein())+"");
        besinYag.setText(String.format("%.2f", liste.get(position).getBesinYag())+"");

        String tarihx = liste.get(position).Date.toString().substring(8,10) + "/" + liste.get(position).Date.toString().substring(5,7) + "/" + liste.get(position).Date.toString().substring(0,4);
        Date date1= null;
        try {
            date1 = new SimpleDateFormat("dd/MM/yyyy").parse(tarihx);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat format = new SimpleDateFormat("dd MMMM yyyy");
        tarih.setText(format.format(date1) + " " + liste.get(position).Date.toString().substring(11,16));
        barkodNo.setText(liste.get(position).getBesinlerId()+"");
        porsiyon.setText(liste.get(position).getPorsiyon()+"");
        double x = liste.get(position).getBesinKalori() * liste.get(position).getPorsiyon();
        String y = String.format("%.2f", x);
        toplam.setText(y+"");

        return convertView;
    }
}
