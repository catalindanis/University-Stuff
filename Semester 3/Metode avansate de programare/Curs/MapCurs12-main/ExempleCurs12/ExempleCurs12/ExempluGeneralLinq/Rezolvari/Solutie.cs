using System;
using System.Collections.Generic;
using System.Linq;
using ExempluGeneralLINQ.Data;
using ExempluGeneralLINQ.Domain;



/*Este Ajunul Craciunului.
 Sistemul informatic al Polului Nord a crapat
din cauza suprasolicitarii.Avem o gramada de 
date brute (liste cu jucarii aruncate prin depozit 
si saci cu scrisori de la copii), dar totul e amestecat. 

Obiectivul Final: Trebuie sa generam 
"Manifestul de incarcare al saniei" – o lista curata, 
validata, verificata cu stocul si organizata pe orase, 
ca Mosului sa poata pleca.*/


class Solutie
{
    static void main(string[] args)
    {
        var scrisori = MockData.GetScrisori();
        var inventar = MockData.GetInventar();

        Console.WriteLine("=== GENERARE MANIFEST COMPLEX (SANIA 2.0) ===");

        
        var raportSanie = GenerareManifestComplex(scrisori, inventar);

       
        foreach (var sac in raportSanie)
        {
            Console.WriteLine($"\n📦 SACUL DE {sac.OrasDestinatie.ToUpper()}");
            Console.WriteLine($"   - Nr. Cadouri: {sac.NumarCadouri}");
            Console.WriteLine($"   - Greutate: {sac.GreutateTotala:F1} KG");
            Console.WriteLine($"   - Cost Productie: {sac.CostTotal} RON");
        }
    }

    public static List<SacDeCraciun> GenerareManifestComplex(
        List<Scrisoare> scrisori, 
        List<JucarieInventar> inventar)
    {

        // VALIDARE (Where)
        // Eliminam copiii obraznici
        
        

        var copiiCuminti = scrisori.Where(s => s.EsteCuminte);


        // APLATIZARE (SelectMany)
        // Problema: Un copil are o LISTA de dorinte.
        // Solutia: Transformam "1 Copil cu 3 dorinte" in "3 Cereri individuale"

        var cereriIndividuale = copiiCuminti
            .SelectMany(copil => copil.Dorinte, (copil, jucarie) => new 
            { 
                Cine = copil.NumeCopil, 
                Oras = copil.Oras, 
                CeVrea = jucarie 
            });


        // CORELARE CU INVENTAR (Join)
        // Aducem detaliile despre greutate si pret

        var cereriCuDetalii = cereriIndividuale
            .Join(inventar,
                  cerere => cerere.CeVrea,
                  stoc => stoc.Nume,
                  (cerere, stoc) => new 
                  {
                      cerere.Oras,
                      stoc.GreutateKG,
                      stoc.PretProductie
                  });

   
        // CREARE SACI (GroupBy + Select Aggregates)
        // Grupam pe orase si calculam totalurile intr-un obiect nou 'SacDeCraciun'

        var listaSaci = cereriCuDetalii
            .GroupBy(x => x.Oras)
            .Select(grup => new SacDeCraciun
            {
                OrasDestinatie = grup.Key,
                NumarCadouri = grup.Count(),
                GreutateTotala = grup.Sum(x => x.GreutateKG),
                CostTotal = grup.Sum(x => x.PretProductie)
            })
            .OrderByDescending(s => s.GreutateTotala) 
            .ToList();

        return listaSaci;
    }
}