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

import com.haulmont.testtask.dao.DaoException;
import com.haulmont.testtask.dao.GenericDao;
import com.haulmont.testtask.entity.Group;
import com.haulmont.testtask.entity.Student;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * The class {@code StudentDao} represents a HyperSql implementation of DAO for Student entity.
 *
 * @version 1.0
 * @author Vladimir
 */
public class StudentDao implements GenericDao<Student> {

    private Connection connection = null;

    protected StudentDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Student persist(Student object) throws DaoException {
        Student student = new Student();
        String sql = "INSERT INTO T_Student (firstname, lastname, middlename, birthdate, group_id) " +
                     "VALUES (?, ?, ?, ?, ?);";
        try (PreparedStatement st = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, object.getFirstName());
            st.setString(2, object.getLastName());
            st.setString(3, object.getMiddleName());
            Date sqlDate = new Date(object.getBirthDate().getTime());
            st.setDate(4, sqlDate);
            st.setLong(5, object.getGroup().getId());
            if (st.executeUpdate() == 1) {
                try (ResultSet rs = st.getGeneratedKeys()) {
                    rs.next();
                    student.setId(rs.getLong(1));
                    student.setFirstName(object.getFirstName());
                    student.setLastName(object.getLastName());
                    student.setMiddleName(object.getMiddleName());
                    student.setBirthDate(object.getBirthDate());
                    student.setGroup(object.getGroup());
                } catch (SQLException e) {
                    throw e;
                }
            } else {
                throw new SQLException("Creating Student failed, no row inserted.");
            }
        } catch (SQLException e) {
            student = null;
            throw new DaoException(e);
        }
        return student;
    }

    @Override
    public void update(Student object) throws DaoException {
        String sql = "UPDATE T_Student SET firstname = ?, lastname = ?, middlename = ?, birthdate = ?, group_id = ? " +
                     "WHERE id = ?;";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, object.getFirstName());
            st.setString(2, object.getLastName());
            st.setString(3, object.getMiddleName());
            Date sqlDate = new Date(object.getBirthDate().getTime());
            st.setDate(4, sqlDate);
            st.setLong(5, object.getGroup().getId());
            st.setLong(6, object.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(Student object) throws DaoException {
        String sql = "DELETE FROM T_Student WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setLong(1, object.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Student getByPrimaryKey(Long key) throws DaoException {
        Student student = new Student();
        String sql ="SELECT firstname, lastname, middlename, birthdate, group_id FROM T_Student WHERE id = ?;";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setLong(1, key);
            ResultSet rs = st.executeQuery();
            rs.next();
            student.setId(key);
            student.setFirstName(rs.getString("firstname"));
            student.setLastName(rs.getString("lastname"));
            student.setMiddleName(rs.getString("middlename"));
            student.setBirthDate(rs.getDate("birthdate"));
            GroupDao groupDao = DaoFactory.getInstance().getGroupDao();
            Group group = groupDao.getByPrimaryKey(rs.getLong("group_id"));
            student.setGroup(group);
        } catch (SQLException e) {
            student = null;
            throw new DaoException(e);
        }
        return student;
    }

    @Override
    public List<Student> getAll() throws DaoException {
        List<Student> list = new ArrayList<Student>();
        String sql ="SELECT id, firstname, lastname, middlename, birthdate, group_id FROM T_Student;";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getLong("id"));
                student.setFirstName(rs.getString("firstname"));
                student.setLastName(rs.getString("lastname"));
                student.setMiddleName(rs.getString("middlename"));
                student.setBirthDate(rs.getDate("birthdate"));
                GroupDao groupDao = DaoFactory.getInstance().getGroupDao();
                Group group = groupDao.getByPrimaryKey(rs.getLong("group_id"));
                student.setGroup(group);
                list.add(student);
            }
        } catch (SQLException e) {
            list = null;
            throw new DaoException(e);
        }
        return list;
    }
}
