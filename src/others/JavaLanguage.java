package others;
import java.util.ArrayList;

/**
 * 
 */

/**
 * @author Ruholla
 *
 */
public class JavaLanguage extends Language// implements SingleTone
{
	private static JavaLanguage Instance;
	//private static Object Ins;
	@Override
	protected void initialize()
	{
		initializeKeywords();
		initializeSpecialCharactor();
		initializeSeperatChar();
		initializePrimitiveTypes();
		
	}
	private void initializePrimitiveTypes()
	{
		getPrimitiveTypes().add( "int");
		getPrimitiveTypes().add( "char");
		getPrimitiveTypes().add( "boolean");
		getPrimitiveTypes().add( "byte");
		getPrimitiveTypes().add( "double");
		getPrimitiveTypes().add( "short");
		getPrimitiveTypes().add( "long");
		getPrimitiveTypes().add( "float");	
	}
	@Override
	public char getStringInitializer()
	{
		return '"';
	}
	protected JavaLanguage(){
		initialize();
	};

	private void  initializeSeperatChar()
	{
		this.separatorCharacters.add( new Character('{'));
		this.separatorCharacters.add( new Character('}'));
		//this.separatorCharacters.add( new Character('('));
		//this.separatorCharacters.add( new Character(')'));
		this.separatorCharacters.add( new Character(';'));
		//this.separatorCharacters.add( new Character(','));
	}
	
	private void initializeKeywords()
	{
	
	}
	
	private void initializeSpecialCharactor()
	{
		specialCharacters.add( new Character('{') );
		specialCharacters.add( new Character('}') );
		specialCharacters.add( new Character(';') );
		specialCharacters.add( new Character('(') );
		specialCharacters.add( new Character(')') );
		specialCharacters.add( new Character('+') );
		specialCharacters.add( new Character('/') );
		specialCharacters.add( new Character('=') );
		specialCharacters.add( new Character('*') );
		specialCharacters.add( new Character('-') );
		specialCharacters.add( new Character('.') );
		specialCharacters.add( new Character(',') );
		specialCharacters.add( new Character('&') );
		specialCharacters.add( new Character('|') );
		specialCharacters.add( new Character('!') );
		specialCharacters.add( new Character('%') );
		specialCharacters.add( new Character('[') );
		specialCharacters.add( new Character(']') );
	}

	
	public static JavaLanguage getInstance()
	{
		if(Instance == null )
		{
			Instance = new JavaLanguage();
		}
		return Instance;
	}
	

}
