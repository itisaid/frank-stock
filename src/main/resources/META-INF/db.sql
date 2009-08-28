drop database if exists stock;

create database stock;

use stock;

create table stock_data(
	id int auto_increment,
	code char(9),
	name varchar(64),
	trade_date date,
	open_price float(10,2),
	high_price float(10,2),
	low_price float(10,2),
	close_price float(10,2),
	volume int,
	primary key(id)
) DEFAULT CHARSET=utf8;
