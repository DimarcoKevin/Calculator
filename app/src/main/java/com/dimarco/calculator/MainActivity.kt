package com.dimarco.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // all number button methods go here
    fun numberClick(view: View) {
        val numberSelected = view as Button
        var number = ""
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
        if (lbl.text == "0") {
            lbl.text = ""
        }
        lbl.append(number)
    }

    // all operator button methods go here
    fun operatorClick(view: View) {
        val operatorSelected = view as Button
        var operator = ""

        // finding which operator button was clicked
        when (operatorSelected.id) {
            buttonPlus.id -> operator = "+"
            buttonMinus.id -> operator = "-"
            buttonTimes.id -> operator = "*"
            buttonDivide.id -> operator = "÷"
        }
        lbl.append(operator)
    }

    // clear button method
    fun clickAC() {
        lbl.text = "0"
    }

    // equals button method
    // does the actual calculations and returns the solution
    fun clickEquals() {
        val numbers = lbl.text.split("+", "-", "*", "÷")
        val operators = mutableListOf<Char>()

        for (char in lbl.text) {
            if (char.equals("+") || char.equals("-") || char.equals("*") || char.equals("÷")) {
                operators.add(char)
            }
        }

        // needing to skip plus and minus for BEDMAS requirements
        for (operator in operators) {
            if (operator.equals("+") || operator.equals("-")) {
                continue
            } else if (operator.equals("*") || operator.equals("÷")) {
                
            }

        }

    }
}
