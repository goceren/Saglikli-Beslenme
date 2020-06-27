using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using SAV.API.Model;
using SAV.API.Utils;
using SAV.DataAccess.Interfaces;
using SAV.Entity;

namespace SAV.API.Controllers
{
    [ApiController]
    [Route("[controller]")]
    public class UserController : Controller
    {
        private readonly IUserServices _userService;
        private readonly IYedigiBesinService _yedigi;
        private readonly IBesinlerService _besinlerService;
        private readonly ISuService _suService;
        public UserController(IUserServices userService, IYedigiBesinService yedigi, IBesinlerService besinlerService, ISuService suService)
        {
            _userService = userService;
            _yedigi = yedigi;
            _besinlerService = besinlerService;
            _suService = suService;
        }

        // REGISTER
        [HttpPost, Route("/api/user/register")]
        public ActionResult CreateUser(Users entity)
        {
            var users = _userService.GetAll();
            if (!users.Any(i => i.Email.Equals(entity.Email)))
            {
                Users user = new Users();
                user.Name = entity.Name;
                user.Salt = Convert.ToBase64String(Common.GetRandomSalt(16));
                user.Password = Convert.ToBase64String(Common.SaltHashPassword(
                    Encoding.ASCII.GetBytes(entity.Password),
                    Convert.FromBase64String(user.Salt)
                ));
                user.Email = entity.Email;
                try
                {
                    _userService.Create(user);
                    var created = _userService.GetAll().Where(i => i.Email == user.Email).FirstOrDefault();
                    Users createdUser = new Users()
                    {
                        Name = created.Name,
                        Email = created.Email,
                        UsersId = created.UsersId
                    };
                    return Ok("Kayit Basarili");
                }
                catch (Exception)
                {
                    return Ok("Kayit Basarisiz");
                }
            }
            else
            {
                return Ok("Bu email zaten kayıtlı");
            }
        }


        // LOGIN
        [HttpPost, Route("/api/user/login")]
        public ActionResult LoginUser(Users entity)
        {
            var users = _userService.GetAll();
            if (users.Any(i => i.Email.Equals(entity.Email)))
            {
                Users user = users.Where(i => i.Email.Equals(entity.Email)).FirstOrDefault();

                var client_post_hash_password = Convert.ToBase64String(
                    Common.SaltHashPassword(
                        Encoding.ASCII.GetBytes(entity.Password),
                        Convert.FromBase64String(user.Salt)));

                if (client_post_hash_password.Equals(user.Password))
                {
                    Users loginUser = new Users()
                    {
                        Email = user.Email,
                        Name = user.Name,
                        UsersId = user.UsersId,
                        Age = user.Age,
                        Length = user.Length,
                        Weight = user.Weight,
                        Admin = user.Admin,
                        Cinsiyet = user.Cinsiyet,
                        LimitKalori = user.LimitKalori,
                        AltLimitKarbon = user.AltLimitKarbon,
                        UstLimitKarbon = user.UstLimitKarbon,
                        AltLimitProtein = user.AltLimitProtein,
                        UstLimitProtein = user.UstLimitProtein,
                        AltLimitYag = user.AltLimitYag,
                        UstLimitYag = user.UstLimitYag,
                        SuLimit = user.SuLimit,

                    };
                    return Ok(loginUser);
                }
                else
                {
                    return Ok("Email yada şifre hatalı");
                }
            }
            else
            {
                return Ok("Bu email adresi kayitli degil");
            }
        }


