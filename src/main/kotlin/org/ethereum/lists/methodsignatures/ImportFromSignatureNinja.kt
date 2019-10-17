package org.ethereum.lists.methodsignatures

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import java.io.File

fun main() {
    import()
}

private fun import() {
    val jsonArray = Klaxon().parseJsonArray(File("signature_ninja_signatures.json").reader())

    var new = 0
    var total = 0
    jsonArray.forEach {
        val fourByte = ((it as JsonObject)["byte"] as String).removePrefix("0x")
        it.array<String>("text")?.forEach { signature ->
            total++
            if (store.upsert(fourByte, signature)) {
                new++
            }
        }
    }

    println("new: $new - total: $total")

}
