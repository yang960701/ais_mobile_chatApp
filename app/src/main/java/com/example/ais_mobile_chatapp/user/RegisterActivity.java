package com.example.ais_mobile_chatapp.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    //파이어베이스 인증처리
    private FirebaseAuth nFirebaseAuth;
    //실시간 데이터베이스
    private DatabaseReference nDatabaseRef;

    private EditText ti_Email, ti_Pwd,ti_pWdCheck, ti_UserName;
    private Button btn_Register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nFirebaseAuth = FirebaseAuth.getInstance();
        nDatabaseRef = FirebaseDatabase.getInstance().getReference("ChatAPP");


        ti_Email = findViewById(R.id.ti_email);
        ti_pWdCheck = findViewById(R.id.ti_pwdcheck);
        ti_Pwd = findViewById(R.id.ti_pwd);
        ti_UserName = findViewById(R.id.ti_userName);
        btn_Register = findViewById(R.id.btn_register);

        btn_Register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //회원가입 처리 시작
                String strEmail = ti_Email.getText().toString();
                String strPwd = ti_Pwd.getText().toString();
                String strPwdCheck = ti_pWdCheck.getText().toString();
                String strUserName = ti_UserName.getText().toString();

                if(strEmail.length()>0 && strPwd.length()>0 && strPwdCheck.length()>0) {
                    if (strPwd.equals(strPwdCheck)) {
                //FirebaseAuth 진행
                         nFirebaseAuth.createUserWithEmailAndPassword(strEmail,strPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                             @Override
                             public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<AuthResult> task) {
                                 if (task.isSuccessful()) {
                                     FirebaseUser firebaseUser = nFirebaseAuth.getCurrentUser();
                                     UserAccount account = new UserAccount();
                                     account.setIdToken(firebaseUser.getUid());
                                     account.setEmail(firebaseUser.getEmail());
                                     account.setPassword(strPwd);
                                     account.setUser_name(strUserName);

                                     //setValue -> 데이터베이스에 insert
                                     nDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);

                                     Toast.makeText(RegisterActivity.this, "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();

                                     Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                     startActivity(intent);
                                 } else {

                                     if (task.getException() != null) {
                                         Toast.makeText(RegisterActivity.this, "이미 등록된 이메일이거나 형식에 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                                     }

                                 }
                             }
                         });
                    }else{Toast.makeText(RegisterActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();}
                }else{   Toast.makeText(RegisterActivity.this, "이메일 혹은 비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();}
            }

        });
    }

}




