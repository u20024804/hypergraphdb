/*
 * This file is part of the HyperGraphDB source distribution. This is copyrighted
 * software. For permitted uses, licensing options and redistribution, please see 
 * the LicensingInformation file at the root level of the distribution. 
 *
 * Copyright (c) 2005-2006
 *  Kobrix Software, Inc.  All rights reserved.
 */
/*
 * Created on Aug 11, 2005
 *
 */
package org.hypergraphdb.query;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HyperGraph;

/**
 * <p>
 * The <code>AtomValueCondition</code> represents a query condition on
 * the atom value. The condition specifies a comparison operator and
 * value to compare against. The possible comparison operators are
 * the constants listed in the <code>ComparisonOperator</code> class. 
 * The value compared against must be of a recognizable <code>HGAtomType</code>.
 * </p>
 * 
 * @author Borislav Iordanov
 */
public class AtomValueCondition extends SimpleValueCondition 
{
   
	public AtomValueCondition(Object value)
	{
		super(value);
	}

    public AtomValueCondition(Object value, ComparisonOperator operator)
    {
    	super(value, operator);
    }

	protected boolean satisfies(HyperGraph hg, HGHandle atomHandle, Object atom, HGHandle type) 
	{
		return compareToValue(hg, atom, type);
	}
	
	public String toString()
	{
		StringBuffer result = new StringBuffer("valueIs(");
		result.append(getOperator());
		result.append(",");
		result.append(String.valueOf(getValue()));
		result.append(")");
		return result.toString();
	}
}