using System;
using Microsoft.EntityFrameworkCore.Migrations;

namespace SAV.DataAccess.Migrations
{
    public partial class updateYedigi : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "YedigiBesin",
                columns: table => new
                {
                    UsersId = table.Column<int>(nullable: false),
                    BesinlerId = table.Column<int>(nullable: false),
                    Date = table.Column<DateTime>(nullable: false),
                    Porsiyon = table.Column<int>(nullable: false)
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
        }
    }
}
