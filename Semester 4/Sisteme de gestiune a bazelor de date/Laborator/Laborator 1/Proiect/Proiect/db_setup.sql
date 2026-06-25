CREATE DATABASE sgbd_ed;

CREATE TABLE departments(
    id bigint GENERATED ALWAYS AS IDENTITY,
    name varchar(255),
    location varchar(255),
    CONSTRAINT pk_departments PRIMARY KEY(id)
);

CREATE TABLE employees(
    id bigint GENERATED ALWAYS AS IDENTITY,
    first_name varchar(50),
    last_name varchar(50),
    email varchar(50),
    department_id bigint,
    CONSTRAINT fk_department_id FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE CASCADE
);

CREATE TABLE skills(
    id bigint GENERATED ALWAYS AS IDENTITY,
    name varchar(255),
    CONSTRAINT pk_skills PRIMARY KEY(id)
);

CREATE TABLE employee_skills(
    employee_id bigint,
    skill_id bigint,
    CONSTRAINT fk_employee_id FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE,
    CONSTRAINT fk_skill_id FOREIGN KEY (skill_id) REFERENCES skills(id) ON DELETE CASCADE,
    PRIMARY KEY(employee_id, skill_id)
);

INSERT INTO departments(name, location) VALUES
('Human Resources', 'Bucuresti'),
('Engineering', 'Cluj-Napoca'),
('Finance', 'Timisoara'),
('Sales', 'Iasi'),
('Marketing', 'Constanta'),
('IT', 'Brasov');

INSERT INTO employees (first_name, last_name, email, department_id) VALUES
('Andrei', 'Popescu', 'andrei.popescu@company.com', (SELECT id FROM departments WHERE name = 'Engineering')),
('Maria', 'Ionescu', 'maria.ionescu@company.com', (SELECT id FROM departments WHERE name = 'Human Resources')),
('Vlad', 'Georgescu', 'vlad.georgescu@company.com', (SELECT id FROM departments WHERE name = 'Finance')),
('Elena', 'Dumitru', 'elena.dumitru@company.com', (SELECT id FROM departments WHERE name = 'Sales')),
('Radu', 'Stan', 'radu.stan@company.com', (SELECT id FROM departments WHERE name = 'Marketing')),
('Ioana', 'Marin', 'ioana.marin@company.com', (SELECT id FROM departments WHERE name = 'Engineering')),
('Mihai', 'Petrescu', 'mihai.petrescu@company.com', (SELECT id FROM departments WHERE name = 'Finance')),
('Ana', 'Ilie', 'ana.ilie@company.com', (SELECT id FROM departments WHERE name = 'Human Resources')),
('Cristian', 'Nistor', 'cristian.nistor@company.com', (SELECT id FROM departments WHERE name = 'Sales')),
('Bianca', 'Enache', 'bianca.enache@company.com', (SELECT id FROM departments WHERE name = 'Marketing')),
('Alex', 'Popa', 'alex.popa@company.com', (SELECT id FROM departments WHERE name = 'IT'));