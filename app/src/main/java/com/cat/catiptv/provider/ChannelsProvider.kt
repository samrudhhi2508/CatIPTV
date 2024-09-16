package com.cat.catiptv.provider

import com.cat.catiptv.model.Channel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class ChannelsProvider {
    private val channels = mutableListOf<Channel>()
    private val filteredChannels = mutableListOf<Channel>()
    private val sourceUrl = "https://raw.githubusercontent.com/FunctionError/PiratesTv/main/combined_playlist.m3u"

    // HTTP client
    private val client = OkHttpClient()

    // Fetch and parse the M3U file asynchronously using coroutines
    suspend fun fetchM3UFile(): List<Channel> = withContext(Dispatchers.IO) {
        val request = Request.Builder().url(sourceUrl).build()

        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Failed to load M3U file")

                val fileText = response.body?.string() ?: ""
                val lines = fileText.split("\n")

                var name: String? = null
                var logoUrl = getDefaultLogoUrl()
                var streamUrl: String? = null

                for (line in lines) {
                    if (line.startsWith("#EXTINF:")) {
                        name = extractChannelName(line)
                        logoUrl = extractLogoUrl(line) ?: getDefaultLogoUrl()
                    } else if (line.isNotEmpty()) {
                        streamUrl = line
                        if (name != null) {
                            channels.add(
                                Channel(
                                    name = name,
                                    logoUrl = logoUrl,
                                    streamUrl = streamUrl
                                )
                            )
                        }
                        // Reset for the next channel
                        name = null
                        logoUrl = getDefaultLogoUrl()
                        streamUrl = null
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            throw IOException("Failed to fetch M3U file: ${e.message}")
        }

        return@withContext channels
    }

    private fun getDefaultLogoUrl(): String {
        return "assets/images/tv-icon.png"
    }

    private fun extractChannelName(line: String): String {
        val parts = line.split(",")
        return parts.last()
    }

    private fun extractLogoUrl(line: String): String? {
        val parts = line.split("\"")
        return if (parts.size > 1 && isValidUrl(parts[1])) {
            parts[1]
        } else if (parts.size > 5 && isValidUrl(parts[5])) {
            parts[5]
        } else {
            null
        }
    }

    private fun isValidUrl(url: String): Boolean {
        return url.startsWith("https") || url.startsWith("http")
    }

    // Filter the channels by query
    fun filterChannels(query: String): List<Channel> {
        filteredChannels.clear()
        filteredChannels.addAll(channels.filter { it.name.contains(query, ignoreCase = true) })
        return filteredChannels
    }
}
