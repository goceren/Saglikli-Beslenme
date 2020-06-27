using SAV.DataAccess.Interfaces;
using SAV.Entity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SAV.DataAccess.EFCoreManager
{
    public class EFCoreSuManager : ISuService
    {
        public void Create(Su entity)
        {
            using (var context = new DatabaseContext())
            {
                context.Su.Add(entity);
                context.SaveChanges();
            }
        }

        public void Delete(Su entity)
        {
            using (var context = new DatabaseContext())
            {
                context.Su.Remove(entity);
                context.SaveChanges();
            }
        }

        public List<Su> GetByUserId(int id)
        {
            using (var context = new DatabaseContext())
            {
                return context.Su.Where(i => i.Kullanici == id).ToList();

            }
        }

        public void Update(Su entity)
        {
            using (var context = new DatabaseContext())
            {
                context.Su.Update(entity);
                context.SaveChanges();
            }
        }
    }
}
