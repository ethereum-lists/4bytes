package org.ethereum.lists.methodsignatures

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.kethereum.abi.getAllFunctions
import org.kethereum.metadata.model.EthereumMetadataString
import org.kethereum.metadata.parse
import org.kethereum.methodsignatures.toTextMethodSignature
import java.io.File

fun main() {
    import()
}

private fun import() {
    // downloaded via `ipfs get /ipns/k51qzi5uqu5dll0ocge71eudqnrgnogmbr37gsgl12uubsinphjoknl6bbi41p`
    File("sourcify/k51qzi5uqu5dll0ocge71eudqnrgnogmbr37gsgl12uubsinphjoknl6bbi41p/contracts/full_match").listFiles()?.forEach { network ->
        network.listFiles()?.forEach {
            val json = File(it, "metadata.json").readText()
            EthereumMetadataString(json).parse(Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build())?.output?.abi?.getAllFunctions()?.forEach { signature ->
                if (store.upsert(signature)) {
                    println("found new signature: " + signature.toTextMethodSignature().normalizedSignature)
                }

            }
        }
    }
}
