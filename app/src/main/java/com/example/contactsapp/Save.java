/*
    Saves a new contact if no existing numbers or emails are present in the new contact
    Also used to update an existing contact with duplication constraint
 */


package com.example.contactsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Save extends AppCompatActivity {
    Spinner spinner;
    Context context;
    ArrayList<String> fetchCategory=new ArrayList<String>();
    boolean newCat=false,merge=false,dontdupaddr=false,update=false;
    String getid,id,name,nickname,mobile,altmobile,mail,altmail,addr,altaddr,getnewcat,selectedCategory;
    Cursor cursor;
    EditText _name,_nickname,_mobile,_altmobile,_mail,_altmail,_addr,_altaddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        Log.i("Save","Activity");

        _name= (EditText) findViewById(R.id.name);
        _nickname= (EditText) findViewById(R.id.nickname);
        _mobile= (EditText) findViewById(R.id.number);
        _altmobile= (EditText) findViewById(R.id.altumber);
        _mail= (EditText) findViewById(R.id.mail);
        _altmail= (EditText) findViewById(R.id.altmail);
        _addr= (EditText) findViewById(R.id.address);
        _altaddr= (EditText) findViewById(R.id.altaddress);

        if(getIntent().hasExtra("id"))
        {
            getid=getIntent().getStringExtra("id");
            Log.i("Id ",getid+" Length = "+ String.valueOf(getid.length()));
            update=true;
            updateOps(getid);
        }
        else
            update=false;

        context=getApplicationContext();
        SQLiteDatabase db=this.openOrCreateDatabase("ContactsDB",MODE_PRIVATE,null);

        db.execSQL("CREATE TABLE IF NOT EXISTS CATEGORIES ( CATEGORY VARCHAR(20) PRIMARY KEY)");;
        cursor=db.rawQuery("SELECT COUNT(*) FROM CATEGORIES",null);
        while(cursor.moveToNext())
        {
            int cInt=Integer.parseInt(cursor.getString(0));
            if(cInt==0)
            {
                ArrayList<String> allCategories = new ArrayList<String>();
                allCategories.add("");
                allCategories.add("Home");
                allCategories.add("Office");
                allCategories.add("Family");
                allCategories.add("Help");
                allCategories.add("Misc");
                for(int i=0;i<6;i++)
                {
                    db.execSQL("INSERT INTO CATEGORIES VALUES ('"+allCategories.get(i)+"')");
                }
            }
        }

        cursor=db.rawQuery("SELECT DISTINCT UPPER(CATEGORY) FROM CATEGORIES",null);

        while(cursor.moveToNext())
        {
            String cat = cursor.getString(0);
            fetchCategory.add(cat);
        }
        spinner=(Spinner)findViewById(R.id.categories);
        ArrayAdapter<String> spinnerAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,fetchCategory);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory=spinner.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory="";


            }

        });

        if(update && selectedCategory.length()>0)
        {
            if(getPos(spinner,selectedCategory)>=0)
                spinner.setSelection(getPos(spinner,selectedCategory));
        }


    }
    //When User Adds a New Category
    public void  getCategory(View view)
    {
        EditText addcat = findViewById(R.id.newcategory);
        getnewcat=addcat.getText().toString();
        SQLiteDatabase db=this.openOrCreateDatabase("ContactsDB",MODE_PRIVATE,null);
        db.execSQL("INSERT INTO CATEGORIES VALUES('"+getnewcat+"')");
        newCat=true;
        Toast toast=Toast.makeText(this,"Added",Toast.LENGTH_SHORT);
        toast.show();
    }
    public void saveData(View view) throws IOException {
        SQLiteDatabase db = this.openOrCreateDatabase("ContactsDB", MODE_PRIVATE, null);

        name = _name.getText().toString();

        nickname = "";
        if (_nickname.getText().toString().trim().length() != 0)
            nickname = _nickname.getText().toString();


        mobile = "";
        if (_mobile.getText().toString().trim().length() != 0)
            mobile = _mobile.getText().toString().trim();


        altmobile = "";
        if (_altmobile.getText().toString().trim().length() != 0)
            altmobile = _altmobile.getText().toString().trim();


        mail = "";
        if (_mail.getText().toString().trim().length() != 0)
            mail = _mail.getText().toString().trim();


        altmail = "";
        if (_altmail.getText().toString().trim().length() != 0)
            altmail = _altmail.getText().toString().trim();


        addr = "";
        if (_addr.getText().toString().trim().length() != 0)
            addr = _addr.getText().toString();


        altaddr = "";
        if (_altaddr.getText().toString().trim().length() != 0)
            altaddr = _altaddr.getText().toString();

        spinner = (Spinner) findViewById(R.id.categories);


        if (newCat == true)
            selectedCategory = getnewcat;


        if (mail.length() == 0 && altmail.length() != 0) {
            mail = altmail;
            altmail = "";
        }

        mobile=mobile.trim();altmobile=altmobile.trim();mail=mail.trim();altmail=altmail.trim();

        //Detecting Duplicate Numbers and Emails During Update

        if (update && !merge)
        {
            Log.i("SHOW ID",getid+" LENGTH="+getid.length());
            try {
                if (mobile.length() != 0 && !merge) {
                    cursor = db.rawQuery("SELECT ID FROM CONTACTS WHERE MOBILE = '" + mobile + "' OR ALTMOBILE='" + mobile + "' AND ID!='"+getid+"' ", null);
                    if (cursor.moveToNext()) {
                        Log.i("Update Mobile Clash Id ", cursor.getString(0)+"  "+String.valueOf(cursor.getString(0).length()));
                        String id1,id2;
                        id1=getid.trim();
                        id2=cursor.getString(0).trim();
                        if(!id1.equals(id2)) {
                            merge = true;
                            merging(cursor.getString(0));
                        }

                    }

                }
                if (altmobile.length() != 0 && !merge) {
                    cursor = db.rawQuery("SELECT ID FROM CONTACTS WHERE MOBILE = '" + altmobile + "' OR ALTMOBILE='" + altmobile + "' AND ID!='"+getid+"' ", null);
                    if (cursor.moveToNext()) {
                        Log.i("UpdateAltmobileClashId", cursor.getString(0)+" Length="+String.valueOf(cursor.getString(0).length()));
                        String id1,id2;
                        id1=getid.trim();
                        id2=cursor.getString(0).trim();
                        if(!id1.equals(id2)) {
                            merge = true;
                            merging(cursor.getString(0));
                        }



                    }
                }
                if (mail.length() != 0 && !merge) {
                    cursor = db.rawQuery("SELECT ID FROM CONTACTS WHERE MAIL = '" + mail + "' OR ALTMAIL='" + mail + "' AND ID!='"+getid+"' " , null);

                    if (cursor.moveToNext()) {
                        Log.i("Update Mail Clash Id ", cursor.getString(0)+String.valueOf(cursor.getString(0).length()));
                        String id1,id2;
                        id1=getid.trim();
                        id2=cursor.getString(0).trim();
                        if(!id1.equals(id2)) {
                            merge = true;
                            merging(cursor.getString(0));
                        }


                    }
                }
                if (altmail.length() != 0 && !merge) {
                    cursor = db.rawQuery("SELECT ID FROM CONTACTS WHERE MAIL = '" + altmail + "' OR ALTMAIL='" + altmail + "' AND ID!='"+getid+"' ", null);
                    if (cursor.moveToNext()) {
                        Log.i("UpdateAltmailClash Id ", cursor.getString(0)+String.valueOf(cursor.getString(0).length()));
                        String id1,id2;
                        id1=getid.trim();
                        id2=cursor.getString(0).trim();
                        if(!id1.equals(id2)) {
                            merge = true;
                            merging(cursor.getString(0));
                        }


                    }
                }
            }
            catch (Exception e) {
                Log.i("Update", "Error");
            }

        }
        ////Detecting Duplicate Numbers and Emails During Saving a New Contact
        else {
            try {
                if (mobile.length() != 0 && !merge) {

                    cursor = db.rawQuery("SELECT ID FROM CONTACTS WHERE MOBILE = '" + mobile + "' OR ALTMOBILE='" + mobile + "'", null);
                    if (cursor.moveToNext()) {

                        merge = true;
                        merging();


                    }

                }
                if (altmobile.length() != 0 && !merge) {
                    cursor = db.rawQuery("SELECT ID FROM CONTACTS WHERE MOBILE = '"+altmobile+"' OR ALTMOBILE='"+altmobile +"'", null);
                    if (cursor.moveToNext()) {
                        merge = true;
                        merging();



                    }
                }
                if (mail.length() != 0 && !merge) {

                    cursor = db.rawQuery("SELECT ID FROM CONTACTS WHERE MAIL = '" + mail + "' OR ALTMAIL='" + mail + "'", null);

                    if (cursor.moveToNext()) {
                        merge = true;
                        merging();

                    }
                }
                if (altmail.length() != 0 && !merge) {


                    cursor = db.rawQuery("SELECT ID FROM CONTACTS WHERE MAIL = '" + altmail + "' OR ALTMAIL='" + altmail + "'", null);
                    if (cursor.moveToNext()) {
                        merge = true;
                        merging();


                    }
                }

            }
            catch (Exception e) {

            }
        }

        //No Duplicate Numbers or Emails Found!

        if (update && !merge) {
            Log.i("On Update ", "Ok");

            try {
                if (name.length() != 0 && mobile.length() != 0) {
                    db.execSQL("UPDATE CONTACTS SET NAME='" + name + "', NICKNAME='" + nickname + "', MOBILE = '" + mobile + "'," +
                            "ALTMOBILE='" + altmobile + "',MAIL='" + mail + "',ALTMAIL='" + altmail + "',ADDRESS='" + addr + "'," +
                            "ALTADDRESS='" + altaddr + "',CATEGORY='" + selectedCategory + "' WHERE ID='" + getid + "'");

                    Toast.makeText(this, "Update Done", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
                else
                    Toast.makeText(this, "Enter Name and Number", Toast.LENGTH_SHORT);


            }
            catch (Exception e) {
                Log.i("Update", "Error");
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }


        }
        else if(!update && !merge)
            {
            Toast toast;
            if (name.trim().length() == 0 || mobile.trim().length() == 0) {

                toast = Toast.makeText(this, "Enter both Name and Number", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                try {

                    db.execSQL("INSERT INTO CONTACTS(NAME,NICKNAME,MOBILE,ALTMOBILE,MAIL,ALTMAIL,ADDRESS,ALTADDRESS,CATEGORY)" +
                            " VALUES ('" + name + "','" + nickname + "','" + mobile + "','" + altmobile + "','" + mail + "','" + altmail + "'," +
                            "'" + addr + "','" + altaddr + "','" + selectedCategory + "')");

                    toast = Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT);
                    toast.show();

                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                } catch (Exception e) {
                    Log.i("Error ", "Couldn't save");
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
            }
        }


    }


    //Merging During Saving a New Contact
    public void merging()
    {
        Toast toas=Toast.makeText(this,"Found Existing Contact",Toast.LENGTH_SHORT);
        toas.show();
        id=cursor.getString(0);

        Intent intent=new Intent(this,Merge.class);
        intent.putExtra("id",id);
        intent.putExtra("name",name);
        intent.putExtra("nickname",nickname);
        intent.putExtra("mobile",mobile);
        intent.putExtra("altmobile",altmobile);
        intent.putExtra("mail",mail);
        intent.putExtra("altmail",altmail);
        intent.putExtra("address",addr);
        intent.putExtra("altaddress",altaddr);
        intent.putExtra("category",selectedCategory);
        startActivity(intent);

    }

    //Merging in Case of Update
    public void merging(String contact_id)
    {
        Toast toas=Toast.makeText(this,"Found Existing Contact",Toast.LENGTH_SHORT);
        toas.show();
        Intent intent=new Intent(this,Merge.class);
        intent.putExtra("id",contact_id);
        intent.putExtra("update",getid);
        intent.putExtra("name",name);
        intent.putExtra("nickname",nickname);
        intent.putExtra("mobile",mobile);
        intent.putExtra("altmobile",altmobile);
        intent.putExtra("mail",mail);
        intent.putExtra("altmail",altmail);
        intent.putExtra("address",addr);
        intent.putExtra("altaddress",altaddr);
        intent.putExtra("category",selectedCategory);
        startActivity(intent);
    }
    public void updateOps(String nid)
    {
        SQLiteDatabase db=this.openOrCreateDatabase("ContactsDB",MODE_PRIVATE,null);
        cursor=db.rawQuery("SELECT NAME,NICKNAME,MOBILE,ALTMOBILE,MAIL,ALTMAIL,ADDRESS,ALTADDRESS,CATEGORY FROM CONTACTS WHERE ID='"+nid+"' ",null);
        if(cursor.moveToNext())
        {
            _name.setText(cursor.getString(0));
            _nickname.setText(cursor.getString(1));
            _mobile.setText(cursor.getString(2));
            _altmobile.setText(cursor.getString(3));
            _mail.setText(cursor.getString(4));
            _altmail.setText(cursor.getString(5));
            _addr.setText(cursor.getString(6));
            _altaddr.setText(cursor.getString(7));
            if(cursor.getString(8).trim().length()>0)
                selectedCategory=cursor.getString(8);
            else
                selectedCategory="";
        }

    }

    public int getPos(Spinner s,String tmp)
    {
        Adapter adapter=s.getAdapter();
        int t=-1;
        for(int i=0;i<adapter.getCount();i++)
        {
            if(adapter.getItem(i).equals(tmp))
            {
                t=i;
                break;
            }
        }
        return t;
    }


}

