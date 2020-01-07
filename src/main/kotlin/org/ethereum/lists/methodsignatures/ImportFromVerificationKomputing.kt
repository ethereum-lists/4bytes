package org.ethereum.lists.methodsignatures

import org.kethereum.abi.getAllFunctions
import org.kethereum.metadata.model.EthereumMetadataString
import org.kethereum.metadata.parse
import org.kethereum.methodsignatures.toTextMethodSignature
import java.io.File

fun main() {
    import()
}

private fun import() {
    // downloaded via `ipfs get /ipns/QmNmBr4tiXtwTrHKjyppUyAhW1FQZMJTdnUrksA9hapS4u`
    File("verification.komputing/QmNmBr4tiXtwTrHKjyppUyAhW1FQZMJTdnUrksA9hapS4u/contract").listFiles()?.forEach { network ->
        network.listFiles()?.forEach {
            val json = File(it, "metadata.json").readText()

            EthereumMetadataString(json).parse()?.output?.abi?.getAllFunctions()?.forEach { signature ->
                if (store.upsert(signature)) {
                    println("found new signature: " + signature.toTextMethodSignature().normalizedSignature)
                }

            }
        }
    }
}
