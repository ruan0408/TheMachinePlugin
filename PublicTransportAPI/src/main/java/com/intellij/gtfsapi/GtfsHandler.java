package com.intellij.gtfsapi;

import com.intellij.utils.APIConnectionException;
import com.intellij.utils.SmartSampaDir;
import org.onebusaway.gtfs.impl.GtfsDaoImpl;
import org.onebusaway.gtfs.serialization.GtfsReader;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

/**
 * Created by ruan0408 on 11/03/2016.
 */
public class GtfsHandler {

    private static final SmartSampaDir smartSampaDir = SmartSampaDir.getInstance();
    private static final int UPDATE_INTERVAL_IN_DAYS = 3;
    private static final long UPDATE_INTERVAL_IN_MILLIS = Duration.ofDays(UPDATE_INTERVAL_IN_DAYS).toMillis();
    private static final String GTFS_DIR_NAME = "gtfs-sp";
    private static final String GTFS_PATH = smartSampaDir.getPath()+"/"+GTFS_DIR_NAME;

    private GtfsDownloader gtfsDownloader;

    public GtfsHandler(String login, String password) {
        gtfsDownloader = new GtfsDownloader(login, password);
    }

    public GtfsDaoImpl getGtfsAcessor() {
        ensureGtfsIsUsable();
        return loadGtfsAndGetDataAcessor();
    }

    private GtfsDaoImpl loadGtfsAndGetDataAcessor() {
        try {
            GtfsReader gtfsReader = new GtfsReader();
            GtfsDaoImpl acessor = new GtfsDaoImpl();
            gtfsReader.setInputLocation(new File(GTFS_PATH));
            gtfsReader.setEntityStore(acessor);
            gtfsReader.run();
            return acessor;
        } catch (IOException e) {
            e.printStackTrace();
            throw new APIConnectionException("The "+GTFS_DIR_NAME+" was not found");
        }
    }

    private void ensureGtfsIsUsable() {
        try {
            if (!smartSampaDir.hasSubDir(GTFS_DIR_NAME) || gtfsDirNeedsUpdate())
            gtfsDownloader.downloadToDir(GTFS_PATH);
        } catch (IOException e) {
            e.printStackTrace();
            if (!smartSampaDir.hasSubDir(GTFS_DIR_NAME))
                throw new APIConnectionException("Couldn't download the GTFS files");
        }
    }

    private boolean gtfsDirNeedsUpdate() {
        File gtfsDir = new File(GTFS_PATH);
        long timeElapsedSinceModificationInMillis =
                System.currentTimeMillis() - gtfsDir.lastModified();

        return timeElapsedSinceModificationInMillis > UPDATE_INTERVAL_IN_MILLIS;
    }
}
