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

package com.haulmont.testtask.app;

import com.haulmont.testtask.entity.Group;

import com.vaadin.data.util.converter.Converter;
import java.util.Locale;

/**
 * The class {@code GroupConverter} is a class to String-Group and Group-String conversion,
 * to display group data in a table cell.
 *
 * @version 1.0
 * @author Vladimir
 */
class GroupConverter implements Converter<String, Group> {

    /**
     * Method to convert a String to a Group
     * @param s The string contains a newline-separated fields in the following form:
     *          id \n number \n faculty. Can be null.
     * @param aClass The type of the return value.
     * @param locale The locale to use for conversion. Can be null.
     * @throws ConversionException
     */
    @Override
    public Group convertToModel(String s, Class<? extends Group> aClass, Locale locale) throws ConversionException {
        if (s == null) {
            return null;
        }
        String[] parts = s.split("\n");
        if (parts.length != 3) {
            throw new ConversionException("Can not convert String to Group: " + s);
        }
        Group group = new Group();
        try {
            group.setId(Long.valueOf(parts[0]));
            group.setNumber(Integer.valueOf(parts[1]));
            group.setFaculty(parts[2]);
        } catch (Exception e) {
            throw new ConversionException(e.getMessage());
        }
        return group;
    }

    @Override
    public String convertToPresentation(Group group, Class<? extends String> aClass, Locale locale) throws ConversionException {
        if (group != null) {
            return String.valueOf(group.getNumber()) + ", " + group.getFaculty();
        } else {
            return null;
        }
    }

    @Override
    public Class<Group> getModelType() {
        return Group.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}
