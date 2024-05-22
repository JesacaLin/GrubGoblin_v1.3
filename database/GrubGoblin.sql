-- Database: Grub Goblin v1.3

START TRANSACTION;

DROP TABLE IF EXISTS app_user, role, place, deal, review CASCADE;

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
	days_of_week VARCHAR(7) NOT NULL,
    start_time TIME,
    end_time TIME,
	created_at TIMESTAMP DEFAULT NOW(),
	updated_at TIMESTAMP DEFAULT NOW(),
	created_by VARCHAR REFERENCES app_user(username) ON DELETE CASCADE
 );

 CREATE TABLE review (
    review_id SERIAL PRIMARY KEY,
    deal_id INT REFERENCES deal(deal_id) ON DELETE CASCADE,
    stars DECIMAL(2,1),
    review_description VARCHAR(200)
	reviewed_by VARCHAR REFERENCES app_user(username) ON DELETE CASCADE
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
	
--DAYS OF THE WEEK LETTERS ARE m, t, w, h, f, s, u 

INSERT INTO deal (place_id, type_of_deal, deal_description, days_of_week, start_time, end_time, created_at, created_by)
    VALUES
    -- DEAL 1
    (1, 'drinks', '$8 cocktails, 2 types', 'mtwhf', '17:00:00', '19:00:00, '2024-05-01 00:12:00', 'jesaca'),
   
    -- DEAL 2
    (2, 'drinks', '$11 cocktails', 'mtwhf', '17:00:00', '20:00:00', '2024-05-03 00:12:00', 'tofu'),
   
    -- DEAL 3
    (3, 'drinks', '50% off drinks', 'mtwhf', '17:00:00', '20:00:00', '2024-05-05 00:12:00', 'jesaca'),
   
    -- DEAL 4
    (3, 'food', '2 course lunch set menu $25', 'su', '17:00:00', '20:00:00', '2024-05-07 00:12:00', 'jesaca'),
	 
    -- DEAL 5
    (4, 'drinks', '$8 cocktails', 'mtw', '17:00:00', '20:00:00', '2024-05-08 00:12:00',  'tofu');


INSERT INTO review ( deal_id, stars,review_description, reviewed_by)
    VALUES
    (1, 3.9, 'The happy hour drinks were ok, their full price cocktails are much better!', 'jesaca'),
	(1, 4.2, 'The happy hour is solid enough I can pop in and enjoy my book in the cozy vibe', 'tofu'),
    (2, 4.5, 'Solid cocktails and friendly service. Wish it was a dollar or two less. The charcuterie is worth getting.', 'jesaca'),
    (3, 3.0, 'Good place to get a quick drink if you need to rest while shopping, nothing to write home about.', 'tofu'),
    (4, 4.5, 'Very large portions so the deal is worth it!', 'jesaca'),
	 (4, 2.5, 'I can make better pasta than this, very bland!', 'jesaca'),
    (5, 4.0, 'Love the vibe here, good place for drinks with friends. Drinks are STRONG!', 'tofu');
	 (5, 5.0, 'If you have a lot of different dates, bring them here. Cheap!!', 'tofu');

COMMIT;