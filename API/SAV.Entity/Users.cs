using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Text;

namespace SAV.Entity
{
    public class Users
    {
        public int UsersId { get; set; }
        public string Name { get; set; }
        public string Email { get; set; }
        public string Password { get; set; }
        public string Salt { get; set; }
        public int Age { get; set; }
        public double Weight { get; set; }
        public double Length { get; set; }
        public bool Cinsiyet { get; set; }
        public bool Admin { get; set; }
        public double LimitKalori { get; set; }
        public double AltLimitKarbon { get; set; }
        public double AltLimitYag { get; set; }
        public double AltLimitProtein { get; set; }
        public double UstLimitKarbon { get; set; }
        public double UstLimitYag { get; set; }
        public double UstLimitProtein { get; set; }
        public double SuLimit { get; set; }
        public List<YedigiBesin> YedigiBesin { get; set; }


    }
}
