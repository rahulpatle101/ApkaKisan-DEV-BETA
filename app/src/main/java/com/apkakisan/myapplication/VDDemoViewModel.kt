package com.apkakisan.myapplication

class VDDemoViewModel {

    fun incrementCount(count: String): String {
        val count: Int = Integer.parseInt(count)
        return (count + 1).toString()
    }
}