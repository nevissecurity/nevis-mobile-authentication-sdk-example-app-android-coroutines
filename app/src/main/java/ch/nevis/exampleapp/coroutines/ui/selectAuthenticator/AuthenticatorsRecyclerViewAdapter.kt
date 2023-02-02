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
import ch.nevis.exampleapp.coroutines.ui.selectAuthenticator.model.AuthenticatorItemDescriptor
import ch.nevis.mobile.sdk.api.localdata.Authenticator

/**
 * An implementation of [RecyclerView.Adapter] abstract class that renders [Authenticator] objects into recycler items.
 */
class AuthenticatorsRecyclerViewAdapter(

    /**
     * An [Array] that holds authenticators those will be rendered by this adapter.
     */
    private val authenticators: Array<Authenticator>,

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
        authenticators[position].let { authenticator: Authenticator ->
            holder.binding.authenticator = authenticator
            authenticatorDescriptors[authenticator.aaid()]?.let {
                holder.binding.authenticatorDescriptor = it
            }
        }
    }

    override fun getItemCount() = authenticators.size
    //endregion

    //region AuthenticatorViewHolder
    /**
     * A [RecyclerView.ViewHolder] implementation that represents a authenticator.
     */
    class AuthenticatorViewHolder(val binding: ItemAuthenticatorBinding) :
        RecyclerView.ViewHolder(binding.root)
    //endregion

    //region Companion
    companion object {
        /**
         * A map that holds [AuthenticatorItemDescriptor] objects for each known authenticator identifier.
         */
        private val authenticatorDescriptors = mapOf(
            Authenticator.PIN_AUTHENTICATOR_AAID to AuthenticatorItemDescriptor(R.string.select_authenticator_pin_authenticator_title),
            Authenticator.FINGERPRINT_AUTHENTICATOR_AAID to AuthenticatorItemDescriptor(R.string.select_authenticator_fingerprint_authenticator_title),
            Authenticator.BIOMETRIC_AUTHENTICATOR_AAID to AuthenticatorItemDescriptor(R.string.select_authenticator_biometric_authenticator_title)
        )
    }
    //endregion
}