package com.example.whatstheweather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    TextView AnswerTextView;
    EditText editText;

    public class downloadTask extends AsyncTask<String, Void,String>
    {
        @Override
        protected String doInBackground(String... strings) {
            try
            {
                String result="";
                URL url=new URL(strings[0]);
                HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();
                InputStream in=urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while(data!=-1)
                {
                    char ch=(char)data;
                    result+=ch;
                    data=reader.read();
                }
                return result;
            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(),"COUND NOT FIND WEATHER",Toast.LENGTH_LONG).show();
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject=new JSONObject(s);
                String weather=jsonObject.getString("weather");
                JSONArray array=new JSONArray(weather);
                String finalMessage="";
                for(int k=0;k<array.length();k++)
                {
                    JSONObject smallobject=array.getJSONObject(k);
                    String main=smallobject.getString("main");
                    String description=smallobject.getString("description");
                    if(!main.equals("") && !description.equals(""))
                    {
                        finalMessage+=smallobject.getString("main")+": "+smallobject.getString("description")+"\n";
                    }
                }
                if(!finalMessage.equals(""))
                {
                    AnswerTextView.setText(finalMessage);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"COUND NOT FIND WEATHER",Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(),"COUND NOT FIND WEATHER",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }

    }
    public void clickFunction(View view)
    {
        downloadTask Task=new downloadTask();
        try {
            String result=URLEncoder.encode(editText.getText().toString(),"UTF-8");
            Task.execute("https://openweathermap.org/data/2.5/weather?q=" + result + "&appid=b6907d289e10d714a6e88b30761fae22").get();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"COUND NOT FIND WEATHER",Toast.LENGTH_LONG).show();
        }
        InputMethodManager mgr=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(editText.getWindowToken(),0);
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AnswerTextView=(TextView)findViewById(R.id.AnswertextView);
        editText=(EditText)findViewById(R.id.editText);
    }
}
