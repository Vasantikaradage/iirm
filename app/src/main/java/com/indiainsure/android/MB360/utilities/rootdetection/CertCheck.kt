package com.indiainsure.android.MB360.utilities.rootdetection

import com.indiainsure.android.MB360.utilities.LogMyBenefits
import okio.ByteString.Companion.decodeBase64
import okio.ByteString.Companion.toByteString
import java.security.cert.Certificate

class CertChecker {
    companion object {

        fun doesCertMatchPin(pin: String, cert: Certificate): Boolean {
            LogMyBenefits.d("SSL CLIENT", "${pin.decodeBase64()}")
            val certHash = cert.publicKey.encoded.toByteString().sha256()
            LogMyBenefits.d("SSL SERVER", "$certHash")
            return certHash == pin.decodeBase64()
        }
    }

}