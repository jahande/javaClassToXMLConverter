package jaxComponents;

public class CommentContainer implements TransformableToJax
{
	private int		innerLevel;
	private String	text;
	private int type;
	//-1->line comment	-2->block comment
	
	@Override
	public boolean needToClose()
	{
		return false;
	}
	
	@Override
	public String getName()
	{
		return getText();
	}
	
	public CommentContainer(String text, int innerLevel,int type)
	{
		this.text = text;
		this.innerLevel = innerLevel;
		this.type = type;
	}
	
	@Override
	public String toString()
	{
		String tab = new String();
		for (int i = 0; i < this.innerLevel; i++)
		{
			tab += "\t";
		}
		return tab + "<!--" + this.text + "-->";
	}
	
	

	/**
	 * @return the innerLevel
	 */
	public int getInnerLevel()
	{
		return innerLevel;
	}

	/**
	 * @return the text
	 */
	public String getText()
	{
		return text;
	}

	/**
	 * @return the type
	 */
	public int getType()
	{
		return type;
	}

	/**
	 * @param innerLevel the innerLevel to set
	 */
	public void setInnerLevel(int innerLevel)
	{
		this.innerLevel = innerLevel;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text)
	{
		this.text = text;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type)
	{
		this.type = type;
	}
	
}
