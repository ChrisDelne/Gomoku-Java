public class TestHelper {

    // Costruttore privato perché è una classe di utilità (non va istanziata)
    private TestHelper() {}

    public static int countOccurrences(String text, String sub) {
        if (text == null || sub == null || sub.isEmpty()) {
            return 0;
        }

        int count = 0;
        int idx = 0;

        while ((idx = text.indexOf(sub, idx)) != -1) {
            count++;
            idx += sub.length();
        }

        return count;
    }
}