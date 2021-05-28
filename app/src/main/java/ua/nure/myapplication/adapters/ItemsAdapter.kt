package ua.nure.myapplication.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ua.nure.myapplication.R
import ua.nure.myapplication.activities.ItemActivity
import ua.nure.myapplication.api.models.Item
import ua.nure.myapplication.helpers.isDeadlinePassed


class ItemsAdapter(
    private val context: Context,
    private val items: List<Item>
) :
    RecyclerView.Adapter<ItemsAdapter.EventsViewHolder>() {

    class EventsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tv_item_name)
        val tvUnits: TextView = view.findViewById(R.id.tv_item_units)
        val tvDeadline: TextView = view.findViewById(R.id.tv_item_deadline)
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
        return items.size
    }

    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {
        val item = items[position]

        holder.tvName.text = item.name
        holder.tvUnits.text = item.units.toString()
        holder.tvDeadline.text = item.end_date.substring(0, 10)

        if (isDeadlinePassed(item.end_date.replace("T", "").substring(0, 15))) {
            holder.tvDeadline.setTextColor(Color.RED)
        } else {
            holder.tvDeadline.setTextColor(Color.GREEN)
        }

        holder.llItem.setOnClickListener {
            val intent = Intent(context, ItemActivity::class.java)
            intent.putExtra(ItemActivity.ITEM_ID_KEY, item.pk)
            context.startActivity(intent)
        }
    }
}