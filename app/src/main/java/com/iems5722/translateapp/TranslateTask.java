package com.iems5722.translateapp;

import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by chan on 2/11/2015.
 */
public class TranslateTask extends AsyncTask<String, Void, String> {
    private MainActivity activity;
    public TranslateTask(MainActivity activity){
        this.activity = activity;
    }
    protected String doInBackground(String... Strings) {
        ConnectionDetector cd = new ConnectionDetector(activity.getApplicationContext());
        String mode = Strings[0];
        String input = Strings[1];
        String ServerResponse = "";

        if (input.trim().equals("")) {
            return "Empty Input";
        }


        Boolean isInternetPresent = cd.isConnectingToInternet();
        if (!isInternetPresent){
            System.out.println("Can't connect to internet");
            return "Can't connect to internet";

        }
        System.out.println("Mode: " + mode);
        if (mode.equals("tcp")) {
            String hostname = "iems5722v.ie.cuhk.edu.hk";
            int serverPort = 3001;
            System.out.println("Try connected to " + hostname + " : " + serverPort);
            try {
                Socket client = new Socket(hostname, serverPort);
                System.out.println("Just connected to " + client.getRemoteSocketAddress());
                if (client.isConnected()) {
                    PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                    System.out.println("Send Request to Server: " + input);
                    out.print(input);
                    out.flush();
                    client.setSoTimeout(5000);
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    while (!in.ready()) {
                        ServerResponse = in.readLine();
                        System.out.println("Translate Result: " + ServerResponse);
                        in.close();
                        out.close();
                        client.close();
                        this.activity.storageFile(input, ServerResponse);
                        return ServerResponse;
                    }
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if (mode.equals("http")) {
            String url = "http://iems5722v.ie.cuhk.edu.hk:8080/translate.php?word=" + input;
            System.out.println("URL: " + url);
            HttpClient http_client = new DefaultHttpClient();
            HttpGet request = new HttpGet(url);
            try {
                HttpResponse response = http_client.execute(request);
                System.out.println("Get Server response!");
                HttpEntity entity = response.getEntity();
                ServerResponse = EntityUtils.toString(entity, HTTP.UTF_8);
                this.activity.storageFile(input, ServerResponse);
                return ServerResponse;
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        return null;
    }

    protected void onPostExecute(String result) {
        if (result.equals("Empty Input") || result.equals("Can't connect to internet") ) {
            Toast.makeText(activity, result, Toast.LENGTH_SHORT).show();
            return;
        }
        if (result.equals("Translate Error")) {
            Toast.makeText(activity, result, Toast.LENGTH_SHORT).show();
            activity.t1.setText(result);
            return;
        }
        activity.t1.setText(result);
        return;

    }
}