DROP TABLE students;
DROP TABLE progress;
CREATE DATABASE lab10;
CREATE TABLE students
(id serial NOT NULL PRIMARY KEY,
 name varchar(30) NOT NULL,
 PassportSerial varchar(4) not null,
 PassportNumber varchar(6) not null,
 UNIQUE (PassportSerial, PassportNumber)
);
CREATE TABLE subjects
(id serial NOT NULL PRIMARY KEY,
 name varchar(50) NOT NULL
);
CREATE TABLE progress
(id serial NOT NULL PRIMARY KEY,
 student int NOT NULL REFERENCES students(id) ON DELETE CASCADE,
 subject int NOT NULL REFERENCES subjects(id) ON DELETE CASCADE,
 mark smallint NOT NULL CHECK(mark BETWEEN 2 and 5)
);
--alter table students add constraint uniquePassport UNIQUE (PassportSerial, PassportNumber);

INSERT into students (id, name, PassportSerial, PassportNumber)
values
    (1, 'Вася', 3311, 435678),
    (2, 'Коля', 3322, 342345),
    (3, 'Петя', 3333, 123456),
    (4, 'Маша', 3344, 234567),
    (5, 'Даша', 3355, 345678),
    (6, 'Саша', 3366, 346565);

INSERT into subjects (id, name)
values
    (1, 'Математика'),
    (2, 'Русский язык'),
    (3, 'Физика'),
    (4, 'Информатика');

INSERT into progress (id, student, subject, mark)
values
(1, 1, 1, 5),
(2, 1, 2, 4),
(3, 1, 3, 5),
(4, 1, 4, 4),
(5, 2, 2, 4),
(6, 2, 3, 3),
(7, 2, 4, 4),
(8, 3, 1, 5),
(9, 3, 2, 5),
(10, 3, 3, 5),
(11, 3, 4, 5),
(12, 4, 1, 4),
(13, 4, 2, 4),
(14, 4, 3, 4),
(15, 4, 4, 4),
(16, 5, 3, 3),
(17, 5, 4, 3),
(18, 6, 1, 2),
(19, 6, 2, 2),
(20, 6, 3, 2),
(21, 6, 4, 2);


Select distinct s.name from Students s
                                INNER JOIN Progress p
                                           on s.id = p.student
                                inner join subjects ss
                                           on ss.id = p.subject
WHERE p.mark > 3 and ss.name = 'Математика'
group by s.name;

select avg(p.mark) as "Средний балл" from progress p
                                              inner join subjects s on p.subject = s.id
where s.name = 'Математика';

select avg(p.mark) as "Средний балл" from progress p
                                              inner join subjects s on p.subject = s.id
                                              inner join students s2 on p.student = s2.id
where s2.name = 'Даша';

SELECT count(*), s.name from progress p
                                 inner join subjects s on s.id = p.subject
where p.mark > 2
group by s.name
order by count(*) desc limit 3;

SELECT s.name FROM Students s
                LEFT JOIN Progress p ON s.id = p.student
                GROUP BY s.name
                HAVING COUNT(p.mark) < 4 OR COUNT(CASE WHEN p.mark < 3 THEN 1 END) > 0

