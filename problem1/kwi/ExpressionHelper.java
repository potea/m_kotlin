package com.codemaker.reactivesample;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;

/**
 * Created by kwi on 2017-03-07.
 */

public class ExpressionHelper {

    public static JexlEngine sJexl = new JexlBuilder().create();

    public static String convert(String exp, String errorMsg) {

        JexlExpression jexlExp;
        try {
            jexlExp = sJexl.createExpression(exp);

            // Create a context and add data
            JexlContext jc = new MapContext();
            jc.set("exp", exp);

            // Now evaluate the expression, getting the result
            Number number = (Number) jexlExp.evaluate(jc);
            return number.toString();
        } catch (Exception e) {
            return errorMsg;
        }
    }
}
