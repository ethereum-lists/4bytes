package org.ethereum.lists.methodsignatures

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import okhttp3.OkHttpClient
import okhttp3.Request
import org.kethereum.keccakshortcut.keccak
import org.walleth.khex.toHexString
import java.io.File

const val PAGE_SIZE = 2000
const val url = "https://www.4byte.directory/api/v1/signatures/?page_size=$PAGE_SIZE&ordering=created_at"

val client = OkHttpClient()
val outDir = signatureDirectory.apply { mkdirs() }

var total = 0

fun main(args: Array<String>) {
    import(url)
}

private fun import(url: String) {
    val request = Request.Builder().url(url).build()

    val response = client.newCall(request).execute()
    when (response.code()) {
        200 -> response.body()?.use {
            val string = it.string()

            val jsonObject = Parser().parse(string.reader()) as JsonObject
            val array = jsonObject["results"] as JsonArray<*>

            var new = 0
            array.map { it as JsonObject }.forEach {
                val hexSignature = it["hex_signature"] as String
                val file = File(outDir, hexSignature)
                if (!file.exists()) {
                    file.mkdirs()
                }

                val textSignature = it["text_signature"] as String

                val fullHex = textSignature.toByteArray().keccak().toHexString()
                val signatureTextFile = File(file, fullHex)
                if (!signatureTextFile.exists()) {
                    signatureTextFile.writeText(textSignature)
                    new++
                    total++
                }
            }
            println("processed: ${array.size} - imported: $new - total: $total")
            (jsonObject["next"] as String?)?.let {
                import(it)
            }


        }
        else -> error("Could not get $url")
    }
}
