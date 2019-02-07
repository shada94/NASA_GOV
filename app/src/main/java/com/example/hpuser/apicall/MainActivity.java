package com.example.hpuser.apicall;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    // we"ll make HTTP request to this URL to retrieve weather conditions
    String nasaGovUrl = "https://api.nasa.gov/planetary/apod?api_key=9ia3stF4MRf8hrwppRKgvBgUbNd2tPxxcfzybeuG";
    //the loading Dialog
    ProgressDialog pDialog;
    TextView copyright, date, url, title;
    TextView explanation;
    ImageView imgurl;

    JSONObject jsonObj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        copyright = (TextView) findViewById(R.id.copyright);
        date = (TextView) findViewById(R.id.date);
        explanation = (TextView) findViewById(R.id.explanation);
        url = (TextView) findViewById(R.id.url);
        title = (TextView) findViewById(R.id.title);
        imgurl = (ImageView) findViewById(R.id.urlimg);






        // prepare the loading Dialog
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait while retrieving the informations required");
        pDialog.setCancelable(false);

        // Check if Internet is working
        if (!isNetworkAvailable(this)) {
            // Show a message to the user to check his Internet
            Toast.makeText(this, "Please check your Internet connection", Toast.LENGTH_LONG).show();
        } else {

            pDialog.show();
            // make HTTP request to retrieve the informations
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    nasaGovUrl, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        // Parsing json object response
                        // response will be a json object


                        jsonObj = (JSONObject) response;
                        copyright.setText(jsonObj.getString("copyright"));
                        date.setText(jsonObj.getString("date"));
                        explanation.setText(jsonObj.getString("explanation"));
                        url.setText(jsonObj.getString("url"));
                        title.setText(jsonObj.getString("title"));
                        Picasso.get().load(url.getText().toString()).into(imgurl);


//                        // We'll use the Glide library
//                        Glide
//                                .with(getApplicationContext())
//                                .load(url)
//                                .centerCrop()
//                                .crossFade()
//                                .listener(new RequestListener<String, GlideDrawable>() {
//                                    @Override
//                                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                                        System.out.println(e.toString());
//                                        return false;
//                                    }
//
//                                    @Override
//                                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                                        return false;
//                                    }
//                                })
//                                .into(imgurl);
//// hide the loading Dialog
                        pDialog.dismiss();


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error , try again ! ", Toast.LENGTH_LONG).show();
                        pDialog.dismiss();

                    }


                }


            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
//                    VolleyLog.d("tag", "Error: " + error.getMessage());
                 Toast.makeText(getApplicationContext(), "Error while loading ... ", Toast.LENGTH_SHORT).show();
                    // hide the progress dialog
                    pDialog.dismiss();
                }
            });

            // Adding request to request queue
            AppController.getInstance(this).addToRequestQueue(jsonObjReq);


        }
    }






    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }


}