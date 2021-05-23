package cz.tul.vvoleman.ui;

public class Regex {

    //Heslo: 8-32 znaků, musí obsahovat velké písmeno, číslo
    public static String PASSWORD = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z]).{8,32}$";

}
