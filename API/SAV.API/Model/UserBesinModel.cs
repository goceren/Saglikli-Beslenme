using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace SAV.API.Model
{
    public class UserBesinModel
    {
        public int UsersId { get; set; }
        public string Name { get; set; }
        public string Email { get; set; }
        public string Password { get; set; }
        public string Salt { get; set; }
        public int Age { get; set; }
        public int Weight { get; set; }
        public int Length { get; set; }
        public bool Cinsiyet { get; set; }
        public bool Admin { get; set; }
        public double LimitKalori { get; set; }
        public double AltLimitKarbon { get; set; }
        public double UstLimitKarbon { get; set; }
        public double AltLimitProtein { get; set; }
        public double UstLimitProtein { get; set; }
        public double AltLimitYag { get; set; }
        public double UstLimitYag { get; set; }
        public double SuLimit { get; set; }
        public List<BesinlerModel> BesinlerModel { get; set; }

        public double ToplamKalori { get; set; }
        public double ToplamProtein { get; set; }
        public double ToplamKarbonhidrat { get; set; }
        public double ToplamYag { get; set; }
    }
}
