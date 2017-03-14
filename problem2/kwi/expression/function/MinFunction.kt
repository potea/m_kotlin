package com.codemaker.reactivesample.kotlin.expression.function

import com.codemaker.reactivesample.ExpressionHelper
import com.codemaker.reactivesample.kotlin.KwiSeet
import com.codemaker.reactivesample.kotlin.expression.ExpressionConst
import com.codemaker.reactivesample.kotlin.expression.FunctionExpression

/**
 * Created by kwi on 2017-03-10.
 */
class MinFunction(override var expString: String) : FunctionExpression() {

    override fun getFunctionName(): String {
        return ExpressionConst.EXPRESSION_MIN
    }

    override fun calc(): String {
        var result = Double.MAX_VALUE
        val range = getFunctionRange()

        for(c in range.start .. range.end) {
            if(KwiSeet.getCellByLabel(c)?.calc()?.isNotEmpty()!!) {
                result = Math.min(result, ExpressionHelper.convert(KwiSeet.getCellByLabel(c)?.calc()))
            }
        }

        return result.toString()
    }
}