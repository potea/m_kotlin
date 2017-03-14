package com.codemaker.reactivesample.kotlin.expression.function

import com.codemaker.reactivesample.kotlin.KwiSeet
import com.codemaker.reactivesample.kotlin.expression.ExpressionConst
import com.codemaker.reactivesample.kotlin.expression.FunctionExpression

/**
 * Created by kwi on 2017-03-10.
 */
class SumFunction(override var expString: String) : FunctionExpression() {

    override fun getFunctionName(): String {
        return ExpressionConst.EXPRESSION_SUM
    }

    override fun calc(): String {
        var result = ""
        val range = getFunctionRange()

        for (c in range.start..range.end) {
            if (KwiSeet.getCellByLabel(c)?.calc()?.isNotEmpty()!!) {
                result += KwiSeet.getCellByLabel(c)?.calc() + "+"
            }
        }

        if (result.endsWith('+')) {
            result = result.dropLast(1)
        }

        return result
    }
}