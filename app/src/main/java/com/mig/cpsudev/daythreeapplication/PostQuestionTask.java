package com.mig.cpsudev.daythreeapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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
public class PostQuestionTask extends AsyncTask<String, Void, String> {

    private String questionId;
    private Context mContext;
    private TextView status;
    private ProgressDialog progressDialog;

    public PostQuestionTask(Context mContext, String questionId) {
        this.questionId = questionId;
        this.mContext = mContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(mContext, "Sending Request", "Please wait ...", true);
    }

    @Override
    protected String doInBackground(String... urls) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("number", questionId);
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
    protected void onPostExecute(String feedBackResult) {
        super.onPostExecute(feedBackResult);
        String title = "";
        if (questionId.equals("-1")){
            title = "Now Stop";
        }else {
            title = "Now Showing";
        }
        if (feedBackResult.equals("{\"check_sum\":true}")) {
            new AlertDialog.Builder(mContext)
                    .setTitle(title)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        } else {
            new AlertDialog.Builder(mContext)
                    .setTitle("Cannot Show")
                    .setCancelable(false)
                    .setMessage("Question doesn't exist")
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
