using SAV.DataAccess.Interfaces;
using SAV.Entity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SAV.DataAccess.EFCoreManager
{
    public class EFCoreYedigiBesinManager : IYedigiBesinService
    {
        public void create(YedigiBesin entity)
        {
            using (var context = new DatabaseContext())
            {
                context.Set<YedigiBesin>().Add(entity);
                context.SaveChanges();
            }
        }

        public void Delete(YedigiBesin entity)
        {
            using (var context = new DatabaseContext())
            {
                context.Set<YedigiBesin>().Remove(entity);
                context.SaveChanges();
            }
        }

        public List<YedigiBesin> GetAll()
        {
            using (var context = new DatabaseContext())
            {
                return context.Set<YedigiBesin>().ToList();
            }
        }

        public List<YedigiBesin> GetById(int UserId)
        {
            using (var context = new DatabaseContext())
            {
                return context.Set<YedigiBesin>().Where(i => i.UsersId == UserId).ToList();
            }
        }

        public void Update(YedigiBesin entity)
        {
            using (var context = new DatabaseContext())
            {
                context.Set<YedigiBesin>().Update(entity);
                context.SaveChanges();
            }
        }
    }
}
