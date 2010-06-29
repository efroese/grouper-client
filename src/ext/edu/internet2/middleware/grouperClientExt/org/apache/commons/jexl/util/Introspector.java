/*
 * Copyright 2002,2004 The Apache Software Foundation.
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
package edu.internet2.middleware.grouperClientExt.org.apache.commons.jexl.util;

import edu.internet2.middleware.grouperClientExt.org.apache.commons.jexl.util.introspection.Uberspect;
import edu.internet2.middleware.grouperClientExt.org.apache.commons.jexl.util.introspection.UberspectImpl;
import edu.internet2.middleware.grouperClientExt.org.apache.commons.jexl.util.introspection.UberspectLoggable;
import edu.internet2.middleware.grouperClientExt.org.apache.commons.logging.Log;
import edu.internet2.middleware.grouperClientExt.org.apache.commons.logging.LogFactory;

/**
 *  Little class to manage a Velocity uberspector (Vel 1.4+) for instrospective
 *  services.
 *
 *  @since 1.0
 *  @author <a href="mailto:geirm@apache.org">Geir Magnusson Jr.</a>
 *  @version $Id: Introspector.java,v 1.1 2008-11-30 10:57:26 mchyzer Exp $
 */
public class Introspector {
    /**
     *  The uberspector from Velocity that handles all instrospection patterns.
     */
    private static Uberspect uberSpect;

    static {

        Log logger = LogFactory.getLog(Introspector.class);

        uberSpect = new UberspectImpl();
        ((UberspectLoggable) uberSpect).setRuntimeLogger(logger);
    }

    /**
     *  For now, expose the raw uberspector to the AST.
     *
     *  @return Uberspect The Velocity uberspector.
     */
    public static Uberspect getUberspect() {
        return uberSpect;
    }
}
