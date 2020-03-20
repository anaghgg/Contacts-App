/*
    Simple Adapter for RecyclerView
 */


package com.example.contactsapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.Viewholder>{
    private ArrayList<String> namelist = new ArrayList<String>();
    private ArrayList<String> numberlist = new ArrayList<String>();
    private ArrayList<String> info=new ArrayList<>();
    boolean merge=false;
    private Context context;

    public RecyclerviewAdapter(Context context,ArrayList<String> namelist, ArrayList<String> numberlist) {
        this.namelist = namelist;
        this.numberlist = numberlist;
        this.context = context;
    }

    //Overloading for Merging in Option operation
    public RecyclerviewAdapter(Context context,ArrayList<String> namelist, ArrayList<String> numberlist,ArrayList<String>info)
    {
        merge=true;
        this.namelist = namelist;
        this.numberlist = numberlist;
        this.context = context;
        this.info=info;

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
                if(!merge)
                {
                    Log.i("Clicked on ",namelist.get(position));
                    Intent intent = new Intent(context,GotoContact.class);
                    intent.putExtra("getname",namelist.get(position));
                    intent.putExtra("getnumber",numberlist.get(position));
                    context.startActivity(intent);
                }
                else
                {
                    try
                    {
                        SQLiteDatabase db=context.openOrCreateDatabase("ContactsDB",MODE_PRIVATE,null);
                        Cursor cursor;
                        cursor=db.rawQuery("SELECT ID FROM CONTACTS WHERE NAME='"+namelist.get(position)+"' AND MOBILE='"+numberlist.get(position)+"' ",null);
                        if(cursor.moveToNext())
                        {
                            final String contactid=cursor.getString(0);

                            new AlertDialog.Builder(context)
                                    .setTitle("Merge Entry?")
                                    .setMessage("This will delete the cuurent contact?")

                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            Log.i("Merge","Started");
                                            Intent intent=new Intent(context,Merge.class);
                                            intent.putExtra("id",contactid);
                                            intent.putExtra("update",info.get(0));
                                            intent.putExtra("name",info.get(1));
                                            intent.putExtra("nickname",info.get(2));
                                            intent.putExtra("mobile",info.get(3));
                                            intent.putExtra("altmobile",info.get(4));
                                            intent.putExtra("mail",info.get(5));
                                            intent.putExtra("altmail",info.get(6));
                                            intent.putExtra("address",info.get(7));
                                            intent.putExtra("altaddress",info.get(8));
                                            intent.putExtra("category",info.get(9));
                                            context.startActivity(intent);
                                        }
                                    })


                                    .setNegativeButton(android.R.string.no, null)
                                    .show();
                        }
                    }
                    catch (Exception e)
                    {
                        Log.i("Merge","Address Merging Error");
                    }
                }

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
