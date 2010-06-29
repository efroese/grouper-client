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
package edu.internet2.middleware.grouperClientExt.org.apache.commons.jexl.parser;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

import edu.internet2.middleware.grouperClientExt.org.apache.commons.jexl.JexlContext;
import edu.internet2.middleware.grouperClientExt.org.apache.commons.jexl.util.Introspector;
import edu.internet2.middleware.grouperClientExt.org.apache.commons.jexl.util.introspection.Info;
import edu.internet2.middleware.grouperClientExt.org.apache.commons.jexl.util.introspection.VelMethod;

/**
 * generalized size() function for all classes we can think of.
 * 
 * @author <a href="mailto:geirm@apache.org">Geir Magnusson Jr.</a>
 * @author <a href="hw@kremvax.net">Mark H. Wilkinson</a>
 * @version $Id: ASTSizeFunction.java,v 1.1 2008-11-30 10:57:25 mchyzer Exp $
 */
public class ASTSizeFunction extends SimpleNode {
    /**
     * Create the node given an id.
     * 
     * @param id node id.
     */
    public ASTSizeFunction(int id) {
        super(id);
    }

    /**
     * Create a node with the given parser and id.
     * 
     * @param p a parser.
     * @param id node id.
     */
    public ASTSizeFunction(Parser p, int id) {
        super(p, id);
    }

    /** {@inheritDoc} */
    public Object jjtAccept(ParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    /** {@inheritDoc} */
    public Object value(JexlContext jc) throws Exception {
        SimpleNode arg = (SimpleNode) jjtGetChild(0);

        Object val = arg.value(jc);

        if (val == null) {
            throw new Exception("size() : null arg");
        }

        return new Integer(ASTSizeFunction.sizeOf(val));
    }

    /**
     * Calculate the <code>size</code> of various types: Collection, Array, Map, String,
     * and anything that has a int size() method.
     * 
     * @param val the object to get the size of.
     * @return the size of val
     * @throws Exception if the size cannot be determined.
     */
    public static int sizeOf(Object val) throws Exception {
        if (val instanceof Collection) {
            return ((Collection) val).size();
        } else if (val.getClass().isArray()) {
            return Array.getLength(val);
        } else if (val instanceof Map) {
            return ((Map) val).size();
        } else if (val instanceof String) {
            return ((String) val).length();
        } else {
            // check if there is a size method on the object that returns an
            // integer
            // and if so, just use it
            Object[] params = new Object[0];
            Info velInfo = new Info("", 1, 1);
            VelMethod vm = Introspector.getUberspect().getMethod(val, "size", params, velInfo);
            if (vm != null && vm.getReturnType() == Integer.TYPE) {
                Integer result = (Integer) vm.invoke(val, params);
                return result.intValue();
            }
            throw new Exception("size() : unknown type : " + val.getClass());
        }
    }

}
