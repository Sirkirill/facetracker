package ua.nure.myapplication.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.Spinner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ua.nure.myapplication.R
import ua.nure.myapplication.adapters.ItemsAdapter
import ua.nure.myapplication.api.RetrofitClient
import ua.nure.myapplication.api.models.UserItems
import ua.nure.myapplication.api.responses.DetailResponse
import ua.nure.myapplication.decorations.SpacesItemDecoration
import ua.nure.myapplication.dialogs.SimpleDialog
import ua.nure.myapplication.helpers.LocaleHelper
import ua.nure.myapplication.helpers.restartActivity
import ua.nure.myapplication.storage.SharedPrefManager

class MainActivity : AppCompatActivity() {

    private lateinit var lsMain: FrameLayout
    private var lang: String? = null
    private var userItems: UserItems? = null
    private lateinit var rvItems: RecyclerView
    private lateinit var srlMain: SwipeRefreshLayout

    private val localeHelper: LocaleHelper =
        LocaleHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lsMain = findViewById(R.id.ls_main)
        lsMain.visibility = View.VISIBLE

        lang = intent.getStringExtra("lang")

        rvItems = findViewById(R.id.rv_items)
        rvItems.layoutManager = GridLayoutManager(this, 2)
        rvItems.addItemDecoration(SpacesItemDecoration(2, 20, false))

        srlMain = findViewById(R.id.srl_main)
        srlMain.setOnRefreshListener {
            srlMain.isRefreshing = true
            getItemsList()
        }

        getItemsList()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val item: MenuItem = menu.findItem(R.id.spin_languages_main)

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
                    localeHelper.setLocale(
                        baseContext,
                        LocaleHelper.languages[position]
                    )
                    restartActivity(baseContext, this@MainActivity)
                }
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }

        return true
    }

    private fun getItemsList() {
        val call = RetrofitClient.getInstance(this).api.getUserItems()

        call.enqueue(object : Callback<UserItems> {
            override fun onFailure(call: Call<UserItems>, t: Throwable) {
                Toasty.error(
                    this@MainActivity,
                    t.message!!,
                    Toasty.LENGTH_LONG
                ).show()
                getItemsList()
                srlMain.isRefreshing = false
                lsMain.visibility = View.GONE
            }

            override fun onResponse(call: Call<UserItems>, response: Response<UserItems>) {
                if (response.body() != null) {
                    userItems = response.body()
                    initItemsRecyclerView()
                } else {
                    Toasty.error(
                        this@MainActivity,
                        getString(R.string.something_wrong),
                        Toasty.LENGTH_LONG
                    ).show()
                }
                lsMain.visibility = View.GONE
                srlMain.isRefreshing = false
            }

        })
    }

    private fun initItemsRecyclerView() {
        val adapter = ItemsAdapter(this, userItems!!.assigned_items)
        rvItems.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_logout -> {
                logoutPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logoutPressed() {
        val dialog = SimpleDialog(this, getString(R.string.logout_message), {
            logout()
        })
        dialog.show(supportFragmentManager, null)
    }

    private fun logout() {

        lsMain.visibility = View.VISIBLE

        val call = RetrofitClient.getInstance(this).api.logout()

        call.enqueue(object : Callback<DetailResponse?> {
            override fun onFailure(call: Call<DetailResponse?>, t: Throwable) {
                lsMain.visibility = View.GONE
                Toasty.error(
                    this@MainActivity,
                    t.message!!,
                    Toasty.LENGTH_LONG
                ).show()
            }

            override fun onResponse(
                call: Call<DetailResponse?>,
                response: Response<DetailResponse?>
            ) {
                if (response.body() != null) {
                    SharedPrefManager.getInstance(this@MainActivity).logout()
                    val intent = Intent(this@MainActivity, AuthorizationActivity::class.java)
                    intent.putExtra(
                        "lang",
                        SharedPrefManager.getInstance(this@MainActivity).getLanguage(null)
                    )
                    startActivity(intent)
                    finish()
                } else {
                    Toasty.error(
                        this@MainActivity,
                        getString(R.string.something_wrong),
                        Toasty.LENGTH_LONG
                    ).show()
                }
                lsMain.visibility = View.GONE
            }
        })
    }
}
