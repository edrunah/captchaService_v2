public class RandomStringGenerator implements StringGenerator {

    public String generate(int n) {
        char[] chars = new char[n];
        for(int i = 0; i < n; i++) {
            double charsOrFigure = Math.random();
            if (charsOrFigure <= 0.5) {
                int onechar = 97 + (int) (26 * Math.random());
                chars[i] = (char) onechar;
            } else {
                int oneFigure = 48 + (int) (10 * Math.random());
                chars[i] = (char) oneFigure;
            }
        }
        return new String(chars);
    }
}
