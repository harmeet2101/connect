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
package com.tenpearls.android.components.pulltorefresh;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.util.Log;
import android.view.View;

// TODO: Auto-generated Javadoc
/**
 * The Class InstanceCreationUtils.
 */
class InstanceCreationUtils {

	/** The Constant LOG_TAG. */
	private static final String                LOG_TAG                             = "InstanceCreationUtils";

	/** The Constant VIEW_DELEGATE_CONSTRUCTOR_SIGNATURE. */
	private static final Class<?>[]            VIEW_DELEGATE_CONSTRUCTOR_SIGNATURE = new Class[] {};

	/** The Constant TRANSFORMER_CONSTRUCTOR_SIGNATURE. */
	private static final Class<?>[]            TRANSFORMER_CONSTRUCTOR_SIGNATURE   = new Class[] {};

	/** The Constant BUILT_IN_DELEGATES. */
	private static final HashMap<Class, Class> BUILT_IN_DELEGATES;
	static {
		BUILT_IN_DELEGATES = new HashMap<Class, Class> ();
		BUILT_IN_DELEGATES.put (AbsListViewDelegate.SUPPORTED_VIEW_CLASS, AbsListViewDelegate.class);
		BUILT_IN_DELEGATES.put (WebViewDelegate.SUPPORTED_VIEW_CLASS, WebViewDelegate.class);
	}

	/**
	 * Gets the built in view delegate.
	 * 
	 * @param view the view
	 * @return the built in view delegate
	 */
	static PullToRefreshAttacher.ViewDelegate getBuiltInViewDelegate (final View view) {

		final Set<Map.Entry<Class, Class>> entries = BUILT_IN_DELEGATES.entrySet ();
		for (final Map.Entry<Class, Class> entry : entries) {
			if (entry.getKey ().isInstance (view)) {
				return InstanceCreationUtils.newInstance (view.getContext (), entry.getValue (), VIEW_DELEGATE_CONSTRUCTOR_SIGNATURE, null);
			}
		}

		// Default is the ScrollYDelegate
		return InstanceCreationUtils.newInstance (view.getContext (), ScrollYDelegate.class, VIEW_DELEGATE_CONSTRUCTOR_SIGNATURE, null);
	}

	/**
	 * Instantiate view delegate.
	 * 
	 * @param <T> the generic type
	 * @param context the context
	 * @param className the class name
	 * @param arguments the arguments
	 * @return the t
	 */
	static <T> T instantiateViewDelegate (Context context, String className, Object[] arguments) {

		try {
			Class<?> clazz = context.getClassLoader ().loadClass (className);
			return newInstance (context, clazz, VIEW_DELEGATE_CONSTRUCTOR_SIGNATURE, arguments);
		}
		catch (Exception e) {
			Log.w (LOG_TAG, "Cannot instantiate class: " + className, e);
		}
		return null;
	}

	/**
	 * Instantiate transformer.
	 * 
	 * @param <T> the generic type
	 * @param context the context
	 * @param className the class name
	 * @param arguments the arguments
	 * @return the t
	 */
	static <T> T instantiateTransformer (Context context, String className, Object[] arguments) {

		try {
			Class<?> clazz = context.getClassLoader ().loadClass (className);
			return newInstance (context, clazz, TRANSFORMER_CONSTRUCTOR_SIGNATURE, arguments);
		}
		catch (Exception e) {
			Log.w (LOG_TAG, "Cannot instantiate class: " + className, e);
		}
		return null;
	}

	/**
	 * New instance.
	 * 
	 * @param <T> the generic type
	 * @param context the context
	 * @param clazz the clazz
	 * @param constructorSig the constructor sig
	 * @param arguments the arguments
	 * @return the t
	 */
	private static <T> T newInstance (Context context, Class clazz, Class[] constructorSig, Object[] arguments) {

		try {
			Constructor<?> constructor = clazz.getConstructor (constructorSig);
			return (T) constructor.newInstance (arguments);
		}
		catch (Exception e) {
			Log.w (LOG_TAG, "Cannot instantiate class: " + clazz.getName (), e);
		}
		return null;
	}

}
