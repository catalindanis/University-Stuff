using System;
using System.Diagnostics;
using System.Linq;
using Npgsql;

namespace Proiect.Utils;

/// <summary>
/// Testează performanța interogărilor cu și fără indexuri.
/// - Generează 10.000+ înregistrări de angajați
/// - Testează performanța FĂRĂ indexuri
/// - Creează indexuri și retestează
/// - Afișează EXPLAIN ANALYZE pentru fiecare interogare
/// - Face cleanup la final
/// </summary>
public class IndexingTests
{
    private readonly string _connectionString = "Host=localhost;Username=postgres;Password=dbcata05;Database=sgbd_ed";
    private const int ITERATIONS = 100;
    private const int TEST_DATA_COUNT = 10000;
    private const int EXISTING_EMPLOYEES = 11;

    public void run()
    {
        try
        {
            if (!VerifyDatabaseConnection())
            {
                Console.WriteLine("\nCannot connect to PostgreSQL database.");
                return;
            }

            Console.WriteLine("Preparing database schema...");
            PrepareSchema();
            Console.WriteLine();

            Console.WriteLine("Generating 10.000 test records...");
            GenerateTestData();
            Console.WriteLine();

            Console.WriteLine("Testing WITHOUT INDEXES...");
            RemoveAllIndexes();
            var resultsWithoutIndexes = TestQueries();
            Console.WriteLine();
            
            Console.WriteLine("Creating indexes...");
            CreateIndexes();
            Console.WriteLine();

            Console.WriteLine("Testing WITH INDEXES...");
            var resultsWithIndexes = TestQueries();
            Console.WriteLine();
            
            Console.WriteLine("Performance Comparison...");
            CompareResults(resultsWithoutIndexes, resultsWithIndexes);
            Console.WriteLine();
            
            Console.WriteLine("Query Execution Plans (EXPLAIN ANALYZE)...");
            DisplayQueryPlans();
            Console.WriteLine();
            
            Console.WriteLine("Cleanup...");
            CleanupTestData();
        }
        catch (Exception ex)
        {
            Console.WriteLine($"ERROR: {ex.Message}");
            Console.WriteLine(ex.StackTrace);
            Console.WriteLine();
        }
    }

    private bool VerifyDatabaseConnection()
    {
        try
        {
            using var conn = new NpgsqlConnection(_connectionString);
            conn.Open();
            conn.Close();
            return true;
        }
        catch
        {
            return false;
        }
    }
    
    private void PrepareSchema()
    {
        using var conn = new NpgsqlConnection(_connectionString);
        conn.Open();
        using var cmd = conn.CreateCommand();
        
        cmd.CommandText = @"
            ALTER TABLE employees 
            ADD COLUMN IF NOT EXISTS salary DECIMAL(10, 2) DEFAULT 50000;";
        try
        {
            cmd.ExecuteNonQuery();
            Console.WriteLine("Schema prepared (salary column verified)");
        }
        catch
        {
            Console.WriteLine("Schema already prepared");
        }

        conn.Close();
    }
    
    private void GenerateTestData()
    {
        using var conn = new NpgsqlConnection(_connectionString);
        conn.Open();
        using var cmd = conn.CreateCommand();

        cmd.CommandText = "SELECT COUNT(*) FROM employees;";
        long currentCount = (long) cmd.ExecuteScalar();
        Console.WriteLine($"Current employee count: {currentCount}");

        if (currentCount >= TEST_DATA_COUNT + EXISTING_EMPLOYEES)
        {
            Console.WriteLine($"Already have {currentCount} employees (enough for testing)");
            conn.Close();
            return;
        }

        cmd.CommandText = $"DELETE FROM employees WHERE id > {EXISTING_EMPLOYEES};";
        int deleted = cmd.ExecuteNonQuery();
        if (deleted > 0)
            Console.WriteLine($"Cleaned up {deleted} previous test records");

        Console.WriteLine("Inserting 10.000 new records...");
        cmd.CommandText = @"
            INSERT INTO employees (first_name, last_name, email, department_id, salary)
            SELECT 
                'Employee' || i AS first_name,
                'Test' || i AS last_name,
                'employee' || i || '@test.example.com' AS email,
                ((i - 1) % 6) + 1 AS department_id,
                (40000 + (random() * 60000)::INT)::DECIMAL(10, 2) AS salary
            FROM generate_series(1, 10000) AS t(i);";
        
        cmd.CommandTimeout = 60;
        cmd.ExecuteNonQuery();

        cmd.CommandText = "SELECT COUNT(*) FROM employees;";
        long newCount = (long) cmd.ExecuteScalar();
        Console.WriteLine($"Generated test data - total employees: {newCount}");

        conn.Close();
    }
    
    private void RemoveAllIndexes()
    {
        using var conn = new NpgsqlConnection(_connectionString);
        conn.Open();
        using var cmd = conn.CreateCommand();

        cmd.CommandText = @"
            SELECT indexname FROM pg_indexes 
            WHERE tablename = 'employees' 
            AND indexname NOT LIKE 'pg_toast%';";

        var indexNames = new System.Collections.Generic.List<string>();
        using var reader = cmd.ExecuteReader();
        while (reader.Read())
        {
            indexNames.Add(reader.GetString(0));
        }
        reader.Close();

        foreach (var indexName in indexNames)
        {
            try
            {
                cmd.CommandText = $"DROP INDEX IF EXISTS {indexName};";
                cmd.ExecuteNonQuery();
                Console.WriteLine($"Dropped index: {indexName}");
            }
            catch { }
        }

        if (indexNames.Count == 0)
            Console.WriteLine("(No custom indexes found)");

        conn.Close();
    }
    
