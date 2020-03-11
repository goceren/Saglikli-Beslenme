using SAV.DataAccess.Interfaces;
using SAV.Entity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SAV.DataAccess.EFCoreManager
{
    public class EFCoreBesinlerManager : IBesinlerService
    {
        public void Create(Besinler entity)
        {
            using (var context = new DatabaseContext())
            {
                context.Besinler.Add(entity);
                context.SaveChanges();
            }    
        }

        public void Delete(Besinler entity)
        {
            using (var context = new DatabaseContext())
            {
                context.Besinler.Remove(entity);
                context.SaveChanges();
            }

        }

        public List<Besinler> GetAll()
        {
            using (var context = new DatabaseContext())
            {
                return context.Besinler.ToList();
            }
        }

        public Besinler GetById(int id)
        {
            using (var context = new DatabaseContext())
            {
                return context.Besinler.Where(i => i.BesinlerId == id).FirstOrDefault();
            }
        }

        public void Update(Besinler entity)
        {
            using (var context = new DatabaseContext())
            {
                context.Besinler.Update(entity);
                context.SaveChanges();
            }
        }
    }
}
