package ua.nure.myapplication.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ua.nure.myapplication.helpers.LocaleHelper
import ua.nure.myapplication.storage.SharedPrefManager

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val localHelper = LocaleHelper()
        val lang = localHelper.getLanguage(this)
        localHelper.setLocale(baseContext,lang)

        val intent:Intent = if(SharedPrefManager.getInstance(this).isLoggedIn()){
            Intent(this, MainActivity::class.java)
        }else{
            Intent(this, AuthorizationActivity::class.java)
        }
        intent.putExtra("lang",lang)

        startActivity(intent)
        finish()
    }
}
