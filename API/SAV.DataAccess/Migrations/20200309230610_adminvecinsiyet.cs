using Microsoft.EntityFrameworkCore.Migrations;

namespace SAV.DataAccess.Migrations
{
    public partial class adminvecinsiyet : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<bool>(
                name: "Admin",
                table: "Users",
                nullable: false,
                defaultValue: false);

            migrationBuilder.AddColumn<bool>(
                name: "Cinsiyet",
                table: "Users",
                nullable: false,
                defaultValue: false);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "Admin",
                table: "Users");

            migrationBuilder.DropColumn(
                name: "Cinsiyet",
                table: "Users");
        }
    }
}
