/*
 * Created by Samyak Kamble on 9/16/24, 9:03 PM
 *  Copyright (c) 2024 . All rights reserved.
 *  Last modified 9/16/24, 9:03 PM
 */

package com.cat.catiptv.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cat.catiptv.R
import com.cat.catiptv.model.Channel



class ChannelAdapter(
    private var channels: List<Channel>,
    private val onClick: (Channel) -> Unit
) : RecyclerView.Adapter<ChannelAdapter.ChannelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_channel, parent, false)
        return ChannelViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int) {
        val channel = channels[position]
        holder.bind(channel)
        holder.itemView.setOnClickListener { onClick(channel) }
    }

    override fun getItemCount(): Int = channels.size

    fun updateChannels(newChannels: List<Channel>) {
        channels = newChannels
        notifyDataSetChanged()
    }

    class ChannelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val logo: ImageView = itemView.findViewById(R.id.logo)
        private val name: TextView = itemView.findViewById(R.id.channel_name)

        fun bind(channel: Channel) {
            name.text = channel.name
            Glide.with(itemView.context)
                .load(channel.logoUrl)
                .error(R.drawable.cat) // Fallback image
                .into(logo)
        }
    }
}
