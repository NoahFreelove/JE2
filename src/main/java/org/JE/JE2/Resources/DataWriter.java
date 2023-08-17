package org.JE.JE2.Resources;

import org.JE.JE2.IO.Filepath;
import org.JE.JE2.IO.Logging.Errors.JE2Error;
import org.JE.JE2.IO.Logging.Logger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class DataWriter {

    /**
     * Saves array of string to zip file
     * @param array Array of data. Each string will be saved to its own text file with the name of its index in the array
     * @param output_ABS Absolute filepath to zip entry
     */
    public static void saveArrayToZip(String[] array, Filepath output_ABS) {
        try {
            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(output_ABS.getPath(false)));
            for (int i = 0; i < array.length; i++) {
                ZipEntry zipEntry = new ZipEntry(i + ".txt");
                zipOut.putNextEntry(zipEntry);
                zipOut.write(array[i].getBytes());
                zipOut.closeEntry();
            }
            zipOut.close();
        } catch (IOException e) {
            Logger.log(new JE2Error(e));
        }
    }
}
