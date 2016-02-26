create table interaction (
  id integer primary key not null,
  tutor_id integer not null,
  student_id integer not null,
  report text,
  foreign key(student_id) references student(id),
  foreign key(tutor_id) references tutor(id),
  unique(student_id, tutor_id)
)
