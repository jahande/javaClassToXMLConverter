package parser;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import others.JavaLanguage;
import others.Language;

import fileReadWrite.FilePrinter;
import fileReadWrite.FileReader;
import fileReadWrite.SourceReader;

import jaxComponents.CommentContainer;
import jaxComponents.TransformableToJax;

public class Parser
{
	private static char							addressSeparator;
	private static int							systemType;
	private static String						projectPath;
	// 1->windows 2->Linux
	private static ArrayList<String>			addresses;
	private static Transformer					currenFile;
	private static ArrayList<String>			fileNamesThatTransformed;
	private static ArrayList<ArrayList<String>>	fileMustBeTransformed;
	private static ArrayList<String>			packages;
	private static ArrayList<ArrayList<String>>	imports;
	static
	{
		setPackages( new ArrayList<String>());
		setImports( new ArrayList<ArrayList<String>>());
		fileNamesThatTransformed = new ArrayList<String>();
		fileMustBeTransformed = new ArrayList<ArrayList<String>>();
		setAddresses( new ArrayList<String>());
		
		setSystemType( 1);
		
		switch (getSystemType())
		{
			case 1:
				setAddressSeparator( '\\');
				break;
			case 2:
				setAddressSeparator( '/');
				
			default:
				break;
		}
		
	}
	
	private static int sizeOfRootArrayOfMustTranform()
	{
		return getFileMustBeTransformed().size();
	}
	
	private static int sizeOfInnerArrayOfMustTransform(int indexOfInnerArray)
	{
		return getFileMustBeTransformed().get( indexOfInnerArray).size();
	}
	
	private static void temp()
	{
		parsCurrentFile();
		setProjectPath();
		while (true)
		{
			
			String t = "", fileAddress;
			boolean finished = true;
			for (int i = 0; i < sizeOfRootArrayOfMustTranform(); i++)
			{
				if ( !finished)
				{
					break;
				}
				for (int j = 0; j < sizeOfInnerArrayOfMustTransform( i); j++)
				{
					t = getFileMustBeTransformed( i, j);
					if ( !isItTransformed( t))
					{
						finished = false;
						break;
					}
				}
			}
			if (finished)
			{
				return;
			}
			fileAddress = getAddresses().get( 0);
			fileAddress = cutFolderAddress( fileAddress) + t + ".java";
			getAddresses().add( fileAddress);
			
			parsCurrentFile();
		}
	}
	
	private static String cutFolderAddress(String first)
	{
		int temp = first.lastIndexOf( getAddressSeparator());
		return first.substring( 0, temp + 1);
	}
	
	private static String getAddressOfFileToBeCreat(String firstAddress)
	{
		int dotindex = firstAddress.lastIndexOf( '.');
		return firstAddress.substring( dotindex) + "jax";
	}
	
	private static String getTargetName(String baseAddress)
	{
		// TODO
		return "l";
	}
	
	private static String cutFileName(String address)
	{
		switch (getSystemType())
		{
			default:
			case 1:
				return address.substring( address
						.lastIndexOf( getAddressSeparator()) + 1, address
						.lastIndexOf( '.'));
		}
	}
	
	private static void addArrayOfFileMustBeTransformAndTransformed()
	{
		getFileMustBeTransformed()
				.add( getCurrenFile().getNonePrimitiveTypes());
		getFileNamesThatTransformed().add(
				cutFileName( getAddresses().get( getAddresses().size() - 1)));
	}
	
	private static void parsCurrentFile()
	{
		setCurrenFile( new Transformer( getLastAddress() , JavaLanguage
				.getInstance()));
		getCurrenFile().transform();
		
		addArrayOfFileMustBeTransformAndTransformed();
		
		addImportsAndPackages();
		
		fileCreator();
		
		print();
	}
	
	private static void addImportsAndPackages()
	{
		getImports().add( getCurrenFile().getImports());
		getPackages().add( getCurrenFile().getPakage());
	}
	
	private static String changeAddressFormatJavaToJax(String firstAddress)
	{
		return firstAddress.substring( 0, firstAddress.lastIndexOf( '.') + 1)
				+ "jax";
	}
	
	private static void fileCreator()
	{
		String address = getLastAddress();
		FilePrinter file = new FilePrinter(
				changeAddressFormatJavaToJax( address));
		for (int i = 0; i < getCurrenFile().getNumberOfTags(); i++)
		{
			file.print( getCurrenFile().getTag( i).toString() + "\r\n");
		}
		file.close();
	}
	
	private static String getLastAddress()
	{
		return getAddresses().get( getAddresses().size() - 1);
	}
	
	private static void print()
	{
		System.out.println( getCurrenFile().getPakage());
		for (int i = 0; i < getCurrenFile().getImports().size(); i++)
		{
			System.out.println( getCurrenFile().getImports().get( i));
		}
		for (int i = 0; i < getCurrenFile().getNumberOfTags(); i++)
		{
			System.out.println( getCurrenFile().getTag( i).toString());
		}
		for (int i = 0; i < getCurrenFile().getNumberOfNonePrimitiveTypes(); i++)
		{
			System.out.println( getCurrenFile().getNonePrimitiveTypr( i));
		}
		System.out
				.println( "==================bayad mishodan======================");
		for (int i = 0; i < getFileMustBeTransformed().size(); i++)
		{
			for (int j = 0; j < getFileMustBeTransformed().get( i).size(); j++)
			{
				System.out.println( getFileMustBeTransformed( i, j));
			}
		}
		System.out.println( "=======================shodan==================");
		for (int i = 0; i < getFileNamesThatTransformed().size(); i++)
		{
			System.out.println( getFileNamesThatTransformed().get( i));
		}
	}
	
