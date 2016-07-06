/**
 * 
 */
package jaxComponents;

/**
 * @author Ruholla
 * 
 */
public class TagCloser implements TransformableToJax
{
	private int	type;
	private int	innerLevel;
	
	public TagCloser(int type, int innerLevel)
	{
		// super();
		this.type = type;
		this.innerLevel = innerLevel;
	}
	
	@Override
	public String toString()
	{
		StringBuffer tabs = new StringBuffer();
		for (int i = 0; i < this.innerLevel; i++)
		{
			tabs.append( '\t');
		}
		return tabs + "</" + getName() + '>';
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jaxComponents.TransformableToJax#getName()
	 */
	@Override
	public String getName()
	{
		return JaxSyntax.getTag( getType()).getName();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jaxComponents.TransformableToJax#getType()
	 */
	@Override
	public int getType()
	{
		return this.type;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jaxComponents.TransformableToJax#needToClose()
	 */
	@Override
	public boolean needToClose()
	{
		return false;
	}
	
}
