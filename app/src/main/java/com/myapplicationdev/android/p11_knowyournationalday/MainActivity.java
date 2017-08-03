package com.myapplicationdev.android.p11_knowyournationalday;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {
    private ListView lv;
    String[] values = {"Singapore's National Day is on the 9th of August", "Singapore is now 52 years old", "Theme is '#OneNationTogether'"};
    int score = 0;
    String accessCode= "";

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView)findViewById(R.id.lv);
       adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,values);
        lv.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.options,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_quit) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Are you sure?")
                    // Set text for the positive button and the corresponding OnClickListener when it is clicked
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    // Set text for the negative button and the corresponding OnClickListener when it is clicked
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(MainActivity.this, "You clicked no",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
            // Create the AlertDialog object and return it
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }else if (item.getItemId() == R.id.sendToFriend) {
            String [] list = new String[] { "Email", "SMS"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select the way to enrich your friend")
                    // Set the list of items easily by just supplying an array of the items
                    .setItems(list, new DialogInterface.OnClickListener() {
                        // The parameter "which" is the item index clicked, starting from 0
                        public void onClick(DialogInterface dialog, int which) {
                            StringBuilder list = new StringBuilder();
                            if (which == 0) {
                                Intent email = new Intent(Intent.ACTION_SEND);
                                email.putExtra(Intent.EXTRA_SUBJECT, "Fun facts about Singapore!");
                                for(int i=0;i<values.length;i++){

                                    email.putExtra(Intent.EXTRA_TEXT,  String.valueOf(list.append(values[i])));
                                }

                                email.setType("message/rfc822");

                                startActivity(Intent.createChooser(email,
                                        "Choose an Email client :"));
                            }else {
                                String phoneNumber="";

                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber));
                                for(int i=0;i<values.length;i++){
                                    intent.putExtra("sms_body", String.valueOf(list.append(values[i])));
                                }

                                startActivity(intent);


                                }


                            }

                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }else {
            LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout quiz = (LinearLayout)inflater.inflate(R.layout.quiz, null);
            final RadioGroup rg1 = (RadioGroup) quiz.findViewById(R.id.radioGroup1);
            final RadioGroup rg2 = (RadioGroup) quiz.findViewById(R.id.radioGroup2);
            final RadioGroup rg3 = (RadioGroup)quiz.findViewById(R.id.radioGroup3);


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please Enter")
                    .setView(quiz)
                    .setNegativeButton("Forget", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    })
                    .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            final int selectedButtonId = rg1.getCheckedRadioButtonId();
                            final int selectedButtonId2 = rg2.getCheckedRadioButtonId();
                            final int selectedButtonId3 = rg3.getCheckedRadioButtonId();



                            if(selectedButtonId == (R.id.radio1No)) {
                                score++;
                            }if(selectedButtonId2 == (R.id.radio2Yes)){
                                score++;
                            }if (selectedButtonId3 == (R.id.radio3Yes)) {
                                score++;
                            }
                            Toast.makeText(MainActivity.this, "Score: " + score, Toast.LENGTH_SHORT).show();
                            score = 0;
                        }

                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }




    protected void onStop(){
        super.onStop();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefEdit = prefs.edit();
        prefEdit.putString("accessCode", accessCode);
        prefEdit.commit();
    }
    protected void onStart(){
        super.onStart();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String code = pref.getString("Access code","");
        accessCode = code;
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout passPhrase =
                (LinearLayout) inflater.inflate(R.layout.passphrase, null);
        final EditText etPassphrase = (EditText) passPhrase
                .findViewById(R.id.editTextPassPhrase);

        if(accessCode.equals("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please Login").setView(passPhrase).setCancelable(false)
                    .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String passcode = "738964";
                            if(etPassphrase.getText().toString().equals(passcode)){
                                accessCode = etPassphrase.getText().toString();
                                Toast.makeText(MainActivity.this, "Correct Access Code, Welcome!",Toast.LENGTH_SHORT).show();
                            } else{
                                Toast.makeText(MainActivity.this, "Wrong Access Code",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(MainActivity.this, "Please Rewrite the code again", Toast.LENGTH_LONG).show();
                    finish();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

    }
}
