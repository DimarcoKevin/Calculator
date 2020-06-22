package com.dimarco.calculator

import android.graphics.Path
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.lang.NumberFormatException
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
            button0.id -> number = resources.getString(R.string.zero)
            button1.id -> number = resources.getString(R.string.one)
            button2.id -> number = resources.getString(R.string.two)
            button3.id -> number = resources.getString(R.string.three)
            button4.id -> number = resources.getString(R.string.four)
            button5.id -> number = resources.getString(R.string.five)
            button6.id -> number = resources.getString(R.string.six)
            button7.id -> number = resources.getString(R.string.seven)
            button8.id -> number = resources.getString(R.string.eight)
            button9.id -> number = resources.getString(R.string.nine)
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

            // this checks if the character is an operator
            if (Operator.values().any { it.op == char }) return
        }

        // finding which operator button was clicked
        when (operatorSelected.id) {
            buttonPlus.id -> operator = "+" // Operator.PLUS.op.toString() // This works
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

            // this checks if the character is an operator
            if (Operator.values().any { it.op == char }) {
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

        // checking is string is null or empty
        if (lbl.text == null || lbl.text == "") {
            lbl.text = "Null Error"
            return
        }

        // checking for negative sign
        if (lbl.text[0] == '-') {
            lbl.text = lbl.text.substring(1, lbl.length())
            negative = true
        }

        // running a try catch to protect against missing numbers / operators
        try {
            // finding the delimiter and then splitting the two numbers
            numbers = (lbl.text.split(
                Operator.PLUS.op,
                Operator.MINUS.op,
                Operator.MULTIPLY.op,
                Operator.DIVIDE.op,
                Operator.PERCENT.op
            ))
            number1 = numbers[0].toDouble()
            number2 = numbers[1].toDouble()

        } catch (ex: NumberFormatException) {
            ex.printStackTrace()
            lbl.text = "Number Error"
            return
        } catch (e: Exception) {
            e.printStackTrace()
            lbl.text = "Op Missing"
            return
        }

        // if the number is negative, multiplying by -1
        if (negative) number1 *= -1

        // iterating through to find the operator and perform chosen operation
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
                        error = true
                        break@loop
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
