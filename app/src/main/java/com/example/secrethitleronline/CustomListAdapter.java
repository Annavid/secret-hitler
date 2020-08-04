package com.example.secrethitleronline;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter {

    private final Activity context;
    private final ArrayList<String> nameArray;
    private final ArrayList<String> infoArray;

    CustomListAdapter(Activity context1, ArrayList<String> nameArray, ArrayList<String> infoArray) {
        super(context1, R.layout.list_row_layout);
        this.context = context1;
        this.nameArray = nameArray;
        this.infoArray = infoArray;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"}) View rowView = inflater.inflate(R.layout.list_row_layout, null,true);

        TextView nameTextField = rowView.findViewById(R.id.textView3);
        TextView infoTextField = rowView.findViewById(R.id.textView2);

        nameTextField.setText(getString(nameTextField.getText().toString(), nameArray.get(position)));
        infoTextField.setText(getString(infoTextField.getText().toString(), infoArray.get(position)));

        return rowView;

    }

    private String getString(String toString, String s) {
        return toString + s;
    }

}
