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

package com.haulmont.testtask.dao;

import com.haulmont.testtask.entity.Entity;

import java.util.List;

/**
 * The interface {@code GenericDao} describes a generic DAO for entity.
 *
 * @version 1.0
 * @author Vladimir
 */
public interface GenericDao<T extends Entity> {

    T create() throws Exception;
    T persist(T object) throws Exception;
    void update(T object) throws Exception;
    void delete(T object) throws Exception;
    T getByPrimaryKey(Long key) throws Exception;
    List<T> getAll() throws Exception;
}
