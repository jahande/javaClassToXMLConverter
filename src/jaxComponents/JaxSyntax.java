package jaxComponents;

import java.util.ArrayList;

//import com.sun.xml.internal.bind.v2.schemagen.xmlschema.NestedParticle;

/**
 * CAUTION
 * this class needs to initialize 
 */

/**
 * @author Ruholla
 * 
 */
public class JaxSyntax
{// implements NecessaryToInitialize{

	private static ArrayList<JaxSyntaxTag>	tags	= new ArrayList<JaxSyntaxTag>();
	
	private static void addTag(JaxSyntaxTag in)
	{
		tags.add( in);
	}
	
	public static int numberOfTags()
	{
		return tags.size();
	}
	
	public static JaxSyntaxTag getTag(int index)
	{
		return tags.get( index);
	}
	
	public static int isAStateOfAttribute(String key,int tagIndex, int attributeIndex,
			int decreaseAmuont)
	{
		for (int i = 0; i < getAttribute( tagIndex, attributeIndex)
				.getNumberOfStates()
				- decreaseAmuont; i++)
		{
			if(getState( tagIndex, attributeIndex, i).equals( key))
			{
				return i;
			}
		}
		return -1;
	}
	
	// @Override
	static
	{
		JaxSyntaxAttribute[] attributeTemp = new JaxSyntaxAttribute[5];
		// JaxSyntaxTag tagtemp;
		
		attributeTemp[0] = name();
		attributeTemp[1] = accessType();
		attributeTemp[2] = extendsClass();
		attributeTemp[3] = abstractionType();
		attributeTemp[4] = implementsInterfaces();
		
		JaxSyntax.addTag( new JaxSyntaxTag( "class" , true , attributeTemp));// 0
		// =====================
		attributeTemp = new JaxSyntaxAttribute[1];
		
		attributeTemp[0] = interfaceName();
		
		JaxSyntax.addTag( new JaxSyntaxTag( "implements" , false , attributeTemp));// 1
		// =================
		
		attributeTemp = new JaxSyntaxAttribute[6];
		
		attributeTemp[0] = name();
		attributeTemp[1] = type();
		attributeTemp[2] = staticState();
		attributeTemp[3] = primitive();
		attributeTemp[4] = accessType();
		attributeTemp[5] = finalState();
		
		JaxSyntax.addTag( new JaxSyntaxTag( "field" , false , attributeTemp));// 2
		// ================
		
		attributeTemp = new JaxSyntaxAttribute[5];
		
		attributeTemp[0] = name();
		attributeTemp[1] = type();
		attributeTemp[2] = abstractionType();
		attributeTemp[3] = accessType();
		attributeTemp[4] = staticState();
		
		JaxSyntax.addTag( new JaxSyntaxTag( "method" , true , attributeTemp));// 3
		// ===================
		
		attributeTemp = new JaxSyntaxAttribute[3];
		
		attributeTemp[0] = name();
		attributeTemp[1] = type();
		attributeTemp[2] = finalState();
		
		JaxSyntax
				.addTag( new JaxSyntaxTag( "parameter" , false , attributeTemp));// 4
		// ==================
		
		attributeTemp = new JaxSyntaxAttribute[1];
		
		attributeTemp[0] = accessType();
		
		JaxSyntax.addTag( new JaxSyntaxTag( "constructor" , true ,
				attributeTemp));// 5
		// ===================
		
	}
	
	private static JaxSyntaxAttribute accessType()
	{
		
		String states[] = { "public", "private", "protected", "default" };
		return ( new JaxSyntaxAttribute( "access-type" , false , states) );
		
	}
	
	private static JaxSyntaxAttribute abstractionType()
	{
		
		String states[] = { "final", "abstract", "none" };
		return ( new JaxSyntaxAttribute( "abstraction-type" , false , states) );
		
	}
	
	private static JaxSyntaxAttribute extendsClass()
	{
		
		// String states[] = {"null"};
		return ( new JaxSyntaxAttribute( "extends" , true , null) );
		
	}
	
	private static JaxSyntaxAttribute implementsInterfaces()
	{
		
		String states[] = { "false", "true" };
		return ( new JaxSyntaxAttribute( "implements-interfaces" , false ,
				states) );
		
	}
	
	private static JaxSyntaxAttribute type()
	{
		String states[] = null;
		return ( new JaxSyntaxAttribute( "type" , true , states) );
		
	}
	
	private static JaxSyntaxAttribute primitive()
	{
		String states[] = { "false", "true" };
		return ( new JaxSyntaxAttribute( "primitive" , false , states) );
		
	}
	
	private static JaxSyntaxAttribute staticState()
	{
		String states[] = { "false", "true" };
		return ( new JaxSyntaxAttribute( "static" , false , states) );
		
	}
	
	private static JaxSyntaxAttribute finalState()
	{
		String states[] = { "false", "true" };
		return ( new JaxSyntaxAttribute( "final" , false , states) );
		
	}
	
	private static JaxSyntaxAttribute name()
	{
		return ( new JaxSyntaxAttribute( "name" , true , null) );
	}
	
	private static JaxSyntaxAttribute interfaceName()
	{
		return ( new JaxSyntaxAttribute( "interface-name" , true , null) );
	}
	
	public static String getState(int tagIndex, int attributeIndex,
			int stateIndex)
	{
		return JaxSyntax.getTag( tagIndex).getAttribute( attributeIndex)
				.getState( stateIndex);
	}
	
	public static JaxSyntaxAttribute getAttribute(int tagIndex,
			int atributeIndex)
	{
		return JaxSyntax.getTag( tagIndex).getAttribute( atributeIndex);
	}
}