    private void CreateIndexes()
    {
        using var conn = new NpgsqlConnection(_connectionString);
        conn.Open();
        using var cmd = conn.CreateCommand();

        var indexes = new[]
        {
            ("idx_employees_email", "CREATE INDEX idx_employees_email ON employees(email);"),
            ("idx_employees_department_id", "CREATE INDEX idx_employees_department_id ON employees(department_id);"),
            ("idx_employees_salary", "CREATE INDEX idx_employees_salary ON employees(salary);"),
            ("idx_employees_dept_salary", "CREATE INDEX idx_employees_dept_salary ON employees(department_id, salary);")
        };

        foreach (var (name, statement) in indexes)
        {
            cmd.CommandText = statement;
            cmd.ExecuteNonQuery();
            Console.WriteLine($"Created index: {name}");
        }

        conn.Close();
    }
    
    private TestResult[] TestQueries()
    {
        var queries = new[]
        {
            new QueryDef("Query 1: Search by Email", 
                "SELECT * FROM employees WHERE email = 'employee5000@test.example.com';"),
            new QueryDef("Query 2: Search by Department", 
                "SELECT * FROM employees WHERE department_id = 3;"),
            new QueryDef("Query 3: Salary Range", 
                "SELECT * FROM employees WHERE salary BETWEEN 50000 AND 80000;"),
            new QueryDef("Query 4: Multi-column (Department + Salary)", 
                "SELECT * FROM employees WHERE department_id = 3 AND salary > 60000;")
        };

        var results = new System.Collections.Generic.List<TestResult>();

        foreach (var query in queries)
        {
            var result = MeasureQuery(query);
            results.Add(result);
            Console.WriteLine($"{result.Description}");
            Console.WriteLine($"Avg: {result.AverageMs:F3}ms | Min: {result.MinMs}ms | Max: {result.MaxMs}ms | Total: {result.TotalMs}ms");
        }

        return results.ToArray();
    }
    
    private TestResult MeasureQuery(QueryDef query)
    {
        var times = new long[ITERATIONS];

        using var conn = new NpgsqlConnection(_connectionString);
        conn.Open();

        for (int i = 0; i < ITERATIONS; i++)
        {
            var sw = Stopwatch.StartNew();

            using var cmd = conn.CreateCommand();
            cmd.CommandText = query.Sql;
            using var reader = cmd.ExecuteReader();
            int rowCount = 0;
            while (reader.Read())
                rowCount++;

            sw.Stop();
            times[i] = sw.ElapsedMilliseconds;
        }

        conn.Close();

        return new TestResult
        {
            Description = query.Description,
            AverageMs = times.Average(),
            MinMs = times.Min(),
            MaxMs = times.Max(),
            TotalMs = times.Sum(),
            RowCount = times.Length
        };
    }
    
    private void CompareResults(TestResult[] withoutIndexes, TestResult[] withIndexes)
    {
        Console.WriteLine("Performance Improvement:");
        Console.WriteLine();

        for (int i = 0; i < withoutIndexes.Length; i++)
        {
            var without = withoutIndexes[i];
            var with = withIndexes[i];
            double improvement = ((without.AverageMs - with.AverageMs) / without.AverageMs) * 100;
            double speedup = without.AverageMs / with.AverageMs;

            Console.WriteLine($"{with.Description}");
            Console.WriteLine($"Without indexes: {without.AverageMs:F3}ms avg");
            Console.WriteLine($"With indexes:    {with.AverageMs:F3}ms avg");
            Console.WriteLine($"Improvement:     {improvement:F1}% ({speedup:F2}x faster)");
            Console.WriteLine();
        }
    }
    
    private void DisplayQueryPlans()
    {
        var queries = new[]
        {
            ("Query 1: Search by Email",
                "EXPLAIN ANALYZE SELECT * FROM employees WHERE email = 'employee5000@test.example.com';"),
            ("Query 2: Search by Department",
                "EXPLAIN ANALYZE SELECT * FROM employees WHERE department_id = 3;"),
            ("Query 3: Salary Range",
                "EXPLAIN ANALYZE SELECT * FROM employees WHERE salary BETWEEN 50000 AND 80000;"),
            ("Query 4: Multi-column",
                "EXPLAIN ANALYZE SELECT * FROM employees WHERE department_id = 3 AND salary > 60000;")
        };

        using var conn = new NpgsqlConnection(_connectionString);
        conn.Open();

        foreach (var (description, query) in queries)
        {
            Console.WriteLine($"\n{description}");

            using var cmd = conn.CreateCommand();
            cmd.CommandText = query;
            cmd.CommandTimeout = 30;

            using var reader = cmd.ExecuteReader();
            while (reader.Read())
            {
                string line = reader.GetString(0);
                if (line.Contains("Index Scan") || line.Contains("Index Cond"))
                    Console.ForegroundColor = ConsoleColor.Green;
                else if (line.Contains("Seq Scan"))
                    Console.ForegroundColor = ConsoleColor.Yellow;

                Console.WriteLine(line);
                Console.ResetColor();
            }
        }

        conn.Close();
    }
    
    private void CleanupTestData()
    {
        using var conn = new NpgsqlConnection(_connectionString);
        conn.Open();
        using var cmd = conn.CreateCommand();
        
        cmd.CommandText = $"DELETE FROM employees WHERE id > {EXISTING_EMPLOYEES};";
        int deletedRows = cmd.ExecuteNonQuery();
        Console.WriteLine($"Deleted {deletedRows} test records");

        RemoveAllIndexes();

        conn.Close();
    }

    private record QueryDef(string Description, string Sql);

    private class TestResult
    {
        public string Description { get; set; }
        public double AverageMs { get; set; }
        public long MinMs { get; set; }
        public long MaxMs { get; set; }
        public long TotalMs { get; set; }
        public int RowCount { get; set; }
    }
}