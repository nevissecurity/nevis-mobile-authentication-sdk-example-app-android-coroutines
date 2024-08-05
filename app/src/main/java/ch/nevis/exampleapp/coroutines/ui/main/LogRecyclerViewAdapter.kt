/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.nevis.exampleapp.coroutines.R
import ch.nevis.exampleapp.coroutines.domain.log.LogItem
import java.text.SimpleDateFormat

/**
 * An implementation of [RecyclerView.Adapter] abstract class that renders [LogItem] objects into recycler items.
 *
 * @constructor Creates a new instance.
 * @param context An Android [Context] object for resolving resources and to obtain a [LayoutInflater]
 *  instance.
 */
class LogRecyclerViewAdapter(
    private val context: Context
): RecyclerView.Adapter<LogRecyclerViewAdapter.LogViewHolder>() {

    //region Properties
    /**
     * A [LayoutInflater] object for inflating view holders.
     */
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    /**
     * An [ArrayList] that holds log items that will be rendered by this adapter.
     */
    private var log = ArrayList<LogItem>()

    /**
     * A [SimpleDateFormat] instance that is used for formatting timestamp of log items.
     */
    @SuppressLint("SimpleDateFormat")
    private val dateFormatter = SimpleDateFormat(context.getString(R.string.log_item_date_format))
    //endregion

    //region Public Interface
    /**
     * Adds a new log item to currently rendered log item list.
     *
     * @param logItem The new [LogItem] object.
     */
    fun addLogItem(logItem: LogItem) {
        log.add(logItem)
        notifyDataSetChanged()
    }
    //endregion

    //region RecyclerView.Adapter
    /** @suppress */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        return LogViewHolder(layoutInflater.inflate(R.layout.item_log, parent, false))
    }

    /** @suppress */
    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        holder.messageTextView.text = null

        log[position].let { logItem:LogItem ->
            holder.logItem = logItem
            holder.messageTextView.text = context.getString(R.string.log_item, dateFormatter.format(logItem.date), logItem.message)
        }
    }

    /** @suppress */
    override fun getItemCount() = log.size
    //endregion

    //region AuthenticatorViewHolder
    /**
     * A [RecyclerView.ViewHolder] implementation that represents a log item. It holds only a [TextView] the log message will be set into.
     *
     * @constructor Creates a new instance.
     * @param itemView The item view provided by recycler view.
     */
    inner class LogViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        //region Properties
        /**
         * A [TextView] the log message will be set into.
         */
        val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)

        /**
         * The [LogItem] object that is rendered into the message text view.
         */
        var logItem: LogItem? = null
        //endregion
    }
    //endregion
}
