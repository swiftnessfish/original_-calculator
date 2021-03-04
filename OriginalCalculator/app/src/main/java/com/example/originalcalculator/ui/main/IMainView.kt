package com.example.originalcalculator.ui.main

interface IMainView {

    fun onAcButtonClicked()

    fun onPlusMinusButtonClicked()

    fun onPercentButtonClicked()

    fun onDivisionButtonClicked()

    fun onMultiplicationButtonClicked()

    fun onMinusButtonClicked()

    fun onPlusButtonClicked()

    fun onEqualButtonClicked()

    fun onNumberButtonClicked(number: Int)

    fun onDecimalPointButtonClicked()
}