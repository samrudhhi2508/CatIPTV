
/*
 * Created by Samyak Kamble on 9/16/24, 9:05 PM
 *  Copyright (c) 2024 . All rights reserved.
 *  Last modified 9/16/24, 9:05 PM
 */

package com.cat.catiptv.screens

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.cat.catiptv.Adapter.ChannelAdapter
import com.cat.catiptv.R
import com.cat.catiptv.model.Channel
import com.cat.catiptv.provider.ChannelsProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private lateinit var channelAdapter: ChannelAdapter
    private lateinit var searchEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private val channelsProvider = ChannelsProvider()
    private var channels: List<Channel> = listOf()
    private var filteredChannels: List<Channel> = listOf()
    private var handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.recycler_view)
        searchEditText = view.findViewById(R.id.search_edit_text)
        progressBar = view.findViewById(R.id.progress_bar)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        channelAdapter = ChannelAdapter(filteredChannels) { channel ->
            // Handle channel click here
            val intent = Intent(requireContext(), PlayerActivity::class.java)
            intent.putExtra("channel", channel)
            startActivity(intent)
        }
        recyclerView.adapter = channelAdapter

        fetchData()

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterChannels(s.toString())
            }
        })

        return view
    }

    private fun fetchData() {
        progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val fetchedChannels = channelsProvider.fetchM3UFile()
                withContext(Dispatchers.Main) {
                    channels = fetchedChannels
                    filteredChannels = channels
                    channelAdapter.updateChannels(filteredChannels)
                    progressBar.visibility = View.GONE
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Error fetching channels: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun filterChannels(query: String) {
        searchRunnable?.let { handler.removeCallbacks(it) }
        searchRunnable = Runnable {
            filteredChannels = channelsProvider.filterChannels(query)
            channelAdapter.updateChannels(filteredChannels)
        }
        handler.postDelayed(searchRunnable!!, 500)
    }
}
