/*
 * Copyright (c) 1998, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0,
 * or the Eclipse Distribution License v. 1.0 which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: EPL-2.0 OR BSD-3-Clause
 */

// Contributors:
//     Denise Smith - 2.4 - April 2012
package org.eclipse.persistence.testing.jaxb.json.attribute;

import org.eclipse.persistence.jaxb.MarshallerProperties;

public class JSONAttributeNoXmlRootElementIncludeRootFalseTestCases extends JSONAttributeNoXmlRootElementTestCases{

    public JSONAttributeNoXmlRootElementIncludeRootFalseTestCases(String name) throws Exception {
        super(name);
        jaxbMarshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);

    }

}
