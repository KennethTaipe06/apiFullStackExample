-- Create new book table with English column names
CREATE TABLE IF NOT EXISTS book (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    publication_year INTEGER,
    category VARCHAR(100)
);

-- If there's existing data in 'libro' table, migrate it to the new 'book' table
INSERT INTO book (title, author, publication_year, category)
SELECT 
    nombre, 
    autor, 
    ano_publicacion, 
    categoria
FROM libro;

-- You can drop the old table after verifying the migration
-- DROP TABLE libro;
