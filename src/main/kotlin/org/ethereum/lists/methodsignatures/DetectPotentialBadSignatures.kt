package org.ethereum.lists.methodsignatures

import org.kethereum.methodsignatures.model.TextMethodSignature
import org.kethereum.methodsignatures.toHexSignature
import java.io.File

fun main() {
    signatureDirectory.listFiles()?.map { it.readText() }?.forEach {
        it.split(";").forEach { signatureText ->
            if (signatureText.substringBefore("(").filter { c-> c.isDigit() }.length > 4) {
                println(signatureText)
            }
        }
    }
}
