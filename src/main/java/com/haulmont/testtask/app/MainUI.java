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
        mainLayout.setMargin(false);
        mainLayout.setSpacing(false);

        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setHeight("48px");
        headerLayout.setWidth("100%");
        headerLayout.setMargin(false);
        headerLayout.setSpacing(true);

        Button mainButton = new Button("Главная");
        mainButton.setHeight("100%");
        mainButton.addStyleName(AppTheme.BORDERLESS);

        Button groupsButton = new Button("Группы");
        groupsButton.setHeight("100%");
        groupsButton.addStyleName(AppTheme.BORDERLESS);

        Button studentsButton = new Button("Студенты");
        studentsButton.setHeight("100%");
        studentsButton.addStyleName(AppTheme.BORDERLESS);

        Label header = new Label(title);
        header.setWidth(null);

        Embedded logo = new Embedded(null, new ThemeResource(AppTheme.HEADER_LOGO));
        logo.setHeight("32px");
        logo.setWidth("32px");

        headerLayout.addComponents(mainButton, groupsButton, studentsButton, header, logo);
        headerLayout.setComponentAlignment(header, Alignment.MIDDLE_RIGHT);
        headerLayout.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
        headerLayout.setExpandRatio(header, 1f);

        VerticalLayout viewsLayout = new VerticalLayout();
        viewsLayout.setSizeFull();
        viewsLayout.setMargin(false);
        viewsLayout.setSpacing(true);

        mainLayout.addComponents(headerLayout, viewsLayout);
        mainLayout.setExpandRatio(viewsLayout, 1f);

        ViewDisplay viewDisplay = new Navigator.ComponentContainerViewDisplay(viewsLayout);
        Navigator navigator = new Navigator(this, viewDisplay);
        navigator.addView(MainView.NAME, new MainView());
        navigator.addView(GroupsView.NAME, new GroupsView());
        navigator.addView(StudentsView.NAME, new StudentsView());

        mainButton.addClickListener(clickEvent -> navigator.navigateTo(MainView.NAME));
        groupsButton.addClickListener(clickEvent -> navigator.navigateTo(GroupsView.NAME));
        studentsButton.addClickListener(clickEvent -> navigator.navigateTo(StudentsView.NAME));

        headerLayout.setStyleName(AppTheme.HEADER_LAYOUT);
        mainLayout.setStyleName(AppTheme.VIEW_LAYOUT);
        viewsLayout.setStyleName(AppTheme.VIEW_LAYOUT);

        setContent(mainLayout);
    }
}
