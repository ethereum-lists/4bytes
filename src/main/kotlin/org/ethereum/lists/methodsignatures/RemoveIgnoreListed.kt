package org.ethereum.lists.methodsignatures

import org.kethereum.methodsignatures.model.TextMethodSignature
import org.kethereum.methodsignatures.toHexSignature
import java.io.File

fun main() {
    blackListFile.readLines().toSet().forEach {signatureText ->
        val file = File(signatureDirectory,TextMethodSignature(signatureText).toHexSignature().hex)
        if (file.exists()) {
            val newContent = file.readText().split(";").toMutableList().apply { remove(signatureText) }.joinToString(";")
            if (newContent.isEmpty()) {
                file.delete()
            } else {
                file.writeText(newContent)
            }
        }
    }
}
