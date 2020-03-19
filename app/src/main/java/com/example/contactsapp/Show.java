package com.example.contactsapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
