package edu.ucsd.cse110.team19.walkwalkrevolution.team;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import static androidx.core.content.ContextCompat.getSystemService;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String senderEmail = intent.getStringExtra("response");
        setSenderFriendStatus(senderEmail);
        setReceiverFriendStatus(senderEmail);
        mergeTeams(senderEmail);
        mergeTeamsRoutes(senderEmail);
    }

    private void mergeTeamsRoutes(String senderEmail) {
        CollectionReference senderRoutes = FirebaseFirestore.getInstance()
                .collection("Users")
                .document(senderEmail)
                .collection("Routes");

        CollectionReference receiverRoutes = FirebaseFirestore.getInstance()
                .collection("Users")
                .document(getUserEmail())
                .collection("Routes");

        CollectionReference senderTeamRoutes = FirebaseFirestore.getInstance()
                .collection("Users")
                .document(senderEmail)
                .collection("Team Routes");

        CollectionReference receiverTeamRoutes = FirebaseFirestore.getInstance()
                .collection("Users")
                .document(getUserEmail())
                .collection("Team Routes");

        senderRoutes.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        String id = document.getId();
                        receiverTeamRoutes.document(id).set(document.getData());
                    }
                }
            }
        });

        receiverRoutes.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        String id = document.getId();
                        senderTeamRoutes.document(id).set(document.getData());
                    }
                }
            }
        });
    }

    private String getUserEmail() {
        return FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }

    private void setSenderFriendStatus(String senderEmail){
        CollectionReference senderList = FirebaseFirestore.getInstance()
                .collection("Users")
                .document(senderEmail)
                .collection("Teammates");
        senderList.document(getUserEmail()).update("friendStatus",true);
    }

    private void setReceiverFriendStatus(String senderEmail){
        CollectionReference receiverList = FirebaseFirestore.getInstance()
                .collection("Users")
                .document(getUserEmail())
                .collection("Teammates");
        receiverList.document(senderEmail).update("friendStatus",true);
    }

    private void mergeTeams(String senderEmail){

        CollectionReference senderList = FirebaseFirestore.getInstance()
                .collection("Users")
                .document(senderEmail)
                .collection("Teammates");

        CollectionReference receiverList = FirebaseFirestore.getInstance()
                .collection("Users")
                .document(getUserEmail())
                .collection("Teammates");




        senderList.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        String id = document.getId();
                        Boolean friendStatus = (Boolean) document.get("friendStatus");
                        if( friendStatus && (id.compareTo(getUserEmail())!=0)) {
                            receiverList.document(id).set(document.getData());
                        }
                    }
                }
            }
        });



        receiverList.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) { //each document is a teammate member
                        String id = document.getId(); // get teammate email
                        Boolean friendStatus = (Boolean) document.get("friendStatus");
                        if (friendStatus && (id.compareTo(senderEmail)!=0)) {
                            senderList.document(id).set(document.getData()); // set sender's list

                        }
                    }
                }
            }
        });

    }

}
