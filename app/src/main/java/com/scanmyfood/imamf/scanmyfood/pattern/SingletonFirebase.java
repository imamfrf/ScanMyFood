package com.scanmyfood.imamf.scanmyfood.pattern;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SingletonFirebase {

    private static final SingletonFirebase ourInstance = new SingletonFirebase();

    public FirebaseDatabase firebaseDatabase;
    public FirebaseAuth firebaseAuth;

    public static SingletonFirebase getInstance() {
        return ourInstance;
    }

    private SingletonFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }
}
