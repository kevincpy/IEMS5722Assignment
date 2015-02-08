package com.iems5722.translateapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;

public class MainActivity extends Activity {
    Button b1;
    Button b2;
    Button b3;
    Button b4;
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
                        final ProgressDialog ringProgressDialog = new ProgressDialog(MainActivity.this);
                        ringProgressDialog.setTitle("Connecting");
                        ringProgressDialog.show();
                        ringProgressDialog.setCancelable(false);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                TcpClient();
                                ringProgressDialog.dismiss();
                            }
                        }).start();
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
        }

        if(word != null){
            t1.setText(word);
        }
        else {
            Toast.makeText(MainActivity.this, "Translate Error", Toast.LENGTH_SHORT).show();
        }

	}
    // share the translate result
    private void shareResult() {
        String input = e1.getText().toString();
        WordDictionary wd = new WordDictionary();
        String word = wd.wordDict.get(input);

        // show some feedback to user: translated text, error message, dialog etc
        if (input.trim().equals("")) {
            Toast.makeText(MainActivity.this, "Empty Input", Toast.LENGTH_SHORT).show();
            return;
        }

        if(word != null){
            t1.setText(word);
        }
        else {
            Toast.makeText(MainActivity.this, "Translate Error", Toast.LENGTH_SHORT).show();
            return;
        }


        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Translate result is " + "\" " + input + " -> " + word +" \" !" + "\n\n### From TranslateApp ### ");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
	// TCP client
    private void TcpClient() {
        String hostname = "iems5722v.ie.cuhk.edu.hk";
        int serverPort = 3001;
        try {
            Socket client = new Socket(hostname, serverPort);
            System.out.println("Just connected to " + client.getRemoteSocketAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//            try {
//
//                DataOutputStream out = new DataOutputStream(client.getOutputStream());
//                out.writeUTF(input + client.getLocalSocketAddress());
//                DataInputStream in = new DataInputStream(client.getInputStream());
//                Toast.makeText(MainActivity.this, "Translate Result: " + in.readUTF(), Toast.LENGTH_SHORT).show();
//                client.close();
//            }





//        try {
//            client = new Socket(serverAddr, serverPort);
//
//            alertDialogBuilder.setMessage("Just connected to" + client.getRemoteSocketAddress());
//        }
//        catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }


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
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
