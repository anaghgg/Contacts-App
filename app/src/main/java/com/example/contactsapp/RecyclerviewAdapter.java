package com.example.contactsapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.Viewholder>{
    private ArrayList<String> namelist = new ArrayList<String>();
    private ArrayList<String> numberlist = new ArrayList<String>();
    private Context context;

    public RecyclerviewAdapter(Context context,ArrayList<String> namelist, ArrayList<String> numberlist) {
        this.namelist = namelist;
        this.numberlist = numberlist;
        this.context = context;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem,parent,false);
        Viewholder holder = new Viewholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {
        holder.name.setText(namelist.get(position));
        holder.number.setText(numberlist.get(position));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Clicked on ",namelist.get(position));
                Intent intent = new Intent(context,GotoContact.class);
                intent.putExtra("getname",namelist.get(position));
                intent.putExtra("getnumber",numberlist.get(position));
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return namelist.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder
    {
        TextView name;
        TextView number;
        RelativeLayout layout;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.listitem_name);
            number=itemView.findViewById(R.id.lisitem_number);
            layout=itemView.findViewById(R.id.itemlayout);
        }
    }
}
