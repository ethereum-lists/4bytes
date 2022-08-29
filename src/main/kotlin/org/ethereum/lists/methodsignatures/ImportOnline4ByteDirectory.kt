package org.ethereum.lists.methodsignatures

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.lang.Thread.sleep
import java.util.concurrent.TimeUnit

const val PAGE_SIZE = 2000
const val url = "https://www.4byte.directory/api/v1/signatures/?page_size=$PAGE_SIZE&ordering=created_at"

val client = OkHttpClient.Builder().apply {
    readTimeout(42, TimeUnit.SECONDS)
}.build()


var total = 0

fun main() {
    import(url)
}

private const val DEFAULT_RETRY_SLEEP = 2
var retrySleepInSeconds = DEFAULT_RETRY_SLEEP

private fun import(url: String): Unit? = try {
    val request = Request.Builder().url(url).build()

    client.newCall(request).execute().use { response ->
        when (response.code) {
            200 -> response.body?.use { body ->
                val string = body.string()

                val jsonObject = Klaxon().parseJsonObject(string.reader())
                val array = jsonObject["results"] as JsonArray<*>

                var new = 0
                array.map { it as JsonObject }.forEach {
                    val hexSignature = it["hex_signature"] as String
                    val textSignature = it["text_signature"] as String
                    if (store.upsert(hexSignature.replace("0x", ""), textSignature)) {
                        new++
                        total++
                    }
                }
                println("processed: ${array.size} - imported: $new - total: $total")

                retrySleepInSeconds = DEFAULT_RETRY_SLEEP

                (jsonObject["next"] as String?)?.let {
                    import(it)
                }
            }

            else -> {
                // throw as exception so we use the retry logic in the handler
                throw IOException("${response.message} (code ${response.code}")
            }
        }
    }
} catch (e: IOException) {
    println("Error fetching " + e.message)
    println("Retry in ${retrySleepInSeconds}s")
    sleep(retrySleepInSeconds * 100L)
    retrySleepInSeconds *= retrySleepInSeconds // exponential back off
    import(url)
}
