package com.codemaker.reactivesample.kotlin.expression

/**
 * Created by kwi on 2017-03-10.
 */
abstract class FunctionExpression() : Expression() {

    data class Range(val start: Char, val end: Char)

    abstract fun getFunctionName(): String

    fun getFunctionRange(): Range {
        var split = expString.split(':')
        return Range(split[0][split[0].length - 1], split[1][0])
    }
}