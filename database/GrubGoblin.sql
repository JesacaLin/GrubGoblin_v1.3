-- Database: Grub Goblin v1.3

START TRANSACTION;

DROP TABLE IF EXISTS app_user, role, place, deal, deal_availability, availability, review CASCADE;

CREATE TABLE app_user (
	username VARCHAR(250) PRIMARY KEY,
	password VARCHAR(250) NOT NULL,
	email VARCHAR(250)
);

CREATE TABLE role (
	username VARCHAR(250) REFERENCES app_user(username),
	user_role VARCHAR(250),
	PRIMARY KEY (username, user_role)
);

 CREATE TABLE place (
    place_id SERIAL PRIMARY KEY,
    place_name VARCHAR(100) NOT NULL,
    address VARCHAR(100) NOT NULL,
    latitude DECIMAL(9,6),
    longitude DECIMAL(9,6) ,
    google_rating DECIMAL(2, 1)
 );

 CREATE TABLE deal (
    deal_id SERIAL PRIMARY KEY,
    place_id INT REFERENCES place(place_id) ON DELETE CASCADE,
    type_of_deal VARCHAR(20),
    deal_description VARCHAR(150) NOT NULL,
	created_at TIMESTAMP DEFAULT NOW(),
	updated_at TIMESTAMP DEFAULT NOW(),
	created_by VARCHAR REFERENCES app_user(username) ON DELETE CASCADE
 );

 CREATE TABLE availability (
    availability_id SERIAL PRIMARY KEY,
    day_of_week INT NOT NULL,
    start_time TIME,
    end_time TIME
 );

 CREATE TABLE deal_availability (
      deal_id INT REFERENCES deal(deal_id) ON DELETE CASCADE,
      availability_id INT REFERENCES availability(availability_id) ON DELETE CASCADE,
      PRIMARY KEY(deal_id, availability_id)
   );

 CREATE TABLE review (
    review_id SERIAL PRIMARY KEY,
    deal_id INT REFERENCES deal(deal_id) ON DELETE CASCADE,
    stars DECIMAL(2,1),
    review_description VARCHAR(200)
 );
 
 INSERT INTO app_user (username, password, email)
 VALUES 
 ('jesaca', '$2a$10$vHEyGfKbCUDC1ZzVY3uR0emfjHG2GBdMVTtBfFZxkUXWVLagdfQTC', 'jesacalin@gmail.com'), 
 ('tofu', '$2a$10$XQenfZZ7kSDVuZev0ktlCuREIYNtiX4c1qR/YCsn4Kz2ydkw0Mp76', 'tofu@gmail.com');
 
 INSERT INTO role (username, user_role)
 VALUES
 ('jesaca', 'admin'),
 ('tofu', 'contributor');


 INSERT INTO place (place_name, address, latitude, longitude, google_rating)
    VALUES
    ('elsa', '136 Atlantic Ave, Brooklyn, NY 11201', 40.690239, -73.995361, 4.7),
    ('vine', '81 Fleet Pl, Brooklyn, NY 11201', 40.693211, -73.981331, 4.9),
    ('osteria brooklyn @dekalb', '445 Albee Square Brooklyn, NY 11201', 40.690740, -73.983270, null),
    ('osteria brooklyn', '458 Myrtle Ave, Brooklyn, NY 11205', 40.693120, -73.967140, 4.6);

INSERT INTO deal (place_id, type_of_deal, deal_description, created_at, created_by)
    VALUES
    -- DEAL 1
    (1, 'drinks', '$8 cocktails, 2 types', '2024-05-01 00:12:00', 'jesaca'),
    (1, 'drinks', '$8 cocktails, 2 types', '2024-05-01 00:12:00', 'jesaca'),
    (1, 'drinks', '$8 cocktails, 2 types', '2024-05-01 00:12:00', 'jesaca'),
    (1, 'drinks', '$8 cocktails, 2 types', '2024-05-01 00:12:00', 'jesaca'),
    (1, 'drinks', '$8 cocktails, 2 types', '2024-05-01 00:12:00', 'jesaca'),
    -- DEAL 2
    (2, 'drinks', '$11 cocktails', '2024-05-03 00:12:00', 'tofu'),
    (2, 'drinks', '$11 cocktails', '2024-05-03 00:12:00', 'tofu'),
    -- DEAL 3
    (3, 'drinks', '50% off drinks', '2024-05-05 00:12:00', 'jesaca'),
    (3, 'drinks', '50% off drinks', '2024-05-05 00:12:00', 'jesaca'),
    -- DEAL 4
    (3, 'food', '2 course lunch set menu $25', '2024-05-07 00:12:00', 'jesaca'),
    -- DEAL 5
    (4, 'drinks', '$8 cocktails', '2024-05-08 00:12:00',  'tofu');

INSERT INTO availability (day_of_week, start_time, end_time)
    VALUES
    -- DEAL 1
    (1, '17:00:00', '19:00:00'),
    -- DEAL 2
    (2, '17:00:00', '19:00:00'),
    -- DEAL 3
    (3, '17:00:00', '19:00:00'),
    -- DEAL 4
    (4, '17:00:00', '19:00:00'),
    -- DEAL 5
    (5, '17:00:00', '19:00:00'),

    -- DEAL 6
    (6, '17:00:00', '20:00:00'),
    -- DEAL 7
    (7, '17:00:00', '20:00:00'),

    -- DEAL 8
    (5, '16:00:00', '18:00:00'),
    -- DEAL 9
    (1, '11:00:00', '14:00:00'),
    -- DEAL 10
    (5, '11:00:00', '14:00:00'),
    -- DEAL 11
    (2, '16:00:00', '21:00:00');


INSERT INTO deal_availability (deal_id, availability_id)
    VALUES
    ( 1, 1),
    ( 2, 2),
    ( 3, 3),
    ( 4, 4),
    ( 5, 5),
    ( 6, 6),
    ( 7, 7),
    ( 8, 8),
    ( 9, 9),
    ( 10, 10),
    ( 11, 11);


INSERT INTO review ( deal_id, stars,review_description)
    VALUES
    (1, 3.9, 'The happy hour drinks were ok, their full price cocktails are much better!'),
    (2, 3.9, 'The happy hour drinks were ok, their full price cocktails are much better!'),
    (3, 3.9, 'The happy hour drinks were ok, their full price cocktails are much better!'),
    (4, 3.9, 'The happy hour drinks were ok, their full price cocktails are much better!'),
    (5, 3.9, 'The happy hour drinks were ok, their full price cocktails are much better!'),
    (6, 4.5, 'Solid cocktails and friendly service. Wish it was a dollar or two less. The charcuterie is worth getting.'),
    (7, 4.5, 'Solid cocktails and friendly service. Wish it was a dollar or two less. The charcuterie is worth getting.'),
    (8, 3.0, 'Good place to get a quick drink if you need to rest while shopping, nothing to write home about.'),
    (9, 3.0, 'Good place to get a quick drink if you need to rest while shopping, nothing to write home about.'),
    (10, 3.0, 'Very large portions so the deal is worth it!'),
    (11, 4.0, 'Love the vibe here, good place for drinks with friends. Drinks are STRONG!');

COMMIT;