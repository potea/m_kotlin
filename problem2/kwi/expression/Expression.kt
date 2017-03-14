package com.codemaker.reactivesample.kotlin.expression

/**
 * Created by kwi on 2017-03-10.
 */
abstract class Expression {
    abstract var expString: String
    abstract fun calc(): String
}