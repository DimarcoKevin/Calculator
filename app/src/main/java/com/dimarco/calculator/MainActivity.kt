package com.dimarco.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

/**
 * A simple calculator application following the UI of the MacOS calculator.
 * This application allows for decimals and positive / negative numbers
 * This application will only allow for 2 numbers to be compared
 * (addition, subtraction, multiplication, division, and percentages)
 *
 * @author Kevin Dimarco
 * www.kevindimarco.com
 * 2020
 */
class MainActivity : AppCompatActivity() {
    private val operatorList = arrayOf('+', '-', '*', '÷', '%')

    // enum to replace operators
    enum class Operator(val op: Char) {
        PLUS('+'),
        MINUS('-'),
        MULTIPLY('*'),
        DIVIDE('÷'),
        PERCENT('%')
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // all number button methods go here
    fun numberClick(view: View) {
        val numberSelected = view as Button
        var number = ""
        if (lbl.text == "0") {
            lbl.text = ""
        }

        // finding which button was selected
        when (numberSelected.id) {
            button0.id -> number = "0"
            button1.id -> number = "1"
            button2.id -> number = "2"
            button3.id -> number = "3"
            button4.id -> number = "4"
            button5.id -> number = "5"
            button6.id -> number = "6"
            button7.id -> number = "7"
            button8.id -> number = "8"
            button9.id -> number = "9"
        }
        lbl.append(number)
    }

    // all operator button methods go here
    // only allows one operator per calculation (scope limitation)
    fun operatorClick(view: View) {
        val operatorSelected = view as Button
        var operator = ""
        var negative = false

        // check if user has already added an operator
        // I think this first character check has fixed the issue that allows multiple operators
        for (char in lbl.text) {
            if (lbl.text[0] == '-' && !negative) {
                negative = true
                continue
            }
            if (operatorList.contains(char)) return
        }

        // finding which operator button was clicked
        when (operatorSelected.id) {
            buttonPlus.id -> operator = "+"
            buttonMinus.id -> operator = "-"
            buttonTimes.id -> operator = "*"
            buttonDivide.id -> operator = "÷"
            buttonPercent.id -> operator = "%"
        }
        lbl.append(operator)
    }

    // clear button method
    fun clickAC(view: View) {
        lbl.text = ""
    }

    // adding decimals to numbers
    // TODO : implement so user can only enter one decimal per number
    fun clickDecimal(view: View) {
        lbl.append(".")
    }

    // toggle positive and negative
    // added check so app doesn't crash if you hit button with no digits present
    fun clickToggle(view: View) {
        if (lbl.text.isEmpty()) {
            return
        }

        if (lbl.text.substring(0, 1) == "-") {
            lbl.text = lbl.text.substring(1, lbl.text.length)
        } else {
            lbl.text = "-" + lbl.text
        }
    }

    // equals button method
    // does the actual calculations and returns the solution
    // TODO : add check for hitting equals without operator
    fun clickEquals(view: View) {
        var negative = false
        if (lbl.text[0] == '-') {
            lbl.text = lbl.text.substring(1, lbl.length())
            negative = true
        }
        val numbers =  (lbl.text.split("+", "-", "*", "÷", "%"))
        var number1 = numbers[0].toDouble()
        val number2 = numbers[1].toDouble()

        // if the number is negative I am multiplying by -1
        if (negative) number1 *= -1

        for (char in lbl.text) {
            if (char == Operator.PLUS.op) {
                lbl.text = (number1 + number2).toString()
                return
            } else if (char == Operator.MINUS.op) {
                lbl.text = (number1 - number2).toString()
                return
            } else if (char == Operator.MULTIPLY.op) {
                lbl.text = (number1 * number2).toString()
                return
            } else if (char == Operator.DIVIDE.op) {
                if (number2 == 0.0) {
                    break
                }
                lbl.text = (number1 / number2).toString()
                return
            } else if (char == Operator.PERCENT.op) {
                lbl.text = ((number1 * number2) / 100).toString()
                return
            }
        }
        lbl.text = "ERROR"
    }
}
