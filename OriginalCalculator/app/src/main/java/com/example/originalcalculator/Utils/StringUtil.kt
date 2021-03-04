package com.example.originalcalculator.Utils

import java.lang.StringBuilder

fun StringBuilder.toCommaString(): String {
    val index = this.indexOf(".")
    return when (index) {
        -1 -> {
            var result = "%,f".format(this.toString().toBigDecimal())
            result = result.dropLast((result.length - result.indexOf(".")))
            result
        }
        else -> {
            var result = "%,f".format(this.substring(0, index).toBigDecimal())
            result = result.dropLast((result.length - result.indexOf("."))) + "."
            if (index < this.count()) result += this.substring(index + 1)
            result
        }
    }
}