﻿using Proiect.Domain;
using Proiect.Repository;
using Proiect.Utils;

namespace Proiect.Service;

// Ofera operatii de business pentru departamente.
public class DepartmentsService
{
    public static readonly DepartmentsService Instance = new  DepartmentsService();
    private readonly IPageableRepository<long, Department> _repository;
    
    // Input: fara parametri.
    // Return: nu returneaza nimic (constructor).
    // Initializeaza repository-ul de departamente.
    // Este folosit la crearea instantei singleton.
    private DepartmentsService()
    {
        _repository = new DepartmentsRepository();
    }
    
    // Input: fara parametri.
    // Return: int.
    // Returneaza numarul total de departamente.
    // Foloseste COUNT din repository.
    public int Size()
    {
        return _repository.Size();
    }

    // Input: string name, string location.
    // Return: void.
    // Creeaza si salveaza un departament nou.
    // Id-ul este generat de baza de date.
    public void Save(string name, string location)
    {
        var entity = new Department(name, location);
        _repository.Save(entity);
    }

    // Input: long id.
    // Return: void.
    // Sterge departamentul dupa id.
    // Operatia este delegata repository-ului.
    public void DeleteById(long id)
    {
        _repository.Delete(id);
    }

    // Input: long id, string name, string location.
    // Return: void.
    // Actualizeaza datele unui departament existent.
    // Trimite entitatea noua la repository.
    public void Update(long id, string name, string location)
    {
        var entity = new Department(name, location);
        _repository.Update(id, entity);
    }

    // Input: long id.
    // Return: Department.
    // Cauta un departament dupa id.
    // Arunca exceptie daca nu exista.
    public Department FindById(long id)
    {
        return _repository.FindById(id);
    }

    // Input: fara parametri.
    // Return: IEnumerable<Department>.
    // Intoarce toate departamentele din baza de date.
    // Este folosit pentru popularea grilelor si listelor.
    public IEnumerable<Department> FindAll()
    {
        return _repository.FindAll();
    }

    // Input: int pageNumber (1-based), int pageSize.
    // Return: PaginatedResponse<Department>.
    // Intoarce o pagina de departamente cu metadate de paginare.
    // Verifica si coriteaza parametrii daca sunt invalizi.
    public PaginatedResponse<Department> FindAllPaginated(int pageNumber, int pageSize)
    {
        return _repository.FindAllPaginated(pageNumber, pageSize);
    }
    
    // Input: long? lastId (null pentru prima pagina), int pageSize.
    // Return: List<Department>.
    // Intoarce o pagina de departamente folosind un cursor simplu (lastId).
    // Este o alternativa la paginarea offset-based, mai eficienta pentru baze mari.
    public PaginatedResponse<Department> FindAllPaginatedByCursor(long? lastId, int pageSize)
    {
        return _repository.FindAllPaginatedByCursor(lastId, pageSize);
    }
}