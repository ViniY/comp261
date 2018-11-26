public class BruteForceSearch {
    String pattern;
    String text;
    public BruteForceSearch(String pattern, String text){
        this.pattern = pattern;
        this.text = text;
    }
    public int bruteForceSearch(String pattern , String text){
        //make sure the pattern is not empty if so return -1
        long start = System.currentTimeMillis();
        if (pattern.length() < 1) return -1;
        for (int k = 0; k < text.length() - pattern.length() + 1; k++) {
            boolean found = true;
            for (int i = 0; i < pattern.length(); i++) {
                if (pattern.charAt(i) != text.charAt(k + i)) {
                    found = false;
                    break;
                }
            }
            if (found) { return k; }
        }
        long end = System.currentTimeMillis();
        System.out.println(Math.abs(end-start));
        return -1; // no such pattern in the text
    }
}
