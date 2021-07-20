package com.example.ais_mobile_chatapp.user;

import java.io.Serializable;
import java.util.HashMap;

//사용자 계정 정보 모델 클래스
public class UserAccount implements Serializable {
    private String idToken; //FirebaseUid(고유 정보)
    private String email;
    private String password;
    private String user_name;

    public UserAccount(){}//파이어베이스에서 이 생성자 안만들어주면 오류가 난다.

    public UserAccount(String idToken, String email) {
        this.idToken = idToken;
        this.email = email;
    }

    public UserAccount(String idToken, String email, String user_name) {
        this.idToken = idToken;
        this.email = email;
        this.user_name = user_name;
    }

    public UserAccount(String idToken, String email, String password, String user_name) {
        this.idToken = idToken;
        this.email = email;
        this.password = password;
        this.user_name = user_name;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "idToken='" + idToken + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", user_name='" + user_name + '\'' +
                '}';
    }

    public HashMap<String,Object> toMap(){
        HashMap<String,Object> map = new HashMap<>();
        map.put("idToken", idToken);
        map.put("email", email);
        map.put("password", password);
        map.put("user_name", user_name);

        return map;
    }
}
