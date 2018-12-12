package com.mashupgroup.weatherbear

class Temperature {
    companion object {

        /**
         * Convert tomorrowTemperature unit from celsius to fahrenheit
         */
        fun convertCtoF(celsius: Float): Float {
            return celsius * 1.8f + 32
        }

        /**
         * Convert tomorrowTemperature unit from fahrenheit to celsius
         */
        fun convertFtoC(fahrenheit: Float): Float {
            return (fahrenheit - 32) / 1.8f
        }

    }
}