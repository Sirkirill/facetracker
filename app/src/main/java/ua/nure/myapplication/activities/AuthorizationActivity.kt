package ua.nure.myapplication.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewParent
import android.widget.*
import androidx.viewpager.widget.ViewPager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ua.nure.myapplication.R
import ua.nure.myapplication.adapters.AuthorizationPagerAdapter
import ua.nure.myapplication.api.RetrofitClient
import ua.nure.myapplication.helpers.LocaleHelper
import ua.nure.myapplication.helpers.restartActivity

class AuthorizationActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var pagerAuth: ViewPager
    private lateinit var tvLogin: TextView
    private lateinit var tvRegister: TextView
    private var lang:String? = null

    private val localeHelper: LocaleHelper =
        LocaleHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)

        title = getString(R.string.login)

        pagerAuth = findViewById(R.id.pager_authorization)
        val adapter = AuthorizationPagerAdapter(supportFragmentManager)
        pagerAuth.adapter = adapter

        pagerAuth.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                title =
                    if (position == 0) getString(R.string.login) else getString(R.string.register)
            }
        })

        tvLogin = findViewById(R.id.tv_login)
        tvLogin.setOnClickListener(this)
        lang = intent.getStringExtra("lang")
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.authorization_menu, menu)
        val item: MenuItem = menu.findItem(R.id.spin_languages)

        val adapter: ArrayAdapter<String> =
            ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                LocaleHelper.languages
            )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val spinLanguages: Spinner = item.actionView as Spinner

        val current = LocaleHelper.languages.indexOf(lang)

        spinLanguages.adapter = adapter


        spinLanguages.setSelection(current)

        spinLanguages.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View,
                position: Int, id: Long
            ) {
                if (position != current) {
                    localeHelper.setLocale(baseContext,
                        LocaleHelper.languages[position])
                    restartActivity(baseContext,this@AuthorizationActivity)
                }
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }

        return true
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tv_login -> {
                pagerAuth.currentItem = 0
            }

        }
    }
}
