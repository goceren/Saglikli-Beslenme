using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using SAV.DataAccess.Interfaces;
using SAV.Entity;

namespace SAV.API.Controllers
{
    [ApiController]
    [Route("[controller]")]
    public class BesinlerController : ControllerBase
    {

        private readonly IBesinlerService _besinlerService;
        public BesinlerController(IBesinlerService besinlerService)
        {
            _besinlerService = besinlerService;
        }

        [HttpGet, Route("/api/besinler/all")]
        public ActionResult GetBesinler()
        {
            var besinler = _besinlerService.GetAll();
            return Ok(besinler);
        }

        [HttpGet("{id}"), Route("/api/besinler/{id}")]
        public ActionResult GetBesinler(int id)
        {
            return Ok(_besinlerService.GetById(id));
        }

        [HttpPost(), Route("/api/besinler/create")]
        public ActionResult CreateBesin(Besinler besinler)
        {
            if (ModelState.IsValid)
            {
                if (besinler.besinKalori == 0)
                {
                    besinler.besinKalori = (besinler.besinKarbonhidrat * 4) + (besinler.besinProtein * 4) + (besinler.besinYag * 9);
                }
                
                _besinlerService.Create(besinler);
                return Ok(besinler);
            }
            else
            {
                return Ok("Hatali");
            }
        }

        [HttpPost(), Route("/api/besinler/update")]
        public ActionResult UpdateBesin(Besinler besinler)
        {
            if (ModelState.IsValid)
            {
                if (besinler.besinKalori == 0)
                {
                    besinler.besinKalori = (besinler.besinKarbonhidrat * 4) + (besinler.besinProtein * 4) + (besinler.besinYag * 9);
                }
                _besinlerService.Update(besinler);
                return Ok(besinler);
            }
            else
            {
                return Ok("Hatali");
            }
        }

        [HttpGet(), Route("/api/besinler/delete/{bid}")]
        public ActionResult DeleteKullaniciBesinler(int bid)
        {
            Besinler besinler = _besinlerService.GetById(bid);
            if (besinler != null)
            {
                _besinlerService.Delete(besinler);
                return Ok("basarili");
            }
            else
            {
                return Ok("Hatali");
            }
        }

        [HttpGet("{kid}"), Route("/api/butunBesinler/{kid}")]
        public ActionResult GetBesinlerWithUser(int kid)
        {
            var usersBesinler = _besinlerService.GetByUserId(kid);
            var falseBesin = _besinlerService.GetAllFalse();
            List<Besinler> besinler = new List<Besinler>();

            foreach (var item in falseBesin)
            {
                if (item.KullaniciId != kid)
                {
                    besinler.Add(item);
                }
            }
            besinler.AddRange(usersBesinler);
            return Ok(besinler);
        }

        [HttpGet("{bid}"), Route("/api/butunBesinler/besin/{bid}")]
        public ActionResult GetBesinlerWithUserById(int bid)
        {
            var usersBesinler = _besinlerService.GetById(bid);
            if (usersBesinler != null)
            {
                return Ok(usersBesinler);
            }
            else
            {
                return Ok("hata");
            }
        }

    }
}