        // UPDATE BOY KİLO
        [HttpPost, Route("/api/user/updateDetails")]
        public ActionResult UpdateDetails(Users entity)
        {
            var users = _userService.GetAll();
            if (users.Any(i => i.Email.Equals(entity.Email)))
            {
                var selectedUser = users.Where(i => i.UsersId == entity.UsersId).FirstOrDefault();
                entity.Salt = selectedUser.Salt;
                entity.Password = selectedUser.Password;
                if (entity.Cinsiyet == true)
                {
                    entity.LimitKalori = (66.5 + (13.75 * entity.Weight) + (5.03 * entity.Length) - (6.75 * entity.Age));
                    if (entity.Age >= 2 && entity.Age <= 10)
                    {
                        entity.AltLimitKarbon = 9 * (entity.LimitKalori / 20);
                        entity.UstLimitKarbon = 3 * (entity.LimitKalori / 5);
                        entity.AltLimitProtein = entity.LimitKalori / 20;
                        entity.UstLimitProtein = entity.LimitKalori / 5;
                        entity.AltLimitYag = entity.LimitKalori / 5;
                        entity.UstLimitYag = 7 * (entity.LimitKalori / 20);
                    }

                    if (entity.Age >= 11 && entity.Age <= 14)
                    {
                        entity.AltLimitKarbon = 9 * (entity.LimitKalori / 20);
                        entity.UstLimitKarbon = 3 * (entity.LimitKalori / 5);
                        entity.AltLimitProtein = 2 * (entity.LimitKalori / 25);
                        entity.UstLimitProtein = entity.LimitKalori / 5;
                        entity.AltLimitYag = entity.LimitKalori / 5;
                        entity.UstLimitYag = 7 * (entity.LimitKalori / 20);
                    }

                    if (entity.Age >= 15 && entity.Age <= 17)
                    {
                        entity.AltLimitKarbon = 9 * (entity.LimitKalori / 20);
                        entity.UstLimitKarbon = 3 * (entity.LimitKalori / 5);
                        entity.AltLimitProtein = 9 * (entity.LimitKalori / 100);
                        entity.UstLimitProtein = entity.LimitKalori / 5;
                        entity.AltLimitYag = entity.LimitKalori / 5;
                        entity.UstLimitYag = 7 * (entity.LimitKalori / 20);
                    }

                    if (entity.Age >= 18 && entity.Age <= 64)
                    {
                        entity.AltLimitKarbon = 9 * (entity.LimitKalori / 20);
                        entity.UstLimitKarbon = 3 * (entity.LimitKalori / 5);
                        entity.AltLimitProtein = entity.LimitKalori / 10;
                        entity.UstLimitProtein = entity.LimitKalori / 5;
                        entity.AltLimitYag = entity.LimitKalori / 5;
                        entity.UstLimitYag = 7 * (entity.LimitKalori / 20);
                    }

                    if (entity.Age >= 65)
                    {
                        entity.AltLimitKarbon = 9 * (entity.LimitKalori / 20);
                        entity.UstLimitKarbon = 3 * (entity.LimitKalori / 5);
                        entity.AltLimitProtein = 3 * (entity.LimitKalori / 25);
                        entity.UstLimitProtein = entity.LimitKalori / 5;
                        entity.AltLimitYag = entity.LimitKalori / 5;
                        entity.UstLimitYag = 7 * (entity.LimitKalori / 20);
                    }
                }
                else
                {
                    entity.LimitKalori = (655.1 + (9.56 * entity.Weight) + (1.85 * entity.Length) - (4.68 * entity.Age));
                    if (entity.Age >= 2 && entity.Age <= 6)
                    {
                        entity.AltLimitKarbon = 9 * (entity.LimitKalori / 20);
                        entity.UstLimitKarbon = 3 * (entity.LimitKalori / 5);
                        entity.AltLimitProtein = entity.LimitKalori / 20;
                        entity.UstLimitProtein = entity.LimitKalori / 5;
                        entity.AltLimitYag = entity.LimitKalori / 5;
                        entity.UstLimitYag = 7 * (entity.LimitKalori / 20);
                    }
                    if (entity.Age >= 7 && entity.Age <= 10)
                    {
                        entity.AltLimitKarbon = 9 * (entity.LimitKalori / 20);
                        entity.UstLimitKarbon = 3 * (entity.LimitKalori / 5);
                        entity.AltLimitProtein = 7 * (entity.LimitKalori / 100);
                        entity.UstLimitProtein = entity.LimitKalori / 5;
                        entity.AltLimitYag = entity.LimitKalori / 5;
                        entity.UstLimitYag = 7 * (entity.LimitKalori / 20);
                    }
                    if (entity.Age >= 11 && entity.Age <= 14)
                    {
                        entity.AltLimitKarbon = 9 * (entity.LimitKalori / 20);
                        entity.UstLimitKarbon = 3 * (entity.LimitKalori / 5);
                        entity.AltLimitProtein = 9 * (entity.LimitKalori / 100);
                        entity.UstLimitProtein = entity.LimitKalori / 5;
                        entity.AltLimitYag = entity.LimitKalori / 5;
                        entity.UstLimitYag = 7 * (entity.LimitKalori / 20);
                    }
                    if (entity.Age >= 15 && entity.Age <= 17)
                    {
                        entity.AltLimitKarbon = 9 * (entity.LimitKalori / 20);
                        entity.UstLimitKarbon = 3 * (entity.LimitKalori / 5);
                        entity.AltLimitProtein = entity.LimitKalori / 10;
                        entity.UstLimitProtein = entity.LimitKalori / 5;
                        entity.AltLimitYag = entity.LimitKalori / 5;
                        entity.UstLimitYag = 7 * (entity.LimitKalori / 20);
                    }
                    if (entity.Age >= 18 && entity.Age <= 50)
                    {
                        entity.AltLimitKarbon = 9 * (entity.LimitKalori / 20);
                        entity.UstLimitKarbon = 3 * (entity.LimitKalori / 5);
                        entity.AltLimitProtein = 3 * (entity.LimitKalori / 25);
                        entity.UstLimitProtein = entity.LimitKalori / 5;
                        entity.AltLimitYag = entity.LimitKalori / 5;
                        entity.UstLimitYag = 7 * (entity.LimitKalori / 20);
                    }
                    if (entity.Age >= 51)
                    {
                        entity.AltLimitKarbon = 9 * (entity.LimitKalori / 20);
                        entity.UstLimitKarbon = 3 * (entity.LimitKalori / 5);
                        entity.AltLimitProtein = 7 * (entity.LimitKalori / 50);
                        entity.UstLimitProtein = entity.LimitKalori / 5;
                        entity.AltLimitYag = entity.LimitKalori / 5;
                        entity.UstLimitYag = 7 * (entity.LimitKalori / 20);
                    }
                }
                entity.SuLimit = entity.Weight * 0.033;
                _userService.Update(entity);
                Users returnUser = new Users()
                {
                    Email = entity.Email,
                    Name = entity.Name,
                    UsersId = entity.UsersId,
                    Age = entity.Age,
                    Length = entity.Length,
                    Weight = entity.Weight,
                    Admin = entity.Admin,
                    Cinsiyet = entity.Cinsiyet,
                    LimitKalori = entity.LimitKalori,
                    AltLimitKarbon = entity.AltLimitKarbon,
                    UstLimitKarbon = entity.UstLimitKarbon,
                    AltLimitProtein = entity.AltLimitProtein,
                    UstLimitProtein = entity.UstLimitProtein,
                    AltLimitYag = entity.AltLimitYag,
                    UstLimitYag = entity.UstLimitYag,
                    SuLimit = entity.SuLimit,

                };
                return Ok(returnUser);
            }
            else
            {
                return Ok("Bu email adresi kayitli degil");
            }
        }

