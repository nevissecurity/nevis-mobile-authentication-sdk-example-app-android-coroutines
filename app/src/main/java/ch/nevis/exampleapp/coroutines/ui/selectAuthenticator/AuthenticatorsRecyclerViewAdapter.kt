/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.selectAuthenticator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ch.nevis.exampleapp.coroutines.R
import ch.nevis.exampleapp.coroutines.databinding.ItemAuthenticatorBinding
import ch.nevis.exampleapp.coroutines.ui.selectAuthenticator.model.AuthenticatorItem
import ch.nevis.mobile.sdk.api.localdata.Authenticator

/**
 * An implementation of [RecyclerView.Adapter] abstract class that renders [Authenticator] objects into recycler items.
 */
class AuthenticatorsRecyclerViewAdapter(

    /**
     * An [Array] that holds authenticator items those will be rendered by this adapter.
     */
    private val authenticatorItems: Array<AuthenticatorItem>,

    /**
     * Reference for the listener implementation that will be notified about authenticator selection.
     */
    private val authenticatorSelectedListener: AuthenticatorSelectedListener
) : RecyclerView.Adapter<AuthenticatorsRecyclerViewAdapter.AuthenticatorViewHolder>() {

    //region RecyclerView.Adapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuthenticatorViewHolder {
        val binding =
            ItemAuthenticatorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AuthenticatorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AuthenticatorViewHolder, position: Int) {
        holder.binding.authenticatorSelectedListener = authenticatorSelectedListener
        val authenticatorItem = authenticatorItems[position]
        holder.binding.aaid = authenticatorItem.aaid
        holder.binding.titleResId = authenticatorItem.titleResId

        val isEnabled = authenticatorItem.isEnabled()
        holder.binding.isEnabled = isEnabled
        holder.itemView.isEnabled = isEnabled

        if (!isEnabled) {
            if (!authenticatorItem.isPolicyCompliant) {
                holder.binding.message =
                    holder.itemView.context.getString(R.string.select_authenticator_authenticator_is_not_policy_compliant)
            } else if (!authenticatorItem.isUserEnrolled) {
                holder.binding.message =
                    holder.itemView.context.getString(R.string.select_authenticator_authenticator_is_not_enrolled)
            }
        }
    }

    override fun getItemCount() = authenticatorItems.size
    //endregion

    //region AuthenticatorViewHolder
    /**
     * A [RecyclerView.ViewHolder] implementation that represents a authenticator.
     */
    class AuthenticatorViewHolder(val binding: ItemAuthenticatorBinding) :
        RecyclerView.ViewHolder(binding.root)
    //endregion
}