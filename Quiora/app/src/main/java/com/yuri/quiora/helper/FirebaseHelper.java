package com.yuri.quiora.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {
    private static FirebaseAuth auth;
    private static FirebaseUser user;

    public static DatabaseReference getReference(){
        return FirebaseDatabase.getInstance().getReference();
    }

    public static DatabaseReference getChatRoomReference(){
        return getReference().child("chatRooms");
    }

    public static DatabaseReference getReferenceVoo(){
        return getReference().child("voos");
    }

    public static DatabaseReference getReferenceUsuarios(){
        return getReference().child("usuarios");
    }

    public static String getUserUid(){
        return getCurrentUser().getUid();
    }

    public static FirebaseUser getCurrentUser(){
        if(auth == null){
            auth = FirebaseAuth.getInstance();
        }
        return auth.getCurrentUser();
    }

    public static FirebaseAuth getAuth(){
        if(auth == null){
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }
}