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
package com.tenpearls.android.utilities;

import java.util.ArrayList;

/**
 * This class is responsible for determining collection types or states. For
 * example, a collection is null or empty.
 * 
 * @author 10Pearls
 * 
 */
public class CollectionUtility {

	/**
	 * Determines if an {@link ArrayList} is {@code null} or empty.
	 * 
	 * @param arrayList The array list
	 * @return {@code true} if the provided string is {@code null} or empty.
	 *         {@code false} otherwise
	 */
	public static boolean isEmptyOrNull (ArrayList<?> arrayList) {

		if (arrayList == null)
			return true;

		return (arrayList.size () <= 0);
	}
}
