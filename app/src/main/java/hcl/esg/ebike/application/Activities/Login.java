package hcl.esg.ebike.application.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import hcl.esg.ebike.application.R;

public class Login extends AppCompatActivity {
    TextInputEditText editTextEmail, editTextPassword;
    Button signin_btn;
    TextView signup_btn;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            checkUserRole();
        }
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        signin_btn = findViewById(R.id.signin_btn);
        signup_btn = findViewById(R.id.signup_btn);

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                finish();
            }
        });
        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Login.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            DatabaseReference fieldRef = FirebaseDatabase.getInstance().getReference("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                            fieldRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    String empId = snapshot.child("emp_id").getValue(String.class);
                                    String userRole = snapshot.child("userRole").getValue(String.class);
                                    // Storing data
                                    SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("empId", empId);
                                    editor.putString("userRole", userRole);
                                    editor.apply();

                                    checkUserRole();

                                    // Handle the retrieved data

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                    Log.e("FirebaseData", "Error reading data");
                                }


                            });

                            Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, UserNavigation.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Login.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }
    public void checkUserRole(){
        SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        String userRole = sharedPreferences.getString("userRole",null);
        if(Objects.equals(userRole, "Admin")){
            Toast.makeText(Login.this, "Login as admin Successful", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Login.this, AdminApp.class);
            startActivity(intent);
            finish();

        }
        else if (Objects.equals(userRole, "Security")){
            Toast.makeText(Login.this, "Login as security Successful", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Login.this, SecurityNavigation.class);
            startActivity(intent);
            finish();

        }
        else{
            Toast.makeText(Login.this, "Login  Successful", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Login.this, UserNavigation.class);
            startActivity(intent);
            finish();



        }


    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Login.this);
        alertDialog.setTitle("Exit App");
        alertDialog.setMessage("Do you want to exit app?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }




}