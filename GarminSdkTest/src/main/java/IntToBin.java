public class IntToBin {

    public static void main(String[] args) {
        System.out.println("57      = " + intToString(57, 8));
        System.out.println("180     = " + intToString(180, 8));
        System.out.println("-180    = " + intToString(-180, 8));
        System.out.println("360     = " + intToString(360, 8));
        System.out.println("9999999 = " + intToString(2598503, 8));
        System.out.println("2598503 = " + intToString(2598503, 8));
        System.out.println("360999999 = " + intToString(999999, 8));

        // 00111001001001111010011001100111 = 958899816
        // 00111001001001111010011001100111 = 958899817


    }

    public static String intToString(int number, int groupSize) {
        StringBuilder result = new StringBuilder();

        for(int i = 31; i >= 0 ; i--) {
            int mask = 1 << i;
            result.append((number & mask) != 0 ? "1" : "0");

            if (i % groupSize == 0)
                result.append(" ");
        }
        result.replace(result.length() - 1, result.length(), "");

        return result.toString();
    }
}