        [HttpPost, Route("/api/yedigibesin/ekle")]
        public ActionResult GetUser(YedigiBesin model)
        {
            var user = _userService.GetById(model.UsersId);
            var besin = _besinlerService.GetById(model.BesinlerId);
            if (user != null && besin != null)
            {
                _yedigi.create(model);
                return Ok("Eklendi");
            }
            else
            {
                return Ok("Boyle bir kullanici yada besin yok");
            }
        }

        [HttpGet("id"), Route("/api/user/{id}")]
        public ActionResult GetUser(int id)
        {
            var user = _userService.GetById(id);
            if (user != null)
            {
                Users returnUser = new Users()
                {
                    Age = user.Age,
                    Email = user.Email,
                    Length = user.Length,
                    Name = user.Name,
                    Password = "",
                    Salt = "",
                    UsersId = user.UsersId,
                    Weight = user.Weight,
                    Cinsiyet = user.Cinsiyet,
                    Admin = user.Admin,
                    LimitKalori = user.LimitKalori,
                    AltLimitKarbon = user.AltLimitKarbon,
                    UstLimitKarbon = user.UstLimitKarbon,
                    AltLimitProtein = user.AltLimitProtein,
                    UstLimitProtein = user.UstLimitProtein,
                    AltLimitYag = user.AltLimitYag,
                    UstLimitYag = user.UstLimitYag,
                    SuLimit = user.SuLimit,
                };
                return Ok(returnUser);
            }
            else
            {
                return Ok("Boyle bir kullanici yok");
            }
        }



