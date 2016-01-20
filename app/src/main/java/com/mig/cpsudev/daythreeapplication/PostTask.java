package com.mig.cpsudev.daythreeapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.DialogPreference;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Mig on 20-Jan-16.
 */
public class PostTask extends AsyncTask<String, Void, String> {

    private Context mContext;
    private String mAnswer;
    private ProgressDialog progressDialog;

    public PostTask(Context mContext, String mAnswer) {
        this.mContext = mContext;
        this.mAnswer = mAnswer;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(mContext, "Please wait", "Sending Request ...", true);
    }

    @Override
    protected String doInBackground(String... urls) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("content", mAnswer);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json = jsonObject.toString();

        OkHttpClient okHttpClient = new OkHttpClient();

        Request.Builder builder = new Request.Builder();
        Request request = builder
                .url(urls[0])
                .post(RequestBody.create(JSON, json))
                .build();

        String responseText = "";
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                responseText = response.body().string();
            } else {
                responseText = "Not Success - Code : " + response.code();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        progressDialog.dismiss();
        return responseText;
    }

    @Override
    protected void onPostExecute(String feedbackResult) {
        super.onPostExecute(feedbackResult);
        if (feedbackResult.equals("{\"check_sum\":true}")) {
            new AlertDialog.Builder(mContext)
                    .setTitle("Connected")
                    .setCancelable(false)
                    .setMessage("Go to Next Page ?")
                    .setPositiveButton("Go", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(mContext, ControlActivity.class);
                            mContext.startActivity(i);
                        }
                    })
                    .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        } else {
            new AlertDialog.Builder(mContext)
                    .setTitle("Not Connect")
                    .setCancelable(false)
                    .setMessage("Wrong Urlt\nPlease Try Again")
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }
}
