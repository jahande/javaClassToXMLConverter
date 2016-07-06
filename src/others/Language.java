package others;

import java.util.ArrayList;

/**
 * 
 */

/**
 * @author Ruholla
 * 
 */
public abstract class Language // implements SingleTone
{
	protected ArrayList<String>		primitiveTypes		= new ArrayList<String>();
	protected boolean				hasInstance			= false;
	protected ArrayList<String>		keyWords			= new ArrayList<String>();
	protected ArrayList<Character>	specialCharacters	= new ArrayList<Character>();
	protected ArrayList<Character>	separatorCharacters	= new ArrayList<Character>();
	
	// protected comment
	
	protected abstract void initialize();
	
	public abstract char getStringInitializer();
	
	public boolean isItPrimitiveType(String type)
	{
		return getPrimitiveTypes().contains( type);
	}
	
	public int getSizeOfSepratorCharacters()
	{
		return this.separatorCharacters.size();
	}
	
	public int getSizeOfSpecialCharacters()
	{
		return this.specialCharacters.size();
	}
	
	public String getKeyWords(int index)
	{
		return keyWords.get( index);
	}
	
	public Character getSpecialCharactors(int index)
	{
		return specialCharacters.get( index);
	}
	
	public int isInKeyWords(String key)
	{
		return keyWords.indexOf( key);
	}
	
	public int isInSpecialCharacter(Character key)
	{
		return specialCharacters.indexOf( key);
	}
	
	public int getNumberOfKeyWords()
	{
		return keyWords.size();
	}
	
	public int getNumberOfSpecialCharactors()
	{
		return specialCharacters.size();
	}
	
	/**
	 * @return the separatorCharacters
	 */
	public Character getSeparatorCharacters(int index)
	{
		return this.separatorCharacters.get( index);
	}
	
	public boolean isInSeparatorCharacters(Character key)
	{
		return this.separatorCharacters.contains( key);
	}
	
	/**
	 * @return the separatorCharacters
	 */
	public ArrayList<Character> getSeparatorCharacters()
	{
		return separatorCharacters;
	}
	
	/**
	 * @param separatorCharacters
	 *            the separatorCharacters to set
	 */
	protected void setSeparatorCharacters(
			ArrayList<Character> separatorCharacters)
	{
		this.separatorCharacters = separatorCharacters;
	}
	
	public int indexOfSeperatedCharacters(Character c)
	{
		return getSeparatorCharacters().indexOf( c);
	}
	
	/**
	 * 
	 * @param input
	 *            , logically must have just one character
	 * @return the index of the first character of input if was in
	 *         separatorCharactor otherwise -1
	 */
	public int indexOfSeperatedCharacters(String input)
	{
		return getSeparatorCharacters().indexOf( input.charAt( 0));
	}
	
	/**
	 * @return the primitiveTypes
	 */
	protected ArrayList<String> getPrimitiveTypes()
	{
		return primitiveTypes;
	}
	
	/**
	 * @param primitiveTypes
	 *            the primitiveTypes to set
	 */
	protected void setPrimitiveTypes(ArrayList<String> primitiveTypes)
	{
		this.primitiveTypes = primitiveTypes;
	}
	
	/**
	 * 
	 * @param input
	 *            a string(probably a word from a *.java file)
	 * @return index of start of a started comment in the input string -1 if no
	 *         comment has been started
	 */
	// public abstract int isThereComment(String input);
	/**
	 * 
	 * @param input
	 *            a string(probably a word from a *.java file)
	 * @return index of end of a started comment in the input string -1 if no
	 *         comment has been started
	 */
	// public abstract int doesACommentEnds (String input);
	
}
