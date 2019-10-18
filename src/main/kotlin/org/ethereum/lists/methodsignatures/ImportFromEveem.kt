package org.ethereum.lists.methodsignatures

import org.kethereum.methodsignatures.model.TextMethodSignature
import org.kethereum.methodsignatures.toHexSignature
import java.io.File

fun main() {
    import()
}

private fun import() {

    var new = 0L
    var known = 0L

    println("please wait - this can take a while")

    File("tmp_eveem").readText().lines().forEach { _signature ->
        val signature = _signature.replace(" storage","")
        if (!signature.isBlank()) {

            val sig = TextMethodSignature(signature).toHexSignature()

            if (store.upsert(sig.hex, signature)) {
                new++
            } else {
                known++
            }
        }
    }
    println("new signatures $new - known signatures: $known")
}
