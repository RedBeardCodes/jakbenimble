create table if not exists users (
	id integer primary key autoincrement,
	first_name text not null check (email <> ''),
	last_name text not null check (email <> ''),
	email text unique not null check (email <> ''),
	created_at integer not null,
	updated_at integer not null,
	is_active integer default 1 not null check (is_active in (0, 1))
);
