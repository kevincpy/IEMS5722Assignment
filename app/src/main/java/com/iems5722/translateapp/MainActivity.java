package com.iems5722.translateapp;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

public class MainActivity extends Activity {
    Button b1;
    Button b2;
    Button b3;
    Button b4;
    Button b5;
    EditText e1;
    TextView t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // get references to layout objects
        b1 = (Button) findViewById(R.id.button1);
        e1 = (EditText) findViewById(R.id.input1);
        t1 = (TextView) findViewById(R.id.text1);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button) findViewById(R.id.button3);
        b4 = (Button) findViewById(R.id.button4);
        b5 = (Button) findViewById(R.id.button5);

        // add click listener to button to call translateText()
        b1.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        translateText();
                    }
                }
        );
        b2.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        shareResult();
                    }
                }
        );
        b3.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TranslateJob("tcp");
                    }
                }
        );
        b4.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TranslateJob("http");
                    }
                }
        );

        b5.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent historyAct = new Intent();
                        historyAct.setClass(MainActivity.this, History.class);
                        startActivity(historyAct);
                        MainActivity.this.finish();
                    }
                }
        );

    }


    // translate look up
    private void translateText() {
        // get user input
        String input = e1.getText().toString();

        // try get word out of dictionary
        WordDictionary wd = new WordDictionary();
        String word = wd.wordDict.get(input);
        // show some feedback to user: translated text, error message, dialog etc
        if (input.trim().equals("")) {
            Toast.makeText(MainActivity.this, "Empty Input", Toast.LENGTH_SHORT).show();
            return;
        }

        if (word != null) {
            t1.setText(word);
            storageFile(input, word);
            return;
        } else {
            Toast.makeText(MainActivity.this, "Translate Error", Toast.LENGTH_SHORT).show();
            t1.setText("Translate Error");
            return;
        }

    }

    // share the translate result
    private void shareResult() {
        String input = e1.getText().toString();
        String word = t1.getText().toString();

        // show some feedback to user: translated text, error message, dialog etc
        if (input.trim().equals("")) {
            Toast.makeText(MainActivity.this, "Empty Input", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!word.isEmpty()) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Translate result is " + "\" " + input + " -> " + word + " \" !" + "\n\n### From TranslateApp ### ");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        } else {
            Toast.makeText(MainActivity.this, "Please press Translate", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void TranslateJob(String mode) {
        String input = e1.getText().toString();

        new TranslateTask(MainActivity.this).execute(mode, input);

    }

    public void storageFile(String word, String result) {
        String FILENAME = "translate_history";
        String record = word + ": " + result + "\n";
        try {
            FileOutputStream fileOutputStream = openFileOutput(FILENAME, Context.MODE_APPEND);
            fileOutputStream.write(record.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    // Options menu - not needed for this app
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }


        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}