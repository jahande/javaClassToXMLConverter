package parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;

import others.Language;

import fileReadWrite.FileReader;
import fileReadWrite.SourceReader;

import jaxComponents.CommentContainer;
import jaxComponents.JaxSyntax;
import jaxComponents.TagCloser;
import jaxComponents.TagContainer;
import jaxComponents.TransformableToJax;

/**
 * must important functions
 * sentence parser
 * comments to tag
 * put space to current  sentence
 * 
 * 
 */

/**
 * @author Ruholla
 * 
 */
public class Transformer
{
	private String							pakage;
	private ArrayList<String>				imports;
	private int								numberOfBlocksInInitializer;
	private int								numberOfBlocksInMethodImplementation;
	private ArrayList<String>				nonePrimitiveTypes;
	private int								lineNumber;
	private Scanner							parserTemp;
	private Language						languageToTransform;
	private boolean							isInBlockComment;
	private SourceReader					sourceReader;
	
	private ArrayList<TransformableToJax>	tags;
	// and comment!
	/**
	 * hint: the size of stack shows inner level
	 */
	private Stack<Integer>					orderedIndexes;
	// ordered index of tags that shows where we are.
	// private ArrayList<String> nameOfCurrentClass;
	private String							currentSentence;
	
	// current sd
	// private int currentInnerLevel;
	private ArrayList<String>				seperatedWords;
	// private String remindedFromLastWord;
	// the test2 use this
	private boolean							sufficientWordsForATag;
	
	// the test2 use this
	
	{
		this.tags = new ArrayList<TransformableToJax>();
		this.orderedIndexes = new Stack<Integer>();
		this.setInBlockComment( false);
		// this.seperatedWord = new ArrayList<String>();
		this.setSufficientWordsForATag( false);
		this.seperatedWords = new ArrayList<String>();
		setLineNumber( 0);
		setNonePrimitiveTypes( new ArrayList<String>());
		setImports( new ArrayList<String>());
		setNumberOfBlocksInMethodImplementation( 0);
		setNumberOfBlocksInInitializer( 0);
	}
	
	public Transformer(String fileAddress, Language l)
	{
		
		try
		{
			sourceReader = new FileReader( fileAddress);
		}
		catch (Throwable t)
		{
			
		}
		
		this.languageToTransform = l;
		
	}
	
	public int getNumberOfTags()
	{
		return getTags().size();
	}
	
	public int numberOfNonePrimitiveTypes()
	{
		return getNonePrimitiveTypes().size();
	}
	
	public int getNumberOfNonePrimitiveTypes()
	{
		return getNonePrimitiveTypes().size();
	}
	
	private int isLineCommentStatrs(int from)
	{
		return commonSourceForComment( getStartLineCommentExpression(), from);
	}
	
	/**
	 * @param from
	 *            index of first character of the string
	 * @param upTo
	 *            index of last character of the string
	 * @return true if and only if there is " in this.currentSentence with index
	 *         less than from and there is " in this.currentSentence with index
	 *         greater than upTo and there was not another " between both of
	 *         them(they were seriate)
	 */
	private boolean isInSign(int from, int upTo)
	// Sign-> "  it means "is it between two " ?
	{
		int startIndexOfSign = 0, endIndexOfSign = -1;
		int temp;
		while (true)
		{
			startIndexOfSign = this.currentSentence.indexOf( "\"",
					endIndexOfSign + 1);
			if (startIndexOfSign == -1)
			{
				return false;
			}
			temp = startIndexOfSign;
			while (true)
			{
				
				endIndexOfSign = this.currentSentence.indexOf( "\"", temp + 1);
				if (endIndexOfSign == -1)
				{
					// this case in act is an exception
					return false;
				}
				if (this.currentSentence.charAt( endIndexOfSign - 1) == '\\')
				{
					temp = endIndexOfSign;
				}
				else
				{
					break;
				}
			}
			if (startIndexOfSign < from && endIndexOfSign > upTo)
			{
				return true;
			}
		}
	}
	
	private int isBodyCommentStatrs(int from)
	{
		return commonSourceForComment( getStartBlockExpression(), from);
	}
	
	private int isBodyCommentEnds(int from)
	{
		return commonSourceForComment( getEndBlockExpression(), from);
	}
	
	/**
	 * please pay attention if the key was between two " ( the expression \"
	 * will be ignored; it means that it must be used as programming syntax)
	 * 
	 * @param key
	 *            string that we want to know its existence
	 * @param from
	 *            search starts from "from"
	 * @return the index of key if exists else -1
	 */
	private int commonSourceForComment(String key, int from)
	{
		from -= key.length();
		while (true)
		{
			from = getCurrentSentence().indexOf( key, from + key.length());
			if (from == -1)
			{
				return -1;
			}
			else if ( !isInSign( from, from + key.length() - 1))
			{
				return from;
			}
		}
	}
	
	private boolean hasNextLine()
	{
		return this.sourceReader.hasNext();
	}
	
	private void goNextLine()
	{
		
		this.currentSentence = this.sourceReader.nextLine();
		
	}
	
