using System;
using Microsoft.EntityFrameworkCore.Migrations;

namespace SAV.DataAccess.Migrations
{
    public partial class databaseCreate : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "Besinler",
                columns: table => new
                {
                    BesinlerId = table.Column<int>(nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    KullaniciId = table.Column<int>(nullable: false),
                    besinAdi = table.Column<string>(nullable: true),
                    besinProtein = table.Column<double>(nullable: false),
                    besinKarbonhidrat = table.Column<double>(nullable: false),
                    besinYag = table.Column<double>(nullable: false),
                    besinKalori = table.Column<double>(nullable: false),
                    barkodNo = table.Column<string>(nullable: true),
                    ozel = table.Column<bool>(nullable: false)
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
                    Length = table.Column<double>(nullable: false),
                    Cinsiyet = table.Column<bool>(nullable: false),
                    Admin = table.Column<bool>(nullable: false),
                    LimitKalori = table.Column<double>(nullable: false),
                    AltLimitKarbon = table.Column<double>(nullable: false),
                    AltLimitYag = table.Column<double>(nullable: false),
                    AltLimitProtein = table.Column<double>(nullable: false),
                    UstLimitKarbon = table.Column<double>(nullable: false),
                    UstLimitYag = table.Column<double>(nullable: false),
                    UstLimitProtein = table.Column<double>(nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Users", x => x.UsersId);
                });

            migrationBuilder.CreateTable(
                name: "YedigiBesin",
                columns: table => new
                {
                    UsersId = table.Column<int>(nullable: false),
                    BesinlerId = table.Column<int>(nullable: false),
                    Date = table.Column<DateTime>(nullable: false),
                    Porsiyon = table.Column<int>(nullable: false),
                    ozel = table.Column<bool>(nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_YedigiBesin", x => new { x.BesinlerId, x.UsersId, x.Date });
                    table.ForeignKey(
                        name: "FK_YedigiBesin_Besinler_BesinlerId",
                        column: x => x.BesinlerId,
                        principalTable: "Besinler",
                        principalColumn: "BesinlerId",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_YedigiBesin_Users_UsersId",
                        column: x => x.UsersId,
                        principalTable: "Users",
                        principalColumn: "UsersId",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateIndex(
                name: "IX_YedigiBesin_UsersId",
                table: "YedigiBesin",
                column: "UsersId");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "YedigiBesin");

            migrationBuilder.DropTable(
                name: "Besinler");

            migrationBuilder.DropTable(
                name: "Users");
        }
    }
}
