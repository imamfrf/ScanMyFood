package com.scanmyfood.imamf.scanmyfood.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.scanmyfood.imamf.scanmyfood.Model.User
import com.scanmyfood.imamf.scanmyfood.R
import com.scanmyfood.imamf.scanmyfood.extension.toast
import com.scanmyfood.imamf.scanmyfood.util.Constant
import com.scanmyfood.imamf.scanmyfood.util.Constant.DEFAULT.DEFAULT_NOT_SET
import kotlinx.android.synthetic.main.activity_register.*
import java.text.SimpleDateFormat
import java.util.*


class RegisterActivity : AppCompatActivity() {

    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mDatabaseReference: DatabaseReference
    var cal = Calendar.getInstance()
    var et_tglLhr: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mDatabaseReference = FirebaseDatabase.getInstance().reference.child(Constant.CHILD.CHILD_USERS)
        mFirebaseAuth = FirebaseAuth.getInstance()

        buttonRegister.setOnClickListener {
            validateInput()
        }

        textViewLogin.setOnClickListener {
            if (mFirebaseAuth.currentUser != null)
                FirebaseAuth.getInstance().signOut()
            finish()
        }

        // create an OnDateSetListener
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }

        // when you click on the button, show DatePickerDialog that is set with OnDateSetListener
        editTextTanggalLahir.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(this@RegisterActivity,
                        dateSetListener,
                        // set DatePickerDialog to point to today's date when it loads up
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show()
            }

        })
    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        editTextTanggalLahir.setText(sdf.format(cal.getTime()))
    }


    // Check input field and register if valid
    private fun validateInput() {
        val email = editTextEmail.text.toString().trim()
        val nama = editTextNamaLengkap.text.toString().trim()
        val tanggalLahir = editTextTanggalLahir.text.toString().trim()
        val tinggiBadan = editTextTinggiBadan.text.toString()
        val beratBadan = editTextBeratBadan.text.toString()
        val password = editTextPassword.text.toString().trim()
        val confirmPassword = editTextKonfirmasiPassword.text.toString().trim()
        val checkedRadioButtonId = radioGroupJenisKelamin.checkedRadioButtonId
        val jenisKelaminRadioButton = findViewById<RadioButton>(checkedRadioButtonId)
        val jenisKelamin = jenisKelaminRadioButton.text.toString().trim()

        if (inputDataNotEmpty(email, nama, password, confirmPassword, tanggalLahir, tinggiBadan, beratBadan, jenisKelamin)) {
            val usia = calculateAge(tanggalLahir)
            val kebutuhanKalori = calculateCalories(jenisKelamin, tinggiBadan.toDouble(), beratBadan.toDouble(), usia)
            if (password.equals(confirmPassword)) {
                registerUser(email, nama, password, tanggalLahir, tinggiBadan.toDouble(), beratBadan.toDouble(), jenisKelamin, usia, kebutuhanKalori)
            } else {
                toast("Kata sandi tidak cocok")
            }
        } else {
            toast("Data tidak lengkap")
        }
    }

    // Register user to firebase
    private fun registerUser(email: String, nama: String, password: String, tanggalLahir: String, tinggiBadan: Double, beratBadan: Double, jenisKelamin: String, usia: Int, kebutuhanKalori: Double) {
        startLoadingIndicator()
        mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                toast("Register berhasil")
                val userId = mFirebaseAuth.currentUser!!.uid
                val photoUrl = DEFAULT_NOT_SET
                val newUser = User(userId, nama, email, tanggalLahir, jenisKelamin, tinggiBadan, beratBadan, kebutuhanKalori, usia, photoUrl)
                // send user data to firebase database
                mDatabaseReference.child(userId).setValue(newUser).addOnCompleteListener {
                    stopLoadingIndicator()
                    sendEmailVerification()
                    FirebaseAuth.getInstance().signOut()
                }
            } else {
                stopLoadingIndicator()
                val errorCode = (task.exception as FirebaseAuthException).errorCode
                when (errorCode) {
//                    "ERROR_WEAK_PASSWORD" ->
//                        toast("Password harus memiliki minimal 6 karakter")
                    "ERROR_EMAIL_ALREADY_IN_USE" ->
                        toast("Alamat email sudah digunakan")
                    else ->
                        toast("" + (task.exception as FirebaseAuthException).message)
                }
            }
        }
    }

    // Send email verification to user
    private fun sendEmailVerification() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.sendEmailVerification()?.addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val snackbar = Snackbar
                        .make(constraintLayout, "Email verifikasi dikirim ke ${user.email}", Snackbar.LENGTH_LONG)
                        .setAction("BUKA EMAIL") {
                            val intent = Intent(Intent.ACTION_MAIN)
                            intent.addCategory(Intent.CATEGORY_APP_EMAIL)
                            startActivity(Intent.createChooser(intent, "Choose email client"))
                        }
                snackbar.show()
            } else {
                Toast.makeText(baseContext,
                        "Gagal mengirim email verifikasi",
                        Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun inputDataNotEmpty(email: String, nama: String, password: String, confirmPassword: String, tanggalLahir: String, tinggiBadan: String, beratBadan: String, jenisKelamin: String): Boolean {
        return !(TextUtils.isEmpty(email) || TextUtils.isEmpty(nama) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) ||
                TextUtils.isEmpty(tanggalLahir) || TextUtils.isEmpty(tinggiBadan) || TextUtils.isEmpty(beratBadan) || TextUtils.isEmpty(jenisKelamin))
    }

    // Progress bar
    private fun startLoadingIndicator() {
        aviLoadingIndicatorView.smoothToShow()
    }

    private fun stopLoadingIndicator() {
        aviLoadingIndicatorView.smoothToHide()
    }

    private fun calculateAge(tanggalLahir: String): Int {
        val currentDate = Calendar.getInstance().time
        val birthDate = SimpleDateFormat("dd/MM/yyyy").parse(tanggalLahir)
        val formatter = SimpleDateFormat("yyyyMMdd")
        val date1 = Integer.parseInt(formatter.format(birthDate))
        val date2 = Integer.parseInt(formatter.format(currentDate))
        return (date2 - date1) / 10000
    }

    private fun calculateCalories(jenisKelamin: String, tinggiBadan: Double, beratBadan: Double, usia: Int): Double {
        if (jenisKelamin.equals("Laki-Laki")) {
            val bmr = 88.362 + (13.397 * beratBadan) + (4.799 * tinggiBadan) - (5.677 * usia)
            val tee = bmr * 1.375
            return tee
        } else {
            val bmr = 447.593 + (9.247 * beratBadan) + (3.098 * tinggiBadan) - (4.33 * usia)
            val tee = bmr * 1.375
            return tee
        }
    }
}
