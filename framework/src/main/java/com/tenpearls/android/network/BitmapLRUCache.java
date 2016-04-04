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
package com.tenpearls.android.network;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Subclass of {@link LruCache}. Used in implementing {@link ImageLoader} for
 * Volley library.
 * 
 * @author 10Pearls
 * 
 */
public class BitmapLRUCache extends LruCache<String, Bitmap> implements ImageCache {

	/**
	 * Gets the default lru cache size.
	 * 
	 * @return the default lru cache size
	 */
	public static int getDefaultLruCacheSize () {

		final int maxMemory = (int) (Runtime.getRuntime ().maxMemory () / 1024);
		final int cacheSize = maxMemory / 8;

		return cacheSize;
	}

	/**
	 * Instantiates a new bitmap lru cache.
	 */
	public BitmapLRUCache () {

		this (getDefaultLruCacheSize ());
	}

	/**
	 * Instantiates a new bitmap lru cache.
	 * 
	 * @param sizeInKiloBytes the size in kilo bytes
	 */
	public BitmapLRUCache (int sizeInKiloBytes) {

		super (sizeInKiloBytes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.util.LruCache#sizeOf(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	protected int sizeOf (String key, Bitmap value) {

		return value.getRowBytes () * value.getHeight () / 1024;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.android.volley.toolbox.ImageLoader.ImageCache#getBitmap(java.lang
	 * .String)
	 */
	@Override
	public Bitmap getBitmap (String url) {

		return get (url);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.android.volley.toolbox.ImageLoader.ImageCache#putBitmap(java.lang
	 * .String, android.graphics.Bitmap)
	 */
	@Override
	public void putBitmap (String url, Bitmap bitmap) {

		put (url, bitmap);
	}
}