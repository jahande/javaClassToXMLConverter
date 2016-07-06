package jaxComponents;
/**
 * each attribute has some states or has a state name
 */

/**
 * @author Ruholla
 *
 */
public class JaxSyntaxAttribute {
	
	private final String name;//name of the attribute for example access-type, type,...
	private final String[] states;
	private final boolean hasName;
	
	
	public JaxSyntaxAttribute(String name, boolean hasName, String[] states) {
		// TODO Auto-generated constructor stub
		this.name=name;
		this.states=states;
		this.hasName = hasName;
	}
	
	
	
	/*public void addState(String stateName)
	{
		states.add(stateName);
	}

	public void setName(String name) {
		this.name = name;
	}*/

	public String getName() {
		return name;
	}
	public String getState(int index) {
		return states[index];
	}
	public int getNumberOfStates()
	{
		return states.length;
	}

	public boolean hasName() {
		return hasName;
	}

}