	/**
	 * this function remove all comments in this.currentSentence and put them
	 * into tags
	 */
	private void commentsToTag()
	{
		int lastStartIndex = 0;
		int lineCommentIndex;
		int blockCommentStartIndex, blockCommentEndIndex;
		String temp1;
		while (true)
		{
			lineCommentIndex = isLineCommentStatrs( lastStartIndex);
			
			blockCommentStartIndex = isBodyCommentStatrs( lastStartIndex);
			blockCommentEndIndex = isBodyCommentEnds( blockCommentStartIndex
					+ getStartBlockExpression().length());
			// in fact search start after "/*"
			
			if (isInBlockComment()
					&& blockCommentEndIndex != -1
					&& ( blockCommentEndIndex < blockCommentStartIndex || blockCommentStartIndex == -1 ))
			{
				
				addTag( new CommentContainer( setString( getCurrentSentence()
						.substring( 0, blockCommentEndIndex)) ,
						getCurrentInnerLevel() , -2));
				
				setCurrentSentence( setString( getCurrentSentence()
						.substring(
								blockCommentEndIndex
										+ getEndBlockExpression().length())));
				setInBlockComment( false);
				continue;
			}
			else if (lineCommentIndex != -1
					&& !isInBlockComment()
					&& ( lineCommentIndex < blockCommentStartIndex || blockCommentStartIndex == -1 ))
			{
				temp1 = getCurrentSentence().substring(
						lineCommentIndex
								+ getStartLineCommentExpression().length());
				
				// TODO access field with function!
				tags.add( new CommentContainer( setString( temp1) ,
						getCurrentInnerLevel() , -1));
				temp1 = getCurrentSentence().substring( 0, lineCommentIndex);
				
				setCurrentSentence( setString( temp1));
				break;
			}
			else if (blockCommentStartIndex != -1
					&& ( lineCommentIndex > blockCommentStartIndex || lineCommentIndex == -1 ))
			{
				// priority is important
				if (blockCommentEndIndex == -1)
				{
					addTag( new CommentContainer(
							setString( getCurrentSentence().substring(
									blockCommentStartIndex
											+ getStartBlockExpression()
													.length())) ,
							getCurrentInnerLevel() , -2));
					setCurrentSentence( setString( getCurrentSentence()
							.substring( 0, blockCommentStartIndex)));
					
					setInBlockComment( true);
					
					break;
				}
				else if (blockCommentEndIndex > blockCommentStartIndex)
				{
					try
					{
						addTag( new CommentContainer(
								setString( getCurrentSentence().substring(
										blockCommentStartIndex
												+ getStartBlockExpression()
														.length(),
										blockCommentEndIndex)) ,
								getCurrentInnerLevel() , -2));
					}
					catch (Throwable t)
					{
						System.out.println( t.getMessage());
					}
					setCurrentSentence( setString( getCurrentSentence()
							.substring( 0, blockCommentStartIndex))
							+ ' '
							+ setString( getCurrentSentence().substring(
									blockCommentEndIndex
											+ getEndBlockExpression().length())));
				}
			}
			else if (blockCommentEndIndex == -1 && isInBlockComment())
			{
				addTag( new CommentContainer( setString( getCurrentSentence()) ,
						getCurrentInnerLevel() , -2));
				setCurrentSentence( setString( null));
				break;
			}
			else
			{
				break;
			}
		}
	}
	
	/**
	 * this function separate words of current sentence (after removing its
	 * comments and putting space between its special characters)
	 * 
	 * @return if
	 */
	private void sentenceParser()
	{
		String temp;
		
		while (getParserTemp().hasNext())
		{
			temp = getParserTemp().next();
			
			if (temp != null && temp.length() == 1 && isASeparatorChar( temp))
			{
				addToSeparatedWords( temp);
				setSufficientWordsForATag( true);
				return;
			}
			else if (temp != null
					&& temp.charAt( 0) == getLanguageToTransform()
							.getStringInitializer())
			{
				organizeStringInitializers( temp);
			}
			else
			{
				addToSeparatedWords( temp);
			}
		}
		setSufficientWordsForATag( false);
	}
	
	/**
	 *this function must completed if the requests of project increased the
	 * TODO mark is for this reason
	 */
	private void organizeStringInitializers(String firstWord)
	{
		// TODO organize for larger project need to expand
		if (firstWord.startsWith( "\"") && firstWord.length() > 1
				&& firstWord.endsWith( "\""))
		{
			return;
		}
		while (getParserTemp().hasNext())
		{
			firstWord = getParserTemp().next();
			if (firstWord.charAt( firstWord.length() - 1) == getLanguageToTransform()
					.getStringInitializer())
			{
				return;
			}
		}
	}
	
	/**
	 * 
	 * @param from
	 * @return false if and only if in current sentence there is at least one
	 *         special Characters and it was not after a wide space or before
	 *         wide space or and was not at the end or begin
	 */
	private boolean isSecialInSentenceNeedsToSpace(int from)
	{
		int index;
		for (int i = 0; i < getLanguageToTransform()
				.getNumberOfSpecialCharactors(); i++)
		{// && isInSign( index, upTo)
			index = getCurrentSentence().indexOf(
					getLanguageToTransform().getSpecialCharactors( i), from);
			if (index != -1
					&& !isInSign( index, index)
					&& ( ( index > 0 && getCurrentSentence().charAt( index - 1) != ' ' ) || ( index < getLengthOfSentence() - 1 && getCurrentSentence()
							.charAt( index + 1) != ' ' ) ))
			{
				return true;
			}
		}
		return false;
	}
	
