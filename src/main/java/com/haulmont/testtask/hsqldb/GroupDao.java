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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * The class {@code GroupDao} represents a HyperSql implementation of DAO for Group entity.
 *
 * @version 1.0
 * @author Vladimir
 */
public class GroupDao implements GenericDao<Group> {

    private Connection connection = null;

    protected GroupDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Group persist(Group object) throws DaoException {
        Group group = new Group();
        String sql = "INSERT INTO T_Group (num, faculty) VALUES (?, ?);";
        try (PreparedStatement st = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, object.getNumber());
            st.setString(2, object.getFaculty());
            if (st.executeUpdate() == 1) {
                try (ResultSet rs = st.getGeneratedKeys()) {
                    rs.next();
                    group.setId(rs.getLong(1));
                    group.setNumber(object.getNumber());
                    group.setFaculty(object.getFaculty());
                } catch (SQLException e) {
                    throw e;
                }
            } else {
                throw new SQLException("Creating Group failed, no row inserted.");
            }
        } catch (SQLException e) {
            group = null;
            throw new DaoException(e);
        }
        return group;
    }

    @Override
    public void update(Group object) throws DaoException {
        String sql = "UPDATE T_Group SET num = ?, faculty = ? WHERE id = ?;";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, object.getNumber());
            st.setString(2, object.getFaculty());
            st.setLong(3, object.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(Group object) throws DaoException {
        String sql = "DELETE FROM T_Group WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setLong(1, object.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Group getByPrimaryKey(Long key) throws DaoException {
        Group group = new Group();
        String sql = "SELECT num, faculty FROM T_Group WHERE id = ?;";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setLong(1, key);
            ResultSet rs = st.executeQuery();
            rs.next();
            group.setId(key);
            group.setNumber(rs.getInt("num"));
            group.setFaculty(rs.getString("faculty"));
        } catch (SQLException e) {
            group = null;
            throw new DaoException(e);
        }
        return group;
    }

    @Override
    public List<Group> getAll() throws DaoException {
        List<Group> list = new ArrayList<Group>();
        String sql = "SELECT id, num, faculty FROM T_Group;";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Group group = new Group();
                group.setId(rs.getLong("id"));
                group.setNumber(rs.getInt("num"));
                group.setFaculty(rs.getString("faculty"));
                list.add(group);
            }
        } catch (SQLException e) {
            list = null;
            throw new DaoException(e);
        }
        return list;
    }
}