        // kullanıcının Bugün yediği
        [HttpGet("id"), Route("/api/user/getbesin/{id}")]
        public ActionResult GetUserWithBesin(int id)
        {
            try
            {
                var user = _userService.GetByIdWithYedigiBesinler(id);
                var besinler = user.YedigiBesin.Where(i => i.Date.Date == DateTime.Now.Date);
                List<BesinlerModel> besinlerModel = new List<BesinlerModel>();
                foreach (var item in besinler)
                {
                    BesinlerModel besin = new BesinlerModel()
                    {
                        besinAdi = item.Besin.besinAdi,
                        besinKalori = item.Besin.besinKalori,
                        besinKarbonhidrat = item.Besin.besinKarbonhidrat,
                        BesinlerId = item.Besin.BesinlerId,
                        besinProtein = item.Besin.besinProtein,
                        besinYag = item.Besin.besinYag,
                        Date = item.Date,
                        Porsiyon = item.Porsiyon
                    };
                    besinlerModel.Add(besin);
                }
                double toplamKalori = 0, toplamKarbon = 0, toplamProtein = 0, toplamYag = 0;
                foreach (var item in besinlerModel)
                {
                    toplamKalori += (item.besinKalori * item.Porsiyon);
                    toplamKarbon += (item.besinKarbonhidrat * item.Porsiyon);
                    toplamProtein += (item.besinProtein * item.Porsiyon);
                    toplamYag += (item.besinYag * item.Porsiyon);
                }

                if (user != null && besinler != null)
                {
                    UserBesinModel model = new UserBesinModel()
                    {
                        Age = user.Age,
                        Email = user.Email,
                        Admin = user.Admin,
                        Length = (int)user.Length,
                        Name = user.Name,
                        Password = "",
                        Salt = "",
                        UsersId = user.UsersId,
                        Weight = (int)user.Weight,
                        Cinsiyet = user.Cinsiyet,
                        LimitKalori = user.LimitKalori,
                        AltLimitKarbon = user.AltLimitKarbon,
                        UstLimitKarbon = user.UstLimitKarbon,
                        AltLimitProtein = user.AltLimitProtein,
                        UstLimitProtein = user.UstLimitProtein,
                        AltLimitYag = user.AltLimitYag,
                        UstLimitYag = user.UstLimitYag,
                        SuLimit = user.SuLimit,

                        BesinlerModel = besinlerModel,
                        ToplamKalori = toplamKalori,
                        ToplamKarbonhidrat = toplamKarbon,
                        ToplamProtein = toplamProtein,
                        ToplamYag = toplamYag,

                    };
                    return Ok(model);
                }
                else
                {
                    return Ok("Boyle bir kullanici yok");
                }
            }
            catch (Exception)
            {
                return Ok("Bos");
            }

        }

