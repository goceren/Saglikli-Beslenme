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
                _besinlerService.Create(besinler);
                return Ok(besinler);
            }
            else
            {
                return Ok("Hatali");
            }
        }
    }
}
