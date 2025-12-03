
-- task one
SELECT * FROM tank JOIN tank_info ti
	ON tank.tank_info_id = ti.tank_info_id

EXEC from_OneN_to_NOne 'tank', 'tank_info_id', 'tank_info'

SELECT * FROM tank JOIN tank_info ti
	ON tank.tank_id = ti.tank_id

-- task two
SELECT t.tank_id, m.manufacturer_id FROM tank t JOIN manufacturer m
	ON t.manufacturer_id = m.manufacturer_id;

EXEC from_OneN_To_MN 'tank', 'manufacturer_id', 'manufacturer';

SELECT * FROM tank_manufacturer;

-- task three
SELECT * FROM country_tank

EXEC from_MN_to_OneN 'tank', 'country', 'country_tank'

SELECT * FROM tank JOIN country
	ON tank.country_id = country.country_id

-- task four

SELECT * FROM country c JOIN president p
	ON c.country_id = p.country_id

EXEC from_OneN_to_OneOne 'country', 'president', 'country_id'

SELECT * FROM country c JOIN president p
	ON c.country_id = p.country_id