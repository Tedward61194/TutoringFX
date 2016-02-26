create table student (
  id integer primary key not null,
  name varchar(255) not null collate nocase,
  email varchar(255) not null, 
  enrolled date not null,
  unique(name)
)
