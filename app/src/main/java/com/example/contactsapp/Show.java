/*
    Shows:
         -All the contacts
         -Contacts who live nearby a selected contact
         -Contacts living nearby with whom the user wishes to merge the existing contact
*/

package com.example.contactsapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Show extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        Bundle b=getIntent().getExtras();

        //Merging with a contact that lives nearby
        if(getIntent().hasExtra("mergemode"))
        {
            Log.i("Geocode","Transitioned to Show activity");

            try{
                ArrayList<String>info=b.getStringArrayList("info");
                ArrayList<String>names=b.getStringArrayList("namelist");
                ArrayList<String>numbers=b.getStringArrayList("numberlist");
                RecyclerView rv = findViewById(R.id.recycler);
                RecyclerviewAdapter adapter = new RecyclerviewAdapter(this,names,numbers,info);
                rv.setAdapter(adapter);
                rv.setLayoutManager(new LinearLayoutManager(this));
            }
            catch (Exception e)
            {
                Log.e("ShowNearby","Error",e);
            }



        }

        //Show contacts living nearby
        else if(getIntent().hasExtra("addressconflict"))
        {
            try{
                ArrayList<String>names=b.getStringArrayList("namelist");
                ArrayList<String>numbers=b.getStringArrayList("numberlist");
                RecyclerView rv = findViewById(R.id.recycler);
                RecyclerviewAdapter adapter = new RecyclerviewAdapter(this,names,numbers);
                rv.setAdapter(adapter);
                rv.setLayoutManager(new LinearLayoutManager(this));
            }
            catch (Exception e)
            {
                Log.e("ShowNearby","Error",e);
            }
        }

        //Show all contacts
        else
        {
            SQLiteDatabase db = this.openOrCreateDatabase("ContactsDB",MODE_PRIVATE,null);
            Cursor cursor;
            try {
                if (getIntent().hasExtra("search")) {
                    String searchname = getIntent().getStringExtra("search");
                    cursor = db.rawQuery("SELECT NAME,MOBILE,CATEGORY FROM CONTACTS WHERE NAME LIKE ?", new String[]{searchname + "%"});
                } else
                    cursor = db.rawQuery("SELECT NAME,MOBILE,CATEGORY FROM CONTACTS", null);
                ArrayList<String> names = new ArrayList<String>();
                ArrayList<String> numbers = new ArrayList<String>();
                while(cursor.moveToNext())
                {
                    names.add(cursor.getString(0));
                    numbers.add(cursor.getString(1));
                }

                RecyclerView rv = findViewById(R.id.recycler);
                RecyclerviewAdapter adapter = new RecyclerviewAdapter(this,names,numbers);
                rv.setAdapter(adapter);
                rv.setLayoutManager(new LinearLayoutManager(this));

            }
            catch (Exception e)
            {
                Toast toast=Toast.makeText(this,"Error",Toast.LENGTH_LONG);
                toast.show();
            }
        }

    }
    public void home(View view)
    {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
