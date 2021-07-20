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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.N)
public class RequestAddFriendListAdapter extends RecyclerView.Adapter<RequestAddFriendListAdapter.RequestAddFriendListHolder> {

    private ArrayList<UserAccount> requestAddFriendList;
    private RequestAddFriendListHolder requestAddFriendListHolder;
    private UserAccount currentUserAccount;
    private DatabaseReference nDatabaseRef;

    public RequestAddFriendListAdapter(ArrayList<UserAccount> requestAddFriendList, UserAccount currentUserAccount) {
        this.requestAddFriendList = requestAddFriendList;
        this.currentUserAccount = currentUserAccount;
    }

    @NonNull
    @NotNull
    @Override
    public RequestAddFriendListAdapter.RequestAddFriendListHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_request_add_friend_list,parent,false);
        requestAddFriendListHolder = new RequestAddFriendListHolder(holderView);

        nDatabaseRef = FirebaseDatabase.getInstance().getReference("ChatAPP");
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

                nDatabaseRef.child("Relation")
                        .child(currentUserAccount.getIdToken())
                        .child("RequestAddFriendList")
                        .child(userAccount.getIdToken())
                        .removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Log.e("RequestAddFriendList", "remove successs");
                                }
                            }
                        });

                Map<String,Object> updates = new HashMap<>();
                updates.put(currentUserAccount.getIdToken()+"/FriendList/"+userAccount.getIdToken(), userAccount);
                updates.put(userAccount.getIdToken()+"/FriendList/"+currentUserAccount.getIdToken(), currentUserAccount);

                nDatabaseRef.child("Relation")
                        .updateChildren(updates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if(!task.isSuccessful()){
                                    Log.e("complete", "but not successful");
                                }else{
                                    requestAddFriendList.remove(position);
                                    notifyItemRemoved(position);
                                    Toast.makeText(v.getContext(), "친구추가 성공!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Log.e("failure", e.getMessage());
                                Toast.makeText(v.getContext(), "친구추가 실패, 다시 한번 확인하시오", Toast.LENGTH_SHORT).show();
                            }
                        });
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
