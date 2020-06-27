package com.example.sagliklibeslen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sagliklibeslen.Model.Besinler;

import java.util.List;

public class BesinAdapter extends BaseAdapter {
    private Context context;
    private List<Besinler> liste;

    public BesinAdapter(Context context, List<Besinler> liste){
        this.context=context;
        this.liste=liste;
    }
    @Override
    public int getCount() {
        return liste.size();
    }

    @Override
    public Besinler getItem(int position) {
        return liste.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.besinler_layout,parent,false);
        LinearLayout linearLayout;
        TextView besinAdi=convertView.findViewById(R.id.besinAdi);
        TextView besinKalori=convertView.findViewById(R.id.besin_kalori);
        TextView besinKarbon=convertView.findViewById(R.id.besin_karbon);
        TextView besinProtein=convertView.findViewById(R.id.besin_protein);
        TextView besinYag=convertView.findViewById(R.id.besin_yag);
        TextView barkodNo=convertView.findViewById(R.id.barkodNo);
        linearLayout = convertView.findViewById(R.id.linear);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BesinGuncelle.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle extras = new Bundle();
                extras.putSerializable("obj",getItem(position));
                intent.putExtras(extras);
                context.startActivity(intent);
            }
        });
        besinAdi.setText(liste.get(position).getBesinAdi());
        besinKalori.setText(String.format("%.2f", liste.get(position).getBesinKalori())+"");
        besinKarbon.setText(String.format("%.2f", liste.get(position).getBesinKarbonhidrat())+"");
        besinProtein.setText(String.format("%.2f", liste.get(position).getBesinProtein())+"");
        besinYag.setText(String.format("%.2f", liste.get(position).getBesinYag())+"");
        if(liste.get(position).getBarkodNo().equals("0")){
            barkodNo.setText(liste.get(position).getBesinlerId()+"");
        }
        else{
            barkodNo.setText(liste.get(position).getBarkodNo()+"");
        }


        return convertView;
    }

}
