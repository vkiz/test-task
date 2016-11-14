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

import com.haulmont.testtask.dao.DaoException;
import com.haulmont.testtask.entity.Group;
import com.haulmont.testtask.hsqldb.DaoFactory;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.ui.*;

import java.util.List;
import java.util.logging.Logger;

/**
 * The class {@code GroupsView} represents a view,
 * that displays information about the groups of students.
 *
 * @version 1.0
 * @author Vladimir
 */
class GroupsView extends VerticalLayout implements View {

    static final String NAME = "groups";

    private Table table;
    private Button addButton;
    private Button editButton;
    private Button deleteButton;

    private static Logger logger = Logger.getLogger(GroupsView.class.getName());

    GroupsView() {
        createUI();
        processEvents();
        fillTable();
    }

    private void createUI() {
        try {
            table = new Table();
            table.addContainerProperty("number", Integer.class, null,
                    "Номер группы", null, Table.Align.LEFT);
            table.addContainerProperty("faculty", String.class, null,
                    "Название факультета", null, Table.Align.LEFT);
            table.setColumnWidth("number", 200);
            table.setColumnExpandRatio("faculty", 1f);
            table.setSelectable(true);
            table.setImmediate(true);
            table.setNullSelectionAllowed(false);
            table.setSizeFull();

            HorizontalLayout buttonsLayout = new HorizontalLayout();
            buttonsLayout.setSpacing(true);

            addButton = new Button("Добавить", new ThemeResource(AppTheme.BUTTON_ADD));
            editButton = new Button("Изменить", new ThemeResource(AppTheme.BUTTON_EDIT));
            deleteButton = new Button("Удалить", new ThemeResource(AppTheme.BUTTON_DELETE));
            editButton.setEnabled(false);
            deleteButton.setEnabled(false);
            buttonsLayout.addComponents(addButton, editButton, deleteButton);

            setMargin(true);
            setSpacing(true);
            setSizeFull();
            addComponents(table, buttonsLayout);
            setExpandRatio(table, 1f);
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    private void processEvents() {
        try {
            table.addValueChangeListener(valueChangeEvent -> {
                if (table.getValue() != null) {
                    editButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                } else {
                    editButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                }
            });

            table.addItemClickListener(itemClickEvent -> {
                if (itemClickEvent.isDoubleClick() &&
                        itemClickEvent.getButton() == MouseButton.LEFT) {
                    Object itemId = itemClickEvent.getItemId();
                    if (itemId != null) {
                        getUI().addWindow(new GroupWindow(table, itemId));
                    }
                }
            });

            addButton.addClickListener(clickEvent ->
                    getUI().addWindow(new GroupWindow(table, null)));

            editButton.addClickListener(clickEvent -> {
                Object itemId = table.getValue();
                if (itemId != null) {
                    getUI().addWindow(new GroupWindow(table, itemId));
                }
            });

            deleteButton.addClickListener(clickEvent -> {
                Object itemId = table.getValue();
                if (itemId != null) {
                    Object selItemId = table.prevItemId(itemId);
                    if (selItemId == null) {
                        selItemId = table.nextItemId(itemId);
                    }
                    Group group = new Group();
                    Long id = (Long) itemId;
                    group.setId(id);
                    try {
                        DaoFactory.getInstance().getGroupDao().delete(group);
                        table.removeItem(itemId);
                        if (selItemId != null) {
                            table.select(selItemId);
                            table.setCurrentPageFirstItemId(selItemId);
                        }
                    } catch (DaoException e) {
                        logger.severe(e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    private void fillTable() {
        try {
            List<Group> groups = DaoFactory.getInstance().getGroupDao().getAll();
            table.removeAllItems();
            for (Group group : groups) {
                table.addItem(new Object[] { group.getNumber(), group.getFaculty() }, group.getId());
            }
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }
}
