package com.janeullah.textextractorfrommedia.constants

/**
 * https://kotlinlang.org/docs/reference/enum-classes.html
 */
enum class RecognizableTypes(val text: String) {
    IMAGE("image"),
    DOCUMENT("document")
}

fun getMatchingRecognizer(item: String): RecognizableTypes? {
    for (value in RecognizableTypes.values()) {
        if (value.text.equals(item, ignoreCase = true)) {
            return value
        }
    }
    return null
}