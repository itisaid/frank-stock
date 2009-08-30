DROP DATABASE IF EXISTS stock;

CREATE DATABASE stock CHARACTER SET UTF8;

USE stock;

CREATE TABLE stock_data(
	id INT AUTO_INCREMENT,
	code CHAR(9),
	name VARCHAR(64),
	trade_date DATE,
	open_price FLOAT(10,2),
	high_price FLOAT(10,2),
	low_price FLOAT(10,2),
	close_price FLOAT(10,2),
	volume INT,
	PRIMARY KEY(id)
) DEFAULT CHARSET=UTF8;
