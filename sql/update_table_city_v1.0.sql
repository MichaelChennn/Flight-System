alter table city add column lat double after country;
alter table city add column lon double after lat;

update city set lat = 48.137154, lon = 11.576124 where city.name = 'Munich';
update city set lat = 38.736946, lon = -9.142685 where city.name = 'Lisbon';