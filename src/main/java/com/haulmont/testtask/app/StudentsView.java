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
import com.haulmont.testtask.entity.Student;
import com.haulmont.testtask.hsqldb.DaoFactory;

import com.vaadin.data.Container.Filterable;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * The class {@code StudentsView} represents a view,
 * that displays information about a students.
 *
 * @version 1.0
 * @author Vladimir
 */
public class StudentsView extends VerticalLayout implements View {

    public static final String NAME = "students";

    private Table table;
    private Button addButton;
    private Button editButton;
    private Button deleteButton;
    private TextField lastNameText;
    private TextField groupText;
    private Button filterButton;

    private static Logger logger = Logger.getLogger(StudentsView.class.getName());

    public StudentsView() {
        createUI();
        processEvents();
    }

    private void createUI() {
        try {
            Panel filterPanel = new Panel("Фильтр");
            HorizontalLayout filterLayout = new HorizontalLayout();
            filterLayout.setMargin(true);
            filterLayout.setSpacing(true);

            lastNameText = new TextField();
            groupText = new TextField();
            lastNameText.setInputPrompt("фамилия");
            groupText.setInputPrompt("номер группы");
            filterButton = new Button("Применить");

            filterLayout.addComponents(lastNameText, groupText, filterButton);
            filterPanel.setContent(filterLayout);

            table = new Table();
            table.addContainerProperty("lastName", String.class, null,
                    "Фамилия", null, Table.Align.LEFT);
            table.addContainerProperty("firstName", String.class, null,
                    "Имя", null, Table.Align.LEFT);
            table.addContainerProperty("middleName", String.class, null,
                    "Отчество", null, Table.Align.LEFT);
            table.addContainerProperty("birthDate", Date.class, null,
                    "Дата рождения", null, Table.Align.LEFT);
            table.addContainerProperty("group", Group.class, null,
                    "Группа", null, Table.Align.LEFT);
            table.setColumnWidth("lastName", 200);
            table.setColumnWidth("firstName", 200);
            table.setColumnWidth("middleName", 200);
            table.setColumnWidth("birthDate", 200);
            table.setColumnExpandRatio("group", 1f);
            table.setSelectable(true);
            table.setImmediate(true);
            table.setNullSelectionAllowed(false);
            table.setSizeFull();

            table.setConverter("birthDate", new StringToDateConverter() {
                @Override
                public DateFormat getFormat(Locale locale) {
                    return new SimpleDateFormat("dd.MM.yyyy");
                }
            });

            table.setConverter("group", new GroupConverter());

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
            addComponents(filterPanel, table, buttonsLayout);
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
                        itemClickEvent.getButton() == MouseEventDetails.MouseButton.LEFT) {
                    Object itemId = itemClickEvent.getItemId();
                    if (itemId != null) {
                        getUI().addWindow(new StudentWindow(table, itemId));
                    }
                }
            });

            addButton.addClickListener(clickEvent ->
                    getUI().addWindow(new StudentWindow(table, null)));

            editButton.addClickListener(clickEvent -> {
                Object itemId = table.getValue();
                if (itemId != null) {
                    getUI().addWindow(new StudentWindow(table, itemId));
                }
            });

            deleteButton.addClickListener(clickEvent -> {
                Object itemId = table.getValue();
                if (itemId != null) {
                    Object selItemId = table.prevItemId(itemId);
                    if (selItemId == null) {
                        selItemId = table.nextItemId(itemId);
                    }
                    Student student = new Student();
                    Long id = (Long) itemId;
                    student.setId(id);
                    try {
                        DaoFactory.getInstance().getStudentDao().delete(student);
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

            filterButton.addClickListener(clickEvent -> {
                String lastName = lastNameText.getValue().trim();
                String groupNum = groupText.getValue().trim();

                Filterable filterContainer = (Filterable) table.getContainerDataSource();
                filterContainer.removeAllContainerFilters();

                SimpleStringFilter lastNameFilter = new SimpleStringFilter("lastName", lastName, true, false);
                GroupNumberFilter groupNumFilter = new GroupNumberFilter("group", groupNum);
                filterContainer.addContainerFilter(new And(lastNameFilter, groupNumFilter));
            });

        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    private void fillTable() {
        try {
            List<Student> students = DaoFactory.getInstance().getStudentDao().getAll();
            table.removeAllItems();
            for (Student student : students) {
                table.addItem(new Object[] { student.getLastName(), student.getFirstName(), student.getMiddleName(),
                        student.getBirthDate(), student.getGroup() }, student.getId());
            }
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        fillTable();
    }
}
