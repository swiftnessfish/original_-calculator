package com.example.originalcalculator.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.originalcalculator.Utils.toCommaString
import java.lang.StringBuilder
import java.math.BigDecimal
import java.math.RoundingMode

class MainViewModel : ViewModel(), IMainView {

    companion object {
        val MAX = BigDecimal(999999999999999999)
    }

    val text = MutableLiveData<String>()

    val calculationMode = MutableLiveData<CalculationMode>()

    val stringBuilder = StringBuilder()

    private var number1 = BigDecimal.ZERO

    private var number2 = BigDecimal.ZERO

    private var modeChangeFlag = false

    override fun onAcButtonClicked() {
        stringBuilder.clear()
        stringBuilder.append("0")
        text.postValue("0")

        if (calculationMode.value == CalculationMode.NONE) {
            number1 = BigDecimal.ZERO
        } else {
            number2 = BigDecimal.ZERO
        }
    }

    override fun onPlusMinusButtonClicked() {
        if (stringBuilder.toString() == "0") return

        stringBuilder.clear()
        if (calculationMode.value == CalculationMode.NONE) {
            number1 = number1.negate()
            stringBuilder.append(number1.toString())
        } else {
            number2 = number2.negate()
            stringBuilder.append(number2.toString())
        }
        text.postValue(stringBuilder.toCommaString())
    }

    override fun onPercentButtonClicked() {
        // 小数点以下は6桁以上表示しない
        if (stringBuilder.count() - stringBuilder.indexOf(".") >= 4) return

        stringBuilder.clear()
        if (calculationMode.value == CalculationMode.NONE) {
            number1 = number1.divide(BigDecimal(100), 6, RoundingMode.HALF_UP)
            stringBuilder.append(number1.toString())
        } else {
            number2 = number2.divide(BigDecimal(100), 6, RoundingMode.HALF_UP)
            stringBuilder.append(number2.toString())
        }
        text.postValue(stringBuilder.toCommaString())
    }

    override fun onDivisionButtonClicked() {
        calculationMode.postValue(CalculationMode.DIVISION)
        modeChangeFlag = true
    }

    override fun onMultiplicationButtonClicked() {
        calculationMode.postValue(CalculationMode.MULTIPLICATION)
        modeChangeFlag = true
    }

    override fun onMinusButtonClicked() {
        calculationMode.postValue(CalculationMode.MINUS)
        modeChangeFlag = true
    }

    override fun onPlusButtonClicked() {
        calculationMode.postValue(CalculationMode.PLUS)
        modeChangeFlag = true
    }

    override fun onEqualButtonClicked() {
        val result = when (calculationMode.value) {
            CalculationMode.DIVISION -> {
                if (number2 == BigDecimal.ZERO) {
                    text.postValue("エラー")
                    number1 = BigDecimal.ZERO
                    number2 = BigDecimal.ZERO
                    calculationMode.postValue(CalculationMode.NONE)
                    return
                }
                // 小数点6桁で切り上げ
                number1.divide(number2, 6, RoundingMode.HALF_UP)
            }
            CalculationMode.MULTIPLICATION -> {
                number1.multiply(number2)
            }
            CalculationMode.PLUS -> {
                number1 + number2
            }
            CalculationMode.MINUS -> {
                number1 - number2
            }
            else -> {
                return
            }
        }

        if (result >= MAX) {
            text.postValue("エラー")
            number1 = BigDecimal.ZERO
            number2 = BigDecimal.ZERO
            calculationMode.postValue(CalculationMode.NONE)
            return
        }

        stringBuilder.clear()
        stringBuilder.append(result.toString())
        text.postValue(stringBuilder.toCommaString())
        number1 = result
        number2 = BigDecimal.ZERO
        calculationMode.postValue(CalculationMode.NONE)
        modeChangeFlag = true
    }

    override fun onNumberButtonClicked(number: Int) {
        if (!modeChangeFlag) {
            // 9桁以上入力させない
            if (stringBuilder.count { it != '.' } >= 9) return
            // 小数点以下は５桁まで
            if (stringBuilder.indexOf(".") != -1 && stringBuilder.count() - stringBuilder.indexOf(".") > 5) return
        }

        // 計算ボタン押下後初めの入力時や現在の数が０の場合は現在の表示数を含めない
        if (modeChangeFlag || stringBuilder.toString() == "0" || stringBuilder.toString() == "エラー") {
            stringBuilder.clear()
        }
        stringBuilder.append(number)
        text.postValue(stringBuilder.toCommaString())
        modeChangeFlag = false

        if (calculationMode.value == CalculationMode.NONE) {
            number1 = BigDecimal(stringBuilder.toString())
        } else {
            number2 = BigDecimal(stringBuilder.toString())
        }
    }

    override fun onDecimalPointButtonClicked() {
        // 9桁以上入力させない
        if (stringBuilder.count { it != '.' } >= 9) return
        // すでに小数点がある場合は処理を中断する
        if (stringBuilder.any { it == '.' }) return

        stringBuilder.append(".")
        text.postValue(stringBuilder.toCommaString())
    }
}
