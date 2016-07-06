package fileReadWrite;
import java.io.File;
import java.util.Scanner;


/**
 * its possible to want to read from a (large) string; so this class must not
 * expand more and for this type of source we can create another class if there
 * was need to
 * 
 * @author Ruholla
 * 
 */
public class FileReader implements SourceReader
{
	private Scanner	fileScanner;
	
	public FileReader(String add) throws Throwable
	{
		
		try
		{
			this.fileScanner = new Scanner(new File(add));
			
		}
		catch (Throwable t)
		{
			System.out.println(t.getMessage());
			throw t;
		}
		
	}
	
	public FileReader(File f) throws Throwable
	{
		try
		{
			this.fileScanner = new Scanner(f);
		}
		catch (Throwable t)
		{
			System.out.println(t.getMessage());
			throw t;
		}
	}
	
	protected FileReader()
	{
	}
	
	@Override
	public boolean hasNext()
	{
		return fileScanner.hasNext();
	}
	
	/**
	 * its duty is to separate the words of the file and put "\t\t"
	 */
	/*
	 * public String nextString() { try { return fileScanner.next(); }
	 * catch(Throwable t) { System.out.println(t.getMessage()+'\n' +
	 * t.fillInStackTrace()); return null; } }
	 */
	@Override
	public String nextLine()
	{
		return fileScanner.nextLine();
	}
}
