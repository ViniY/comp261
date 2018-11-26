public class BoyerMooreSearch {
    private  String text;
    private  String pattern;
    private static final int MAXCHAR = 256;
    private int[] badtable;
    private int[] goodtable;

    public  BoyerMooreSearch(String pattern, String text){
        this.pattern = pattern;
        this.text = text;
    }
    public int search(String parttern, String text){
        if(parttern.length()==0 || text.length()==0) return -1;//empty nothing found
        char[] patternArray = parttern.toCharArray();
        char[] textArray = text.toCharArray();
        BadMatchTable(patternArray);
        goodSuffixes(patternArray);
        for (int i = patternArray.length - 1, j = i; i < textArray.length;  i += Math.max(goodtable[patternArray.length - 1 - j], badtable[textArray[i]])){//max is the maximum index to shift
            for (j = patternArray.length - 1; patternArray[j] == textArray[i]; i--, j--) {
                //found
                if (j == 0) {
                    return i;
                }
            }
        }
        return -1;
    }
    public int[] BadMatchTable(char[] pattern){// fill in the array of bad characters
        int[] badtable = new int[MAXCHAR];//set the array size one greater than 255
       for(int i=0; i < pattern.length;i++){
           badtable [pattern[i]] = pattern.length-1-i;
       }
        this.badtable = badtable;
       return badtable;
    }
    public int[] goodSuffixes(char[] pattern){
        int[] goodtable = new int[pattern.length];//
        // tracking the last index of prefix pattern
        int lastPrefix = pattern.length;

        for(int index = pattern.length; index>0 ; index--){//loops from the back
            if(checkPrefix(pattern,index)){
                lastPrefix = index;//update the last prefix index
            }
           goodtable[pattern.length - index] = lastPrefix + pattern.length - index;
        }
        for (int i=0; i <pattern.length-1; i++){
            int suffixLength = 0;
            while(pattern[i-suffixLength] == pattern[pattern.length-1-suffixLength]){ suffixLength++;}//the maximum length of the substring ends at index and is a suffix.
            goodtable[suffixLength] = pattern.length-1 + i +suffixLength;
        }
        this.goodtable = goodtable;
        return goodtable;
    }

    private boolean checkPrefix(char[] pattern, int index) {
        for(int i=0;i<pattern.length-index;i++){
            if(pattern[i]!= pattern[i=index]) return false;//not match then return false
        }
        return true;
    }


}
