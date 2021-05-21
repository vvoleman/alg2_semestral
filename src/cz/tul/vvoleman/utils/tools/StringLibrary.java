package cz.tul.vvoleman.utils.tools;

public class StringLibrary {

    /**
     * Draws menu with inputed lines
     * @param lines lines
     * @return Menu
     */
    public static String drawMenu(String[] lines){
        int offset = 3;
        int length = getHighestLength(lines)+offset+1;

        StringBuilder sb = new StringBuilder();
        sb.append(drawLine(length + offset)).append("\n");
        int temp;
        for(String s : lines){
            temp = s.length();
            sb.append(String.format("* %s*%n",s+drawSpace(length-temp)));
        }
        sb.append(drawLine(length + offset));

        return sb.toString();
    }

    /**
     * Draws space
     * @param n number of spaces
     * @return Space
     */
    private static String drawSpace(int n){
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < n; i++) {
            s.append(" ");
        }
        return s.toString();
    }

    /**
     * Draws line
     * @param length Length of line
     * @return Line
     */
    public static String drawLine(int length,char c){
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < length; i++) {
            s.append(c);
        }

        return s.toString();
    }

    public static String drawLine(int length){
        return drawLine(length,'*');
    }

    /**
     * Returns highest length of a string
     * @param lines Lines
     * @return Highest length from lines
     */
    public static int getHighestLength(String[] lines){
        int count = 0;
        int temp;
        for(String s : lines){
            temp = s.length();
            if(temp > count){
                count = temp;
            }
        }
        return count;
    }

}
