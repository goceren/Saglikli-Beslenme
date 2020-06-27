using SAV.Entity;
using System;
using System.Collections.Generic;
using System.Text;

namespace SAV.DataAccess.Interfaces
{
    public interface ISuService
    {
        void Create(Su entity);
        void Update(Su entity);
        void Delete(Su entity);

        List<Su> GetByUserId(int id);
    }
}
