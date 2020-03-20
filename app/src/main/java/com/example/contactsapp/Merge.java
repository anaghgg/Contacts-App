/*
      Used to merge :
         -an yet to save contact having numbers or emails that are already present in some existing Contact
         -an existing contact which after being updated has numbers or emails that are already present in some other existing Contact
         -an existing contact that the user wishes to merge with some other contact living nearby
*/
package com.example.contactsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Merge extends AppCompatActivity {
    String id,name,nickname,mobile,altmobile,mail,altmail,address,altaddress,category,orgname,orgmobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merge);
        id=getIntent().getStringExtra("id");
        Log.i("CLASH ID",id);

        SQLiteDatabase db=this.openOrCreateDatabase("ContactsDB",MODE_PRIVATE,null);
        Cursor cursor;
        name=getIntent().getStringExtra("name");
        nickname=getIntent().getStringExtra("nickname");
        mobile=getIntent().getStringExtra("mobile");
        altmobile=getIntent().getStringExtra("altmobile");
        mail=getIntent().getStringExtra("mail");
        altmail=getIntent().getStringExtra("altmail");
        address=getIntent().getStringExtra("address");
        altaddress=getIntent().getStringExtra("altaddress");
        category=getIntent().getStringExtra("category");

        TextView textname=findViewById(R.id.nameshown);

        cursor=db.rawQuery("SELECT NAME FROM CONTACTS WHERE ID = '"+id+"' ",null);
        if(cursor.moveToNext())
            textname.setText(cursor.getString(0));
    }
    public void merge(View view)
    {
        SQLiteDatabase db=this.openOrCreateDatabase("ContactsDB",MODE_PRIVATE,null);
        Cursor cursor;
        String savedid,savedname,savednickname,savedmobile,savedaltmobile,savedmail,savedaltmail,savedaddress,savedaltaddress,savedcatgory;
        //Name
        try {
            cursor=db.rawQuery("SELECT * FROM CONTACTS WHERE ID='"+id+"' ",null);
            if(cursor.moveToNext())
            {
                savedid=cursor.getString(0);
                savedname=cursor.getString(1);
                savednickname=cursor.getString(2);
                savedmobile=cursor.getString(3);
                savedaltmobile=cursor.getString(4);
                savedmail=cursor.getString(5);
                savedaltmail=cursor.getString(6);
                savedaddress=cursor.getString(7);
                savedaltaddress=cursor.getString(8);
                savedcatgory=cursor.getString(9);

                if(savednickname.trim().length()==0 && nickname.trim().length()!=0)
                    savednickname=nickname;

                if(savedaltmobile.trim().length()==0)
                {
                    if(savedmobile.equals(mobile) && altmobile.trim().length()!=0)
                        savedaltmobile=altmobile;
                    else if(altmobile.trim().length()!=0)
                        savedaltmobile=mobile;

                }

                if(savedmail.trim().length()==0 && savedaltmail.trim().length()==0 && mail.trim().length()!=0 && altmail.trim().length()!=0)
                {
                    savedmail=mail;
                    savedaltmail=altmail;
                }
                else if(savedmail.trim().length()==0 && mail.trim().length()!=0)
                    savedmail=mail;
                else if(savedaltmail.trim().length()==0 && mail.trim().length()!=0)
                {
                    if(!savedmail.trim().equals(mail.trim()))
                        savedaltmail=mail;

                    else if(altmail.trim().length()!=0)
                        savedaltmail=altmail;
                }

                if(savedaddress.trim().length()==0 && address.trim().length()!=0)
                    savedaddress=address;

                if(savedaltaddress.trim().length()==0 && altaddress.trim().length()!=0)
                    savedaltaddress=altaddress;

                if(savedcatgory.trim().length()==0 && category.trim().length()!=0 )
                    savedcatgory=category;
                try {
                    db.execSQL("UPDATE CONTACTS SET NAME='" + savedname + "',NICKNAME='" + savednickname + "', MOBILE='" + savedmobile + "', ALTMOBILE='" + savedaltmobile + "', " +
                            "MAIL='" + savedmail + "', ALTMAIL='" + savedaltmail + "', ADDRESS='" + savedaddress + "', ALTADDRESS='" + savedaltaddress + "'," +
                            " CATEGORY='" + savedcatgory + "' WHERE ID='" + savedid + "' ");


                    Toast toast = Toast.makeText(this, "Merged Successfully", Toast.LENGTH_SHORT);
                    toast.show();

                    if (getIntent().hasExtra("update")) {
                        String delete_id = getIntent().getStringExtra("update");
                        try {
                            db.execSQL("DELETE FROM CONTACTS WHERE ID='" + delete_id + "' ");
                        } catch (Exception e) {
                            Log.i("Merge", "Couldn't Delete");
                        }

                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(this,"Failed",Toast.LENGTH_SHORT);
                }

            }

        }
        catch (Exception e)
        {
            Toast.makeText(this,"Failed",Toast.LENGTH_SHORT);
        }
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
