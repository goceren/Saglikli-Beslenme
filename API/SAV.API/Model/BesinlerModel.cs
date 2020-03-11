using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace SAV.API.Model
{
    public class BesinlerModel
    {
        public int BesinlerId { get; set; }
        public string besinAdi { get; set; }
        public double besinProtein { get; set; }
        public double besinKarbonhidrat { get; set; }
        public double besinYag { get; set; }
        public double besinKalori { get; set; }
        public DateTime Date { get; set; }
        public int Porsiyon { get; set; }
    }
}
