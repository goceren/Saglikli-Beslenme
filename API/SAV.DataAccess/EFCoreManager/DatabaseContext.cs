using Microsoft.EntityFrameworkCore;
using SAV.Entity;
using System;
using System.Collections.Generic;
using System.Text;

namespace SAV.DataAccess.EFCoreManager
{
    public class DatabaseContext : DbContext
    {
        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {

            //optionsBuilder.UseSqlServer(@"data source = (localdb)\mssqllocaldb; Database=SAV; Trusted_Connection=true;");

        }
        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<YedigiBesin>()
                .HasKey(c => new { c.BesinlerId, c.UsersId, c.Date });
        }

        public DbSet<Besinler> Besinler { get; set; }
        public DbSet<Users> Users { get; set; }
    }
}
