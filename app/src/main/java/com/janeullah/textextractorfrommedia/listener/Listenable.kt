package com.janeullah.textextractorfrommedia.listener


interface Listenable<T, U> {

    fun onSuccess(arg: U): T

}
