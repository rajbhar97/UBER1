package com.example.uber1

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.auth.api.credentials.IdentityProviders
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*
import java.util.concurrent.TimeUnit

class SplashScreenActivity : AppCompatActivity() {

        companion object {
            private val LOGIN_REQUEST_CODE =7171
        }

    private lateinit var providers: List<AuthUI.IdpConfig>
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var listener:FirebaseAuth.AuthStateListener


    override fun onStart() {
        super.onStart()
        delaySplashScreen();
    }

    override fun onStop() {
        if (firebaseAuth !=null && listener !=null) firebaseAuth.removeAuthStateListener(listener)
        super.onStop()
    }

    private fun delaySplashScreen() {

        Completable.timer(3, TimeUnit.SECONDS,AndroidSchedulers.mainThread())

                .subscribe({
                    firebaseAuth.addAuthStateListener(listener);
                })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()


    }

    private fun init() {

        providers = Arrays.asList(

                AuthUI.IdpConfig.PhoneBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build()

        )

        firebaseAuth = FirebaseAuth.getInstance()
        listener = FirebaseAuth.AuthStateListener { myFirebaseAuth ->

            val user = myFirebaseAuth.currentUser

            if (user != null)
                Toast.makeText(this@SplashScreenActivity, "Welcome"+user.uid, Toast.LENGTH_SHORT ).show()

            else
                showLoginLayout()
        }
    }

    private fun showLoginLayout() {

        val authMethodPickerLayout = AuthMethodPickerLayout.Builder(R.layout.layout_sign_in)
                .setPhoneButtonId(R.id.btn_phone_sign_up)
                .setGoogleButtonId(R.id.btn_google_sign_up)
                .build();

        startActivityForResult(

                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAuthMethodPickerLayout(authMethodPickerLayout)
                        .setIsSmartLockEnabled(false)
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(providers)
                        .build()
                , LOGIN_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == LOGIN_REQUEST_CODE){

            val response = IdpResponse.fromResultIntent(data)
            if(resultCode ==Activity.RESULT_OK){
                val  user = FirebaseAuth.getInstance().currentUser
            }
            else
                Toast.makeText(this@SplashScreenActivity, ""+response!!.error!!.message,Toast.LENGTH_SHORT).show()

    }
    }
}


