using Sem12.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Sem12.Model
{
    public enum Dificultate { Usoara, Medie, Grea }
    public class Sarcina : Entity<string>
    {
        public Dificultate TipDificultate { get; set; }
        public int NrOreEstimate { get; set; }
        public override string ToString()
        {
            return ID + " " + TipDificultate + " " + NrOreEstimate;
        }
    }

}
