ALTER TABLE media
    ADD created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ADD updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;
