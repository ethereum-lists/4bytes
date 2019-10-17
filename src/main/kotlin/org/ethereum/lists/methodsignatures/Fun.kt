package org.ethereum.lists.methodsignatures

import kotlin.system.exitProcess

fun error(message: String) {
    println("Error: $message")
    exitProcess(1)
}