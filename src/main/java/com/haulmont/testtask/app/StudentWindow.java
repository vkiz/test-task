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
import com.haulmont.testtask.entity.Student;
import com.haulmont.testtask.hsqldb.DaoFactory;
import com.haulmont.testtask.hsqldb.StudentDao;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.validator.DateRangeValidator;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;

import java.util.*;
import java.util.logging.Logger;

/**
 * The class {@code StudentWindow} represents a dialog window
 * to create and edit a student.
 *
 * @version 1.0
 * @author Vladimir
 */
class StudentWindow extends Window {

    private Table table;
    private Object itemId;

    private TextField lastNameText;
    private TextField firstNameText;
    private TextField middleNameText;
    private PopupDateField birthDateField;
    private ComboBox groupComboBox;

    private Button okButton;
    private Button cancelButton;

    private static Logger logger = Logger.getLogger(StudentWindow.class.getName());

    private static final String REQUIRED = "Обязательное для заполнения поле";
    private static final String FORMAT = "Укажите дату в формате \"дд.мм.гггг\"";

    StudentWindow(Table table, Object itemId) {
        this.table = table;
        this.itemId = itemId;
        createUI();
        fillGroups();
        processItem();
    }

    private void createUI() {
        setIcon(new ThemeResource(AppTheme.STUDENT_ICON));
        setStyleName(AppTheme.MODAL_WINDOW);
        setWidth("480px");
        setHeight("350px");
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

        lastNameText = new TextField("Фамилия");
        lastNameText.setImmediate(true);
        lastNameText.setNullRepresentation("");
        lastNameText.setRequired(true);
        lastNameText.setRequiredError(REQUIRED);
        lastNameText.addValidator(new NullValidator(REQUIRED, false));
        lastNameText.setValidationVisible(false);
        lastNameText.setMaxLength(32);
        lastNameText.setWidth("100%");

        firstNameText = new TextField("Имя");
        firstNameText.setImmediate(true);
        firstNameText.setNullRepresentation("");
        firstNameText.setRequired(true);
        firstNameText.setRequiredError(REQUIRED);
        firstNameText.addValidator(new NullValidator(REQUIRED, false));
        firstNameText.setValidationVisible(false);
        firstNameText.setMaxLength(32);
        firstNameText.setWidth("100%");

        middleNameText = new TextField("Отчество");
        middleNameText.setImmediate(true);
        middleNameText.setNullRepresentation("");
        middleNameText.setMaxLength(32);
        middleNameText.setWidth("100%");

        birthDateField = new PopupDateField("Дата рождения");
        birthDateField.setDateFormat("dd.MM.yyyy");
        birthDateField.setInputPrompt("дд.мм.гггг");
        birthDateField.setImmediate(true);
        Date minDate = new GregorianCalendar(1, 0, 1).getTime();
        Date maxDate = new GregorianCalendar(9999, 11, 31).getTime();
        birthDateField.setRangeStart(minDate);
        birthDateField.setRangeEnd(maxDate);
        birthDateField.addValidator(new DateRangeValidator(FORMAT, minDate, maxDate, Resolution.DAY));
        birthDateField.setDateOutOfRangeMessage(FORMAT);
        birthDateField.setConversionError(FORMAT);
        birthDateField.setParseErrorMessage(FORMAT);
        birthDateField.setWidth("100%");

        groupComboBox = new ComboBox("Группа");
        groupComboBox.setImmediate(true);
        groupComboBox.setRequired(true);
        groupComboBox.setRequiredError(REQUIRED);
        groupComboBox.addValidator(new NullValidator(REQUIRED, false));
        groupComboBox.setValidationVisible(false);
        groupComboBox.setNullSelectionAllowed(false);
        groupComboBox.setTextInputAllowed(false);
        groupComboBox.setPageLength(5);
        groupComboBox.setWidth("100%");

        formLayout.addComponents(lastNameText, firstNameText, middleNameText, birthDateField, groupComboBox);

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

    private void fillGroups() {
        try {
            List<Group> groups = DaoFactory.getInstance().getGroupDao().getAll();
            IndexedContainer container = new IndexedContainer();
            container.addContainerProperty("groupText", String.class, null);
            container.addContainerProperty("number", Integer.class, null);
            container.addContainerProperty("faculty", String.class, null);
            container.addContainerProperty("group", Group.class, null);
            for (Group group : groups) {
                int number = group.getNumber();
                String faculty = group.getFaculty();
                Object groupId = group.getId();
                String groupText = String.valueOf(number) + ", " + faculty;
                Item item = container.addItem(groupId);
                if (item != null) {
                    item.getItemProperty("groupText").setValue(groupText);
                    item.getItemProperty("number").setValue(number);
                    item.getItemProperty("faculty").setValue(faculty);
                    item.getItemProperty("group").setValue(group);
                }
            }
            container.sort(new Object[] {"faculty", "number"}, new boolean[] {true, true});
            groupComboBox.removeAllItems();
            groupComboBox.setContainerDataSource(container);
            groupComboBox.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
            groupComboBox.setItemCaptionPropertyId("groupText");
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    private void processItem() {
        if (itemId != null) {
            setCaption("Редактирование студента");
            if (table != null) {
                Item item = table.getItem(itemId);
                if (item != null) {
                    try {
                        String lastName = String.valueOf(item.getItemProperty("lastName").getValue());
                        String firstName = String.valueOf(item.getItemProperty("firstName").getValue());
                        String middleName = String.valueOf(item.getItemProperty("middleName").getValue());
                        Date birthDate = (Date) item.getItemProperty("birthDate").getValue();
                        Group group = (Group) item.getItemProperty("group").getValue();
                        Object groupItemId = group.getId();
                        lastNameText.setValue(lastName);
                        firstNameText.setValue(firstName);
                        middleNameText.setValue(middleName);
                        birthDateField.setValue(birthDate);
                        groupComboBox.setValue(groupItemId);
                    } catch (Exception e) {
                        logger.severe(e.getMessage());
                    }
                }
            }
        } else {
            setCaption("Добавление студента");
            lastNameText.focus();
        }

        okButton.addClickListener(clickEvent -> {
            if (isValidFields()) {
                try {
                    Student student = new Student();
                    student.setLastName(lastNameText.getValue());
                    student.setFirstName(firstNameText.getValue());
                    student.setMiddleName(middleNameText.getValue());
                    student.setBirthDate(birthDateField.getValue());
                    Object groupItemId = groupComboBox.getValue();
                    Item groupItem = groupComboBox.getItem(groupItemId);
                    Group group = (Group) groupItem.getItemProperty("group").getValue();
                    student.setGroup(group);
                    StudentDao studentDao = DaoFactory.getInstance().getStudentDao();
                    if (itemId != null) {
                        Long id = (Long) itemId;
                        student.setId(id);
                        studentDao.update(student);
                        if (table != null) {
                            Item item = table.getItem(itemId);
                            if (item != null) {
                                item.getItemProperty("lastName").setValue(student.getLastName());
                                item.getItemProperty("firstName").setValue(student.getFirstName());
                                item.getItemProperty("middleName").setValue(student.getMiddleName());
                                item.getItemProperty("birthDate").setValue(student.getBirthDate());
                                item.getItemProperty("group").setValue(student.getGroup());
                            }
                        }
                    } else {
                        student = studentDao.persist(student);
                        if (student != null && student.getId() != null) {
                            if (table != null) {
                                Object itemId = table.addItem(new Object[] {
                                        student.getLastName(),
                                        student.getFirstName(),
                                        student.getMiddleName(),
                                        student.getBirthDate(),
                                        student.getGroup()}, student.getId());
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
            String strVal = lastNameText.getValue().trim();
            ObjectProperty<String> strProp = new ObjectProperty<>(strVal);
            lastNameText.setPropertyDataSource(strProp);
            lastNameText.validate();
        } catch (Exception e) {
            res = false;
        }
        try {
            String strVal = firstNameText.getValue().trim();
            ObjectProperty<String> strProp = new ObjectProperty<>(strVal);
            firstNameText.setPropertyDataSource(strProp);
            firstNameText.validate();
        } catch (Exception e) {
            res = false;
        }
        try {
            String strVal = middleNameText.getValue().trim();
            ObjectProperty<String> strProp = new ObjectProperty<>(strVal);
            middleNameText.setPropertyDataSource(strProp);
        } catch (Exception e) {
        }
        try {
            birthDateField.validate();
        } catch (Exception e) {
            res = false;
        }
        try {
            groupComboBox.validate();
        } catch (Exception e) {
            res = false;
        }
        lastNameText.setValidationVisible(!lastNameText.isValid());
        firstNameText.setValidationVisible(!firstNameText.isValid());
        birthDateField.setValidationVisible(!birthDateField.isValid());
        groupComboBox.setValidationVisible(!groupComboBox.isValid());
        return res;
    }
}
