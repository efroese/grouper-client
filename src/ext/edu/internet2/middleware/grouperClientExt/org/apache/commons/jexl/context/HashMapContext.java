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

package edu.internet2.middleware.grouperClientExt.org.apache.commons.jexl.context;

import edu.internet2.middleware.grouperClientExt.org.apache.commons.jexl.JexlContext;

import java.util.HashMap;
import java.util.Map;

/**
 *  Implementation of JexlContext based on a HashMap.
 *
 *  @since 1.0
 *  @author <a href="mailto:geirm@apache.org">Geir Magnusson Jr.</a>
 *  @version $Id: HashMapContext.java,v 1.1 2008-11-30 10:57:26 mchyzer Exp $
 */
public class HashMapContext extends HashMap implements JexlContext {
    /** serialization version id jdk13 generated. */
    static final long serialVersionUID = 5715964743204418854L;
    /**
     * {@inheritDoc}
     */
    public void setVars(Map vars) {
        clear();
        putAll(vars);
    }

    /**
     * {@inheritDoc}
     */
    public Map getVars() {
        return this;
    }
}
