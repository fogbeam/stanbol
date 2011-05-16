/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.stanbol.entityhub.yard.solr;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.io.File;
import java.util.Arrays;

import org.apache.stanbol.entityhub.servicesapi.model.Representation;
import org.apache.stanbol.entityhub.servicesapi.query.FieldQuery;
import org.apache.stanbol.entityhub.servicesapi.query.QueryResultList;
import org.apache.stanbol.entityhub.servicesapi.query.TextConstraint;
import org.apache.stanbol.entityhub.servicesapi.yard.Yard;
import org.apache.stanbol.entityhub.servicesapi.yard.YardException;
import org.apache.stanbol.entityhub.test.yard.YardTest;
import org.apache.stanbol.entityhub.yard.solr.impl.SolrYard;
import org.apache.stanbol.entityhub.yard.solr.impl.SolrYardConfig;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This test uses the system property "basedir" to configure an embedded Solr Server. This property is set by
 * the mvn surefire plugin. When using this Unit Test within a build environment that does not set this
 * property one need to set it manually to the base directory of this module.
 * <p>
 * 
 * @author Rupert Westenthaler
 * 
 */
public class SolrYardTest extends YardTest {
    /**
     * The SolrYard used for the tests
     */
    private static Yard yard;
    /**
     * The SolrDirectoryManager also tested within this unit test
     */
    public static final String TEST_YARD_ID = "testYard";
    public static final String TEST_SOLR_CORE_NAME = "test";
    protected static final String TEST_INDEX_REL_PATH = File.separatorChar + "target" + File.separatorChar
                                                        + SolrDirectoryManager.DEFAULT_SOLR_DATA_DIR;
    private static final Logger log = LoggerFactory.getLogger(SolrYardTest.class);

    @BeforeClass
    public static final void initYard() throws YardException {
        // get the working directory
        // use property substitution to test this feature!
        String prefix = System.getProperty("basedir") == null ? "." : "${basedir}";
        String solrServerDir = prefix + TEST_INDEX_REL_PATH;
        log.info("Test Solr Server Directory: " + solrServerDir);
        System.setProperty(SolrDirectoryManager.MANAGED_SOLR_DIR_PROPERTY, solrServerDir);
        SolrYardConfig config = new SolrYardConfig(TEST_YARD_ID, TEST_SOLR_CORE_NAME);
        config.setName("Solr Yard Test");
        config.setDescription("The Solr Yard instance used to execute the Unit Tests defined for the Yard Interface");
        // create the Yard used for the tests
        yard = new SolrYard(config);
    }

    @Override
    protected Yard getYard() {
        return yard;
    }

    /*
     * Three unit tests that check that SolrYardConfig does throw IllegalArgumentExceptions when parsing an
     * illegal parameters.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolrYardConfigInitWithNullParams() {
        new SolrYardConfig(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolrYardConfigInitWithNullUrl() {
        new SolrYardConfig(TEST_YARD_ID, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolrYardConfigInitWithNullID() {
        new SolrYardConfig(null, TEST_SOLR_CORE_NAME);
    }
    
    @Test
    public void testFieldQuery() throws YardException {
        // NOTE: this does not test if the updated view of the representation is
        // stored, but only that the update method works correctly
        Yard yard = getYard();
        
        String id1 = "urn:yard.test.testFieldQuery:representation.id1";
        String id2 = "urn:yard.test.testFieldQuery:representation.id2";
        String field = "urn:the.field:used.for.this.Test";
        Representation test1 = create(id1, true);
        Representation test2 = create(id2, true);
        // change the representations to be sure to force an update even if the
        // implementation checks for changes before updating a representation
        test1.add(field, "This is the text content of a field with value1.");
        test2.add(field, "This is the text content of a field with value2.");
        Iterable<Representation> updatedIterable = yard.update(Arrays.asList(test1, test2));
        assertNotNull(updatedIterable);

        FieldQuery query = yard.getQueryFactory().createFieldQuery();
        query.setConstraint(field, new TextConstraint("text content"));
        QueryResultList<Representation> results = yard.find(query);
        assertEquals(2, results.size());

        // fetch the light / minimal representation
        query = yard.getQueryFactory().createFieldQuery();
        query.setConstraint(field, new TextConstraint("value2"));
        results = yard.find(query);
        assertEquals(1, results.size());
        Representation result = results.iterator().next();
        assertEquals("urn:yard.test.testFieldQuery:representation.id2", result.getId());
        assertEquals(null, result.getFirst(field));

        // fetch the full representation
        results = yard.findRepresentation(query);
        assertEquals(1, results.size());
        result = results.iterator().next();
        assertEquals("urn:yard.test.testFieldQuery:representation.id2", result.getId());
        assertEquals("This is the text content of a field with value2.", result.getFirst(field));
    }

    /**
     * This Method removes all Representations create via {@link #create()} or
     * {@link #create(String, boolean)} from the tested {@link Yard}. It also removes all Representations
     * there ID was manually added to the {@link #representationIds} list.
     */
    @AfterClass
    public static final void clearUpRepresentations() throws YardException {
        yard.remove(representationIds);
    }

}
