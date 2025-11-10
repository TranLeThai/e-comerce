package com.example.e_comerce.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.e_comerce.R;
import java.util.List;

public class MonAnAdapter extends BaseAdapter {
    private Context context;
    private List<MonAn> listMonAn;

    public MonAnAdapter(Context context, List<MonAn> listMonAn) {
        this.context = context;
        this.listMonAn = listMonAn;
    }

    @Override
    public int getCount() {
        return listMonAn.size();
    }

    @Override
    public Object getItem(int position) {
        return listMonAn.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_monan, parent, false);

        MonAn mon = listMonAn.get(position);

        ImageView img = view.findViewById(R.id.imgMonAn);
        TextView ten = view.findViewById(R.id.tvTenMon);
        TextView gia = view.findViewById(R.id.tvGiaMon);

        img.setImageResource(mon.getHinh());
        ten.setText(mon.getTen());
        gia.setText(mon.getGia());

        return view;
    }
}
