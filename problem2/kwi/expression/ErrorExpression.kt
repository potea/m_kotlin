package com.codemaker.reactivesample.kotlin.expression

/**
 * Created by kwi on 2017-03-14.
 */
class ErrorExpression(override var expString: String) : Expression() {

    override fun calc(): String {
        return ExpressionConst.ERROR_MSG
    }
}