create table tutor (
  id integer primary key not null,
  name varchar(255) not null collate nocase,
  email varchar(255) not null collate nocase,
  subject_id integer not null,
  foreign key(subject_id) references subject(id),
  unique(name)
)
