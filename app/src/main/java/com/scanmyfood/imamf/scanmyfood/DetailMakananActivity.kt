package com.scanmyfood.imamf.scanmyfood

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.scanmyfood.imamf.scanmyfood.Model.List_Makanan
import com.scanmyfood.imamf.scanmyfood.util.Constant.CHILD.CHILD_MAKANAN
import com.scanmyfood.imamf.scanmyfood.util.Constant.DEFAULT.DEFAULT_NOT_SET
import com.scanmyfood.imamf.scanmyfood.util.Constant.KEY.KEY_ID_MAKANAN
import com.scanmyfood.imamf.scanmyfood.util.Constant.KEY.KEY_NAMA_MAKANAN
import kotlinx.android.synthetic.main.activity_detail_makanan.*

class DetailMakananActivity : AppCompatActivity() {

    private var mExtras: Bundle? = null
    private var mEventId: String? = null
    private var mEventName: String? = null
    private var mFirebaseAuth: FirebaseAuth? = null
    private var mFirebaseUser: FirebaseUser? = null
    private var mFirebaseDatabase: FirebaseDatabase? = null
    private var mDatabaseReference: DatabaseReference? = null
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_makanan)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        mExtras = intent?.extras
        mEventName = mExtras?.getString(KEY_NAMA_MAKANAN)
        collapse_toolbar.title = mEventName
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        setupFirebase()
        mEventId = mExtras?.getString(KEY_ID_MAKANAN)
        if (mEventId != null) {
            fetchEvent(mEventId!!)
            mapFragment.getMapAsync {
                mMap = it
                val lat = mExtras?.getDouble("lat")
                val lng = mExtras?.getDouble("lng")
                val location = LatLng(lat!!, lng!!)
                mMap.addMarker(MarkerOptions().position(location).title("Marker in Location"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupFirebase() {
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseUser = mFirebaseAuth?.currentUser
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mFirebaseDatabase?.reference
    }

    private fun fetchEvent(id: String) {
        mDatabaseReference?.child(CHILD_MAKANAN)?.child(id)
                ?.addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        val makanan = p0.getValue(List_Makanan::class.java)
                        textViewEventName.text = makanan?.namaMakanan
                        textViewNamaKatering.text = makanan?.namaCatering
                        textViewTeleponKatering.text = makanan?.nomorHp
                        val firstPhotoUrl = makanan?.photo
                        if (firstPhotoUrl == DEFAULT_NOT_SET) {
                            Glide.with(applicationContext).load(R.drawable.default_image_not_set).into(imageViewNamaMakanan)
                        } else {
                           Glide.with(applicationContext).load(firstPhotoUrl).into(imageViewNamaMakanan)
                        }
                    }
                })
    }

//    private fun fetchOwnerEvent(id: String) {
//        mDatabaseReference?.child(CHILD_USERS)?.child(id)?.addValueEventListener(object : ValueEventListener {
//            override fun onCancelled(p0: DatabaseError) {
//            }
//
//            override fun onDataChange(p0: DataSnapshot) {
//                val user = p0.getValue(User::class.java)
//                textViewEventOwner.text = user?.nama
//            }
//
//        })
//    }
}
