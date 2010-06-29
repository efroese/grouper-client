/*
 * Copyright 2002-2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.internet2.middleware.grouperClientExt.org.apache.commons.jexl.resolver;

import edu.internet2.middleware.grouperClientExt.org.apache.commons.jexl.JexlContext;
import edu.internet2.middleware.grouperClientExt.org.apache.commons.jexl.JexlExprResolver;

/**
 *  Simple resolver to try the expression as-is from the context.
 *
 *  For example, you could resolve ant-ish properties (foo.bar.woogie)
 *  using this...
 *
 *  hint, hint...
 *
 *  @since 1.0
 *  @author <a href="mailto:geirm@adeptra.com">Geir Magnusson Jr.</a>
 *  @version $Id: FlatResolver.java,v 1.1 2008-11-30 10:57:31 mchyzer Exp $
 */
public class FlatResolver implements JexlExprResolver {
    /**
     *  Flag to return NO_VALUE on null from context.
     *  this allows jexl to try to evaluate
     */
    protected boolean noValOnNull = true;

    /**
     * Default CTOR.
     */
    public FlatResolver() {
    }

    /**
     *  CTOR that lets you override the default behavior of
     *  noValOnNull, which is true. (jexl gets a shot after if null)
     *
     *  @param valOnNull Whether NO_VALUE will be returned instead of null.
     */
    public FlatResolver(boolean valOnNull) {
        noValOnNull = valOnNull;
    }

    /**
     *  Try to resolve expression as-is.
     *
     *  @param context The context for resolution.
     *  @param expression The flat expression.
     *  @return The resolved value.
     */
    public Object evaluate(JexlContext context, String expression) {
        Object val = context.getVars().get(expression);

        if (val == null && noValOnNull) {
            return JexlExprResolver.NO_VALUE;
        }

        return val;
    }
}
