--Вам даны 2 следующих скрипта, которые создают таблицы в БД:

--1. Таблица company
create table company
(
    id   integer NOT NULL,
    name character varying,
    CONSTRAINT company_key PRIMARY KEY (id)
);

--2. Таблица person
create table person
(
    id         integer NOT NULL,
    name       character varying,
    company_id integer references company (id),
    CONSTRAINT person_pkey PRIMARY KEY (id)
);

insert into company (id, name)
values (1, 'google');
insert into company (id, name)
values (2, 'apple');
insert into company (id, name)
values (3, 'VTB');
insert into company (id, name)
values (4, 'Luxoft');
insert into company (id, name)
values (5, 'Yandex');
insert into company (id, name)
values (6, 'NAUMEN');

insert into person (id, name, company_id)
values (1, 'Anna', 1);
insert into person (id, name, company_id)
values (2, 'Peter', 1);
insert into person (id, name, company_id)
values (3, 'Donald', 3);
insert into person (id, name, company_id)
values (4, 'Misha', 2);
insert into person (id, name, company_id)
values (5, 'Olya', 3);
insert into person (id, name, company_id)
values (6, 'Barak', 4);
insert into person (id, name, company_id)
values (7, 'Nike', 4);
insert into person (id, name, company_id)
values (8, 'Andry', 5);
insert into person (id, name, company_id)
values (9, 'Elya', 6);
insert into person (id, name, company_id)
values (10, 'Muller', 6);
/**
1. В одном запросе получить
- имена всех person, которые не состоят в компании с id = 5;
- название компании для каждого человека.
 */

select p.name, c.name
from person as p
         inner join company as c on c.id = p.company_id
where company_id != 5;

/**
2. Необходимо выбрать название компании с максимальным количеством человек + количество человек в этой компании
(нужно учесть, что таких компаний может быть несколько).
 */
select c.name, cp.count_person
from company c
         inner join (select count(id) AS count_person, company_id from person group by company_id) cp
                    on c.id = cp.company_id
         inner join (select max(A.count_person) as max_person
                     from (select count(id) AS count_person from person group by company_id) A) find_max
                    on cp.count_person = find_max.max_person;