	private void putSpaceToCurrentSentence()
	{
		putSpaceForStringInitializer();
		
		if ( !isSecialInSentenceNeedsToSpace( 0))
		{
			return;
		}
		
		StringBuffer buf = new StringBuffer( getCurrentSentence());
		// int lastIndexOfSpecialCharacter = -1;
		int numberOfInsertedSpace = 0;
		for (int i = 0; i < getLengthOfSentence(); i++)
		{
			// TODO
			
			if (getLanguageToTransform().isInSpecialCharacter(
					getCurrentSentence().charAt( i)) != -1
					&& !isInSign( i, i))
			{
				if (getCurrentSentence().charAt( i - 1) != ' ')
				{
					buf.insert( i + numberOfInsertedSpace, ' ');
					numberOfInsertedSpace++;
				}
				if (i + 1 < getLengthOfSentence()
						&& getCurrentSentence().charAt( i + 1) != ' ')
				{
					buf.insert( i + numberOfInsertedSpace + 1, ' ');
					numberOfInsertedSpace++;
				}
			}
			
		}
		setCurrentSentence( buf.toString());
		
		/*
		 * ArrayList<String> temp = new ArrayList<String>(); int
		 * lastIndexOfSpecialCharacter = -1; for (int i = 0; i <
		 * getLengthOfSentence(); i++) { // TODO
		 * 
		 * if (getLanguageToTransform().isInSpecialCharacter(
		 * getCorrentSentence().charAt( i)) != -1 && !isInSign( i, i)) {
		 * temp.add( subStringOfSentence( lastIndexOfSpecialCharacter + 1, i));
		 * temp.add( " "); temp.add( subStringOfSentence( i, i + 1)); temp.add(
		 * " "); }
		 * 
		 * } StringBuffer sentenceTemp = new StringBuffer(); for (int i = 0; i <
		 * temp.size(); i++) { // TODO ensure sentenceTemp.append( temp.get(
		 * i)); } setCurrentSentence( sentenceTemp.toString());
		 */
	}
	
	/**
	 * StringInitializer here mean -> " that for example if we want to
	 * initialize a string use it. Example :String x="name";
	 */
	private void putSpaceForStringInitializer()
	{
		int numberOfInsertedSpaces = 0;
		boolean isInAnInitializer = false;
		StringBuffer buf = new StringBuffer( getCurrentSentence());
		for (int i = 0; i < getCurrentSentence().length(); i++)
		{
			if (getCurrentSentence().charAt( i) == getLanguageToTransform()
					.getStringInitializer()
					&& !isInSign( i, i))
			{
				if (isInAnInitializer)
				{
					buf.insert( i + numberOfInsertedSpaces + 1, ' ');
					isInAnInitializer = false;
				}
				else
				{
					buf.insert( i + numberOfInsertedSpaces, ' ');
					isInAnInitializer = true;
				}
				numberOfInsertedSpaces++;
			}
		}
		setCurrentSentence( buf.toString());
		/*
		 * int x, y, last = -1; String temp[] = new String[3]; while (true) { x
		 * = getCorrentSentence().indexOf(
		 * getLanguageToTransform().getStringInitializer(), last + 1); if (x ==
		 * -1) { break; } y = getCorrentSentence().indexOf(
		 * getLanguageToTransform().getStringInitializer(), x + 1); if (y == -1)
		 * { break; } temp[0] = subStringOfSentence( 0, x); temp[1] =
		 * subStringOfSentence( x, y + 1); temp[2] = subStringOfSentence( y +
		 * 1); setCurrentSentence( this.setString( temp[0]) + ' ' + setString(
		 * temp[1]) + ' ' + setString( temp[2])); last = y; }
		 */
	}
	
