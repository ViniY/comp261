import java.util.ArrayList;

/**
 * A new instance of LempelZiv is created for every run.
 */
public class LempelZiv {
	/**
	 * Take uncompressed input as a text string, compress it, and return it as a
	 * text string.
	 */
	ArrayList<Tuple> tuples = new ArrayList<>();
	private static final int WINDOW_SIZE = 100;

	public String compress(String input) {
		// TODO fill this in.
		StringBuilder output = new StringBuilder();
		int pointer = 0;
		while(pointer < input.length()){
			int lookahead = 0;
			int prevMatch = -1;
			while(true){
				int start;
				if(pointer<WINDOW_SIZE) start= 0;
				else start = pointer - WINDOW_SIZE;
				String s = input.substring(start, pointer);
				String pattern = input.substring(pointer, pointer + lookahead);
				int match = s.indexOf(pattern);

				if(pointer + lookahead >= input.length()){
					match = -1;
				}

				if(match > -1){	//matching
					prevMatch = match;
					lookahead++;
				}else{
					int offset;
					if(prevMatch>-1) offset = s.length() - prevMatch;
					else offset = 0;
					Character nextChar = input.charAt(pointer + lookahead - 1);
					Tuple tuple = new Tuple(offset, lookahead - 1, nextChar);
					tuples.add(tuple);
					output.append(tuple.toString());
					pointer += lookahead;
					break;
				}
			}
		}

		return output.toString();

		//return "";
	}

	/**
	 * Take compressed input as a text string, decompress it, and return it as a
	 * text string.
	 */
	public String decompress(String compressed) {
		// TODO fill this in.
		StringBuilder output = new StringBuilder();
		int pointer = 0;
		for(Tuple tuple: tuples){
			if(tuple.length == 0 && tuple.offset == 0){
				output.append(tuple.nextChar);
				pointer++;
			}else{
				output.append(output.substring(pointer - tuple.offset, pointer - tuple.offset + tuple.length));
				pointer += tuple.length;

				if (tuple.nextChar != null)	output.append(tuple.nextChar);
				pointer++;
			}
		}
		return output.toString();
	}

	/**
	 * The getInformation method is here for your convenience, you don't need to
	 * fill it in if you don't want to. It is called on every run and its return
	 * value is displayed on-screen. You can use this to print out any relevant
	 * information from your compression.
	 */
	public String getInformation() {
		return "";
	}





	private  class Tuple{
		int offset;
		int length;
		Character nextChar;

		public Tuple(int offset, int length, char nextChar){
			this.offset = offset;
			this.length = length;
			this.nextChar = nextChar;
		}

		@Override
		public String toString() {
			return "[" + offset + ", " + length + ", " + nextChar + "]";
		}
	}

}

