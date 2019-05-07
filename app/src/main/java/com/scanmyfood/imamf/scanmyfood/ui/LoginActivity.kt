package com.scanmyfood.imamf.scanmyfood.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.scanmyfood.imamf.scanmyfood.Model.User
import com.scanmyfood.imamf.scanmyfood.R
import com.scanmyfood.imamf.scanmyfood.extension.toast
import com.scanmyfood.imamf.scanmyfood.pattern.SingletonFirebase
import com.scanmyfood.imamf.scanmyfood.util.Constant
import com.scanmyfood.imamf.scanmyfood.util.PreferenceHelper
import com.scanmyfood.imamf.scanmyfood.util.PreferenceHelper.set
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var mSingletonFirebase: SingletonFirebase
    private lateinit var mFirebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mSingletonFirebase = SingletonFirebase.getInstance()
        mFirebaseAuth = mSingletonFirebase.firebaseAuth

        viewDimScreen.visibility = View.GONE

        buttonLogin.setOnClickListener {
            loginUser()
        }

        textViewDaftar.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    // Login user
    private fun loginUser() {
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString().trim()
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            startLoadingIndicator()
            mFirebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        stopLoadingIndicator()
                        if (task.isSuccessful) {
                            checkIfEmailVerified()
                        } else {
                            val errorCode = (task.exception as FirebaseAuthException).errorCode
                            when (errorCode) {
                                "ERROR_WRONG_PASSWORD" ->
                                    toast("Alamat email/password Anda salah")
                                "ERROR_USER_NOT_FOUND" ->
                                    toast("Akun belum terdaftar")
                                else ->
                                    toast("" + (task.exception as FirebaseAuthException).message)
                            }
                        }
                    }
        } else if (email.isEmpty() && password.isEmpty()) {
            toast("Mohon masukkan email dan password")
        } else if (email.isEmpty()) {
            toast("Mohon masukkan email")
        } else if (password.isEmpty()) {
            toast("Mohon masukkan password")
        }
    }

    // Get user's data
    private fun getUserData() {
        val userReference = FirebaseDatabase.getInstance().reference.child(Constant.CHILD.CHILD_USERS)
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userReference.child(userId!!).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(User::class.java)
                val nama = user?.nama
                val email = user?.email
                val sharedPreferences = PreferenceHelper.defaultPrefs(this@LoginActivity)
                sharedPreferences["nama"] = nama
                sharedPreferences["email"] = email
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in
        val currentUser = mFirebaseAuth.currentUser
        if (currentUser != null) {
            currentUser.let {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }

    // Check if email has been verified
    private fun checkIfEmailVerified() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user!!.isEmailVerified) {
            Toast.makeText(this@LoginActivity, "Login berhasil", Toast.LENGTH_SHORT).show()
            getUserData()
        } else {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this@LoginActivity, "Email belum terverifikasi, silakan cek email Anda.", Toast.LENGTH_SHORT).show()
        }
    }

    // Progress bar
    private fun startLoadingIndicator() {
        viewDimScreen.visibility = View.VISIBLE
        aviLoadingIndicatorView.smoothToShow()
    }

    private fun stopLoadingIndicator() {
        viewDimScreen.visibility = View.GONE
        aviLoadingIndicatorView.smoothToHide()
    }
}