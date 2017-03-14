package com.codemaker.reactivesample.kotlin.expression

import com.codemaker.reactivesample.kotlin.KwiSeet

/**
 * Created by kwi on 2017-03-10.
 */

class NumericExpression(override var expString: String) : Expression(){

    override fun calc(): String {
        var temp = ""
        var exp = expString.substring(1)//= 제거
        for(char in exp) {
            if(KwiSeet.getCellByLabel(char) != null) {
                temp += KwiSeet.getCellByLabel(char)?.calc()
            } else {
                temp += char
            }
        }
        return temp
    }

}