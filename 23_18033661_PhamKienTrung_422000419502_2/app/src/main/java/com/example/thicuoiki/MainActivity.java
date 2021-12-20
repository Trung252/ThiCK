package com.example.thicuoiki;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    SignInButton signInButton;
    Button login;
    EditText edtUser,edtPass;
    public static final String BASE_URL = "https://61c03d2d33f24c00178231f2.mockapi.io/Bank";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        edtUser = findViewById(R.id.edtEmail);
        edtPass = findViewById(R.id.edtPassword);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        edtUser.setText("Trung");
        edtPass.setText("123");
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.sign_in_button:
                        signIn();

                        break;
                    // ...
                }
            }
        });
        login=findViewById( R.id.btnlogin );
        login.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginfun();



            }
        } );

    }
    private void loginfun() {

        String username = edtUser.getText().toString();
        String password = edtPass.getText().toString();
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BASE_URL, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                int y = 0;

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = (JSONObject) response.get(i) ;
                        String u = object.getString("Email");
                        String p = object.getString("Password");

                        if(u.matches(username) && p.matches(password)){
                            y++;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(y != 0){
                    startActivity(new Intent(MainActivity.this, TransferScreen.class));
                    Toast.makeText(MainActivity.this, "Welcome " + username, Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Quá nhiều yêu cầu xin thử lại", Toast.LENGTH_SHORT).show();
                System.out.println(error.getMessage());
            }
        });
        requestQueue.add(jsonArrayRequest);

    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 1);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 1) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult( ApiException.class);
            Log.w("acc", account.getDisplayName() +" "+ account.getEmail()+" "+account.getId());
            Intent intent=new Intent(MainActivity.this,TransferScreen.class);
            startActivity( intent );

        } catch (ApiException e) {
        }
    }
}