
-- all the tanks manufactured by Volgograd Tractor Plant that are still in use
SELECT * FROM tank t 
JOIN manufacturer m ON t.manufacturer_id = m.manufacturer_id
JOIN country_tank ct ON t.tank_id = ct.tank_id
WHERE m.manufacturer_name = 'Volgograd Tractor Plant' AND ct.service_end_year is NULL;

-- the county of origin of all the manufacturers whose tanks are still in use
SELECT DISTINCT c.country_name FROM country c 
JOIN manufacturer m ON c.country_id = m.headquarters_country_id
JOIN country_tank ct ON m.headquarters_country_id = ct.country_id
WHERE ct.service_end_year is NULL;

-- all the countries whose tanks foght in a battle and the battel they wore in
SELECT DISTINCT c.country_name,  b.battle_name FROM country c 
JOIN country_tank ct ON c.country_id = ct.country_id
JOIN tank_battle tb ON ct.tank_id = tb.tank_id
JOIN battle b ON tb.battle_id = b.battle_id
ORDER BY c.country_name, b.battle_name;

-- all the user whose email ands in .com and have favorite tanks that are in still  use
SELECT DISTINCT u.* FROM user_account u
JOIN favorite f ON u.user_account_id = f.user_account_id
JOIN country_tank ct ON ct.tank_id = f.tank_id
WHERE u.email LIKE '%.com' AND ct.service_end_year is NULL;

-- users that have tanks that wore developed during WWII
SELECT DISTINCT u.username FROM tank t
JOIN favorite f ON f.tank_id = t.tank_id
JOIN user_account u ON f.user_account_id = u.user_account_id
WHERE t.made_date_year BETWEEN 1931 AND 1945;

-- all the countryes that used more then 4 tanks
SELECT c.country_name, COUNT(ct.tank_id) AS total_tanks_used
FROM country c JOIN country_tank ct ON c.country_id = ct.country_id
GROUP BY c.country_id, c.country_name
HAVING COUNT(ct.tank_id) > 4 ORDER BY c.country_name;

-- codrins favorite tank that is made by the oldest manufacturer
SELECT t.tank_name, m.manufacturer_name, m.founded_year FROM user_account u 
JOIN favorite f ON f.user_account_id = u.user_account_id
JOIN tank t ON f.tank_id = t.tank_id
JOIN manufacturer m ON t.manufacturer_id = m.manufacturer_id
WHERE u.username = 'codrin_parmac' AND m.founded_year = (
	SELECT MIN(m2.founded_year) FROM user_account u2
	JOIN favorite f2 ON f2.user_account_id = u2.user_account_id
    JOIN tank t2 ON f2.tank_id = t2.tank_id
    JOIN manufacturer m2 ON t2.manufacturer_id = m2.manufacturer_id
    WHERE u2.username = 'codrin_parmac'
)

-- select the manufacturers whose tanks have an above avrage rating
SELECT m.manufacturer_name, AVG(r.rating) AS avrage_rating FROM rating r 
JOIN tank t ON r.tank_id = t.tank_id
JOIN manufacturer m ON t.manufacturer_id = m.manufacturer_id
GROUP BY m.manufacturer_name
HAVING AVG(r.rating) > (
    SELECT AVG(rating) FROM rating r
)

-- select all tanks that are in service and have an above avrage rating
SELECT t.tank_name, c.country_name, AVG(r.rating) AS avrgae_rating FROM tank t
JOIN country_tank ct ON t.tank_id = ct.tank_id
JOIN country c ON ct.country_id = c.country_id
JOIN rating r ON t.tank_id = r.tank_id
GROUP BY t.tank_id, t.tank_name, c.country_name, ct.service_end_year
HAVING AVG(r.rating) > (
    SELECT AVG(rating) FROM rating r
) AND ct.service_end_year is NULL

-- tanks that wore used in designed during WW2 and are in use today
SELECT t.tank_name, t.made_date_year, c.country_name FROM tank t 
JOIN country_tank ct ON ct.tank_id = t.tank_id
JOIN country c ON ct.country_id = c.country_id
WHERE t.made_date_year BETWEEN 1931 AND 1945 AND ct.service_end_year is NULL
