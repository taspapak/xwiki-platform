/*
 * Copyright 2006-2007, XpertNet SARL, and individual contributors as indicated
 * by the contributors.txt.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *
 * @author ludovic
 * @author erwan
 * @author sdumitriu
 */
package com.xpn.xwiki.api;

import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.XWikiException;
import com.xpn.xwiki.objects.BaseObject;
import com.xpn.xwiki.objects.classes.BaseClass;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

public class Class extends Collection
{
    public Class(BaseClass obj, XWikiContext context)
    {
        super(obj, context);
    }

    protected BaseClass getBaseClass()
    {
        return (BaseClass) getCollection();
    }

    public java.lang.Object[] getPropertyNames()
    {
        Element[] properties = getProperties();
        if (properties == null) {
            return super.getPropertyNames();
        }
        String[] props = new String[properties.length];
        for (int i = 0; i < properties.length; i++) {
            String propname = properties[i].getName();
            props[i] = propname;
        }
        return props;
    }

    /**
     * @return an array with the properties of the class
     */
    public Element[] getProperties()
    {
        java.util.Collection coll = getCollection().getFieldList();
        if (coll == null) {
            return null;
        }
        PropertyClass[] properties = new PropertyClass[coll.size()];
        int i = 0;
        for (Iterator it = coll.iterator(); it.hasNext(); i++) {
            properties[i] = new PropertyClass(
                (com.xpn.xwiki.objects.classes.PropertyClass) it.next(), getContext());
        }
        Arrays.sort(properties, new PropertyComparator());
        return properties;
    }

    /**
     * @param name the name of the element
     * @return the PropertyClass for the given name
     * @see PropertyClass
     * @see Element
     */
    public Element get(String name)
    {
        return new PropertyClass(
            (com.xpn.xwiki.objects.classes.PropertyClass) getCollection().safeget(name),
            getContext());
    }

    /**
     * @return the BaseClass (without the wrapping) if you have the programming right.
     */
    public BaseClass getXWikiClass()
    {
        if (hasProgrammingRights()) {
            return (BaseClass) getCollection();
        } else {
            return null;
        }
    }

    /**
     * @return a new object from this class
     */
    public Object newObject() throws XWikiException
    {
        BaseObject obj = (BaseObject) getBaseClass().newObject(getContext());
        return obj.newObjectApi(obj, getContext());
    }
}

class PropertyComparator implements Comparator
{
    public int compare(java.lang.Object o1, java.lang.Object o2)
    {
        PropertyClass po1 = (PropertyClass) o1;
        PropertyClass po2 = (PropertyClass) o2;
        if (po1.getNumber() < po2.getNumber()) {
            return -1;
        }
        if (po1.getNumber() > po2.getNumber()) {
            return 1;
        }
        return 0;
    }
}