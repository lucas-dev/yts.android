package com.alvefard.yify.presentation.util

object StringUtils {
    fun getFormattedRuntime(time: Int): String {
        return try {
            val hours = time / 60
            val minutes = time % 60
            String.format("%dh:%02dm", hours, minutes)
        } catch (e: Exception) {
            "-"
        }

    }

    fun getRatedInfo(rated: String): String {
        return when (rated) {
            "PG" -> return "PG – Parental Guidance Suggested"
            "G" -> return "G – General Audiences"
            "PG-13" -> return "PG-13 – Parents Strongly Cautioned"
            "R" -> return "R – Restricted"
            "NC-17" -> return "NC-17 – Adults Only"
            else -> rated
        }
    }
}