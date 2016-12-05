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
import com.haulmont.testtask.entity.Student;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.*;

/**
 * The class {@code StudentDaoTest} represents a unit test of the StudentDao class.
 *
 * @version 1.0
 * @author Vladimir
 */
public class StudentDaoTest {

    private StudentDao studentDao;
    private GroupDao groupDao;

    @Before
    public void setUp() throws Exception {
        studentDao = DaoFactory.getInstance().getStudentDao();
        groupDao = DaoFactory.getInstance().getGroupDao();
    }

    @After
    public void tearDown() throws Exception {
        studentDao = null;
        groupDao = null;
    }

    @Test
    public void testPersist() throws Exception {
        // create test data
        Group group = new Group();
        group.setNumber(410);
        group.setFaculty("Информационных технологий");
        group = groupDao.persist(group);
        Date birthDate = new GregorianCalendar(1990, 0, 10).getTime();
        Student student = new Student();
        student.setLastName("Иванов");
        student.setFirstName("Иван");
        student.setMiddleName("Иванович");
        student.setBirthDate(birthDate);
        student.setGroup(group);

        // test
        student = studentDao.persist(student);
        assertNotNull("Student must be not null", student);
        assertNotNull("Id must be not null", student.getId());

        // delete test data
        studentDao.delete(student);
        groupDao.delete(group);
    }

    @Test
    public void testUpdate() throws Exception {
        Group group = new Group();
        group.setNumber(310);
        group.setFaculty("Иностранных языков");
        group = groupDao.persist(group);
        Date birthDate = new GregorianCalendar(1990, 0, 10).getTime();
        Student student = new Student();
        student.setLastName("Иванов");
        student.setFirstName("Иван");
        student.setMiddleName("Иванович");
        student.setBirthDate(birthDate);
        student.setGroup(group);
        student = studentDao.persist(student);

        try {
            student.setLastName("Ivanov");
            student.setFirstName("Ivan");
            student.setMiddleName("Ivanovich");
            studentDao.update(student);
        } catch (Exception e) {
            fail("An exception occured: " + e.getMessage());
        }

        studentDao.delete(student);
        groupDao.delete(group);
    }

    @Test
    public void testDelete() throws Exception {
        Group group = new Group();
        group.setNumber(710);
        group.setFaculty("Химии");
        group = groupDao.persist(group);
        Date birthDate = new GregorianCalendar(1991, 5, 20).getTime();
        Student student = new Student();
        student.setLastName("Петров");
        student.setFirstName("Пётр");
        student.setMiddleName("Петрович");
        student.setBirthDate(birthDate);
        student.setGroup(group);
        student = studentDao.persist(student);

        try {
            studentDao.delete(student);
        } catch (Exception e) {
            fail("An exception occured: " + e.getMessage());
        }
    }

    @Test
    public void testGetByPrimaryKey() throws Exception {
        Group group = new Group();
        group.setNumber(415);
        group.setFaculty("Информационных технологий");
        group = groupDao.persist(group);
        Date birthDate = new GregorianCalendar(1991, 10, 20).getTime();
        Student student = new Student();
        student.setLastName("Сидоров");
        student.setFirstName("Иван");
        student.setMiddleName("Петрович");
        student.setBirthDate(birthDate);
        student.setGroup(group);
        student = studentDao.persist(student);

        Long id = student.getId();
        student = studentDao.getByPrimaryKey(id);
        assertNotNull("Student must be not null", student);

        studentDao.delete(student);
        groupDao.delete(group);
    }

    @Test
    public void testGetAll() throws Exception {
        Group group = new Group();
        group.setNumber(315);
        group.setFaculty("Иностранных языков");
        group = groupDao.persist(group);
        Date birthDate = new GregorianCalendar(1991, 10, 20).getTime();
        Student student = new Student();
        student.setLastName("Sidorov");
        student.setFirstName("Ivan");
        student.setMiddleName("Petrovich");
        student.setBirthDate(birthDate);
        student.setGroup(group);
        student = studentDao.persist(student);

        List<Student> students = studentDao.getAll();
        assertNotNull("List must be not null", students);
        assertTrue("List size must be greater than 0", students.size() > 0);

        studentDao.delete(student);
        groupDao.delete(group);
    }
}
