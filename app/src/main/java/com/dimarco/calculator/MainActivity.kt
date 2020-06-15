package com.dimarco.calculator

import android.graphics.Path
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.ceil
import kotlin.math.floor

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
    // TODO : find way to remove this array and replace with enum
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
    fun clickDecimal(view: View) {
        var decimalUsed = false

        for (char in lbl.text) {
            if (char == '.' ) {
                decimalUsed = true
            }

            if (operatorList.contains(char)) {
                decimalUsed = false
            }
        }

        if (!decimalUsed) {
            lbl.append(".")
        }
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

    // will check if the input is a whole number or not
    private fun isWholeNumber(input: Double): Boolean {
        return (input == floor(input) && input == ceil(input))
    }

    // will round any results to a maximum of six decimal places
    private fun round(input: Double): Double {
        val df = DecimalFormat("#.######")
        df.roundingMode = RoundingMode.CEILING
        return df.format(input).toDouble()
    }

    // equals button method
    // does the actual calculations and returns the solution
    // TODO : add ability to change text size if too many digits are added
    fun clickEquals(view: View) {
        // variables needed to find solution
        val wholeNumber: Int?
        var result: Double = 0.0
        var negative: Boolean = false
        var error: Boolean = true

        // initializing number variables
        var numbers: List<String>
        var number1: Double
        var number2: Double

        // checking for negative sign
        if (lbl.text[0] == '-') {
            lbl.text = lbl.text.substring(1, lbl.length())
            negative = true
        }

        // running a try catch to protect against missing numbers / operators
        try {
            // finding the delimiter and then splitting the two numbers
            numbers = (lbl.text.split(Operator.PLUS.op, Operator.MINUS.op, Operator.MULTIPLY.op, Operator.DIVIDE.op, Operator.PERCENT.op))
            number1 = numbers[0].toDouble()
            number2 = numbers[1].toDouble()
        } catch (e: Exception) {
            // TODO : specific error messages
            e.printStackTrace()
            lbl.text = "Missing Error"
            return
        }

        // if the number is negative I am multiplying by -1
        if (negative) number1 *= -1

        // iterating through to find the operator
        loop@ for (char in lbl.text) {
            when (char) {
                Operator.PLUS.op -> {
                    result = number1 + number2
                    error = false
                    break@loop
                }

                Operator.MINUS.op -> {
                    result = number1 - number2
                    error = false
                    break@loop
                }

                Operator.MULTIPLY.op -> {
                    result = number1 * number2
                    error = false
                    break@loop
                }

                Operator.DIVIDE.op -> {
                    // checking for division by 0
                    if (number2 == 0.0) {
                        lbl.text = "ERROR"
                        return
                    }
                    result = number1 / number2
                    error = false
                    break@loop
                }

                Operator.PERCENT.op -> {
                    result = ((number1 * number2) / 100)
                    error = false
                    break@loop
                }
            }
        }

        // checking to see if it hit an error along the way
        if (error) {
            lbl.text = "Error"
            return
        }

        // checking to see if it needs to return an int or a double
        if (isWholeNumber(result)) {
            wholeNumber = result.toInt()
            lbl.text = wholeNumber.toString()
        } else {
            result = round(result)
            lbl.text = result.toString()
        }
    }
}
