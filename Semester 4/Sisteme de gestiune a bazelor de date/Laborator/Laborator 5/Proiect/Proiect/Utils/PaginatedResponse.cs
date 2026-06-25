namespace Proiect.Utils;

/// <summary>
/// Response object pentru paginarea offset-based.
/// Contine datele paginii curente + metadate de paginare.
/// </summary>
public class PaginatedResponse<T>
{
    // Input: IEnumerable<T> data, int pageNumber, int pageSize, int totalRecords.
    // Return: nu returneaza nimic (constructor).
    // Calculeaza numarul total de pagini si flaguri de navigare.
    public PaginatedResponse(IEnumerable<T> data, int pageNumber, int pageSize, int totalRecords)
    {
        Data = data;
        PageNumber = pageNumber;
        PageSize = pageSize;
        TotalRecords = totalRecords;
        TotalPages = (totalRecords + pageSize - 1) / pageSize; // Math.Ceiling
        HasNextPage = pageNumber < TotalPages;
        HasPreviousPage = pageNumber > 1;
    }

    // Datele paginii curente
    public IEnumerable<T> Data { get; set; }

    // Numarul paginii curente (1-based)
    public int PageNumber { get; set; }

    // Dimensiunea paginii (cate inregistrari per pagina)
    public int PageSize { get; set; }

    // Numarul total de inregistrari din tabel
    public int TotalRecords { get; set; }

    // Numarul total de pagini
    public int TotalPages { get; set; }

    // True daca exista o pagina urmatoare
    public bool HasNextPage { get; set; }

    // True daca exista o pagina anterioara
    public bool HasPreviousPage { get; set; }
}