	private static void fileCreator(Transformer t, FilePrinter file)
	{
		// FileWriter file =new FileWriter( address)
		for (int i = 0; i < t.getNumberOfTags(); i++)
		{
			file.print( t.getTag( i).toString() + '\n');
		}
		file.close();
	}
	
	private static void fileCreator(String t, String address)
	{
		FilePrinter file = new FilePrinter( address);
		
		file.print( t + '\n' + t);
		file.print( "\n");
		file.print( t);
		
		file.close();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO must removed after test
		// ---------its just for test
		args = new String[1];
		args[0] = "D:\\testing\\We.java";
		// ------------end test
		
		getAddresses().add( args[0]);
		
		temp();
		// parsCurrentFile();
		// fileCreator( "salam", "D:\\hehe.jax");
	}
	
	/**
	 * @return the fileNamesThatTransformed
	 */
	private static ArrayList<String> getFileNamesThatTransformed()
	{
		return fileNamesThatTransformed;
	}
	
	/**
	 * @param fileNamesThatTransformed
	 *            the fileNamesThatTransformed to set
	 */
	private static void setFileNamesThatTransformed(
			ArrayList<String> fileNamesThatTransformed)
	{
		Parser.fileNamesThatTransformed = fileNamesThatTransformed;
	}
	
	/**
	 * @return the fileMustBeTransformed
	 */
	private static ArrayList<ArrayList<String>> getFileMustBeTransformed()
	{
		return fileMustBeTransformed;
	}
	
	/**
	 * @param fileMustBeTransformed
	 *            the fileMustBeTransformed to set
	 */
	private static void setFileMustBeTransformed(
			ArrayList<ArrayList<String>> fileMustBeTransformed)
	{
		Parser.fileMustBeTransformed = fileMustBeTransformed;
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return getFileMustBeTransformed().get( x).get( y);
	 */
	private static String getFileMustBeTransformed(int x, int y)
	{
		return getFileMustBeTransformed().get( x).get( y);
	}
	
	/**
	 * search in just one field
	 * 
	 * @param key
	 * @return
	 */
	private boolean isInFieldFilesThatNeedsToTransform(String key)
	{
		boolean mustBetransforIt = false;
		for (int i = 0; i < getFileMustBeTransformed().size(); i++)
		{
			mustBetransforIt = getFileMustBeTransformed().get( i)
					.contains( key);
			if (mustBetransforIt)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * search in just one field
	 * 
	 * @param key
	 * @return
	 * 
	 */
	private static boolean isItTransformed(String key)
	{
		return getFileNamesThatTransformed().contains( key);
	}
	
	/**
	 * @return the currenFile
	 */
	private static Transformer getCurrenFile()
	{
		return Parser.currenFile;
	}
	
	/**
	 * @param currenFile
	 *            the currenFile to set
	 */
	private static void setCurrenFile(Transformer currenFile)
	{
		Parser.currenFile = currenFile;
	}
	
	/**
	 * @return the addresses
	 */
	private static ArrayList<String> getAddresses()
	{
		return addresses;
	}
	
	/**
	 * @param addresses
	 *            the addresses to set
	 */
	private static void setAddresses(ArrayList<String> addresses)
	{
		Parser.addresses = addresses;
	}
	
	/**
	 * @return the systemType
	 */
	private static int getSystemType()
	{
		return systemType;
	}
	
	/**
	 * @param systemType
	 *            the systemType to set
	 */
	private static void setSystemType(int systemType)
	{
		Parser.systemType = systemType;
	}
	
	/**
	 * @return the addressSeparator
	 */
	private static char getAddressSeparator()
	{
		return addressSeparator;
	}
	
	/**
	 * @param addressSeparator
	 *            the addressSeparator to set
	 */
	private static void setAddressSeparator(char addressSeparator)
	{
		Parser.addressSeparator = addressSeparator;
	}
	
	/**
	 * @return the packages
	 */
	private static ArrayList<String> getPackages()
	{
		return packages;
	}
	
	/**
	 * @return the imports
	 */
	private static ArrayList<ArrayList<String>> getImports()
	{
		return imports;
	}
	
	/**
	 * @param packages
	 *            the packages to set
	 */
	private static void setPackages(ArrayList<String> packages)
	{
		Parser.packages = packages;
	}
	
	/**
	 * @param imports
	 *            the imports to set
	 */
	private static void setImports(ArrayList<ArrayList<String>> imports)
	{
		Parser.imports = imports;
	}
	
	/**
	 * @return the projectPath
	 */
	private static String getProjectPath()
	{
		return projectPath;
	}
	
	/**
	 * @param projectPath
	 *            the projectPath to set
	 */
	private static void setProjectPath(String projectPath)
	{
		Parser.projectPath = projectPath;
	}
	
	private static void setProjectPath()
	{
		int numberOfDots = 0,temp=0;
		String finalAddress=getLastAddress();
		if (getPackages().get( 0) == null)
		{
			setProjectPath( getAddresses().get( 0));
		}
		else
		{
			numberOfDots++;// because its not default package
			for (int i = 0; i < getPackages().get( 0).length(); i++)
			{
				if (getPackages().get( 0).charAt( i) == '.')
				{
					numberOfDots++;
				}
			}
			for (int i = numberOfDots; i > 0; i--)
			{
				temp=finalAddress.lastIndexOf( getAddressSeparator());
				finalAddress = finalAddress.substring( 0, temp);
			}
			
		}
	}
	
}
