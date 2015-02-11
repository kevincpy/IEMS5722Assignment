package com.iems5722.translateapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Kevin on 11/2/15.
 */
public class DeleteHistoryDialog extends DialogFragment{
    @Override

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.delete_history);
        builder.setMessage(R.string.delete_history_dialog)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        String FILENAME = "translate_history";
                        ArrayList<String> record_list;
                        Bundle args = getArguments();
                        String value = args.getString("record");
                        record_list = args.getStringArrayList("record_list");
                        System.out.println("\"" + value + "\"" + " Deleted!");

                        try {
                            record_list.remove(value);
                            System.out.println("record_list\n"+record_list);
                            File file = new File(FILENAME);
                            FileOutputStream fileOutputStream = new FileOutputStream(file, false);

                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                            objectOutputStream.writeObject(record_list);
                            objectOutputStream.close();
                            fileOutputStream.close();
                            Toast.makeText(getActivity(), "\"" + value + "\"" + " Deleted!", Toast.LENGTH_SHORT).show();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
