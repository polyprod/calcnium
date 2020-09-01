package com.app.calcnium

import android.icu.text.DecimalFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity()
{
    // Define variables for startup capital bar range:
    var minStartupCapital = 0
    var maxStartupCapital = 500000
    var stepStartupCapital = 10000

    // Define variables for monthly savings bar range:
    var minMonthlySavings = 500
    var maxMonthlySavings = 15000
    var stepMonthlySavings = 500

    // Define variables for amount of years bar range:
    var minAmountOfYears = 1
    var maxAmountOfYears = 40
    var stepAmountOfYears = 1

    // Define constant for number formatting:
    val DECIMAL_FORMAT = "###,###.#"

    // Init variables with start values (SEK) for the app:
    var startupCapital = 100000
    var monthlySavings = 6000
    var amountOfYears = 30
    var switchStateSEK = true

    // Define function for formatting numbers:
    private fun formatValue(value: Int, formatString: String): String?
    {
        val formatSymbols = android.icu.text.DecimalFormatSymbols(Locale.ENGLISH)
        formatSymbols.groupingSeparator = ' '
        formatSymbols.decimalSeparator = '.'
        val formatter = DecimalFormat(formatString, formatSymbols)
        return formatter.format(value)
    }

    // Define function for calculating both sums:
    private fun calculateSums()
    {
        var totalSum = startupCapital + 0.0 // Force this to be a double.
        var noReturnSum = startupCapital

        for (i in 1..amountOfYears)
        {
            totalSum += monthlySavings * 12
            totalSum *= 1.085
        }

        for (i in 1..amountOfYears)
        {
            noReturnSum += monthlySavings * 12
        }

        val newInt = totalSum.roundToInt()
        val newFormat = formatValue(newInt, DECIMAL_FORMAT)

        if (switchStateSEK)
        {
            amountWithReturn.text = newFormat.toString() + " kr"
        }
        else // US version:
        {
            amountWithReturn.text = "$" + newFormat.toString()
        }

        val formattedAmountNoReturn = formatValue(noReturnSum, DECIMAL_FORMAT)

        if (switchStateSEK)
        {
            amountWithoutReturn.text = formattedAmountNoReturn.toString() + " kr"
        }
        else // US version:
        {
            amountWithoutReturn.text = "$" + formattedAmountNoReturn.toString()
        }
    }

    // Define function for formatting, converting, and printing all amounts:
    fun updateAmounts()
    {
        if (switchStateSEK)
        {
            val formattedStartupCapital = formatValue(startupCapital, DECIMAL_FORMAT)
            amountStartupCapital.text = formattedStartupCapital.toString() + " kr"

            val formattedMonthlySavings = formatValue(monthlySavings, DECIMAL_FORMAT)
            amountMonthlySavings.text = formattedMonthlySavings.toString() + " kr"

            amountAmountOfYears.text = amountOfYears.toString() + " år"

            calculateSums()
        }
        else // USD version:
        {
            val formattedStartupCapital = formatValue(startupCapital, DECIMAL_FORMAT)
            amountStartupCapital.text = "$" + formattedStartupCapital.toString()

            val formattedMonthlySavings = formatValue(monthlySavings, DECIMAL_FORMAT)
            amountMonthlySavings.text = "$" + formattedMonthlySavings.toString()

            if (amountOfYears == 1)
            {
                amountAmountOfYears.text = amountOfYears.toString() + " year"
            }
            else
            {
                amountAmountOfYears.text = amountOfYears.toString() + " years"
            }

            calculateSums()
        }
    }

    // Define function for updating textViews:


    private fun updateTextViews()
    {
        if (switchStateSEK)
        {
            textWithReturn.text = "Med 8,5 % i årsavkastning:"
            textWithoutReturn.text = "Eller under madrassen..."
            textStartupCapital.text = "Startbelopp"
            textMonthlySavings.text = "Månadssparande"
            textAmountOfYears.text = "Antal år"
        }
        else
        {
            textWithReturn.text = "With 8.5 % in annual return:"
            textWithoutReturn.text = "Or under the mattress..."
            textStartupCapital.text = "Startup capital"
            textMonthlySavings.text = "Monthly savings"
            textAmountOfYears.text = "Amount of years"
        }
    }

    // Define function for updating views for startupCapital and monthlySavings
    // based on switch state:
    private fun updateAmountViews()
    {
        if (switchStateSEK)
        {
            startupCapital *= 10
            monthlySavings *= 10
        }
        else // USD version:
        {
            startupCapital /= 10
            monthlySavings /= 10
        }
    }

    // Define function for updating seekbar:
    private fun updateBars()
    {
        if (switchStateSEK)
        {
            minStartupCapital = 0
            maxStartupCapital = 500000
            stepStartupCapital = 10000
            minMonthlySavings = 500
            maxMonthlySavings = 15000
            stepMonthlySavings = 500
            minAmountOfYears = 1
            maxAmountOfYears = 40
            stepAmountOfYears = 1
        }
        else // USD version:
        {
            minStartupCapital = 0
            maxStartupCapital = 50000
            stepStartupCapital = 1000
            minMonthlySavings = 50
            maxMonthlySavings = 1500
            stepMonthlySavings = 50
            minAmountOfYears = 1
            maxAmountOfYears = 40
            stepAmountOfYears = 1
        }
    }

    // Define function for updating startupCapital and monthlySavings globals:
    private fun updateStartupAndMonthlySavings()
    {
        // Get current progress levels from bars:
        val newStartupCapital = minStartupCapital +
                (barStartupCapital.progress * stepStartupCapital)
        val newMonthlySavings = minMonthlySavings +
                (barMonthlySavings.progress * stepMonthlySavings)

        // Now update the globals:
        startupCapital = newStartupCapital
        monthlySavings = newMonthlySavings
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main) // Defines our UI.

        // Retrieve widgets from the UI we need to interact with:
        val barStartupCapital = findViewById<SeekBar>(R.id.barStartupCapital)
        val barMonthlySavings = findViewById<SeekBar>(R.id.barMonthlySavings)
        val barAmountOfYears = findViewById<SeekBar>(R.id.barAmountOfYears)

        // Set maximum value for startup capital bar:
        barStartupCapital.max = (maxStartupCapital - minStartupCapital) / stepStartupCapital

        // Set maximum value for monthly savings bar:
        barMonthlySavings.max = (maxMonthlySavings - minMonthlySavings) / stepMonthlySavings

        // Set maximum value for amount of years bar:
        barAmountOfYears.max = (maxAmountOfYears - minAmountOfYears) / stepAmountOfYears

        // Function calls:
        updateTextViews()
        updateAmounts()

        // Now keep track of switch state and bar levels:

        /* ////////////////////// APPLICATION "LOOP" BEGINS HERE //////////////////////  */

        // Current switch state:
        switchCurrency.setOnCheckedChangeListener { _, isChecked ->
            switchStateSEK = !isChecked
            updateTextViews()
            updateBars()
            updateStartupAndMonthlySavings()
            updateAmounts()
        }

        // Keep track of current progress level for startup capital bar and update global value:
        barStartupCapital?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener
        {
            override fun onStartTrackingTouch(seek: SeekBar)
            {
            }

            override fun onStopTrackingTouch(seek: SeekBar)
            {
            }

            @RequiresApi(Build.VERSION_CODES.N)
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean)
            {
                // Update value accordingly to the switch state and then the global variable:
                val progressCustom = minStartupCapital + (seek.progress * stepStartupCapital)
                startupCapital = progressCustom

                // Function call:
                updateAmounts()
            }
        })

        // Keep track of current progress level for monthly savings bar and update global:
        barMonthlySavings?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener
        {
            override fun onStartTrackingTouch(seek: SeekBar)
            {
            }

            override fun onStopTrackingTouch(seek: SeekBar)
            {
            }

            @RequiresApi(Build.VERSION_CODES.N)
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean)
            {
                // Update value accordingly to the switch state and then the global variable:
                val progressCustom = minMonthlySavings + (seek.progress * stepMonthlySavings)
                monthlySavings = progressCustom

                // Function call:
                updateAmounts()
            }
        })

        // Keep track of current progress level for amount of years bar and update global:
        barAmountOfYears?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener
        {
            override fun onStartTrackingTouch(seek: SeekBar)
            {
            }

            override fun onStopTrackingTouch(seek: SeekBar)
            {
            }

            @RequiresApi(Build.VERSION_CODES.N)
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean)
            {
                // Update value accordingly to the switch state and then the global variable:
                val progressCustom = minAmountOfYears + (seek.progress * stepAmountOfYears)
                amountOfYears = progressCustom

                // Function call:
                updateAmounts()
            }
        })
    }
}