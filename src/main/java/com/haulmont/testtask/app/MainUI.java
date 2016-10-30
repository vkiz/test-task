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

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;

/**
 * The class {@code MainUI} represents an entry point to the application.
 * To navigate between views in the application use a class {@code Navigator}.
 *
 * @version 1.0
 * @author Vladimir
 */
@Theme(AppTheme.THEME_NAME)
public class MainUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        String title = "Информация о студентах института";
        getPage().setTitle(title);

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);

        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidth("100%");
        headerLayout.setMargin(false);
        headerLayout.setSpacing(true);

        Button mainViewButton = new Button("Главная");
        Button groupsViewButton = new Button("Группы");
        Button studentsViewButton = new Button("Студенты");
        Label header = new Label(title);
        header.setWidth(null);
        Embedded logo = new Embedded(null, new ThemeResource(AppTheme.HEADER_LOGO));

        headerLayout.addComponent(mainViewButton);
        headerLayout.addComponent(groupsViewButton);
        headerLayout.addComponent(studentsViewButton);
        headerLayout.addComponent(header);
        headerLayout.setComponentAlignment(header, Alignment.MIDDLE_RIGHT);
        headerLayout.setExpandRatio(header, 1f);
        headerLayout.addComponent(logo);

        VerticalLayout viewsLayout = new VerticalLayout();
        viewsLayout.setSizeFull();
        viewsLayout.setMargin(false);
        viewsLayout.setSpacing(true);

        mainLayout.addComponent(headerLayout);
        mainLayout.addComponent(viewsLayout);
        mainLayout.setExpandRatio(viewsLayout, 1f);

        setContent(mainLayout);

        ViewDisplay viewDisplay = new Navigator.ComponentContainerViewDisplay(viewsLayout);
        Navigator navigator = new Navigator(this, viewDisplay);

        mainLayout.setStyleName(AppTheme.LAYOUT_BORDER);
        headerLayout.setStyleName(AppTheme.LAYOUT_BORDER);
        viewsLayout.setStyleName(AppTheme.LAYOUT_BORDER);
    }
}
