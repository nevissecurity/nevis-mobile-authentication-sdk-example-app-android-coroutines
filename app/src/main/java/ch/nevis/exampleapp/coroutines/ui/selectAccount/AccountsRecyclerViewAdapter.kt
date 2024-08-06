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
 * An implementation of [RecyclerView.Adapter] abstract class that renders [Account] objects into
 * recycler items.
 *
 * @constructor Creates a new instance.
 * @param accounts An [Array] that holds accounts those will be rendered by this adapter.
 * @param accountSelectedListener Reference for the listener implementation that will be notified about
 *  account selection.
 */
class AccountsRecyclerViewAdapter(
    private val accounts: Array<Account>,
    private val accountSelectedListener: AccountSelectedListener
) : RecyclerView.Adapter<AccountsRecyclerViewAdapter.AccountViewHolder>() {

    //region RecyclerView.Adapter
    /** @suppress */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val binding =
            ItemAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccountViewHolder(binding)
    }

    /** @suppress */
    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.binding.accountSelectedListener = accountSelectedListener
        accounts[position].let { account: Account ->
            holder.binding.account = account
        }
    }

    /** @suppress */
    override fun getItemCount() = accounts.size
    //endregion

    //region AccountViewHolder
    /**
     * A [RecyclerView.ViewHolder] implementation that represents an account.
     *
     * @constructor Creates a new instance.
     * @param binding The binding.
     */
    class AccountViewHolder(val binding: ItemAccountBinding) : RecyclerView.ViewHolder(binding.root)
    //endregion
}
