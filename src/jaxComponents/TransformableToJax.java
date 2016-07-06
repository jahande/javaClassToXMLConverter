package jaxComponents;
/**
 * 
 */

/**
 * @author Ruholla
 *
 */
public interface TransformableToJax {
	
	/**
	 * create beginning format of the jax tag
	 * @return
	 * the transformed syntax
	 */
	public String toString();
	/**
	 * create end format of the jax tag 
	 * @return
	 * the transformed syntax if the tag needs to end
	 * else null
	 */
	//public String end();
	/**
	 * @return
	 * type number
	 */
	public int getType();
	
	/**
	 * 
	 * @return
	 * name of the tag
	 */
	public String getName();
	
	public boolean needToClose();

}
