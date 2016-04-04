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

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * <p>
 * A gateway to the <a
 * href="https://android.googlesource.com/platform/frameworks/volley">Volley</a>
 * library. Exposes a singleton interface to manage network calls, cache and
 * image fetching.
 * </p>
 * 
 * @author 10Pearls
 * 
 */
public class VolleyManager {

	/** The s instance. */
	private static VolleyManager sInstance = null;

	/** The m request queue. */
	private RequestQueue         mRequestQueue;

	/** The m image loader. */
	private ImageLoader          mImageLoader;

	/** The m cache. */
	private BitmapLRUCache       mCache;

	/**
	 * Instantiates a new volley manager.
	 * 
	 * @param context the context
	 * @param cacheSize the cache size
	 */
	private VolleyManager (Context context, int cacheSize) {

		mRequestQueue = Volley.newRequestQueue (context);
		mCache = new BitmapLRUCache (cacheSize);
		mImageLoader = new ImageLoader (this.mRequestQueue, new ImageLoader.ImageCache () {

			public void putBitmap (String url, Bitmap bitmap) {

				mCache.put (url, bitmap);
			}

			public Bitmap getBitmap (String url) {

				return mCache.get (url);
			}
		});
	}

	/**
	 * Provides a single, global point of access for the instance of this class.
	 * 
	 * @return A singleton object
	 */
	public static VolleyManager getInstance () {

		if (sInstance == null) {
			throw new IllegalStateException ("Did you call VolleyManager.initialize()?");
		}

		return sInstance;
	}

	/**
	 * Initializes the {@link RequestQueue}, {@link ImageLoader} and.
	 * 
	 * @param context A valid context.
	 * @param cacheSize The required cache size in Kilobytes.
	 *            {@link BitmapLRUCache}.
	 */
	public static void initialize (Context context, int cacheSize) {

		if (sInstance == null) {
			sInstance = new VolleyManager (context, cacheSize);
		}
	}

	/**
	 * Returns the network requests queue. All network calls must be dispatched
	 * from this single queue.
	 * 
	 * @return A single request queue that will be used across the whole
	 *         application.
	 */
	public RequestQueue getRequestQueue () {

		return this.mRequestQueue;
	}

	/**
	 * Returns the image loader. All image loading calls must be done through
	 * this.
	 * 
	 * @return A single image loader that will be used across the whole
	 *         application.
	 */
	public ImageLoader getImageLoader () {

		return this.mImageLoader;
	}
}
