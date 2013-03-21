/*******************************************************************************
 * Copyright (c) 1998, 2013 Oracle and/or its affiliates. All rights reserved.
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0 
 * which accompanies this distribution. 
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at 
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     Oracle - initial API and implementation from Oracle TopLink
 ******************************************************************************/  
package org.eclipse.persistence.testing.tests.jpa.fieldaccess.fetchgroups;

import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * <p><b>Purpose</b>: To collect the tests that will run against Application Server only.
 */
public class FAServerTestSuite extends TestSuite {
    
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.setName("FieldAccess FetchGroups ServerTestSuite");
        suite.addTest(FetchGroupMergeWithCacheTests.suite());
        suite.addTest(FetchGroupTrackerWeavingTests.suite());
        suite.addTest(NestedDefaultFetchGroupTests.suite());
        suite.addTest(NestedFetchGroupTests.suite());
        suite.addTest(NestedNamedFetchGroupTests.suite());
        suite.addTest(SimpleDefaultFetchGroupTests.suite()); 
        suite.addTest(SimpleFetchGroupTests.suite());
        suite.addTest(SimpleNamedFetchGroupTests.suite());
        suite.addTest(SimpleSerializeFetchGroupTests.suite());
        return suite;
    }
}
