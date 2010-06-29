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

package edu.internet2.middleware.grouperClientExt.org.apache.commons.jexl.util.introspection;

import edu.internet2.middleware.grouperClientExt.org.apache.commons.logging.Log;

/**
 * Marker interface to let an uberspector indicate it can and wants to log.
 * 
 * Thanks to Paulo for the suggestion
 * 
 * @since 1.0
 * @author <a href="mailto:geirm@apache.org">Geir Magnusson Jr.</a>
 * @version $Id: UberspectLoggable.java,v 1.1 2008-11-30 10:57:20 mchyzer Exp $
 * 
 */
public interface UberspectLoggable {
    /**
     * Sets the logger. This will be called before any calls to the uberspector
     * @param logger the logger.
     */
    void setRuntimeLogger(Log logger);
}
