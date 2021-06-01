package ua.nure.myapplication.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ua.nure.myapplication.R
import ua.nure.myapplication.activities.ItemActivity
import ua.nure.myapplication.api.models.Post


class ItemsAdapter(
    private val context: Context,
    private val posts: List<Post>?
) :
    RecyclerView.Adapter<ItemsAdapter.EventsViewHolder>() {

    class EventsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvRoom: TextView = view.findViewById(R.id.tv_room)
        val tvPhoto: ImageView = view.findViewById(R.id.tv_photo)
        val tvUser: TextView = view.findViewById(R.id.tv_user)
        val llItem: LinearLayout = view.findViewById(R.id.ll_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        val view: View = LayoutInflater.from(context).inflate(
            R.layout.items_item, parent,
            false
        )
        return EventsViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (posts == null) {
            return 0
        }
        return posts.size
    }

    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {
        val post = posts!![position]
        holder.tvRoom.text = post!!.room.name
        if (post!!.photo != null) {
            Picasso.get().load(post!!.photo.image).into(holder.tvPhoto)
        }
        holder.tvUser.text = post?.photo?.user?.username



//        if (isDeadlinePassed(item.end_date.replace("T", "").substring(0, 15))) {
//            holder.tvDeadline.setTextColor(Color.RED)
//        } else {
//            holder.tvDeadline.setTextColor(Color.GREEN)
//        }

        holder.llItem.setOnClickListener {
            val intent = Intent(context, ItemActivity::class.java)
            intent.putExtra(ItemActivity.ITEM_ID_KEY, post.pk)
            context.startActivity(intent)
        }
    }
}