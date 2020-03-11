using System;
using System.Collections.Generic;
using System.Text;

namespace SAV.Entity
{
    public class Besinler
    {
        public int BesinlerId { get; set; }
        public string besinAdi { get; set; }
        public double besinProtein { get; set; }
        public double besinKarbonhidrat { get; set; }
        public double besinYag { get; set; }
        public double besinKalori { get; set; }
    }
}
