using Microsoft.EntityFrameworkCore;
using SAV.DataAccess.Interfaces;
using SAV.Entity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SAV.DataAccess.EFCoreManager
{
    public class EFCoreUserManager : IUserServices
    {
        public void ChangePassword(Users entity)
        {
            throw new NotImplementedException();
        }

        public void Create(Users entity)
        {
            using (var context = new DatabaseContext())
            {
                context.Users.Add(entity);
                context.SaveChanges();
            }
        }

        public void Delete(Users entity)
        {
            using (var context = new DatabaseContext())
            {
                context.Users.Remove(entity);
                context.SaveChanges();
            }
        }

        public List<Users> GetAll()
        {
            using (var context = new DatabaseContext())
            {
                return context.Users.ToList();
            }
        }

        public Users GetById(int id)
        {
            using (var context = new DatabaseContext())
            {
                return context.Users.Where(i => i.UsersId == id).FirstOrDefault();
            }
        }

        public Users GetByIdWithYedigiBesinler(int id)
        {
            using (var context = new DatabaseContext())
            {
                var user = context.Users.Where(i => i.UsersId == id).Include(i => i.YedigiBesin).ThenInclude(i => i.Besin).FirstOrDefault();
                //var user = context.Users.Where(i => i.UsersId == id).Include(i => i.YedigiBesin).FirstOrDefault();
                return user;
            }
        }

        public void Update(Users entity)
        {
            using (var context = new DatabaseContext())
            {
                context.Update(entity);
                context.SaveChanges();
            }
        }
    }
}
