using SAV.Entity;
using System;
using System.Collections.Generic;
using System.Text;

namespace SAV.DataAccess.Interfaces
{
    public interface IBesinlerService
    {
        void Create(Besinler entity);
        void Update(Besinler entity);
        void Delete(Besinler entity);

        List<Besinler> GetAllFalse();
        List<Besinler> GetAll();
        List<Besinler> GetByUserId(int id);

        Besinler GetById(int id);

    }
}
