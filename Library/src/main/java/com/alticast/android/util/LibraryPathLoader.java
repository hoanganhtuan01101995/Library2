/*
 *  Copyright (c) 2012 Alticast Corp.
 *  All rights reserved. http://www.alticast.com/
 *
 *  This software is the confidential and proprietary information of
 *  Alticast Corp. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into
 *  with Alticast.
 */
package com.alticast.android.util;

import java.io.File;
import java.util.ArrayList;


public class LibraryPathLoader {

    private static final Log LOG = Log.createLog("LibraryPathLoader");

    private ArrayList<String> libraryPath = new ArrayList<String>();

    public LibraryPathLoader(String libPath) {

        if (libPath != null) {
            for (String path : libPath.split(File.pathSeparator)) {
                libraryPath.add(cleanupPath(path));
            }
        }

        String systemLibPath = System.getProperty("java.library.path", ".");
        if (systemLibPath != null && systemLibPath.length() > 0) {
            for (String path : systemLibPath.split(File.pathSeparator)) {
                libraryPath.add(cleanupPath(path));
            }
        }
    }

    private String cleanupPath(String path) {
        return path.endsWith(File.separator) ? path : (path + File.separator);
    }

    public String findLibrary(String libName) {

        if (Log.DBG) {
            LOG.printDbg("findLibrary() - libName = " + libName);
        }
        String fileName = System.mapLibraryName(libName);
        if (Log.DBG) {
            LOG.printDbg("findLibrary() - fileName = " + fileName);
        }

        for (String path : libraryPath) {
            String pathName = path+fileName;
            if (Log.DBG) {
                LOG.printDbg("findLibrary() - pathName = " + pathName);
            }
            File file = new File(pathName);

            if (file.exists()) {
                if (Log.DBG) {
                    LOG.printDbg("findLibrary() - exists");
                }
                return pathName;
            }
        }
        return null;
    }

}
