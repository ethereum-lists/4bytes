package org.ethereum.lists.methodsignatures

import org.kethereum.methodsignatures.FileBackedMethodSignatureStore
import java.io.File

val signatureDirectory = File("signatures")


val outDir = signatureDirectory.apply { mkdirs() }

val blackListFile = File("ignored.lst")
val store = BlacklistAwareFileBackedMethodSignatureStore(blackListFile, FileBackedMethodSignatureStore(outDir))
