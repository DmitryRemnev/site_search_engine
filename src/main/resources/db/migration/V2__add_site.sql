USE search_engine;

CREATE TABLE _site(
    id INT NOT NULL AUTO_INCREMENT,
    status ENUM('INDEXING', 'INDEXED', 'FAILED') NOT NULL,
    status_time DATETIME NOT NULL,
    last_error TEXT,
    url VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY(id));

ALTER TABLE _page
    ADD site_id INT NOT NULL;

ALTER TABLE _lemma
    ADD site_id INT NOT NULL;