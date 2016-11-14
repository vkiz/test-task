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
import com.haulmont.testtask.hsqldb.DaoFactory;
import com.haulmont.testtask.hsqldb.GroupDao;

import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;

import java.util.logging.Logger;

/**
 * The class {@code GroupWindow} represents a dialog window
 * to create and edit the groups of students.
 *
 * @version 1.0
 * @author Vladimir
 */
class GroupWindow extends Window {

    private Table table;
    private Object itemId;
    private Button okButton;
    private Button cancelButton;
    private TextField numberText;
    private TextField facultyText;

    private static Logger logger = Logger.getLogger(GroupWindow.class.getName());

    GroupWindow(Table table, Object itemId) {
        super();
        this.table = table;
        this.itemId = itemId;
        createUI();
        processItem();
    }

    private void createUI() {
        setIcon(new ThemeResource(AppTheme.GROUP_ICON));
        setWidth("450px");
        setHeight("205px");
        setModal(true);
        setResizable(false);
        center();

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);

        FormLayout formLayout = new FormLayout();
        formLayout.setSizeFull();
        formLayout.setMargin(false);
        formLayout.setSpacing(true);

        numberText = new TextField("Номер группы");
        numberText.setImmediate(true);
        numberText.setNullRepresentation("");
        numberText.setRequired(true);
        numberText.setRequiredError("Обязательное для заполнения поле");
        numberText.setConverter(Integer.class);
        numberText.setConversionError("Введите значение от 1 до 999");
        numberText.addValidator(new IntegerRangeValidator("Введите значение от 1 до 999", 1, 999));
        numberText.setValidationVisible(false);
        numberText.setWidth("100%");

        facultyText = new TextField("Название факультета");
        facultyText.setImmediate(true);
        facultyText.setNullRepresentation("");
        facultyText.setRequired(true);
        facultyText.setRequiredError("Обязательное для заполнения поле");
        facultyText.addValidator(new NullValidator("Обязательное для заполнения поле", false));
        facultyText.setValidationVisible(false);
        facultyText.setMaxLength(32);
        facultyText.setWidth("100%");

        formLayout.addComponents(numberText, facultyText);

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.setSpacing(true);

        okButton = new Button("ОК");
        okButton.setWidth("125px");
        cancelButton = new Button("Отменить");
        cancelButton.setWidth("125px");

        buttonsLayout.addComponents(okButton, cancelButton);

        mainLayout.addComponents(formLayout, buttonsLayout);
        mainLayout.setExpandRatio(formLayout, 1f);
        mainLayout.setComponentAlignment(buttonsLayout, Alignment.BOTTOM_CENTER);
        setContent(mainLayout);
    }

    private void processItem() {
        if (itemId != null) {
            setCaption("Редактирование группы");
            if (table != null) {
                Item item = table.getItem(itemId);
                if (item != null) {
                    try {
                        String number = String.valueOf(item.getItemProperty("number").getValue());
                        String faculty = String.valueOf(item.getItemProperty("faculty").getValue());
                        numberText.setValue(number);
                        facultyText.setValue(faculty);
                    } catch (Exception e) {
                        logger.severe(e.getMessage());
                    }
                }
            }
        } else {
            setCaption("Добавление группы");
            numberText.focus();
        }

        okButton.addClickListener(clickEvent -> {
            if (isValidFields()) {
                try {
                    Group group = new Group();
                    group.setNumber(Integer.parseInt(numberText.getValue()));
                    group.setFaculty(facultyText.getValue());
                    GroupDao groupDao = DaoFactory.getInstance().getGroupDao();
                    if (itemId != null) {
                        Long id = (Long) itemId;
                        group.setId(id);
                        groupDao.update(group);
                        if (table != null) {
                            Item item = table.getItem(itemId);
                            if (item != null) {
                                item.getItemProperty("number").setValue(group.getNumber());
                                item.getItemProperty("faculty").setValue(group.getFaculty());
                            }
                        }
                    } else {
                        group = groupDao.persist(group);
                        if (group != null && group.getId() != null) {
                            if (table != null) {
                                Object itemId = table.addItem(new Object[]{
                                        group.getNumber(), group.getFaculty()}, group.getId());
                                if (itemId != null) {
                                    table.select(itemId);
                                    table.setCurrentPageFirstItemId(itemId);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.severe(e.getMessage());
                }
                close();
            }
        });

        cancelButton.addClickListener(clickEvent -> close());
    }

    private boolean isValidFields() {
        boolean res = true;

        try {
            Integer intVal = (Integer) numberText.getConvertedValue();
            ObjectProperty<Integer> intProp = new ObjectProperty<>(intVal);
            numberText.setPropertyDataSource(intProp);
            numberText.validate();
        } catch (Exception e) {
            res = false;
        }
        try {
            String strVal = facultyText.getValue().trim();
            ObjectProperty<String> strProp = new ObjectProperty<>(strVal);
            facultyText.setPropertyDataSource(strProp);
            facultyText.validate();
        } catch (Exception e) {
            res = false;
        }
        numberText.setValidationVisible(!numberText.isValid());
        facultyText.setValidationVisible(!facultyText.isValid());

        return res;
    }
}
