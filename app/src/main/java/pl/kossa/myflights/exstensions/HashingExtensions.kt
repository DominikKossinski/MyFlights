package pl.kossa.myflights.exstensions

import java.math.BigInteger
import java.security.MessageDigest

fun md5Simbrief(string: String): String {
    val md = MessageDigest.getInstance("md5")
    return BigInteger(1, md.digest(string.toByteArray())).toString(16).substring(0, 10).uppercase()//.padStart(32, '0')
}