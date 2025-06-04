package com.theminesec.utils


import java.io.ByteArrayInputStream
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.Base64

fun String.toX509Certificate(): X509Certificate {
    val cert = this.replace("-----BEGIN CERTIFICATE-----", "")
        .replace("-----END CERTIFICATE-----", "")
        .replace("\n", "")
        .replace("\r", "").trim()
    try {
        val bytes = Base64.getDecoder().decode(cert)
        val cf = CertificateFactory.getInstance("X.509")
        val certObj = cf.generateCertificate(ByteArrayInputStream(bytes))
        return certObj as? X509Certificate
            ?: throw CertConvertException(
                "Certificate from PEM null"
            )
    } catch (e: Exception) {
        throw CertConvertException(
            "Certificate from PEM exception ${e.message}",
            e
        )
    }
}

open class CertConvertException(errMsg: String, cause: Throwable? = null) :
    Exception(errMsg, cause)