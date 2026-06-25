## Configurarea bazei de date

Baza de date folosita: PostgreSQL

- Scriptul pentru crearea bazei de date

CREATE DATABASE sgbd_ed;

Numele bazei de date poate fi schimbat, daca se schimba ulterior si URL-ul de conectare la baza de date din `appsettings.json`.

- Scripturile de creare a tabelelor

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

Se poate folosi fisierul `db_setup.sql` pentru a crea baza de date si tabelele necesare (include si adaugarea unor date de exemplu).

## Configurarea conexiunii la baza de date

Se realizeaza in fisierul `appsettings.json`.

```json
{
  "ConnectionStrings": {
    "PostgreSqlConnection": "Host=db_host;Port=db_port;Database=db_name;Username=db_username;Password=db_password"
  }
}
```

## Rularea aplicatiei

Pentru a rula aplicatia:

- prin executarea functiei `Main` din `Program.cs` 

- prin folosirea comenzii `dotnet run` in terminal, din interiorul directorulului `/Proiect`.