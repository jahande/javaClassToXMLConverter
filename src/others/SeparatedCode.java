package others;
import java.util.ArrayList;

/**
 * every class that implement this interface, supply a stream of String Arrays.
 * these arrays can be useful because each of them is a complete definition.
 * @author Ruholla
 *
 */
public interface SeparatedCode {
	
	/**
	 * array of string that the Class TagsCreator must change all of them into one or several tags 
	 * @return
	 * An array of string 
	 * for example returns:
	 * 		{"public", "class", "NegativeException", "extends" , "Exception" }
	 * 		{"private" ,"double" , "add" , "(" , "int", "x" }  
	 */
	public ArrayList<String> getArray(); 
	
	public String getMemberOfArray(int index);
	/**
	 * move currentIndex and LastIndex to next group for change into jax by class "Transformer"
	 */
	public void goNext();
	
	public int getCurrentIndex();
	
	public int getLastIndex();
	
	public boolean hasNext();

}
