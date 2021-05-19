package cz.tul.vvoleman;

public class Main {

    public static void main(String[] args) {
        String psc = "40003";
        String[] parts = {psc.substring(0,3),psc.substring(3)};
        System.out.println(parts[0]+" "+parts[1]);
    }
}
