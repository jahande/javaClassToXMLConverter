package fileReadWrite;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class FilePrinter
{
	private String				address;
	private FileOutputStream	createdFile;
	private OutputStreamWriter	creater;
	
	public FilePrinter(String address)
	{
		super();
		setAddress(  address);
		try
		{
			setCreatedFile( new FileOutputStream( address));
		}
		catch (Throwable t)
		{
			System.out.println( t.getMessage());
		}
		setCreater( new OutputStreamWriter( getCreatedFile()));
	}
	
	public void print(String add)
	{
		try
		{
			getCreater().write( add);
			
		}
		catch (Throwable t)
		{
			System.out.println(t.getMessage());
		}
	}
	
	public void close()
	{
		try
		{
			getCreater().flush();
		}
		catch (Throwable t)
		{
			System.out.println(t.getMessage());
		}
	}
	
	/**
	 * @return the address
	 */
	private String getAddress()
	{
		return address;
	}
	
	/**
	 * @return the createdFile
	 */
	private FileOutputStream getCreatedFile()
	{
		return createdFile;
	}
	
	/**
	 * @param address
	 *            the address to set
	 */
	private void setAddress(String address)
	{
		this.address = address;
	}
	
	/**
	 * @param createdFile
	 *            the createdFile to set
	 */
	private void setCreatedFile(FileOutputStream createdFile)
	{
		this.createdFile = createdFile;
	}
	
	/**
	 * @return the creater
	 */
	private OutputStreamWriter getCreater()
	{
		return creater;
	}
	
	/**
	 * @param creater
	 *            the creater to set
	 */
	private void setCreater(OutputStreamWriter creater)
	{
		this.creater = creater;
	}
}
