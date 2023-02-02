/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.selectAccount

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ch.nevis.exampleapp.coroutines.databinding.ItemAccountBinding
import ch.nevis.mobile.sdk.api.localdata.Account

/**
 * An implementation of [RecyclerView.Adapter] abstract class that renders [Account] objects into recycler items.
 */
class AccountsRecyclerViewAdapter(

    /**
     * An [Array] that holds accounts those will be rendered by this adapter.
     */
    private val accounts: Array<Account>,

    /**
     * Reference for the listener implementation that will be notified about account selection.
     */
    private val accountSelectedListener: AccountSelectedListener
) : RecyclerView.Adapter<AccountsRecyclerViewAdapter.AccountViewHolder>() {

    //region RecyclerView.Adapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val binding =
            ItemAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccountViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.binding.accountSelectedListener = accountSelectedListener
        accounts[position].let { account: Account ->
            holder.binding.account = account
        }
    }

    override fun getItemCount() = accounts.size
    //endregion

    //region AccountViewHolder
    /**
     * A [RecyclerView.ViewHolder] implementation that represents an account.
     */
    class AccountViewHolder(val binding: ItemAccountBinding) : RecyclerView.ViewHolder(binding.root)
    //endregion
}