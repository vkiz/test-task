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

import java.sql.SQLException;

/**
 * The class {@code DaoFactory} represents a DAO-factory.
 * It creates instances of DAO for Group and Student entities.
 *
 * @version 1.0
 * @author Vladimir
 */
public class DaoFactory {

    private static DaoFactory instance = null;

    private DaoFactory() {
    }

    public static synchronized DaoFactory getInstance() {
        if (instance == null) {
            instance = new DaoFactory();
        }
        return instance;
    }

    public GroupDao getGroupDao() throws DaoException {
        try {
            return new GroupDao(DatabaseHelper.getConnection());
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    public StudentDao getStudentDao() throws DaoException {
        try {
            return new StudentDao(DatabaseHelper.getConnection());
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    public void releaseResources() throws DaoException {
        try {
            DatabaseHelper.closeConnection();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
