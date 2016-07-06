package jaxComponents;

/**
 * 
 */

/**
 * @author Ruholla
 * 
 */
public class TagContainer implements TransformableToJax
{
	
	private int		innerLevel;
	private int		type;
	// index of this tag in the class "jaxSyntax"
	private int		statesIndex[];	// -1 shows that state name is not a
	// constant from the Syntax list
	// private String identifierName;//its different from tag name
	private String	statesName[];
	
	public TagContainer(int type, int numberOfAttributes, int innerLevel)
	{
		this.innerLevel = innerLevel;
		this.type = type;
		this.statesIndex = new int[numberOfAttributes];
		this.statesName = new String[numberOfAttributes];
	}
	
	@Override
	public boolean needToClose()
	{
		return JaxSyntax.getTag( getType()).doesNeedToClose();
	}
	
	public TagContainer(int IOTTITJS, int[] statesIndex, String[] statesName,
			int innerLevel)
	{
		setInnerLevel( innerLevel);
		setType( IOTTITJS);
		this.statesName = statesName;
		this.statesIndex = statesIndex;
		// setIdentifierName(identifier);
	}
	
	@Override
	/**
	 * @return
	 * the print type(jax syntax) of a tag
	 */
	public String toString()
	{
		StringBuffer result = new StringBuffer();
		StringBuffer tabNumber = new StringBuffer();
		
		for (int i = 0; i < this.innerLevel; i++)
		{
			tabNumber.append( '\t');
		}
		for (int i = 0; i < thisSyntaxTag().getNumberOfAttrributes(); i++)
		{
			if (statesIndex[i] != -1 && statesName[i] == null
					&& !getAttribute( i).hasName())
			{
				result.append( ' ');
				result.append( getAttribute( i).getName());
				result.append( "=\"");
				result.append( getAttribute( i).getState( statesIndex[i])
						+ "\"");
			}
			else if (statesIndex[i] == -1 && statesName[i] != null
					&& getAttribute( i).hasName())
			{
				result.append( ' ' + getAttribute( i).getName() + "=\""
						+ statesName[i] + '"');
			}
			else
			{
				System.out.println( "Error in method begin in class Tag");
				result.append( " error " + i + "th element of for\t");
			}
		}
		if (needToClose())
		{
			return ( tabNumber + "<" + thisSyntaxTag().getName() + result + ">" );
		}
		else
		{
			return ( tabNumber + "<" + thisSyntaxTag().getName() + result + "/>" );		
		}
	}
	
	
	
	private JaxSyntaxTag thisSyntaxTag()
	{
		return JaxSyntax.getTag( type);
	}
	
	private JaxSyntaxAttribute getAttribute(int type)
	{
		return JaxSyntax.getTag( this.type).getAttribute( type);
	}
	
	/**
	 * every TagContainer must have an type
	 * 
	 * @return index of this tag in the class "jaxSyntax"
	 */
	@Override
	public int getType()
	{
		return type;
	}
	
	public void setType(int type)
	{
		if (type >= 0)
		{
			this.type = type;
		}
		else
		{
			System.out.println( "Error in setType in class Tag");
			this.type = 0;
		}
		
	}
	
	/*
	 * public String getIdentifierName() { return identifierName; }
	 * 
	 * public void setIdentifierName(String identifierName) {
	 * if(identifierName!=null) { this.identifierName = identifierName; } else {
	 * System.out.println("Error in method setIdentifier in Tag class");
	 * this.identifierName = "defaultName"; } }
	 */

	public void setInnerLevel(int innerLevel)
	{
		this.innerLevel = innerLevel >= -1 ? innerLevel : 0;
	}
	
	/**
	 * @return the statestype
	 */
	public int[] getStatesIndex()
	{
		return statesIndex;
	}
	
	/**
	 * @return the statesName
	 */
	public String[] getStatesName()
	{
		return statesName;
	}
	
	/**
	 * @param statestype
	 *            the statestype to set
	 */
	public void setStatestype(int[] statesIndex)
	{
		this.statesIndex = statesIndex;
	}
	
	/**
	 * @param statesName
	 *            the statesName to set
	 */
	public void setStatesName(String[] statesName)
	{
		this.statesName = statesName;
	}
	
	@Override
	public String getName()
	{
		return getStatesName()[0];
	}
	
	/**
	 * @param statesIndex
	 *            the statesIndex to set
	 */
	public void setStatesIndex(int[] statesIndex)
	{
		this.statesIndex = statesIndex;
	}
}
