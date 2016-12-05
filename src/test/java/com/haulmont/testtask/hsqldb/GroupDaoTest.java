/*
 * Copyright 2016 Vladimir Kizelbashev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.testtask.hsqldb;

import com.haulmont.testtask.entity.Group;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * The class {@code GroupDaoTest} represents a unit test of the GroupDao class.
 *
 * @version 1.0
 * @author Vladimir
 */
public class GroupDaoTest {

    private GroupDao groupDao;

    @Before
    public void setUp() throws Exception {
        groupDao = DaoFactory.getInstance().getGroupDao();
    }

    @After
    public void tearDown() throws Exception {
        groupDao = null;
    }

    @Test
    public void testPersist() throws Exception {
        // create test data
        Group group = new Group();
        group.setNumber(403);
        group.setFaculty("Информационных технологий");

        // test
        group = groupDao.persist(group);
        assertNotNull("Group must be not null", group);
        assertNotNull("Id must be not null", group.getId());

        // delete test data
        groupDao.delete(group);
    }

    @Test
    public void testUpdate() throws Exception {
        Group group = new Group();
        group.setNumber(303);
        group.setFaculty("Иностранных языков");
        group = groupDao.persist(group);

        try {
            group.setNumber(304);
            groupDao.update(group);
        } catch (Exception e) {
            fail("An exception occured: " + e.getMessage());
        }

        groupDao.delete(group);
    }

    @Test
    public void testDelete() throws Exception {
        Group group = new Group();
        group.setNumber(702);
        group.setFaculty("Химии");
        group = groupDao.persist(group);

        try {
            groupDao.delete(group);
        } catch (Exception e) {
            fail("An exception occured: " + e.getMessage());
        }
    }

    @Test
    public void testGetByPrimaryKey() throws Exception {
        Group group = new Group();
        group.setNumber(405);
        group.setFaculty("Информационных технологий");
        group = groupDao.persist(group);

        Long id = group.getId();
        group = groupDao.getByPrimaryKey(id);
        assertNotNull("Group must be not null", group);

        groupDao.delete(group);
    }

    @Test
    public void testGetAll() throws Exception {
        Group group = new Group();
        group.setNumber(305);
        group.setFaculty("Иностранных языков");
        group = groupDao.persist(group);

        List<Group> groups = groupDao.getAll();
        assertNotNull("List must be not null", groups);
        assertTrue("List size must be greater than 0", groups.size() > 0);

        groupDao.delete(group);
    }
}
