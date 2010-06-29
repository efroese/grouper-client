/*
 * Copyright (C) 2006 Joe Walnes.
 * Copyright (C) 2006, 2007 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 04. June 2006 by Joe Walnes
 */
package edu.internet2.middleware.grouperClientExt.com.thoughtworks.xstream.io.binary;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import edu.internet2.middleware.grouperClientExt.com.thoughtworks.xstream.io.ExtendedHierarchicalStreamWriter;
import edu.internet2.middleware.grouperClientExt.com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import edu.internet2.middleware.grouperClientExt.com.thoughtworks.xstream.io.StreamException;

/**
 * @since 1.2
 */
public class BinaryStreamWriter implements ExtendedHierarchicalStreamWriter {

    private final IdRegistry idRegistry = new IdRegistry();
    private final DataOutputStream out;
    private final Token.Formatter tokenFormatter = new Token.Formatter();

    public BinaryStreamWriter(OutputStream outputStream) {
        out = new DataOutputStream(outputStream);
    }

    public void startNode(String name) {
        write(new Token.StartNode(idRegistry.getId(name)));
    }

    public void startNode(String name, Class clazz) {
        startNode(name);
    }

    public void addAttribute(String name, String value) {
        write(new Token.Attribute(idRegistry.getId(name), value));
    }

    public void setValue(String text) {
        write(new Token.Value(text));
    }

    public void endNode() {
        write(new Token.EndNode());
    }

    public void flush() {
        try {
            out.flush();
        } catch (IOException e) {
            throw new StreamException(e);
        }
    }

    public void close() {
        try {
            out.close();
        } catch (IOException e) {
            throw new StreamException(e);
        }
    }

    public HierarchicalStreamWriter underlyingWriter() {
        return this;
    }

    private void write(Token token) {
        try {
            tokenFormatter.write(out, token);
        } catch (IOException e) {
            throw new StreamException(e);
        }
    }

    private class IdRegistry {

        private long nextId = 0;
        private Map ids = new HashMap();

        public long getId(String value) {
            Long id = (Long) ids.get(value);
            if (id == null) {
                id = new Long(++nextId);
                ids.put(value, id);
                write(new Token.MapIdToValue(id.longValue(), value));
            }
            return id.longValue();
        }

    }
}
