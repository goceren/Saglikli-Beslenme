﻿// <auto-generated />
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Infrastructure;
using Microsoft.EntityFrameworkCore.Metadata;
using Microsoft.EntityFrameworkCore.Migrations;
using Microsoft.EntityFrameworkCore.Storage.ValueConversion;
using SAV.DataAccess.EFCoreManager;

namespace SAV.DataAccess.Migrations
{
    [DbContext(typeof(DatabaseContext))]
    [Migration("20200309230610_adminvecinsiyet")]
    partial class adminvecinsiyet
    {
        protected override void BuildTargetModel(ModelBuilder modelBuilder)
        {
#pragma warning disable 612, 618
            modelBuilder
                .HasAnnotation("ProductVersion", "3.1.1")
                .HasAnnotation("Relational:MaxIdentifierLength", 128)
                .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

            modelBuilder.Entity("SAV.Entity.Besinler", b =>
                {
                    b.Property<int>("BesinlerId")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int")
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<string>("besinAdi")
                        .HasColumnType("nvarchar(max)");

                    b.Property<double>("besinKalori")
                        .HasColumnType("float");

                    b.Property<double>("besinKarbonhidrat")
                        .HasColumnType("float");

                    b.Property<double>("besinProtein")
                        .HasColumnType("float");

                    b.Property<double>("besinYag")
                        .HasColumnType("float");

                    b.HasKey("BesinlerId");

                    b.ToTable("Besinler");
                });

            modelBuilder.Entity("SAV.Entity.Users", b =>
                {
                    b.Property<int>("UsersId")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int")
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<bool>("Admin")
                        .HasColumnType("bit");

                    b.Property<int>("Age")
                        .HasColumnType("int");

                    b.Property<bool>("Cinsiyet")
                        .HasColumnType("bit");

                    b.Property<string>("Email")
                        .HasColumnType("nvarchar(max)");

                    b.Property<double>("Length")
                        .HasColumnType("float");

                    b.Property<string>("Name")
                        .HasColumnType("nvarchar(max)");

                    b.Property<string>("Password")
                        .HasColumnType("nvarchar(max)");

                    b.Property<string>("Salt")
                        .HasColumnType("nvarchar(max)");

                    b.Property<double>("Weight")
                        .HasColumnType("float");

                    b.HasKey("UsersId");

                    b.ToTable("Users");
                });
#pragma warning restore 612, 618
        }
    }
}