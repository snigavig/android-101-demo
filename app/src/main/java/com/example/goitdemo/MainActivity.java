package com.example.goitdemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private TextView mQuote;
    private TextView mAuthor;
    private LinearLayout mContent;
    private ProgressBar mProgressBar;
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mContent = (LinearLayout) findViewById(R.id.content);
        mQuote = (TextView) findViewById(R.id.quoteTextView);
        mAuthor = (TextView) findViewById(R.id.authorTextView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getQuote();
            }
        });
        getQuote();
    }

    private void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
        mContent.setVisibility(View.INVISIBLE);
    }

    private void hideProgress() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mContent.setVisibility(View.VISIBLE);
    }

    private void getQuote(){
        showProgress();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://api.forismatic.com/api/1.0/?method=getQuote&format=json&lang=en";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    Log.e("debug", response);
                    updateView(obj.getString("quoteText"), obj.getString("quoteAuthor"));
                    hideProgress();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgress();
                Snackbar.make(fab, "Error!", Snackbar.LENGTH_LONG).show();
            }
        });
        queue.add(request);
    }

    private void updateView(String quote, String author) {
        mQuote.setText(quote);
        if ("".equals(author)) {
            author = "Anonimous";
        }
        mAuthor.setText(author);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
