package com.example.ais_mobile_chatapp.friend;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ais_mobile_chatapp.user.UserAccount;
import com.example.chatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.N)
public class RequestAddFriendListAdapter extends RecyclerView.Adapter<RequestAddFriendListAdapter.RequestAddFriendListHolder> {

    private ArrayList<UserAccount> requestAddFriendList;
    private RequestAddFriendListHolder requestAddFriendListHolder;
    private FirebaseFirestore nFirestore;
    private UserAccount currentUserAccount;

    public RequestAddFriendListAdapter(ArrayList<UserAccount> requestAddFriendList, FirebaseFirestore nFirestore, UserAccount currentUserAccount) {

        this.requestAddFriendList = requestAddFriendList;
        this.nFirestore = nFirestore;
        this.currentUserAccount = currentUserAccount;

    }

    @NonNull
    @NotNull
    @Override
    public RequestAddFriendListAdapter.RequestAddFriendListHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_request_add_friend_list,parent,false);
        requestAddFriendListHolder = new RequestAddFriendListHolder(holderView);

        return requestAddFriendListHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RequestAddFriendListAdapter.RequestAddFriendListHolder holder, int position) {
        UserAccount userAccount = requestAddFriendList.get(position);

        requestAddFriendListHolder.tv_request_add_friend_email.setText(userAccount.getEmail());
        requestAddFriendListHolder.tv_request_add_friend_name.setText(userAccount.getUser_name());

        requestAddFriendListHolder.btn_request_add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("requestAddFriendLis", currentUserAccount+"");

                nFirestore.collection("relation")
                        .document(currentUserAccount.getEmail())
                        .update("request_add_friend_list", FieldValue.arrayRemove(userAccount)
                            ,"friend_list", FieldValue.arrayUnion(userAccount))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if(!task.isSuccessful()){
                                    Log.e("btn_request_add_friend", "but not successful");
                                }else{
                                    Log.e("btn_request_add_friend", "successful");

                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Log.e("failure", e.getMessage());
                            }
                        });

                nFirestore.collection("relation")
                        .document(userAccount.getEmail())
                        .update("friend_list", FieldValue.arrayUnion(currentUserAccount))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if(!task.isSuccessful()){
                                    Log.e("btn_request_add_friend", "but not successful");
                                }else{
                                    Toast.makeText(v.getContext(), "친구요청 성공!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Log.e("failure", e.getMessage());
                            }
                        });

                requestAddFriendList.remove(position);
                notifyItemRemoved(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(requestAddFriendList != null) {
            return requestAddFriendList.size();
        }
        return 0;
    }

    public class RequestAddFriendListHolder extends RecyclerView.ViewHolder {

        public TextView tv_request_add_friend_email, tv_request_add_friend_name;
        public Button btn_request_add_friend;

        public RequestAddFriendListHolder(@NonNull View itemView) {
            super(itemView);
            tv_request_add_friend_email = itemView.findViewById(R.id.tv_request_add_friend_email);
            tv_request_add_friend_name = itemView.findViewById(R.id.tv_request_add_friend_name);
            btn_request_add_friend = itemView.findViewById(R.id.btn_request_add_friend);


        }


    }
}
