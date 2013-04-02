/*
 * Copyright 2013- Yan Bonnel
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.ybonnel.simpleweb4j.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

public class SimpleEntityManagerUnitTest {

    @Before
    public void setup() {
        try {
            SimpleEntityManager.closeSession();
        } catch (Exception ignore){}
        SimpleEntityManager.hasHibernate = null;
    }

    @After
    public void tearDown() {
        try {
            SimpleEntityManager.closeSession();
        } catch (Exception ignore){}
        SimpleEntityManager.ENTITY_CLASS = "javax.persistence.Entity";
        SimpleEntityManager.hasHibernate = null;
    }

    @Test
    public void testOpenAlreadyOpenSession() {
        SimpleEntityManager.openSession();
        try {
            SimpleEntityManager.openSession();
            fail("An exception must be throw");
        } catch (IllegalStateException ignore){

        }
    }

    @Test(expected = IllegalStateException.class)
    public void testCloseSessionAlreadyClosed() {
        SimpleEntityManager.closeSession();
    }

    @Test
    public void stupidTestOnPrivateConstructor() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        for (Class clazz : SimpleEntityManager.class.getDeclaredClasses()) {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        }
    }

    @Test
    public void testHasEntitiesWithClassNotFound() {
        SimpleEntityManager.ENTITY_CLASS = "notexists.Class";

        assertFalse(SimpleEntityManager.hasEntities());
    }

    @Test
    public void testHasEntitiesWithNoEntities() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        Object oldAnnotatedClasses;
        Class<?> clazz = Class.forName("fr.ybonnel.simpleweb4j.model.SimpleEntityManager$AnnotatedClassesHelper");
        Field annotatedClasses = clazz.getDeclaredField("annotatedClasses");
        annotatedClasses.setAccessible(true);
        oldAnnotatedClasses = annotatedClasses.get(null);

        annotatedClasses.set(null, new ArrayList<Class<?>>());

        assertFalse(SimpleEntityManager.hasEntities());

        annotatedClasses.set(null, oldAnnotatedClasses);

    }
}
