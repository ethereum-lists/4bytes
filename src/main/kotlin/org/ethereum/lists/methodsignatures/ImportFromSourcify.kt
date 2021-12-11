package org.ethereum.lists.methodsignatures

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.kethereum.abi.getAllFunctions
import org.kethereum.contract.abi.types.isETHType
import org.kethereum.contract.abi.types.model.NamedETHType
import org.kethereum.metadata.model.EthereumMetadataString
import org.kethereum.metadata.parse
import org.kethereum.methodsignatures.toTextMethodSignature
import java.io.File

fun main() {
    import()
}

private fun import() {
    // downloaded via `ipfs get /ipns/k51qzi5uqu5dll0ocge71eudqnrgnogmbr37gsgl12uubsinphjoknl6bbi41p`
    File("/home/ligi/tmp/sourcify3/sourcify_extract/data/contracts/full_match").listFiles()?.forEach { network ->
        network.listFiles()?.forEach {
            val json = File(it, "metadata.json").readText()
            try {
                EthereumMetadataString(json).parse()?.output?.abi?.getAllFunctions()
                    ?.forEach { signature ->
                        signature.inputs.forEach {
                            if (!NamedETHType(it.type.substringBefore("[")).isETHType()) {
                                throw IllegalArgumentException("Contains non ETH type: " + it.type)
                            }
                        }
                        signature.toTextMethodSignature()
                        if (store.upsert(signature)) {
                            println("found new signature: " + signature.toTextMethodSignature().normalizedSignature)
                        }

                    }
            } catch (e: Exception) {
                println("---")
                println(e.message)
                println(json)
            }
        }
    }
}
