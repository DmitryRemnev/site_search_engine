USE search_engine;

CREATE TABLE _field(
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    selector VARCHAR(255) NOT NULL,
    weight FLOAT NOT NULL,
    PRIMARY KEY(id));

INSERT INTO _field
    VALUES (1, 'title', 'title', 1.0),
    (2, 'body', 'body', 0.8);

CREATE TABLE _page(
    id INT NOT NULL AUTO_INCREMENT,
    path TEXT NOT NULL,
    code INT NOT NULL,
    content MEDIUMTEXT NOT NULL,
    PRIMARY KEY(id));

CREATE TABLE _lemma(
    id INT NOT NULL AUTO_INCREMENT,
    lemma VARCHAR(255) NOT NULL,
    frequency INT NOT NULL,
    PRIMARY KEY(id));

CREATE TABLE _index(
    id INT NOT NULL AUTO_INCREMENT,
    page_id INT NOT NULL,
    lemma_id INT NOT NULL,
    rating FLOAT NOT NULL,
    PRIMARY KEY(id));

CREATE INDEX lemma_index ON _lemma(lemma);