INSERT INTO country(country_name) VALUES ('United States of America'), ('Russia'), ('Germany'), ('United Kingdom'),
('France'), ('China'), ('Japan'), ('India'), ('Israel'), ('Ukraine'), ('Poland'), ('Turkey'), ('Iran'), ('Iraq'),
('Syria'), ('Egypt'), ('Saudi Arabia'), ('North Korea'), ('South Korea'), ('Vietnam'), ('Pakistan'), ('Canada'),
('Australia'), ('Italy'), ('Finland'), ('Greece'), ('Serbia'), ('Ukraine'), ('Belarus'), ('Romania'), ('URRS');

INSERT INTO manufacturer(manufacturer_name, founded_year, headquarters_country_id) VALUES ('Ford Motor Company',1903, 1),
('Volgograd Tractor Plant', 1930, 2), ('Krupp', 1811, 3), ('Vauxhall Motors', 1857, 4), ('Renault', 1899, 5),
('Uralvagonzavod', 1936, 31), ('Henschel & Sohn', 1810, 3), ('Kharkiv Morozov Machine Building Design Bureau', 1927, 31),
('Mitsubishi Heavy Industries', 1884, 7), ('Israel Military Industries', 1933, 9), ('General Dynamics Land Systems', 1982, 1), 
('Krauss-Maffei Wegmann', 1931, 3), ('Uraltransmash', 1817, 31), ('Ansaldo', 1853, 24), ('Tampella', 1861, 25),
('Military Technical Institute', 1948, 27), ('Leonida', 1943, 30), ('OKMO', 1932, 31), ('Renault', 1899, 5), ('Kirov Plant', 1801, 2),
('Detroit Arsenal', 1940, 1), ('Royal Ordnance Factory', 1930, 4), ('New South Wales Government Railways (NSWGR)', 1855, 23),
('Montreal Locomotive Works', 1888, 22), ('Hellenic Vehicle Industry', 1972, 26)

INSERT INTO tank(tank_name, made_date_year, manufacturer_id) VALUES ('M4 Sherman', 1941, 1), ('T-26', 1932, 18),
('T-36', 1940, 2), ('PT-76', 1951, 2), ('BMD-1', 1968, 2), ('BMD-2', 1985, 2), ('BMD-3', 1985, 2), ('BMD-4', 2004, 2),
('Panzer I', 1932, 7), ('Panzer II', 1934, 2), ('Panzer III', 1935, 7), ('Panzer IV', 1936, 2), ('Mk IV (A22) Churchill', 1941, 4),
('Char B1', 1921, 19), ('Char D2', 1936, 19), ('Char 2C', 1921, 19), ('Renault FT', 1917, 19), ('MS-1', 1928, 6),
('Tiger I', 1938, 7), ('Tiger II', 1943, 7), ('ISU-122', 1942, 13), ('IS-2', 1943, 20),  ('T-34', 1938, 8), 
('M26 Pershing', 1942, 21), ('M60', 1956, 21), ('M1 Abrams', 1972, 21), ('MBT-70', 1965, 1), ('M48 Patton', 1952, 1),
('FV 4030 Challenger', 1983, 22), ('FV4007 (A41) Centurion', 1945, 4), ('Tiger 131', 1943, 7), ('M3 Lee', 1941, 21), 
('Churchill Crocodile', 1944, 4), ('Panzer IV', 1936, 3), ('T-80', 1976, 8), ('K1', 1978, 11), ('2S3 Akatsiya', 1967, 13), 
('2S19 Msta-S', 1980, 13), ('TACAM R-2', 1943, 17), ('Panzer 35', 1934, 17), ('AC4', 1943, 23), ('AC3 Thunderbolt', 1943, 23),
('P26/40', 1940, 14), ('Type 3 Chi-Nu', 1943, 9), ('Grizzly I', 1943, 24), ('Ram', 1941, 24), ('Type 97 Chi-Ha', 1936, 9),
('Merkava', 1970, 10), ('Leopard 2', 1979, 25), ('Carro Armato M13/40', 1939, 14);


INSERT INTO country_tank(country_id, tank_id, service_start_year, service_end_year) VALUES (22, 1, 1941, 1944),
(1, 1, 1942, 1955),(1, 25, 1961, 1997), (1, 26, 1980, NULL), (2, 2, 1933, 1941), (2, 3, 1940, 1945),
(2, 4, 1951, NULL), (2, 5, 1969, NULL), (2, 6, 1985, NULL), (2, 7, 1990, NULL), (2, 8, 2004, NULL),
(2, 23, 1940, NULL), (2, 22, 1943, 1970), (2, 21, 1944, 1975), (2, 18, 1928, 1935), (3, 9, 1934, 1954),
(3, 10, 1936, 1945), (3, 11, 1937, 1945), (3, 12, 1939, 1967), (3, 19, 1942, 1945), (3, 20, 1944, 1945), 
(4, 13, 1941, 1952), (31, 22, 1950, 1980), (6, 4, 1955, 1970), (6, 25, 1980, 1990), (9, 25, 1970, 2000),
(9, 26, 1982, NULL), (9, 22, 1948, 1956);

