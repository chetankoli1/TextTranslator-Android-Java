package project.chetankoli.textlanguagetranslator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import project.chetankoli.textlanguagetranslator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    static String url = "https://script.google.com/macros/s/AKfycbxkQeDET0ZTCZrWaAz1TRIy677dVaaSi-5FT2GYHeTbTLIaCtlx-_8HHHxLXYxZ3e-cbw/exec";
    public ActivityMainBinding binding;
    String languageToLoad = "";
    String[] languages = {"Hindi","English","French","Japanese","Sanskrit"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayAdapter ad
                = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                languages);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.langToConvert.setOnItemSelectedListener(this);
        binding.langToConvert.setAdapter(ad);


        binding.translateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                convertText();
            }
        });

    }

    public void convertText(){
        binding.pb.setVisibility(View.VISIBLE);
        binding.convertedText.setVisibility(View.GONE);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        binding.pb.setVisibility(View.GONE);
                        binding.convertedText.setVisibility(View.VISIBLE);
                        try {
                            JSONObject object = new JSONObject(response);
                            String msg = object.getString("status");
                            String convertedText = object.getString("translatedText");
                            binding.convertedText.setText(convertedText);
                            Toast.makeText(MainActivity.this, convertedText, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                binding.pb.setVisibility(View.GONE);
                binding.convertedText.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map params = new HashMap();
                params.put("source_lang", "auto");
                params.put("target_lang", languageToLoad);
                params.put("text",binding.editText.getText().toString());

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(request);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();
        if(item.equals("Hindi")){
            languageToLoad="hi";
        }else if(item.equals("English")){
            languageToLoad="en";
        }else if(item.equals("French")){
            languageToLoad="fr";
        }else if(item.equals("Japanese")){
            languageToLoad="de";
        }else if(item.equals("Sanskrit")){
            languageToLoad="bho";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}