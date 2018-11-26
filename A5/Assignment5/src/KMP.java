/**
 * A new KMP instance is created for every substring search performed. Both the
 * pattern and the text are passed to the constructor and the search method. You
 * could, for example, use the constructor to create the match table and the
 * search method to perform the search itself.
 */
public class KMP {

	int[] table;
	int patternlength;
	int textlength;
	public KMP(String pattern, String text) {
		// TODO maybe fill this in.
		//text is the fulltext
		// pattern is the desire string

 		this.patternlength = pattern.length();
 		this.textlength = text.length();
		this.table = new int[patternlength];//match table
		table[0]=-1;
		int pos = 1;//current position for the table
		int pattern_cursor =0;// current position in the pattern
		while(pos<pattern.length()-1){
			if(pattern.charAt(pos)==pattern.charAt(pattern_cursor)){
				table[pos] = table[pattern_cursor];
				pos++;
				pattern_cursor++;
			}
			else{
				table[pos] = table[pattern_cursor];
				pattern_cursor = table[pattern_cursor];
				while(pattern_cursor>=0&& pattern.charAt(pos)!= pattern.charAt(pattern_cursor)){
					pattern_cursor = table[pattern_cursor];
				}
				pos++;
				pattern_cursor++;
			}
		}
		table[pos] = pattern_cursor;
	}

	/**
	 * Perform KMP substring search on the given text with the given pattern.
	 * 
	 * This should return the starting index of the first substring match if it
	 * exists, or -1 if it doesn't.
	 */
	public int search(String pattern, String text) {
		// TODO fill this in.
		long start = System.currentTimeMillis();
		int k = 0 ; //the start of match in Parttern
		int i=0; //position of current character in text
		while ((k+i)<textlength){
			if(pattern.charAt(k) == text.charAt(k+i)){//matched
				k++;
				if(k==patternlength) {
					long end = System.currentTimeMillis();
					System.out.println(Math.abs(end-start));
					return i;//found S
				}

			}
				//mismatch, no self overlapping occurs
			else if(table[k]==-1){
				k=0;
				i=i+k+1;
			}
				//mismatch && self overlapping
			else {
				i=k+i - table[k];
				k = table[k];
			}
			long end = System.currentTimeMillis();
			if(Math.abs(end-start)>50000) return -1;
		}
		long end = System.currentTimeMillis();
		System.out.println(Math.abs(end-start));
		return -1;
	}
}
