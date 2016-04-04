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

import java.util.Hashtable;

import android.content.Context;
import android.graphics.Typeface;

import com.tenpearls.android.R;

/**
 * This class serves as a gateway to fonts in assets directory. It also
 * maintains an internal font cache to avoid heavy memory allocation each time
 * same font is requested.
 *
 * @author 10Pearls
 */
public class FontUtility {

    /**
     * The font cache.
     */
    private static Hashtable<String, Typeface> fontCache = new Hashtable<String, Typeface>();

    /**
     * Gets font {@link Typeface} from assets directory.
     *
     * @param fontPathFromAssets The path to font file, relative to assets
     *                           directory
     * @param context            A valid context
     * @return Typeface object
     */
    public static Typeface getFontFromAssets(String fontPathFromAssets, Context context) {

        Typeface tf = fontCache.get(fontPathFromAssets);

        if (tf == null) {

            try {
                tf = Typeface.createFromAsset(context.getAssets(), fontPathFromAssets);
            } catch (Exception e) {
                return null;
            }

            fontCache.put(fontPathFromAssets, tf);
        }

        return tf;
    }

    private static Typeface getTypeface(Context context, String fontPathFromAssets, Typeface tf) {

        if (tf == null) {

            try {
                tf = Typeface.createFromAsset(context.getAssets(), fontPathFromAssets);
            } catch (Exception e) {
                return null;
            }

            fontCache.put(fontPathFromAssets, tf);
        }


        return tf;
    }

    public static Typeface getBoldFontFromAssets(Context context) {

        String fontPathFromAssets = context.getString(R.string.bold_font);
        Typeface tf = fontCache.get(fontPathFromAssets);


        return getTypeface(context, fontPathFromAssets, tf);
    }

    public static Typeface getRegularFontFromAssets(Context context) {

        String fontPathFromAssets = context.getString(R.string.regular_font);
        Typeface tf = fontCache.get(fontPathFromAssets);

        return getTypeface(context, fontPathFromAssets, tf);
    }

    public static Typeface getThinFontFromAssets(Context context) {

        String fontPathFromAssets = context.getString(R.string.thin_font);
        Typeface tf = fontCache.get(fontPathFromAssets);

        return getTypeface(context, fontPathFromAssets, tf);
    }


}
