package gitlet;

// TODO: any imports you need here

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.HashMap;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */
    Commit(String log, String parentCommit, HashMap<String, String> blobs) {
        _log = log;
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        _date = sdf.format(date);
        _parent = parentCommit;
        _blobs = blobs;
        if (_parent == null) {
            _hash = Utils.sha1(log, _date);
        } else {
            String blobsha = null;
            ArrayList<String> blobarray = new ArrayList<String>();
            for (String ugh : blobs.values()) {
                if (blobsha == null) {
                    blobsha = Utils.sha1(ugh);
                } else {
                    blobsha = Utils.sha1(blobsha, ugh);
                }
            }
            _hash = Utils.sha1(log, _date, parentCommit, blobsha);
        }
        try {
            File next = new File(".gitlet/commits/" + _hash +".ser");
            FileOutputStream fileOut = new FileOutputStream(next);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(this);
        } catch (IOException e) {
            return;
        }

    }

    String getHash() {
        return _hash;
    }

    String getFirstTwo() {
        return _hash.substring(0, 2);
    }

    String getParent() {
        if (_parent == null) {
            return null;
        }
        return _parent;
    }

    HashMap<String, String> getBlobs() {
        return _blobs;
    }

    String getMessage() {
        return _log;
    }
    void getDate() {
        System.out.println(_date);
    }
    void getLog() {
        System.out.println("===");
        System.out.println("Commit " + getHash());
        getDate();
        System.out.println(getMessage());
        System.out.println("");
        return;
    }


    File newFile;
    String _log;
    HashMap<String, String> _blobs;
    String _date;
    String _parent;
    String _hash;
}




}
