package com.codemaker.reactivesample.kotlin.expression

/**
 * Created by kwi on 2017-03-10.
 */
class ConstantExpression(override var expString: String) : Expression() {

    override fun calc(): String {
        return expString
    }
}