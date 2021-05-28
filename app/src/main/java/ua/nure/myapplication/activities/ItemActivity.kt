package ua.nure.myapplication.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ua.nure.myapplication.R
import ua.nure.myapplication.api.RetrofitClient
import ua.nure.myapplication.api.models.Item
import ua.nure.myapplication.helpers.isDeadlinePassed
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ItemActivity : AppCompatActivity() {
    companion object {
        const val ITEM_ID_KEY = "item_id_key"
    }

    private var itemId: Int = -1
    private var item:Item? = null
    private lateinit var lsItem: FrameLayout

    private lateinit var tvName:TextView
    private lateinit var tvUnits:TextView
    private lateinit var tvFrom:TextView
    private lateinit var tvTo:TextView
    private lateinit var tvDeadlineExpired:TextView
    private lateinit var tvLastChange:TextView
    private lateinit var tvCreator:TextView
    private lateinit var tvDescription:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        title = getString(R.string.task)

        itemId = intent.getIntExtra(ITEM_ID_KEY,-1)
        lsItem = findViewById(R.id.ls_item)

        tvName = findViewById(R.id.tv_name)
        tvUnits = findViewById(R.id.tv_units)
        tvFrom = findViewById(R.id.tv_from)
        tvTo = findViewById(R.id.tv_to)

        tvDeadlineExpired = findViewById(R.id.tv_deadline_expired)
        tvDeadlineExpired.visibility = View.GONE

        tvLastChange = findViewById(R.id.tv_last_change)
        tvCreator = findViewById(R.id.tv_creator)
        tvDescription = findViewById(R.id.tv_description)

        getItem()
    }

    private fun getItem(){
        lsItem.visibility = View.VISIBLE
        val call = RetrofitClient.getInstance(this).api.getItem(itemId)

        call.enqueue(object : Callback<Item>{
            override fun onFailure(call: Call<Item>, t: Throwable) {
                Toasty.error(
                    this@ItemActivity,
                    t.message!!,
                    Toasty.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<Item>, response: Response<Item>) {
                if (response.body() != null) {
                    item = response.body()
                    initItemData()
                } else {
                    Toasty.error(
                        this@ItemActivity,
                        getString(R.string.something_wrong),
                        Toasty.LENGTH_LONG
                    ).show()
                }
                lsItem.visibility = View.GONE
            }

        })
    }

    private fun initItemData() {
        tvName.text = item!!.name
        tvUnits.text = item!!.units.toString()

        val fromDate = item!!.start_date.substring(0,10)
        val fromTime = item!!.start_date.substring(11,16)
        tvFrom.text = String.format(getString(R.string.from_date),fromDate,fromTime)

        val toDate = item!!.end_date.substring(0,10)
        val toTime = item!!.end_date.substring(11,16)
        tvTo.text = String.format(getString(R.string.from_date),toDate,toTime)

        if (isDeadlinePassed(item!!.end_date.replace("T", "").substring(0, 15))) {
            tvDeadlineExpired.visibility = View.VISIBLE
        } else {
            tvDeadlineExpired.visibility = View.GONE
        }

        val changeDate = item!!.last_change.substring(0,10)
        val changeTime = item!!.last_change.substring(11,16)
        tvLastChange.text = String.format(getString(R.string.from_date),changeDate,changeTime)
        tvCreator.text = String.format(getString(R.string.username_tag),item!!.creator.username)

        tvDescription.text = item!!.description
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        }
        return true
    }
}
