package com.scanmyfood.imamf.scanmyfood.pattern;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SingletonFirebase {

    public static FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    public static FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

    private static final SingletonFirebase ourInstance = new SingletonFirebase();

    public static SingletonFirebase getInstance() {
        return ourInstance;
    }

    private SingletonFirebase() {
    }
}