	private boolean isASeparatorChar(String input)
	{
		if (getLanguageToTransform().indexOfSeperatedCharacters( input) == -1
				|| input.length() != 1)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	/**
	 * @param input
	 * @return if input has not any SpecialChar -1 otherwise index of the char
	 *         in the input;
	 */
	private int indexOfSpecialChar(String input)
	{
		for (int i = 0; i < input.length(); i++)
		{
			if (getLanguageToTransform()
					.isInSpecialCharacter( input.charAt( i)) != -1)
			{
				return i;
			}
		}
		return -1;
		
	}
	
	private int indexOfSeparatorChar(String input)
	{
		for (int i = 0; i < input.length(); i++)
		{
			if (getLanguageToTransform().isInSeparatorCharacters(
					input.charAt( i)))
			{
				return i;
			}
		}
		return -1;
		
	}
	
	private int indexOfSeparatorChar(String input, int from)
	{
		for (int i = from; i < input.length(); i++)
		{
			if (getLanguageToTransform().isInSeparatorCharacters(
					input.charAt( i)))
			{
				return i;
			}
		}
		return -1;
		
	}
	
	private void endTheTag()
	{
		if (getOrderedIndexes().size() < 1)
		{
			System.out.println( "Error in endTheTag in Transformer");
			return;
		}
		int index = getOrderedIndexes().pop();
		addTag( new TagCloser( getTags().get( index).getType() ,
				getCurrentInnerLevel()));
	}
	
	private void whatTagImport()
	{
		getImports().add( getCurrentSentence());
	}
	
	private void whatAttribute(int transformableToJaxType)
	{
		// int indexArray[];
		// A
		
		switch (transformableToJaxType)
		{
			case -40:
				break;
			case -30:
				break;
			case -20:

				return;// ignored the implementation of method
				// break;
			case -10:
				endTheTag();
				break;
			case 0:
				whatAttributeClass();
				break;
			
			case 2:
				whatTagField();
				break;
			case 3:
				whatTagMethod();
				break;
			case 5:
				whatAtrributConstructor();
				break;
			// this.whatAttribute( 0);
			case 15:
				whatTagImport();
				break;
			case 16:
				whatTagPackage();
				break;
		}
	}
	
	private void whatAttributeClass()
	{
		final int implementType = 1;
		final int classType = 0;
		
		int wordNumber = 0;
		
		int indexArray[] = new int[JaxSyntax.getTag( classType)
				.getNumberOfAttrributes()];
		
		String stringArray[] = new String[JaxSyntax.getTag( classType)
				.getNumberOfAttrributes()];
		
		// access type
		indexArray[1] = JaxSyntax.isAStateOfAttribute( getSeperatedWord( last()
				+ wordNumber), classType, 1, 1);
		if (indexArray[1] == -1)
		{
			indexArray[1] = 3;
		}
		else
		{
			wordNumber++;
		}
		
		// abstraction
		indexArray[3] = JaxSyntax.isAStateOfAttribute( getSeperatedWord( last()
				+ wordNumber), classType, 3, 1);
		if (indexArray[3] == -1)
		{
			indexArray[3] = 2;
		}
		else
		{
			wordNumber++;
		}
		
		// tag type->already understood ->"class"
		wordNumber++;
		
		// name
		stringArray[0] = getSeperatedWord( ( this.last() + wordNumber ));
		wordNumber++;
		// addNonePrimitiveType( stringArray[0]);
		
		if (reachEndOfDifinition( this.last() + wordNumber + 1))
		{
			
			// extends
			stringArray[2] = new String( "null");
			// implements
			indexArray[4] = 0;
			// end
			indexArray[0] = -1;
			stringArray[1] = null;
			indexArray[2] = -1;
			stringArray[3] = null;
			stringArray[4] = null;
			
			// addNonePrimitiveType( type)
			addTag( new TagContainer( classType , indexArray , stringArray ,
					getCurrentInnerLevel()));
			
			return;
		}
		
		// extends
		if (getSeperatedWord( this.last() + wordNumber).equals(
				JaxSyntax.getAttribute( classType, 2).getName()))
		{
			wordNumber++;// because of word "extends"
			stringArray[2] = getSeperatedWord( this.last() + wordNumber);
			wordNumber++;
			
			addNonePrimitiveType( stringArray[2]);
		}
		else
		{
			stringArray[2] = "null";
		}
		
		if (reachEndOfDifinition( this.last() + wordNumber + 1))
		{
			
			// implements
			indexArray[4] = 0;
			
			// end
			indexArray[0] = -1;
			stringArray[1] = null;
			indexArray[2] = -1;
			stringArray[3] = null;
			stringArray[4] = null;
			
			// addNonePrimitiveType( type)
			addTag( new TagContainer( classType , indexArray , stringArray ,
					getCurrentInnerLevel()));
			
			return;
		}
		
		// implements
		String tempp1 = JaxSyntax.getAttribute( classType, 4).getName();
		String tempp2 = getSeperatedWord( this.last() + wordNumber);
		if (tempp1.startsWith( tempp2))
		// tempp1="implements-intefaces"
		{
			indexArray[4] = 1;// true
			// END
			stringArray[1] = null;
			stringArray[3] = null;
			stringArray[4] = null;
			
			indexArray[0] = -1;
			indexArray[2] = -1;
			// creating
			// add to this.tags
			addTag( new TagContainer( classType , indexArray , stringArray ,
					getCurrentInnerLevel()));
		}
		
		wordNumber++;
		
		while ( !reachEndOfDifinition( this.last() + wordNumber + 1))
		{
			int[] implementIndexesArray = { -1 };
			String[] implementNamesArray = { getSeperatedWord( this.last()
					+ wordNumber) };
			addTag( new TagContainer( implementType , implementIndexesArray ,
					implementNamesArray , getCurrentInnerLevel()));
			// addNonePrimitiveType( type)
			
			wordNumber++;// name of interface
			wordNumber++;// character ','
		}
		
		return;
		
	}
	
	private void whatAtrributConstructor()
	{
		final int constructorType = 5;
		final int parameterType = 4;
		int wordNumber = 0;
		int[] indexes = new int[JaxSyntax.getTag( constructorType)
				.getNumberOfAttrributes()];
		
		String[] names = new String[JaxSyntax.getTag( constructorType)
				.getNumberOfAttrributes()];
		
		// access Type
		indexes[0] = JaxSyntax.isAStateOfAttribute( getSeperatedWord( last()
				+ wordNumber), constructorType, 0, 1);
		
		if (indexes[0] == -1)
		{
			indexes[0] = 3;
		}
		else
		{
			wordNumber++;
		}
		
		// at end
		names[0] = null;
		// adding tag
		addTag( new TagContainer( constructorType , indexes , names ,
				getCurrentInnerLevel()));
		
		// ==========adding parameter tags=====
		
		wordNumber++;// name of constructor
		wordNumber++;// "("
		
		while ( !reachEndOfDifinition( last() + wordNumber + 2))// ->")"
		// +
		// "{"
		{
			int[] paramIndexes = new int[JaxSyntax.getTag( parameterType)
					.getNumberOfAttrributes()];
			
			String[] paramNames = new String[JaxSyntax.getTag( parameterType)
					.getNumberOfAttrributes()];
			
			String temp = getSeperatedWord( wordNumber + last());
			
			if (JaxSyntax.getAttribute( parameterType, 2).getName().equals(
					temp))
			{
				paramIndexes[2] = 1;
				wordNumber++;
				// defaultItems--;
			}
			else
			{
				paramIndexes[2] = 0;
			}
			
			// type of variable
			paramNames[1] = getSeperatedWord( last() + wordNumber);
			// TODO if needs to add to none primitive types
			// addNonePrimitiveType( paramNames[1]);
			wordNumber++;
			if (getSeperatedWord( last() + wordNumber).equals( "["))
			{
				paramNames[1] += "[]";
				wordNumber += 2;
			}
			
			// name of variable
			paramNames[0] = getSeperatedWord( last() + wordNumber);
			wordNumber++;
			if (getSeperatedWord( last() + wordNumber).equals( "[")
					&& !paramNames[1].endsWith( "]"))
			{
				paramNames[1] += "[]";
				wordNumber += 2;
			}
			
			// End
			paramNames[2] = null;
			paramIndexes[0] = -1;
			paramIndexes[1] = -1;
			
			// creating tag
			addTag( new TagContainer( parameterType , paramIndexes ,
					paramNames , getCurrentInnerLevel()));
			// ->"'"
			wordNumber++;
		}
		
	}
	
	private void whatTagField()
	{
		final int fieldType = 2;
		int[] indexesArray = new int[JaxSyntax.getTag( fieldType)
				.getNumberOfAttrributes()];
		String[] namesArray = new String[JaxSyntax.getTag( fieldType)
				.getNumberOfAttrributes()];
		
		int wordNumber = 0;// the index of word in separated words
		
		// access type
		indexesArray[4] = JaxSyntax.isAStateOfAttribute(
				getSeperatedWord( last() + wordNumber), fieldType, 4, 1);
		if (indexesArray[4] == -1)
		{
			indexesArray[4] = 3;
		}
		else
		{
			wordNumber++;
		}
		
		// static
		if (getSeperatedWord( wordNumber + last()).equals(
				JaxSyntax.getAttribute( fieldType, 2).getName()))
		{
			indexesArray[2] = 1;
			wordNumber++;
			
			// final
			if (getSeperatedWord( wordNumber + last()).equals(
					JaxSyntax.getAttribute( fieldType, 5).getName()))
			{
				indexesArray[5] = 1;// true
				wordNumber++;
			}
			else
			{
				indexesArray[5] = 0;
			}
		}
		// final
		else if (getSeperatedWord( wordNumber + last()).equals(
				JaxSyntax.getAttribute( fieldType, 5).getName()))
		{
			indexesArray[5] = 1;// true
			wordNumber++;
			
			// static
			if (getSeperatedWord( wordNumber + last()).equals(
					JaxSyntax.getAttribute( fieldType, 2).getName()))
			{
				indexesArray[2] = 1;
				wordNumber++;
			}
			else
			{
				indexesArray[2] = 0;
			}
		}
		else
		{
			indexesArray[2] = 0;// false
			indexesArray[5] = 0;
			
		}
		
		// type
		namesArray[1] = getSeperatedWord( wordNumber + last());
		wordNumber++;
		if ( !getLanguageToTransform().isItPrimitiveType( namesArray[1]))
		{
			getNonePrimitiveTypes().add( namesArray[1]);
			indexesArray[3] = 0;
		}
		else
		{
			indexesArray[3] = 1;
		}
		
		if (getSeperatedWord( last() + wordNumber).equals( "["))
		{
			namesArray[1] += "[]";
			wordNumber += 2;
		}
		
		// name
		namesArray[0] = getSeperatedWord( wordNumber + last());
		wordNumber++;
		if (getSeperatedWord( wordNumber + last()).equals( "[")
				&& !namesArray[1].endsWith( "]"))
		{
			namesArray[1] += "[]";
			wordNumber += 2;
		}
		
		// end
		indexesArray[0] = -1;
		indexesArray[1] = -1;
		namesArray[2] = null;
		namesArray[3] = null;
		namesArray[4] = null;
		namesArray[5] = null;
		
		// create
		
		addTag( new TagContainer( fieldType , indexesArray , namesArray ,
				getCurrentInnerLevel()));
		
	}
	
	private boolean reachEndOfDifinition(int index)
	{
		// Because of "(" or "{" or ")" OK?!
		if (index < current())
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	private boolean isClassTag()
	{
		for (int i = last(); i < current(); i++)
		{
			/*
			 * System.out.print( codeToTransfer.getArray().get( i) + "o" +
			 * codeToTransfer.getArray().get( i).length()); String s =
			 * codeToTransfer.getArray().get( i); ArrayList<String> sr = new
			 * ArrayList<String>(); sr.add( "class"); System.out.println(
			 * sr.get( 0).length() + "E"); if (s == sr.get( 0)) {
			 * isThereEqualSign = false; } if (JaxSyntax.getTag( 0).getName() ==
			 * "class") { isThereEqualSign = false; }
			 */
			if (getSeperatedWords().get( i).equals(
					JaxSyntax.getTag( 0).getName()))
			{
				return true;
			}
		}
		return false;
	}
	
	private void whatTagMethod()
	{
		final int paramTypeIndex = 4;
		int temp;
		int indexOfWord = 0;
		final int methodeIndex = 3;
		int[] indexesArray = new int[JaxSyntax.getTag( methodeIndex)
				.getNumberOfAttrributes()];
		String[] namesArray = new String[JaxSyntax.getTag( methodeIndex)
				.getNumberOfAttrributes()];
		// access type
		indexesArray[3] = JaxSyntax.isAStateOfAttribute(
				getSeperatedWord( last() + indexOfWord), methodeIndex, 3, 1);
		if (indexesArray[3] == -1)
		{
			indexesArray[3] = 3;
		}
		else
		{
			indexOfWord++;
		}
		
		// Temp ->final type
		temp = JaxSyntax.isAStateOfAttribute( getSeperatedWord( indexOfWord
				+ last()), methodeIndex, 2, 1);
		// static
		if (getSeperatedWord( indexOfWord + last()).equals(
				JaxSyntax.getAttribute( methodeIndex, 4).getName()))
		{
			indexesArray[4] = 1;
			indexOfWord++;
			
			// final
			
			temp = JaxSyntax.isAStateOfAttribute( getSeperatedWord( indexOfWord
					+ last()), methodeIndex, 2, 1);
			if (temp == -1)
			{
				indexesArray[2] = 2;
				// indexOfWord++;
			}
			else
			{
				indexesArray[2] = temp;
				indexOfWord++;
			}
		}
		// final
		else if (temp != -1)
		{
			indexesArray[2] = temp;
			indexOfWord++;
			
			// static
			if (getSeperatedWord( indexOfWord + last()).equals(
					JaxSyntax.getAttribute( methodeIndex, 4).getName()))
			{
				indexesArray[4] = 1;
				indexOfWord++;
			}
			else
			{
				indexesArray[4] = 0;
				// indexesArray[2]=2;
			}
		}
		else
		{
			indexesArray[4] = 0;// false
			indexesArray[2] = 2;// none
		}
		
		// return type
		namesArray[1] = getSeperatedWord( indexOfWord + last());
		indexOfWord++;
		if (getSeperatedWord( indexOfWord + last()).equals( "["))
		{
			namesArray[1] += "[]";
			indexOfWord += 2;
		}
		
		// name
		namesArray[0] = getSeperatedWord( indexOfWord + last());
		indexOfWord++;
		if (getSeperatedWord( indexOfWord + last()).equals( "[")
				&& !namesArray[1].endsWith( "]"))
		{
			namesArray[1] += "[]";
			indexOfWord += 2;
		}
		
		// end
		indexesArray[0] = -1;
		indexesArray[1] = -1;
		namesArray[2] = null;
		namesArray[3] = null;
		namesArray[4] = null;
		// create
		addTag( new TagContainer( methodeIndex , indexesArray , namesArray ,
				getCurrentInnerLevel()));
		
		// "("
		indexOfWord++;
		
		// parameters
		while ( !reachEndOfDifinition( last() + indexOfWord + 2))// 1-> ")" and
		// "{"
		{
			String paramTemp;
			int[] paramIndexes = new int[JaxSyntax.getTag( paramTypeIndex)
					.getNumberOfAttrributes()];
			String[] paramNames = new String[JaxSyntax.getTag( paramTypeIndex)
					.getNumberOfAttrributes()];
			
			paramTemp = getSeperatedWord( last() + indexOfWord);
			if (JaxSyntax.getAttribute( paramTypeIndex, 2).getName().equals(
					paramTemp))
			{
				indexOfWord++;
				paramIndexes[2] = 1;// true
			}
			else
			{
				paramIndexes[2] = 0;
			}
			
			// type
			paramNames[1] = getSeperatedWord( indexOfWord + last());
			indexOfWord++;
			if (getSeperatedWord( indexOfWord + last()).equals( "["))
			{
				paramNames[1] += "[]";
				indexOfWord += 2;
			}
			
			// name
			paramNames[0] = getSeperatedWord( indexOfWord + last());
			indexOfWord++;
			if (getSeperatedWord( last() + indexOfWord).equals( "[")
					&& !paramNames[1].endsWith( "]"))
			{
				paramNames[1] += "[]";
				indexOfWord += 2;
			}
			// end
			paramIndexes[0] = -1;
			paramIndexes[1] = -1;
			paramNames[2] = null;
			
			// create
			addTag( new TagContainer( paramTypeIndex , paramIndexes ,
					paramNames , getCurrentInnerLevel()));
			
			// because "," if there is another and ")" otherwise
			indexOfWord++;
			
		}
	}
	
	private boolean isATagEnds()
	{
		// int temp;
		if (getSeperatedWord( 0).equals( "}"))
		{
			if (isInABlockInMethodImplementation()
					&& isInAmethodOrConstructor())
			{
				// temp=getNumberOfBlocksInMethodImplementation();
				// setNumberOfBlocksInMethodImplementation( temp-1);
				return false;
			}
			else
			{
				return true;
			}
		}
		else
		{
			return false;
		}
	}
	
	private int typeOfLastTagNeededToClose()
	{
		if (getOrderedIndexes().size() - 1 < 0)
		{
			return -30;
		}
		
		return getTags().get(
				getOrderedIndexes().get( getOrderedIndexes().size() - 1))
				.getType();
	}
	
	private int lastTypeThatNeedsToClose()
	{
		if (getOrderedIndexes().size() == 0)
		{
			return -1000;
		}
		return getTag( getOrderedIndexes().get( getOrderedIndexes().size() - 1))
				.getType();
	}
	
	private boolean isAnImplementationExpands()
	{
		
		if (getSeperatedWord( getSeperatedWords().size() - 1).equals( "{")
				&& isInAmethodOrConstructor())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private boolean isAnEmplementationEnds()
	{
		if (isInABlockInMethodImplementation() && isInAmethodOrConstructor()
				&& getSeperatedWords().contains( "}"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private boolean isInAmethodOrConstructor()
	{
		if (lastTypeThatNeedsToClose() == 3 || lastTypeThatNeedsToClose() == 5)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private boolean isImport()
	{
		if (getSeperatedWord( 0).equals( "import"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private boolean isPackage()
	{
		if(getSeperatedWord( 0).equals( "package"))
		{
			return true;
		}
		else
		{
			return false;
		}
			
	}
	
	private  void whatTagPackage()
	{
		setPakage( getCurrentSentence());
	}
	
	private int whatTag()// in this implement the order of "for"s is important
	{
		boolean isThereEqualSign = false;
		int temp;
		
		// int typeLastTagNeededToClose = typeOfLastTagNeededToClose();
		if (isImport())
		{
			return 15;
		}
		else if(isPackage())
		{
			return 16;
		}
		if (isAnImplementationExpands())
		{
			temp = getNumberOfBlocksInMethodImplementation();
			setNumberOfBlocksInMethodImplementation( temp + 1);
			return -40;
		}
		if (isAnEmplementationEnds())
		{
			temp = getNumberOfBlocksInMethodImplementation();
			setNumberOfBlocksInMethodImplementation( temp - 1);
			return -40;
		}
		if (getSeperatedWord( 0).equals( ";"))
		{
			return -30;
		}
		// TODO initializers of inner classes???
		else if (isInABlockInMethodImplementation()
				|| ( !isATagEnds() && isInAmethodOrConstructor() ))
		{
			return -40;
		}
		else if (isATagEnds())
		{
			return -10;
		}
		else if (isClassTag())
		{
			return 0;
		}
		
		for (int i = last(); i < this.current(); i++)
		{
			if (getSeperatedWord( i).equals( "="))
			{
				isThereEqualSign = true;
			}
		}
		for (int i = this.last(); i < this.current(); i++)
		{
			
			if (getSeperatedWord( i).equals( "(") && !isThereEqualSign)
			{
				if (getSeperatedWord( last() + i - 1).equals(
						nameOfCurrentClass()))
				{
					return 5;
				}
				else
				{
					return 3;
				}
			}
		}
		if (isFieldTag())
		{
			return 2;
		}
		
		return -40;
		
	}
	
	private boolean isFieldTag()
	{
		for (int i = this.last(); i < this.current(); i++)
		{
			
			if (getSeperatedWord( i).equals( ";"))
			{
				return true;
			}
		}
		return false;
	}
	
	public void transform()
	{
		// the order is very important
		goNextLine();
		
		commentsToTag();
		putSpaceToCurrentSentence();
		setParserTemp( getCurrentSentence());
		sentenceParser();
		if (isSufficientWordsForATag())
		{
			// TODO
			transformWordsToTag();
			// at end
			setSufficientWordsForATag( false);
			setSeperatedWords( new ArrayList<String>());
		}
		while (hasNextLine() || getParserTemp().hasNext())
		{
			if ( !getParserTemp().hasNext())
			{
				if (hasNextLine())
				{
					goNextLine();
					increaseByOneLineNumber();
					commentsToTag();
					putSpaceToCurrentSentence();
					setParserTemp( getCurrentSentence());
				}
				else
				{
					break;
				}
			}
			
			sentenceParser();
			
			if (isSufficientWordsForATag())
			{
				// TODO call tag creators
				transformWordsToTag();
				
				// at end
				
				setSeperatedWords( new ArrayList<String>());
				setSufficientWordsForATag( false);
			}
			else
			{
				continue;
			}
			
		}
		
	}
	
	private void transformWordsToTag()
	{
		whatAttribute( whatTag());
	}
	
	private String getCurrentSentence()
	{
		return this.currentSentence;
	}
	
	private int getCurrentInnerLevel()
	{
		return this.getOrderedIndexes().size();
	}
	
	private void setCurrentSentence(String in)
	{
		this.currentSentence = in;
	}
	
	private String getStartBlockExpression()
	{
		return "/*";
	}
	
	private String getEndBlockExpression()
	{
		return "*/";
	}
	
	private String getStartLineCommentExpression()
	{
		return "//";
	}
	
	private void addTag(TransformableToJax tag)
	{
		if (tag.needToClose())
		{
			getOrderedIndexes().push( getTags().size());
		}
		
		// reset
		setNumberOfBlocksInInitializer( 0);
		setNumberOfBlocksInMethodImplementation( 0);
		// tag.
		getTags().add( tag);
	}
	
	private void setInBlockComment(boolean isInBlockComment)
	{
		this.isInBlockComment = isInBlockComment;
	}
	
	private boolean isInBlockComment()
	{
		return isInBlockComment;
	}
	
	private String setString(String input)
	{
		if (input == null)
		{
			return ( new String( "") );
		}
		else
		{
			return input;
		}
	}
	
	/**
	 * @return the languageToTransform
	 */
	private Language getLanguageToTransform()
	{
		return languageToTransform;
	}
	
	/**
	 * @param languageToTransform
	 *            the languageToTransform to set
	 */
	private void setLanguageToTransform(Language languageToTransform)
	{
		this.languageToTransform = languageToTransform;
	}
	
	/**
	 * @return the sufficientWordsForATag
	 */
	private boolean isSufficientWordsForATag()
	{
		return sufficientWordsForATag;
	}
	
	/**
	 * @param sufficientWordsForATag
	 *            the sufficientWordsForATag to set
	 */
	private void setSufficientWordsForATag(boolean sufficientWordsForATag)
	{
		this.sufficientWordsForATag = sufficientWordsForATag;
	}
	
	/**
	 * @return the parserTemp
	 */
	private Scanner getParserTemp()
	{
		return parserTemp;
	}
	
	/**
	 * @param parserTemp
	 *            the parserTemp to set
	 */
	private void setParserTemp(Scanner parserTemp)
	{
		this.parserTemp = parserTemp;
	}
	
	private void setParserTemp(String sentence)
	{
		this.parserTemp = new Scanner( sentence);
	}
	
	private void addToSeparatedWords(String in)
	{
		this.seperatedWords.add( in);
	}
	
	private int getLengthOfSentence()
	{
		return getCurrentSentence().length();
	}
	
	private String subStringOfSentence(int beginIndex)
	{
		return getCurrentSentence().substring( beginIndex);
	}
	
	private String subStringOfSentence(int beginIndex, int endIndex)
	{
		return getCurrentSentence().substring( beginIndex, endIndex);
	}
	
	/**
	 * @return the seperatedWords
	 */
	private ArrayList<String> getSeperatedWords()
	{
		return seperatedWords;
	}
	
	private String getSeperatedWord(int index)
	{
		return getSeperatedWords().get( index);
	}
	
	/**
	 * @param seperatedWords
	 *            the seperatedWords to set
	 */
	private void setSeperatedWords(ArrayList<String> seperatedWords)
	{
		this.seperatedWords = seperatedWords;
	}
	
	public TransformableToJax getTag(int index)
	{
		return getTags().get( index);
	}
	
	/**
	 * @return the tags
	 */
	private ArrayList<TransformableToJax> getTags()
	{
		return tags;
	}
	
	/**
	 * @param tags
	 *            the tags to set
	 */
	private void setTags(ArrayList<TransformableToJax> tags)
	{
		this.tags = tags;
	}
	
	/**
	 * @return the lineNumber
	 */
	private int getLineNumber()
	{
		return lineNumber;
	}
	
	/**
	 * @param lineNumber
	 *            the lineNumber to set
	 */
	private void setLineNumber(int lineNumber)
	{
		this.lineNumber = lineNumber;
	}
	
	private void increaseByOneLineNumber()
	{
		setLineNumber( getLineNumber() + 1);
	}
	
	private int current()
	{
		return getSeperatedWords().size();
	}
	
	private int last()
	{
		return 0;
	}
	
	private String nameOfCurrentClass()
	{
		int indexOfTag = 0;
		for (int i = getOrderedIndexes().size() - 1; i >= 0; i--)
		{
			indexOfTag = getOrderedIndexes().get( i);
			if (getTags().get( indexOfTag).getType() == 0)
			{
				return getTags().get( indexOfTag).getName();
			}
		}
		System.out
				.println( "Error in name of current class func at Transformer class");
		return "name_";
	}
	
	/**
	 * @return the orderedIndexes
	 */
	private Stack<Integer> getOrderedIndexes()
	{
		return orderedIndexes;
	}
	
	/**
	 * @param orderedIndexes
	 *            the orderedIndexes to set
	 */
	private void setOrderedIndexes(Stack<Integer> orderedIndexes)
	{
		this.orderedIndexes = orderedIndexes;
	}
	
	/**
	 * @return the nonePrimitiveTypes
	 */
	public ArrayList<String> getNonePrimitiveTypes()
	{
		return nonePrimitiveTypes;
	}
	
	public String getNonePrimitiveTypr(int index)
	{
		return getNonePrimitiveTypes().get( index);
	}
	
	/**
	 * @param nonePrimitiveTypes
	 *            the nonePrimitiveTypes to set
	 */
	private void setNonePrimitiveTypes(ArrayList<String> nonePrimitiveTypes)
	{
		this.nonePrimitiveTypes = nonePrimitiveTypes;
	}
	
	private void addNonePrimitiveType(String type)
	{
		getNonePrimitiveTypes().add( type);
	}
	
	/**
	 * @return the isInABlockInMethodImplementation
	 */
	private boolean isInABlockInMethodImplementation()
	{
		return ( this.numberOfBlocksInMethodImplementation > 0 );
	}
	
	/**
	 * @return the numberOfBlocksInInitializer
	 */
	private int getNumberOfBlocksInInitializer()
	{
		return numberOfBlocksInInitializer;
	}
	
	/**
	 * @return the numberOfBlocksInMethodImplementation
	 */
	private int getNumberOfBlocksInMethodImplementation()
	{
		return numberOfBlocksInMethodImplementation;
	}
	
	/**
	 * @param numberOfBlocksInInitializer
	 *            the numberOfBlocksInInitializer to set
	 */
	private void setNumberOfBlocksInInitializer(int numberOfBlocksInInitializer)
	{
		this.numberOfBlocksInInitializer = numberOfBlocksInInitializer;
	}
	
	/**
	 * @param numberOfBlocksInMethodImplementation
	 *            the numberOfBlocksInMethodImplementation to set
	 */
	private void setNumberOfBlocksInMethodImplementation(
			int numberOfBlocksInMethodImplementation)
	{
		this.numberOfBlocksInMethodImplementation = numberOfBlocksInMethodImplementation;
	}
	
	/**
	 * @return the imports
	 */
	public ArrayList<String> getImports()
	{
		return imports;
	}
	
	/**
	 * @param imports
	 *            the imports to set
	 */
	private void setImports(ArrayList<String> imports)
	{
		this.imports = imports;
	}

	/**
	 * @return the pakage
	 */
	public String getPakage()
	{
		return pakage;
	}

	/**
	 * @param pakage the pakage to set
	 */
	private void setPakage(String pakage)
	{
		this.pakage = pakage;
	}
	
}
