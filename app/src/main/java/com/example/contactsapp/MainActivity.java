package com.example.contactsapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public boolean dontdupaddr=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("Main ","Activity");
        SQLiteDatabase db = this.openOrCreateDatabase("ContactsDB", MODE_PRIVATE, null);
        //db.execSQL("DROP TABLE IF EXISTS CONTACTS");

        db.execSQL("CREATE TABLE IF NOT EXISTS CONTACTS (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(20) NOT NULL, NICKNAME VARCHAR(20)," +
                "MOBILE VARCHAR(13) NOT NULL, ALTMOBILE VARCHAR(13), MAIL VARCHAR(30), ALTMAIL VARCHAR(30), ADDRESS VARCHAR(60), ALTADDRESS VARCHAR(60), CATEGORY VARCHAR(20)," +
                "HOMELAT VARCHAR(15), HOMELONG VARCHAR(15), OFFICELAT VACRHAR(15), OFFICELONG VARCHAR(15))");

    }

    public void saveContact(View view) {
        Intent intent = new Intent(this, Save.class);
        startActivity(intent);

    }

    public void showContacts(View view) {
        Intent showintent = new Intent(this, Show.class);
        startActivity(showintent);
    }

    public void searchContact(View view) {
        EditText searchbutton = findViewById(R.id.searchname);
        String name = searchbutton.getText().toString();
        Intent showintent = new Intent(this, Show.class);
        showintent.putExtra("search", name);
        startActivity(showintent);
    }

}
