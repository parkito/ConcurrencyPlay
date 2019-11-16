package com.concurrency.play.net.tls.factory


class FactoryConstants {

    companion object {
        val TLS_VERSION = "TLSv1.2"

        val ciphers = arrayOf("TLS_AES_128_GCM_SHA256")
        val protocols = arrayOf("TLSv1.2") //"TLSv1.3"
    }
}
