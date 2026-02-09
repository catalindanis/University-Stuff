using ExempluGeneralLINQ.Data;
using ExempluGeneralLINQ.Domain;

namespace ExempleCurs12.ExempluGeneralLinq;

/*Este Ajunul Craciunului.
 Sistemul informatic al Polului Nord a crapat
din cauza suprasolicitarii.Avem o gramada de
date brute (liste cu jucarii aruncate prin depozit
si saci cu scrisori de la copii), dar totul e amestecat.

Obiectivul Final: Trebuie sa generam
"Manifestul de incarcare al saniei" – o lista curata,
validata, verificata cu stocul si organizata pe orase,
ca Mosului sa poata pleca.*/

public class ExercitiuGeneral
{
    public static void Run()
    {
        var scrisori = MockData.GetScrisori();
        var inventar = MockData.GetInventar();

        Console.WriteLine("=== GENERARE MANIFEST COMPLEX (SANIA 2.0) ===");


        var raportSanie = GenerareManifestComplex(scrisori, inventar);


        if (!raportSanie.Any())
        {
            Console.WriteLine("⚠️  Manifestul este gol! Trebuie sa implementam logica LINQ.");
        }
        else
        {
            foreach (var sac in raportSanie)
            {
                Console.WriteLine($"\n📦 SACUL DE {sac.OrasDestinatie.ToUpper()}");
                Console.WriteLine($"   - Nr. Cadouri: {sac.NumarCadouri}");
                Console.WriteLine($"   - Greutate:    {sac.GreutateTotala:F1} KG");
                Console.WriteLine($"   - Cost:        {sac.CostTotal} RON");
            }
        }
    }

    public static List<SacDeCraciun> GenerareManifestComplex(
        List<Scrisoare> scrisori,
        List<JucarieInventar> inventar)
    {
        // TODO 1: VALIDARE (Where)
        // Eliminam copiii obraznici

        //var copiiCuminti = 


        // TODO 2: APLATIZARE (SelectMany)
        // Problema: Un copil are o LISTA de dorinte.
        // Solutia: Transformam "1 Copil cu 3 dorinte" in "3 Cereri individuale"

        //var cereriIndividuale = 


        // TODO 3: CORELARE CU INVENTAR (Join)
        // Aducem detaliile despre greutate si pret

        //var cereriCuDetalii = 

        // TODO 4: CREARE SACI (GroupBy + Select Aggregates)
        // Grupam pe orase si calculam totalurile intr-un obiect nou 'SacDeCraciun'

        //var listaSaci = 


        //return listaSaci;
        return new List<SacDeCraciun>();
    }
}