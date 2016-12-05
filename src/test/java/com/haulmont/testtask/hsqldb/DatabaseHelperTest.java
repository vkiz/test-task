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

import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.*;

/**
 * The class {@code DatabaseHelperTest} represents a unit test of the DatabaseHelper class.
 *
 * @version 1.0
 * @author Vladimir
 */
public class DatabaseHelperTest {

    @Test
    public void testGetConnection() throws Exception {
        Connection connection = DatabaseHelper.getConnection();
        assertNotNull("Connection must be not null", connection);
    }
}
