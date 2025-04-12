package com.example.fetchtest

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

class ItemRepository {

    suspend fun fetchItems(): List<Item> {
        return withContext(Dispatchers.IO) {
            val url = URL("https://fetch-hiring.s3.amazonaws.com/hiring.json")
            val connection = url.openConnection() as HttpURLConnection
            try {
                connection.requestMethod = "GET"
                connection.connect()

                val inputStream = connection.inputStream
                val response = inputStream.bufferedReader().use { it.readText() }
                parseJson(response)
            } finally {
                connection.disconnect()
            }
        }
    }

    private fun parseJson(jsonString: String): List<Item> {
        val jsonArray = JSONArray(jsonString)
        val items = mutableListOf<Item>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            items.add(
                Item(
                    id = jsonObject.getInt("id"),
                    listId = jsonObject.getInt("listId"),
                    name = jsonObject.optString("name", null)
                )
            )
        }
        return items.filter { !it.name.isNullOrEmpty() }
    }
}
