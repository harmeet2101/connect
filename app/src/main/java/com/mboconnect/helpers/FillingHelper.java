package com.mboconnect.helpers;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by vishal.chhatwani on 10/8/2015.
 */
public class FillingHelper {

    public static boolean isExternalStorageWritable() {

        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public static File getStorageDir(Context context, String directoryName) {

        // Get the directory for the app's private pictures directory.
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), directoryName);
        if (!file.mkdirs()) {
            //    Logger.error ("ZUBIE", "Directory not created");
        }
        return file;
    }

    public static void writeToFile(String data, String fileName, Context context) {

        if (!isExternalStorageWritable())
            return;

        File file = new File(getStorageDir(context, "test"), fileName);

        try {
            FileWriter filewriter = new FileWriter(file, false);
            BufferedWriter out = new BufferedWriter(filewriter);
            out.write(data);
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
