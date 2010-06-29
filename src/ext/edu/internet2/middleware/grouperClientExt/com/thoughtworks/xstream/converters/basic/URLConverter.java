/*
 * Copyright (C) 2004 Joe Walnes.
 * Copyright (C) 2006, 2007 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 25. March 2004 by Joe Walnes
 */
package edu.internet2.middleware.grouperClientExt.com.thoughtworks.xstream.converters.basic;

import java.net.MalformedURLException;
import java.net.URL;

import edu.internet2.middleware.grouperClientExt.com.thoughtworks.xstream.converters.ConversionException;

/**
 * Converts a java.net.URL to a string.
 *
 * @author J. Matthew Pryor
 */
public class URLConverter extends AbstractSingleValueConverter {

    public boolean canConvert(Class type) {
        return type.equals(URL.class);
    }

    public Object fromString(String str) {
        try {
            return new URL(str);
        } catch (MalformedURLException e) {
            throw new ConversionException(e);
        }
    }

}
