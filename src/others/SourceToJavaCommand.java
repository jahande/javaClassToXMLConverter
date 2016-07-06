package others;
import java.util.ArrayList;

import fileReadWrite.SourceReader;


/**
 * this class add a field that contain some information related to java language
 * but the reason that it does not come into class "JavaLanguage" is another relation to my algorithm 
 * and it clear that class "JavaLanguage" must be independent to my transforming(to jax) algorithm 
 */

/**
 * @author Ruholla
 *
 */
public class SourceToJavaCommand extends SourceToCommand
{
	//@Override
	//private ArrayList<String> source = new ArrayList<String>();
	//private static boolean hasInstance = false;
	//private static SourceToCommand instance;
	private static ArrayList<Character> separatorCharacters;
	
	protected SourceToJavaCommand(){ super(null,null); }
	
	public SourceToJavaCommand( Language in , SourceReader sou)
	{
		super(in , sou );
		initialize();
	}
	
	/*public static SourceToCommand getInstance(String fileAddress)
	{
		if(!hasInstance)
		{
			instance = new SourceToJavaCommand(JavaLanguage.getInstance(), new File );
		}
		return instance;
	}*/
	
	private void initialize()
	{
		if(separatorCharacters != null)
		{
			return;
		}
		
		separatorCharacters  = new ArrayList<Character>();
		
		separatorCharacters.add('{');
		separatorCharacters.add('}');
		separatorCharacters.add(';');
		separatorCharacters.add('/');
	}
	
	
	/* (non-Javadoc)
	 * @see FileToCommand#getCommands()
	 */
	@Override
	public void goNext()
	{
		for(int i=currentIndex ; i<this.source.size() ; i++)
		{
			if( isSeparatorCharacters( this.source.get(i) )!=-1 )
			{
				lastIndex = currentIndex;
				currentIndex = i+1;
				return;
			}
		}
		lastIndex = currentIndex;
		currentIndex = this.source.size();
	}
	
	/* (non-Javadoc)
	 * @see FileToCommand#hasNext()
	 */
	@Override
	public boolean hasNext()
	{
		if(currentIndex >this.source.size())
		{
			return false;
		}
		return true;
	}
	/**
	 * @param
	 * Character key
	 * 
	 * @Return
	 * 
	 * -1 if key was not int this.separatorCharacters 
	 * else 
	 * 		index of the Character
	 * 
	 */
	private int isSeparatorCharacters( String key )
	{
		return this.separatorCharacters.indexOf(new Character(key.charAt(0)));
	}
	
	
}
