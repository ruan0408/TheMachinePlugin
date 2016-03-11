package com.intellij.gtfsapi;

import com.intellij.utils.APIConnectionException;

import java.io.IOException;

/**
 * Created by ruan0408 on 11/03/2016.
 */
public class GtfsDownloader {

    private static final String DOWNLOAD_GTFS_SCRIPT_PATH =
            GtfsAPI.class.getClassLoader().getResource("downloadGtfs.py").getPath();

    private String login;
    private String password;

    public GtfsDownloader(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public void downloadToDir(String pathToDir) {
        try {
            startDownloadProcessOnPath(pathToDir);
        } catch (Exception e) {
            e.printStackTrace();
            throw APIConnectionException.throwGTFSConnectionException();
        }
    }

    private void startDownloadProcessOnPath(String path)
            throws IOException, InterruptedException {
        ProcessBuilder downloaderProcessBuilder = new ProcessBuilder("python",
                DOWNLOAD_GTFS_SCRIPT_PATH, login, password, path);
        Process downloaderProcess = downloaderProcessBuilder.start();
        downloaderProcess.waitFor();
    }
}
