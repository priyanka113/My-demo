package com.piktale.assist;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;


public class AlertHelper {

    private Context con;
    private View dialoglayout;

    public AlertHelper(Context con) {
        this.con = con;

    }

    public AlertDialog.Builder createDialog(int resource) {
        LayoutInflater inflater = (LayoutInflater) con
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialoglayout = inflater.inflate(resource, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(con);
        builder.setView(dialoglayout);

        return builder;
    }

    public AlertDialog.Builder createProgress(int resource) {
        LayoutInflater inflater = (LayoutInflater) con
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialoglayout = inflater.inflate(resource, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(con);
        builder.setView(dialoglayout);
        return builder;
    }


    public View getView() {
        return dialoglayout;
    }




}
