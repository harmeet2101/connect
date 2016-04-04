/*
 * 10Pearls - Android Framework v1.0
 * 
 * The contributors of the framework are responsible for releasing 
 * new patches and make modifications to the code base. Any bug or
 * suggestion encountered while using the framework should be
 * communicated to any of the contributors.
 * 
 * Contributors:
 * 
 * Ali Mehmood       - ali.mehmood@tenpearls.com
 * Arsalan Ahmed     - arsalan.ahmed@tenpearls.com
 * M. Azfar Siddiqui - azfar.siddiqui@tenpearls.com
 * Syed Khalilullah  - syed.khalilullah@tenpearls.com
 */
package com.tenpearls.android.entities.base;

/**
 * Any entity which is to be mapped from an API response, must inherit from this
 * class.
 * 
 * @author 10Pearls
 * 
 */
public abstract class BaseWebServiceEntity{

	/**
	 * Transformation of JSON response to a typed object should be done inside
	 * this method.
	 * 
	 * @param json JSON representation of the object
	 */
	public abstract void deserializeFromJSON (String json);
}
