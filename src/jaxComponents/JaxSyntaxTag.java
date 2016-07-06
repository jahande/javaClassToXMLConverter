package jaxComponents;
/**
 *IMPORTANT:this class must be initialized before use 
 */

/**
 * @author Ruholla
 *
 */
public class JaxSyntaxTag {
	
	private final String name;//name of tag for example class, field, method ,..
	private final JaxSyntaxAttribute[] attributes;
	private final boolean needsToClose;

	/**
	 * @return
	 * name of tag for example class, field, method ,..
	 */
	public String getName() {
		return name;
	}
	
	public JaxSyntaxTag (String name,boolean neadsToClose,  JaxSyntaxAttribute[] array)
	{
		// TODO Auto-generated constructor stub
		this.name = name;
		attributes = array;
		this.needsToClose = neadsToClose;
	}
	
		
	
	/*private void setName(String name) {
		this.name = name;
	}
	
	public void addAttribute(JavaSyntaxAttribute in)
	{
		attributes.add(in);
	}*/
	
	public int getNumberOfAttrributes()
	{
		return attributes.length;
	}

	public boolean doesNeedToClose() {
		return needsToClose;
	}
	public JaxSyntaxAttribute getAttribute(int index)
	{
		return attributes[index];
	}

}
