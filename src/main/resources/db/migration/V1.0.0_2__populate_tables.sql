INSERT INTO STARSHIPS(STARSHIP_ID, STARSHIP_NAME, MOVIE_NAME, NUMBER_OF_PASSENGERS)
    VALUES (nextval('starships_seq'), 'USS Río Grande', 'Star Trek', 4),
           (nextval('starships_seq'), 'USS Orinoco', 'Star Trek', 6),
           (nextval('starships_seq'), 'USS Enterprise', 'Star Trek',  10),
           (nextval('starships_seq'), 'Millennium Falcon', 'Star Wars', 4),
           (nextval('starships_seq'), 'Federation Cruiser', 'FTL', 6);