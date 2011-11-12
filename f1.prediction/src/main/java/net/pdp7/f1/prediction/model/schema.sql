create table drivers (
	driver_name                        varchar(100) primary key
);

create table seasons (
	season                             number(4) primary key
);

create table teams (
	team_name                          varchar(100) primary key
);

create table season_team_drivers (
	season                             number(4) not null references seasons(season),
	team_name                          varchar(100) not null references teams(team_name),
	driver_name                        varchar(100) not null references drivers(driver_name)
);

create table circuits (
	circuit_name                       varchar(100) primary key
);

create table calendar (
	season                             number(4) not null references seasons(season),
	round                              integer not null, 
	grand_prix                         varchar(100) not null, 
	circuit_name                       varchar(100) not null references circuits(circuit_name),
	primary key (season, round)
);
