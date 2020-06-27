using System;
using System.Collections.Generic;
using System.Text;

namespace SAV.Entity
{
    public class YedigiBesin
    {
        public int UsersId { get; set; }
        public int BesinlerId { get; set; }
        public int Porsiyon { get; set; }
        public Besinler Besin { get; set; }
        public DateTime Date { get; set; }
        public bool ozel { get; set; }
    }
}
