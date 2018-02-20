package org.ethereum.lists.methodsignatures

import org.kethereum.keccakshortcut.keccak
import org.walleth.khex.toHexString

fun main(args: Array<String>) {
    var total = 0
    signatureDirectory.listFiles().forEach {
        it.listFiles().forEach { signatureTextFile ->
            val signatureText = signatureTextFile.readText()
            if (signatureText.toByteArray().keccak().toHexString() != signatureTextFile.name) {
                error("signature fail " + (signatureTextFile.name + " " + signatureText))
            }
            total++
        }
    }
    println("total: $total")
}
