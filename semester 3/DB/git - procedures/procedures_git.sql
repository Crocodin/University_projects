CREATE PROCEDURE alt_column_type AS
BEGIN
	ALTER TABLE country ALTER COLUMN country_name NVARCHAR(100) NOT NULL;
	PRINT 'Changed the country_name typr from VARCHAR to NVARCHAR'
END;

CREATE PROCEDURE rev_column_type AS
BEGIN
	ALTER TABLE country ALTER COLUMN country_name VARCHAR(100) NOT NULL;
	PRINT 'Changed the country_name typr from NVARCHAR to VARCHAR'
END;

-- ======= -- 1/2

CREATE PROCEDURE alt_const_made_year AS
BEGIN
	-- 1913 the yera of the first moder tank
	ALTER TABLE tank ADD CONSTRAINT DF_tank_made_date_year DEFAULT 1913 FOR made_date_year;
	PRINT 'Added default made_date_year to be 1913 (the date of the making of the first tank) in the tank table'
END;

CREATE PROCEDURE rev_const_made_year AS
BEGIN
	ALTER TABLE tank DROP CONSTRAINT DF_tank_made_date_year;
	PRINT 'Removed the default made_date_year from the tank table'
END;

-- ======= -- 2/3

CREATE PROCEDURE add_tank_info AS
BEGIN
	CREATE TABLE tank_info(
		tank_info_id INT PRIMARY KEY IDENTITY,
		mass INT, 
		length INT,
		width INT,
		height INT,
		crew VARCHAR(100),
		engine VARCHAR(100)
	);
	PRINT 'Added new Tank_Info table'
END;

CREATE PROCEDURE del_tank_info AS
BEGIN
	DROP TABLE tank_info;
	PRINT 'Deleted the Tank_Info table'
END;

-- ======= -- 3/4

CREATE PROCEDURE add_column_country AS
BEGIN
	ALTER TABLE country ADD GDP INT;
	PRINT 'Added new GDP column to the Country table'
END;

CREATE PROCEDURE del_column_country AS
BEGIN
	ALTER TABLE country DROP COLUMN GDP;
	PRINT 'Removed the GDP column from the Country table'
END;

-- ======= -- 4/5

CREATE PROCEDURE add_foreign_key AS
BEGIN
	-- ALTER TABLE tank ADD tank_info_id INT FOREIGN KEY REFERENCES tank_info(tank_info_id);
	ALTER TABLE tank ADD tank_info_id INT;
	ALTER TABLE tank ADD CONSTRAINT FK_tank_info_id FOREIGN KEY (tank_info_id) REFERENCES tank_info(tank_info_id)
	PRINT 'Added a new foreign key to the Tank tabel referenceing the Tank_Info table'
END;

CREATE PROCEDURE del_foreign_key AS
BEGIN
	ALTER TABLE tank DROP CONSTRAINT FK_tank_info_id;
	ALTER TABLE tank DROP COLUMN tank_info_id;
	PRINT 'Removed the Tank_Info foreign kay referance in the Tank table'
END;