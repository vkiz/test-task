-- groups table

create table T_GROUP (
  ID bigint generated by default as identity
  (start with 1, increment by 1) not null primary key,
  NUM int not null,
  FACULTY nvarchar(32) not null
);

-- students table

create table T_STUDENT (
  ID bigint generated by default as identity
  (start with 1, increment by 1) not null primary key,
  FIRST_NAME nvarchar(32) not null,
  LAST_NAME nvarchar(32) not null,
  MIDDLE_NAME nvarchar(32),
  BIRTH_DATE date,
  GROUP_ID bigint not null,
  --
  constraint FK_GROUP_STUDENT foreign key (GROUP_ID) references T_GROUP(ID) on delete restrict
);
