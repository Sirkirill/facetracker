package ua.nure.myapplication.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.squareup.picasso.Picasso
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ua.nure.myapplication.R
import ua.nure.myapplication.api.RetrofitClient
import ua.nure.myapplication.api.models.FlagUpdate
import ua.nure.myapplication.api.models.Post

class ItemActivity : AppCompatActivity() {
    companion object {
        const val ITEM_ID_KEY = "item_id_key"
    }

    private var itemId: Int = -1
    private var post:Post? = null
    private lateinit var lsItem: FrameLayout

    private lateinit var tvPhoto:ImageView
    private lateinit var tvRoom:TextView
    private lateinit var tvUser:TextView
    private lateinit var tvNote:com.google.android.material.textfield.TextInputEditText
    private lateinit var tvImporance:CheckBox
    private lateinit var tvReacted:CheckBox
    private lateinit var tvButton:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        title = getString(R.string.task)

        itemId = intent.getIntExtra(ITEM_ID_KEY,-1)
        lsItem = findViewById(R.id.ls_item)
        tvImporance = findViewById(R.id.is_important)
        tvReacted = findViewById(R.id.is_reacted)
        tvRoom = findViewById(R.id.tv_room)
        tvPhoto = findViewById(R.id.tv_photo)
        tvUser = findViewById(R.id.tv_user)
        tvNote = findViewById(R.id.tv_note)

        tvButton = findViewById(R.id.save)

        tvButton.setOnClickListener {
            var flags: FlagUpdate = FlagUpdate(
                is_important = tvImporance.isChecked,
                is_reacted = tvReacted.isChecked,
                note = tvNote.text.toString()
            )

            val call = RetrofitClient.getInstance(this).api.updatePost(itemId, flags)
            call.enqueue(object : Callback<Post> {
                override fun onFailure(call: Call<Post>, t: Throwable) {
                    Toasty.error(
                        this@ItemActivity,
                        t.message!!,
                        Toasty.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    lsItem.visibility = View.GONE
                }
            }
            )
        }
        getItem()
    }

    private fun getItem(){
        lsItem.visibility = View.VISIBLE
        val call = RetrofitClient.getInstance(this).api.getPost(itemId)

        call.enqueue(object : Callback<Post>{
            override fun onFailure(call: Call<Post>, t: Throwable) {
                Toasty.error(
                    this@ItemActivity,
                    t.message!!,
                    Toasty.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (response.body() != null) {
                    post = response.body()
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
        tvImporance.isChecked = post!!.is_important
        tvReacted.isChecked = post!!.is_reacted
        tvRoom.text = post!!.room.name
        if (post!!.photo != null) {
            Picasso.get().load(post!!.photo.image).into(tvPhoto)
        }
        tvUser.text = post?.photo?.user
        tvNote.setText(post?.note)

//        if (isDeadlinePassed(post!!.end_date.replace("T", "").substring(0, 15))) {
//            tvDeadlineExpired.visibility = View.VISIBLE
//        } else {
//            tvDeadlineExpired.visibility = View.GONE
//        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        }
        return true
    }
}
