using SAV.Entity;
using System;
using System.Collections.Generic;
using System.Text;

namespace SAV.DataAccess.Interfaces
{
    public interface IUserServices
    {
        Users GetById(int id);
        List<Users> GetAll();
        void Create(Users entity);
        void Update(Users entity);
        void Delete(Users entity);
        void ChangePassword(Users entity);
        Users GetByIdWithYedigiBesinler(int id);

    }
}