INSERT INTO user_account(username, email, country_of_origin_id) VALUES ('codrin_parmac', 'steelcommander@usa.com', 1),
('bogdan_mocrei', 'kremlin_tanker@ru.ru', 2), ('andrei_matean', 'panzer_expert@de.de', 3),
('paul_berindeiei', 'brit_armour@uk.co', 4), ('cuciu', 'french_charfan@fr.fr', 5), ('dede', 'dragon_of_china@cn.cn', 6),
('teo', 'samurai_driver@jp.jp', 7), ('andreea', 'desertlion_israel@il.il', 9),
('aida', 'steel_turret_ukraine@ua.ua', 10), ('mircea', 'maple_mechanic@ca.ca', 22),
('denisa', 'realgril69@gmail.com', 3), ('bianca', 'cuteforme@gghf.lol', 3);

INSERT INTO battle (battle_name, year, location, description) VALUES
('Battle of Kursk', 1943, 'Russia', 'The largest tank battle in history between German Panzer divisions and Soviet forces.'),
('Battle of El Alamein', 1942, 'Egypt', 'A decisive North African battle featuring British tanks against German units under Rommel.'),
('Battle of Normandy', 1944, 'France', 'The Allied invasion of Western Europe; M4 Sherman tanks faced German Tiger and Panther tanks in intense hedgerow combat.'),
('Battle of the Bulge', 1944, 'Belgium', 'The last major German offensive on the Western Front involving American against German.'),
('Battle of Berlin', 1945, 'Germany', 'The final battle in Europe; Soviet spearheaded the assault on Nazi defenses.'),
('Battle of Khalkhin Gol', 1939, 'Mongolia', 'Soviet T-26 and BT tanks fought Japanese Type 95 and Type 97 tanks in a border conflict preceding WWII.'),
('Battle of Stalingrad', 1942, 'Russia', 'One of WWII’s bloodiest battles, involving German Panzer IVs and Soviet T-34s and T-60s in brutal urban warfare.'),
('Battle of the Yom Kippur War', 1973, 'Sinai and Golan Heights', 'Israeli Merkava and Centurion tanks fought Egyptian and Syrian forces equipped with T-55 and T-62 tanks.'),
('Battle of 73 Easting', 1991, 'Iraq', 'During the Gulf War, American M1 Abrams tanks engaged and destroyed Iraqi T-72s in one of the most lopsided tank battles in history.'),
('Battle of the Golan Heights', 1967, 'Syria', 'During the Six-Day War, Israeli Centurion and Sherman tanks clashed with Syrian.'),
('Battle of Prokhorovka', 1943, 'Russia', 'Part of the larger Kursk offensive; Soviet T-34s fought German Tiger and Panther tanks in close combat.'),
('Battle of Warsaw', 1939, 'Poland', 'German Panzer I and Panzer II tanks spearheaded the invasion of Poland against outdated Polish armor.'),
('Battle of Tobruk', 1941, 'Libya', 'A key siege in North Africa where Australian and British Matilda and Crusader tanks resisted German Panzer attacks.'),
('Battle of Chosin Reservoir', 1950, 'Korea', 'U.S. M26 Pershing and M46 Patton tanks supported infantry against Chinese T-34-85s during the Korean War.'),
('Battle of Sinai (Operation Kadesh)', 1956, 'Egypt', 'Israeli AMX-13 and Sherman tanks fought Egyptian forces using T-34s and SU-100s during the Suez Crisis.');


INSERT INTO tank_battle(tank_id, battle_id) VALUES (12, 1), (19, 1), (20, 1), (23, 1), (22, 1), (13, 2), (11, 2), (12, 2),
(1, 3), (20, 3), (34, 3), (23, 4), (22, 4), (21, 4), (23, 10)

INSERT INTO favorite(tank_id, user_account_id) VALUES (1, 1), (2, 1), (3, 1), (4, 1), (5, 6), (7, 1), (10, 1), (20, 1),
(2, 2), (3, 2), (35, 10), (35, 1), (19, 1), (44, 1), (50, 1), (46, 1)

INSERT INTO rating (tank_id, user_account_id, rating) VALUES
(1, 1, 9), (1, 2, 8), (1, 3, 7), (1, 4, 6), (5, 5, 9), (6, 6, 8), (7, 7, 7), (1, 5, 1), (1, 10, 3), (1, 9, 4), (2, 2, 2),
(8, 8, 1), (9, 9, 6), (10, 10, 7), (11, 11, 9), (12, 12, 8), (17, 1, 8), (17, 2, 7), (17, 3, 6), (17, 4, 9), (17, 5, 7), (18, 6, 8),
(20, 7, 10), (20, 8, 9), (20, 9, 8), (22, 10, 3), (23, 11, 8), (25, 12, 3), (25, 1, 3), (26, 2, 10), (26, 3, 9), (28, 4, 8), (29, 5, 9),
(30, 6, 7), (33, 7, 3), (33, 8, 6), (33, 9, 9), (34, 10, 10), (35, 11, 8), (38, 12, 7), (38, 1, 9), (38, 2, 8), (39, 3, 7), (40, 4, 8),
(41, 5, 6), (42, 6, 9), (43, 7, 10), (44, 8, 1), (45, 9, 4), (46, 10, 9), (47, 11, 10), (48, 12, 8), (49, 1, 9), (2, 5, 2);
