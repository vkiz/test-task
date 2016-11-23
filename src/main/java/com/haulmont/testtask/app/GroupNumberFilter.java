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

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;

/**
 * The class {@code GroupNumberFilter} represents a custom filter of container
 * for filtering a Students items by Group number.
 *
 * @version 1.0
 * @author Vladimir
 */
class GroupNumberFilter implements Container.Filter {

    private String propertyId;
    private String filterString;

    GroupNumberFilter(String propertyId, String filterString) {
        this.propertyId = propertyId;
        this.filterString = filterString;
    }

    /** Apply the filter on a Student item to check if it passes. */
    @Override
    public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
        Property<?> groupProp = item.getItemProperty(propertyId);
        if (groupProp == null || !groupProp.getType().equals(Group.class)) {
            return false;
        }
        if (filterString.isEmpty()) {
            return true;
        }
        try {
            Group group = (Group) groupProp.getValue();
            String numberStr = String.valueOf(group.getNumber());
            return numberStr.contains(filterString);
        } catch (Exception e) {
            return false;
        }
    }

    /** Tells if this filter works on the given property. */
    @Override
    public boolean appliesToProperty(Object propertyId) {
        return propertyId != null && propertyId.equals(this.propertyId);
    }
}
