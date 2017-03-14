package com.codemaker.reactivesample.kotlin

import com.codemaker.reactivesample.kotlin.expression.*
import com.codemaker.reactivesample.kotlin.expression.function.AverageFunction
import com.codemaker.reactivesample.kotlin.expression.function.MaxFunction
import com.codemaker.reactivesample.kotlin.expression.function.MinFunction
import com.codemaker.reactivesample.kotlin.expression.function.SumFunction

/**
 * Created by kwi on 2017-03-14.
 */
object Parser {
    fun parseAndRegister(text: String, kwiCell: KwiCell): Expression {
        if (text.startsWith('=')) {
            var temp = text.substring(1).replace(" ", "")

            //check function
            var function: FunctionExpression? = null
            if(temp.startsWith(ExpressionConst.EXPRESSION_SUM)) {
                function = SumFunction(text)
            } else if(temp.startsWith(ExpressionConst.EXPRESSION_AVERAGE)) {
                function = AverageFunction(text)
            } else if(temp.startsWith(ExpressionConst.EXPRESSION_MAX)) {
                function = MaxFunction(text)
            } else if(temp.startsWith(ExpressionConst.EXPRESSION_MIN)) {
                function = MinFunction(text)
            }

            if(function != null) {
                val range = function.getFunctionRange()
                for(c in range.start .. range.end) {
                    if(KwiSeet.getCellByLabel(c) != null) {
                        if(!kwiCell.checkCycle(KwiSeet.getCellByLabel(c)!!)) {
                            kwiCell.addRegisteredList(c)
                        }
                    }
                }
                return function
            }

            //check numeric
            for(char in temp) {
                if(KwiSeet.getCellByLabel(char) != null) {
                    if(kwiCell.checkCycle(KwiSeet.getCellByLabel(char)!!)) {
                        kwiCell.clearRegisteredList()
                        return ErrorExpression(text)
                    }
                    kwiCell.addRegisteredList(char)
                }
            }
            return NumericExpression(text)
        } else {
            return ConstantExpression(text)
        }
    }
}