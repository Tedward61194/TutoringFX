create table subject (
  id integer primary key not null,
  name varchar(255) not null collate nocase,
  unique(name)
)
