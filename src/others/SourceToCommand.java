package others;
import java.util.ArrayList;

import fileReadWrite.SourceReader;


/**
 * this class creates Commands from a file 
 */

/**
 * @author Ruholla
 * 
 */
public abstract class SourceToCommand implements SeparatedCode {

	protected int lastIndex = -1;
	protected int currentIndex = 0;

	protected ArrayList<String> source = new ArrayList<String>();
	protected Language languageType;  
	protected SourceReader sourceToRead;
	
	protected SourceToCommand( Language in, SourceReader sourcetoRead)
	{
		this.languageType = in;
		this.sourceToRead =sourcetoRead;
		sourceSpecialRead();
		formatter();
	}
	
	//protected abstract SourceReader getInstanceOfSourceReader();
	
	/**
	 * formatter: separates merged this.source members with stringSeparator2 and put them back into this.source
	 * this method increase the size of this.source
	 */
	protected void formatter()
	{
		ArrayList<String> temp;
		
		for(int i=0; i<source.size();i++)
		{
			temp = stringSeparator2(source.get(i));
			if( temp.size()>1 )
			{
				this.source.remove(i);

				this.source.addAll(i, temp);

				i+= temp.size()-1;//very nice
			}
		}
	}
	
	protected ArrayList<String> stringSeparator2(String in)
	{
		ArrayList<String> result = new ArrayList<String>();
		int SCIIL;/**special Character Index In Language */
		int lastSpecialCharacterIndex = -1;
		//char[] specialCharacterTemp = new char[1]; solution-1
		//boolean hasSpecialCharacter = false; solution-0
		
		for( int i=0; i<in.length() ; i++ )
		{
			SCIIL = languageType.isInSpecialCharacter( in.charAt(i) );
			if( SCIIL != -1 )
			{
				//hasSpecialCharacter = true;
				
				//specialCharacterTemp[0] = languageType.getSpecialCharactors(SCIIL).charValue();solution-1
				
				if( lastSpecialCharacterIndex+1 < i )
				{
					result.add( in.substring(lastSpecialCharacterIndex+1, i) );
				}
				
				//result.add( new String(specialCharacterTemp) );solution-1
				result.add( in.substring(i, i+1) );//solution-2
				
				
				lastSpecialCharacterIndex = i;
			}
		}
		
		
		if(  lastSpecialCharacterIndex+1<in.length() )
		{
			result.add( in.substring(lastSpecialCharacterIndex+1, in.length()) ) ;
		}
			
		return result;
	}
	
	protected String stringSeparator(String in)
	{
		String result = new String();
		int lastSpecialCharacterIndex = -1;
		boolean hasSpecialCharacter = false;
		for( int i=0; i<in.length() ; i++ )
		{
			if( languageType.isInSpecialCharacter( in.charAt(i) )!=-1 )
			{
				hasSpecialCharacter = true;
				
				if(in.substring(lastSpecialCharacterIndex+1, i)== null )
				{
					result += ' ' + in.charAt(i)+ ' ';
				}
				else
				{
					result += in.substring(lastSpecialCharacterIndex+1, i) + ' ' + in.charAt(i)+ ' ';
				}
				
				lastSpecialCharacterIndex = i;
			}
		}
		
		
		if( hasSpecialCharacter && in.substring(lastSpecialCharacterIndex+1, in.length() )!= null )
		{
			result += ' ' + in.substring(lastSpecialCharacterIndex+1, in.length()) ;
		}
			
		
		if(hasSpecialCharacter)
		{
			return result;
		}
		else
		{
			return in;
		}
	}
	
	protected void sourceSpecialRead()
	{
		while(sourceToRead.hasNext())
		{
			source.add(sourceToRead.nextLine());
		}
	}

	@Override
	public ArrayList<String> getArray()
	{
		return this.source;
	}

	@Override
	public int getLastIndex()
	{
		return lastIndex;
	}
	
	@Override
	public int getCurrentIndex()
	{
		return currentIndex;
	}
	
	@Override
	public String getMemberOfArray(int index)
	{
		return this.getArray().get(index);
	}
}
