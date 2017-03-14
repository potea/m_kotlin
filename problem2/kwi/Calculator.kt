package com.codemaker.reactivesample.kotlin

import com.codemaker.reactivesample.kotlin.expression.Expression
import com.codemaker.reactivesample.kotlin.expression.ExpressionConst.ERROR_MSG

/**
 * Created by kwi on 2017-03-10.
 */
object Calculator {

    fun <T : Expression?> calc(exp: T): String {
        if(exp == null) {
            return ERROR_MSG
        } else {
            return exp.calc()
        }
    }
}