-- groups table

insert into T_GROUP (ID, NUM, FACULTY) values (1, 101, 'Архитектуры');
insert into T_GROUP (ID, NUM, FACULTY) values (2, 201, 'Биологии');
insert into T_GROUP (ID, NUM, FACULTY) values (3, 301, 'Иностранных языков');
insert into T_GROUP (ID, NUM, FACULTY) values (4, 401, 'Информационных технологий');
insert into T_GROUP (ID, NUM, FACULTY) values (5, 501, 'Математики');
insert into T_GROUP (ID, NUM, FACULTY) values (6, 601, 'Физики');
insert into T_GROUP (ID, NUM, FACULTY) values (7, 701, 'Химии');
insert into T_GROUP (ID, NUM, FACULTY) values (8, 402, 'Информационных технологий');
insert into T_GROUP (ID, NUM, FACULTY) values (9, 302, 'Иностранных языков');
insert into T_GROUP (ID, NUM, FACULTY) values (10, 502, 'Математики');

-- students table

insert into T_STUDENT (ID, FIRST_NAME, LAST_NAME, MIDDLE_NAME, BIRTH_DATE, GROUP_ID)
values (1, 'Иван', 'Андреев', 'Иванович', '1989-06-10', 9);

insert into T_STUDENT (ID, FIRST_NAME, LAST_NAME, MIDDLE_NAME, BIRTH_DATE, GROUP_ID)
values (2, 'Антон', 'Доценко', 'Семёнович', '1987-12-20', 4);

insert into T_STUDENT (ID, FIRST_NAME, LAST_NAME, MIDDLE_NAME, BIRTH_DATE, GROUP_ID)
values (3, 'Пётр', 'Иванов', 'Сергеевич', '1990-11-24', 5);

insert into T_STUDENT (ID, FIRST_NAME, LAST_NAME, MIDDLE_NAME, BIRTH_DATE, GROUP_ID)
values (4, 'Павел', 'Иванов', 'Николаевич', '1995-02-03', 6);

insert into T_STUDENT (ID, FIRST_NAME, LAST_NAME, MIDDLE_NAME, BIRTH_DATE, GROUP_ID)
values (5, 'Владимир', 'Кизельбашев', 'Алексеевич', '1985-08-20', 4);

insert into T_STUDENT (ID, FIRST_NAME, LAST_NAME, MIDDLE_NAME, BIRTH_DATE, GROUP_ID)
values (6, 'Ирина', 'Макарова', 'Петровна', '1991-09-05', 7);

insert into T_STUDENT (ID, FIRST_NAME, LAST_NAME, MIDDLE_NAME, BIRTH_DATE, GROUP_ID)
values (7, 'Любовь', 'Орлова', 'Сергеевна', '1987-03-10', 2);
