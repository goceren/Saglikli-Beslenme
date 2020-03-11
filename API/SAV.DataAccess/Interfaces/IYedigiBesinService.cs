using SAV.Entity;
using System;
using System.Collections.Generic;
using System.Text;

namespace SAV.DataAccess.Interfaces
{
    public interface IYedigiBesinService
    {
        void create(YedigiBesin entity);
        void Update(YedigiBesin entity);
        void Delete(YedigiBesin entity);
        List<YedigiBesin> GetById(int UserId);
        List<YedigiBesin> GetAll();
    }
}
