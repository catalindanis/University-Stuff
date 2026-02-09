using System;
using System.Collections.Generic;
using System.Linq;
using Sem12.Model;
using Sem12.Model.Validator;
using Sem12.repository;
using Sem12.Repository;
using static System.Net.Mime.MediaTypeNames;

namespace Sem12;

class Program
{
    static void Main(string[] args)
    {
        var angajatiRepo = AngajatiFileIO();
        var sarciniRepo = SarciniFileIO();
        var pontajeRepo = PontajeFileIO(angajatiRepo, sarciniRepo, true);

        var s9 = sarciniRepo.FindOne("s9");
        s9.TipDificultate = Dificultate.Grea;
        sarciniRepo.Update(s9);

        var a6 = angajatiRepo.FindOne("a6");
        a6.Nume = "Xulescu";
        angajatiRepo.Update(a6);

        var a6s7 = pontajeRepo.FindOne("a6s7");
        a6s7.Date = DateTime.Today.AddYears(10);
        pontajeRepo.Update(a6s7);
    }

    private static IRepository<string, Sarcina> SarciniFileIO(bool test = false)
    {
        string fileName2 = "..\\..\\..\\data\\sarcini.txt";
        IValidator<Sarcina> validator = new SarcinaValidator();

        IRepository<string, Sarcina> sarciniRepo = new SarciniInFileRepo(fileName2, validator);

        List<Sarcina> entities = [.. sarciniRepo.FindAll()];
        if (test) entities.ForEach(Console.WriteLine);

        return sarciniRepo;
    }

    private static IRepository<string, Angajat> AngajatiFileIO(bool test = false)
    {
        string fileName2 = "..\\..\\..\\data\\angajati.txt";
        IValidator<Angajat> validator = new AngajatValidator();

        IRepository<string, Angajat> angajatRepo = new AngajatInFileRepo(fileName2, validator);

        List<Angajat> entities = [.. angajatRepo.FindAll()];
        if (test) entities.ForEach(Console.WriteLine);

        return angajatRepo;
    }

    private static IRepository<string, Pontaj> PontajeFileIO(
        IRepository<string, Angajat> angajatRepo,
        IRepository<string, Sarcina> sarciniRepo,
        bool test = false)
    {
        string fileName2 = "..\\..\\..\\data\\pontaje.txt";
        IValidator<Pontaj> validator = new PontajValidator();

        IRepository<string, Pontaj> pontajRepo = new PontajInFileRepo(fileName2, validator, angajatRepo, sarciniRepo);

        List<Pontaj> entities = [.. pontajRepo.FindAll()];
        if (test) entities.ForEach(Console.WriteLine);

        return pontajRepo;
    }
}