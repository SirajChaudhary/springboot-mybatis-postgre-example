-- =====================================================
-- 1. Connect to the database
-- =====================================================
-- First, manually create the database once in psql or pgAdmin:
-- CREATE DATABASE exampledb;
-- Then connect to it:
-- Right click the db "exampledb" -> Query Tool -> Paste & Execute this query

-- =====================================================
-- 2. Drop tables if they exist
-- =====================================================
DROP TABLE IF EXISTS employee CASCADE;
DROP TABLE IF EXISTS department CASCADE;
DROP TABLE IF EXISTS project CASCADE;

-- =====================================================
-- 3. Create tables
-- =====================================================

-- Department table
CREATE TABLE department (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

-- Project table
CREATE TABLE project (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    client VARCHAR(100)
);

-- Employee table
CREATE TABLE employee (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    salary NUMERIC(12,2) NOT NULL,
    department_id INT NOT NULL,
    project_id INT NOT NULL,
    CONSTRAINT fk_department FOREIGN KEY(department_id) REFERENCES department(id),
    CONSTRAINT fk_project FOREIGN KEY(project_id) REFERENCES project(id)
);

-- =====================================================
-- 4. Insert sample data
-- =====================================================

-- Departments
INSERT INTO department (name) VALUES
('Engineering'),
('Marketing'),
('Sales'),
('Human Resources');

-- Projects
INSERT INTO project (name, client) VALUES
('Alpha Project', 'TCS'),
('Beta Project', 'Infosys'),
('Gamma Project', 'Wipro');

-- Employees
INSERT INTO employee (name, salary, department_id, project_id) VALUES
('Amit Sharma', 75000, 1, 1),
('Rohit Verma', 68000, 1, 2),
('Priya Singh', 72000, 2, 2),
('Sneha Kapoor', 80000, 3, 3),
('Ankit Mehta', 70000, 1, 3),
('Pooja Nair', 65000, 2, 1),
('Rajesh Kumar', 90000, 3, 2),
('Deepika Reddy', 60000, 4, 3);
