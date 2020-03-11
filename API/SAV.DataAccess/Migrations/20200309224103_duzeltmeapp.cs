using Microsoft.EntityFrameworkCore.Migrations;

namespace SAV.DataAccess.Migrations
{
    public partial class duzeltmeapp : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "Besinler",
                columns: table => new
                {
                    BesinlerId = table.Column<int>(nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    besinAdi = table.Column<string>(nullable: true),
                    besinProtein = table.Column<double>(nullable: false),
                    besinKarbonhidrat = table.Column<double>(nullable: false),
                    besinYag = table.Column<double>(nullable: false),
                    besinKalori = table.Column<double>(nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Besinler", x => x.BesinlerId);
                });

            migrationBuilder.CreateTable(
                name: "Users",
                columns: table => new
                {
                    UsersId = table.Column<int>(nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Name = table.Column<string>(nullable: true),
                    Email = table.Column<string>(nullable: true),
                    Password = table.Column<string>(nullable: true),
                    Salt = table.Column<string>(nullable: true),
                    Age = table.Column<int>(nullable: false),
                    Weight = table.Column<double>(nullable: false),
                    Length = table.Column<double>(nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Users", x => x.UsersId);
                });
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "Besinler");

            migrationBuilder.DropTable(
                name: "Users");
        }
    }
}