        // kullanıcının tarihe göre yediği
        [HttpPost, Route("/api/user/getbesin/date")]
        public ActionResult GetUserDate(GetUserByDate entity)
        {
            try
            {
                var user = _userService.GetByIdWithYedigiBesinler(entity.UsersId);
                var besinler = user.YedigiBesin.Where(i => i.Date.Date == entity.Date.Date);
                List<BesinlerModel> besinlerModel = new List<BesinlerModel>();
                foreach (var item in besinler)
                {
                    BesinlerModel besin = new BesinlerModel()
                    {
                        besinAdi = item.Besin.besinAdi,
                        besinKalori = item.Besin.besinKalori,
                        besinKarbonhidrat = item.Besin.besinKarbonhidrat,
                        BesinlerId = item.Besin.BesinlerId,
                        besinProtein = item.Besin.besinProtein,
                        besinYag = item.Besin.besinYag,
                        Date = item.Date,
                        Porsiyon = item.Porsiyon
                    };
                    besinlerModel.Add(besin);
                }
                double toplamKalori = 0, toplamKarbon = 0, toplamProtein = 0, toplamYag = 0;
                foreach (var item in besinlerModel)
                {
                    toplamKalori += (item.besinKalori * item.Porsiyon);
                    toplamKarbon += (item.besinKarbonhidrat * item.Porsiyon);
                    toplamProtein += (item.besinProtein * item.Porsiyon);
                    toplamYag += (item.besinYag * item.Porsiyon);
                }

                if (user != null && besinler != null)
                {
                    UserBesinModel model = new UserBesinModel()
                    {
                        Age = user.Age,
                        Email = user.Email,
                        Admin = user.Admin,
                        Length = (int)user.Length,
                        Name = user.Name,
                        Password = "",
                        Salt = "",
                        UsersId = user.UsersId,
                        Weight = (int)user.Weight,
                        Cinsiyet = user.Cinsiyet,
                        LimitKalori = user.LimitKalori,
                        AltLimitKarbon = user.AltLimitKarbon,
                        UstLimitKarbon = user.UstLimitKarbon,
                        AltLimitProtein = user.AltLimitProtein,
                        UstLimitProtein = user.UstLimitProtein,
                        AltLimitYag = user.AltLimitYag,
                        UstLimitYag = user.UstLimitYag,
                        SuLimit = user.SuLimit,

                        BesinlerModel = besinlerModel,
                        ToplamKalori = toplamKalori,
                        ToplamKarbonhidrat = toplamKarbon,
                        ToplamProtein = toplamProtein,
                        ToplamYag = toplamYag,
                    };
                    return Ok(model);
                }
                else
                {
                    return Ok("Boyle bir kullanici yok");
                }
            }
            catch (Exception)
            {

                return Ok("Bos");
            }

        }

        [HttpPost, Route("/api/su/ekle")]
        public ActionResult CreateSu(Su model)
        {
            var user = _userService.GetById(model.Kullanici);
            if (ModelState.IsValid)
            {
                if (user != null)
                {
                    _suService.Create(model);
                    return Ok("Eklendi");
                }
                else
                {
                    return Ok("Kullanici Bulunamadı");
                }
            }
            else
            {
                return Ok("Hata");
            }
        }

        [HttpPost, Route("/api/su/delete")]
        public ActionResult DeleteSu(Su model)
        {
            var user = _userService.GetById(model.Kullanici);
            if (ModelState.IsValid)
            {
                if (user != null)
                {
                    _suService.Update(model);
                    return Ok("Eklendi");
                }
                else
                {
                    return Ok("Kullanici Bulunamadı");
                }
            }
            else
            {
                return Ok("Hata");
            }
        }


        [HttpPost, Route("/api/user/su/date")]
        public ActionResult GetIcilenSu(GetIcilenSu entity)
        {

            var user = _userService.GetById(entity.kullanici);
            if (user != null)
            {
                var icilen = _suService.GetByUserId(entity.kullanici).Where(i => i.Date.Date == entity.Date.Date);
                return Ok(icilen);

            }
            else
            {
                return Ok("Kullanici Yok");
            }




        }
    }
}