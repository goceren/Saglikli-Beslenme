using System;
using Microsoft.EntityFrameworkCore.Migrations;

namespace SAV.DataAccess.Migrations
{
    public partial class su : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<double>(
                name: "SuLimit",
                table: "Users",
                nullable: false,
                defaultValue: 0.0);

            migrationBuilder.CreateTable(
                name: "Su",
                columns: table => new
                {
                    SuId = table.Column<int>(nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Kullanici = table.Column<int>(nullable: false),
                    bardakSayisi = table.Column<int>(nullable: false),
                    Date = table.Column<DateTime>(nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Su", x => x.SuId);
                });
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "Su");

            migrationBuilder.DropColumn(
                name: "SuLimit",
                table: "Users");
        }
    }
}
