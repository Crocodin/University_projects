CREATE DATABASE TankingTanks
GO
USE TankingTanks
GO

CREATE TABLE country (
	country_id INT PRIMARY KEY IDENTITY,
	country_name VARCHAR(100) NOT NULL
)

CREATE TABLE manufacturer (
    manufacturer_id INT PRIMARY KEY IDENTITY,
    manufacturer_name VARCHAR(100) NOT NULL,
    founded_year INT,
    headquarters_country_id INT FOREIGN KEY REFERENCES country(country_id)
)

CREATE TABLE tank (
	tank_id INT PRIMARY KEY IDENTITY,
	tank_name VARCHAR(100) NOT NULL,
	made_date_year INT,
	manufacturer_id INT FOREIGN KEY REFERENCES manufacturer(manufacturer_id)
)

CREATE TABLE country_tank (
	country_id INT FOREIGN KEY REFERENCES country(country_id),
	tank_id INT FOREIGN KEY REFERENCES tank(tank_id),
	service_start_year INT NOT NULL,
	service_end_year INT DEFAULT NULL,
	CONSTRAINT pk_ct PRIMARY KEY (country_id, tank_id)
)

CREATE TABLE user_account (
	user_account_id INT PRIMARY KEY IDENTITY,
	username varchar(100) NOT NULL,
	email varchar(100) DEFAULT NULL,
	country_of_origin_id INT FOREIGN KEY REFERENCES country(country_id)
)

CREATE TABLE admin_account (
	user_account_id INT PRIMARY KEY REFERENCES user_account(user_account_id),
	super_admin BIT DEFAULT 0
)

CREATE TABLE rating (
	tank_id INT FOREIGN KEY REFERENCES tank(tank_id),
	user_account_id INT FOREIGN KEY REFERENCES user_account(user_account_id),
	rating INT NOT NULL CHECK (rating BETWEEN 1 AND 10),
	CONSTRAINT pk_rating PRIMARY KEY (tank_id, user_account_id)
)

CREATE TABLE favorite (
	tank_id INT FOREIGN KEY REFERENCES tank(tank_id),
	user_account_id INT FOREIGN KEY REFERENCES user_account(user_account_id),
	CONSTRAINT pk_favorite PRIMARY KEY (tank_id, user_account_id)
)

CREATE TABLE battle (
    battle_id INT PRIMARY KEY IDENTITY,
    battle_name VARCHAR(150) NOT NULL,
    year INT,
    location VARCHAR(150),
    description TEXT
)

CREATE TABLE tank_battle (
    tank_id INT FOREIGN KEY REFERENCES tank(tank_id),
    battle_id INT FOREIGN KEY REFERENCES battle(battle_id),
    role_description VARCHAR(200),
    CONSTRAINT pk_tank_battle PRIMARY KEY (tank_id, battle_id)
)

CREATE TABLE president (
	president_id INT PRIMARY KEY IDENTITY,
	name VARCHAR(200),
	country_id INT FOREIGN KEY REFERENCES country(country_id)
)

