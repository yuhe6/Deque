package gitlet;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */
    private String HEAD = "master";
    private S stage;
    private File workingDir;

    public void Repository() {
        workingDir = new File(System.getProperty("user.dir"));
        String pathToHead = ".gitlet/branches/HEAD.txt";
        if (new File(pathToHead).exists()) {
            HEAD = U.readStringFromFile(pathToHead);
        }
        String pathToStage = ".gitlet/staging/stage.txt";
        if (new File(pathToStage).exists()) {
            stage = U.deserialize(pathToStage, S.class);
        }
    }

    /** The current working directory. */
    //public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    //public static final File GITLET_DIR = join(CWD, ".gitlet");

    /* TODO: fill in the rest of this class. */

    public static void init() {
        File dir = new File(".gitlet");
        if (dir.exists()) {
            System.out.println("a gitlet version-control system already exists in the current directory.");
            return;
        } else {
            dir.mkdir();
            File next = new File(".gitlet/heads/master.ser");
            File currentBranch = new File(".gitlet/current");
            File heads = new File(".gitlet/heads");
            File currentname = new File(".gitlet/current/master.ser");
            File staged = new File(".gitlet/staged");
            File commits = new File(".gitlet/commits");
            File unstaged = new File(".gitlet/unstaged");
            File blobs = new File(".gitlet/blobs");
            try {
                heads.mkdir();
                staged.mkdir();
                commits.mkdir();
                unstaged.mkdir();
                currentBranch.mkdir();
                blobs.mkdir();
                Commit initial = new Commit("initial commit", null, null);
                FileOutputStream fileOut = new FileOutputStream(next);
                ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
                objectOut.writeObject(initial);
                Files.copy(next.toPath(), currentname.toPath());
            } catch (IOException e) {
                return;
            }
        }}
    public static void add(String name) {
        File addedfile = new File(name);
        File next = new File(".gitlet/staged/" + name);
        File saverm = new File(".gitlet/unstaged/" + name);
        if (!addedfile.exists()) {
            System.out.println("File does not exist.");
            return;
        }
        if (saverm.exists()) {
            saverm.delete();
        }
        String hash = Utils.sha1(name, Utils.readContents(addedfile));
        if (getCurrentCommit().getBlobs() != null
                && getCurrentCommit().getBlobs().containsValue(hash)) {
            return;
        }
        Utils.writeContents(next, Utils.readContents(addedfile));
    }

    private File findFile(String fileName, File dir) {
        File[] fileList = dir.listFiles();
        for (File f : fileList) {
            if (f.getName().equals(fileName)) {
                return f;
            }
        }
        return null;

    }

    String getCurrentCommitBranch() {
        String branchh = null;
        File currentB = new File(".gitlet/current");
        for (File file: currentB.listFiles()) {
            branchh = file.getName();
        }
        return branchh;
    }
    /** Fetches the current Commit RETURNS commit. */
    Commit getCurrentCommit() {
        String branchh = null;
        Commit prev = null;
        File currentB = new File(".gitlet/current");
        for (File file: currentB.listFiles()) {
            branchh = file.getName();
        }
        return deserializeCommit(".gitlet/heads/" + branchh);
    }
    /** RETURNS the Commit specified by HASH. */
    Commit getCommit(String hash) {
        Commit newCommit = null;
        File gettin = new File(".gitlet/commits/" + hash);
        if (gettin.exists()) {
            return deserializeCommit(".gitlet/commits/" + hash);
        } else {
            return deserializeCommit(".gitlet/commits/" + hash + ".ser");
        }
    }
    /** Updates the Branch with the commit NEWT. */
    void updateBranch(Commit newt) {
        String nextBranch = null;
        Commit theNext = null;
        File parent = new File(".gitlet/current");
        for (File file : parent.listFiles()) {
            nextBranch = file.getName();
        }
        try {
            File getHead = new File(".gitlet/heads/" + nextBranch);
            FileOutputStream fieOut = new FileOutputStream(getHead);
            ObjectOutputStream obetOut = new ObjectOutputStream(fieOut);
            obetOut.writeObject(newt);
        } catch (IOException e) {
            System.out.println("Didn't work.");
        }
        try {
            File hideous = new File(".gitlet/current/" + nextBranch);
            FileOutputStream ieOut = new FileOutputStream(hideous);
            ObjectOutputStream betOut = new ObjectOutputStream(ieOut);
            betOut.writeObject(newt);
        } catch (IOException e) {
            System.out.println("Didn't work.");
        }
    }

    /** Creates a new BRANCHNAME. */
    void branch(String branchName) {
        File curr = new File(".gitlet/current");
        String path = null;
        File newBranch = new File(".gitlet/heads/" + branchName + ".ser");
        for (File file: curr.listFiles()) {
            path = file.getName();
        }
        if (newBranch.exists()) {
            System.out.println("A branch with that name already exists.");
            return;
        } else {
            Commit currCom = deserializeCommit(".gitlet/current/" + path);
            try {
                FileOutputStream fieOut = new FileOutputStream(newBranch);
                ObjectOutputStream obetOut = new ObjectOutputStream(fieOut);
                obetOut.writeObject(currCom);
            } catch (IOException e) {
                System.out.println("p");
            }
        }
    }

    /** Deserializes and RETURNS commit according to the PATH. */
    Commit deserializeCommit(String path) {
        Commit newCommit = null;
        File pathy = new File(path);
        try {
            FileInputStream f = new FileInputStream(pathy);
            ObjectInputStream objn = new ObjectInputStream(f);
            newCommit = (Commit) objn.readObject();
        } catch (IOException e) {
            String msg = "IOException while loading.";
            System.out.println(msg);
        } catch (ClassNotFoundException e) {
            String msg = "ClassNotFoundException while loading myCat.";
            System.out.println(msg);
        }
        return newCommit;
    }
    /** Deserializes and RETURNS a BLOB according to PATH. */
    Blob deserializeBlob(String path) {
        Blob newBlob = null;
        File pathy = new File(path);
        try {
            FileInputStream f = new FileInputStream(pathy);
            ObjectInputStream objn = new ObjectInputStream(f);
            newBlob = (Blob) objn.readObject();
        } catch (IOException e) {
            String msg = "IOException while loading.";
            return null;
        } catch (ClassNotFoundException e) {
            String msg = "ClassNotFoundException while loading myCat.";
            System.out.println(msg);
        }
        return newBlob;

    }

    public static void commit(String message) {
        HashMap<String, String> blobs = new HashMap<String, String>();
        File staging = new File(".gitlet/staged");
        File unstage = new File(".gitlet/unstaged");
        File parent = new File(".gitlet/current");
        File workingD = new File(".");
        Commit newCommit = null;
        String par = getCurrentCommit().getHash();
        Commit currentCommit = getCurrentCommit();
        if (staging.listFiles().length == 0
                && unstage.listFiles().length == 0) {
            System.out.println("No changes added to the commit.");
            return;
        } else {
            for (File file : staging.listFiles()) {
                Blob next = new Blob(file.getName(), Utils.readContents(file));
                blobs.put(file.getName(), next.getHash());
                file.delete();
            }
            for (File file : unstage.listFiles()) {
                file.delete();
            }
            for (File file : workingD.listFiles()) {
                if (getCurrentCommit().getMessage().compareTo("initial commit")
                        == 0) {
                    break;
                }
                if ((currentCommit.getBlobs().get(file.getName()) != null)
                        && (blobs.get(file.getName()) == null)) {
                    File unstaged = new File(".gitlet/unstaged/"
                            + file.getName());
                    if (unstaged.exists()) {
                        unstaged.delete();
                        continue;
                    } else {
                        blobs.put(file.getName(),
                                currentCommit.getBlobs().get(file.getName()));
                    }
                }
            }
            newCommit = new Commit(message, par, blobs);
            updateBranch(newCommit);
        }
    }

    public static void checkout(String... args) {
        if (args.length == 2) {
            String branchName = args[1];
            if (!(new File(".gitlet/branches/" + branchName + ".txt")).exists()) {
                System.out.println("No such branch exists.");
                return;
            }
            String branchPath = ".gitlet/branches/" + branchName + ".txt";
            String headPath = ".gitlet/branches/" + HEAD + ".txt";
            String newCommitID = U.readStringFromFile(branchPath);
            Commit newCommit = U.deserialize(".gitlet/commits/"
                    + newCommitID + ".txt", Commit.class);
            Commit curr = getCurrentCommit();
            HashMap<String, String> newBlobs = newCommit.getBlobs();
            HashMap<String, String> headBlobs = curr.getBlobs();
            ArrayList<File> fileList = new ArrayList<>();
            for (File f : workingDir.listFiles()) {
                if (f.getName().endsWith(".txt")) {
                    fileList.add(f);
                }
            }
            Commit commitToCheckout = newCommit;
            Commit currCommit = getCurrentCommit();
            for (File f : fileList) {
                if (!currCommit.getBlobs().containsKey(f.getName())
                        && commitToCheckout.getBlobs().containsKey(f.getName())) {
                    System.out.println("There is an untracked file in the way; "
                            + "delete it or add it first.");
                    return;
                }
            }
            ArrayList<String> fileNames = new ArrayList<>(commitToCheckout.getBlobs().keySet());
            for (File f : fileList) {
                if (!commitToCheckout.getBlobs().containsKey(f.getName())
                        && currCommit.getBlobs().containsKey(f.getName())) {
                    Utils.restrictedDelete(f);
                }
            }
            for (String f : fileNames) {
                String blobHash = commitToCheckout.getBlobs().get(f);
                String blobPath = workingDir.getPath() + "/.gitlet/blobs/" + blobHash + ".txt";
                File newFile = new File(blobPath);
                byte[] blobBytes = readContents(newFile);
                Utils.writeContents(new File(f), blobBytes);
            }
            stage.clear();
            U.storeObjectToFile(stage, workingDir.getPath()
                    + "/.gitlet/staging/stage.txt");
            U.writeStringToFile(branchName, ".gitlet/branches/HEAD.txt", false);
        } else if (args.length == 3) {
            String fileName = args[2];
            Commit headCommit = getCurrentCommit();
            HashMap<String, String> commitMap = headCommit.getBlobs();
            if (!commitMap.containsKey(fileName)) {
                System.out.println("File does not exist in that commit.");
                return;
            }
            if ((new File(workingDir.getPath() + fileName)).exists()) {
                Utils.restrictedDelete(workingDir.getPath() + fileName);
            }
            File blob = new File(workingDir.getPath() + "/.gitlet/blobs/"
                    + headCommit.getBlobs().get(fileName) + ".txt");
            byte[] storeRFile = readContents(blob);
            File newFile = new File(workingDir.getPath(), fileName);
            Utils.writeContents(newFile, storeRFile);
        } else if (args.length == 4) {
            String commitID = args[1];
            String fileName = args[3];
            String[] possibleCommit = new File(".gitlet/commits").list();
            for (String identifier : possibleCommit) {
                if (identifier.contains(commitID)) {
                    commitID = identifier;
                    commitID = commitID.substring(0, commitID.length() - 4);
                    break;
                }
            }
            Commit currCommit = U.deserialize(".gitlet/commits/"
                    + commitID + ".txt", Commit.class);
            if (currCommit == null) {
                System.out.println("No commit with that id exists.");
            } else if (!currCommit.getBlobs().containsKey(fileName)) {
                System.out.println("File does not exist in that commit.");
            } else {
                if ((new File(workingDir.getPath() + fileName)).exists()) {
                    Utils.restrictedDelete(workingDir.getPath() + fileName);
                }
                File newFile = new File(workingDir.getPath(), fileName);
                File blob = new File(workingDir.getPath() + "/.gitlet/blobs/"
                        + currCommit.getBlobs().get(fileName) + ".txt");
                byte[] storeRFile = readContents(blob);
                Utils.writeContents(newFile, storeRFile);
            }
        }
    }

    public static void log() {
        Commit thelog = getCurrentCommit();
        Commit parent = null;
        int i = 1;
        if (thelog.getMessage().compareTo("initial commit") == 0) {
            i += 1;
        } else {
            parent = getCommit(thelog.getParent());
        }
        if (thelog.getMessage().compareTo("initial commit") == 0) {
            return;
        }
        while (thelog.getMessage().compareTo("initial commit") != 0) {
            System.out.println("===");
            System.out.println("Commit " + thelog.getHash());
            thelog.getDate();
            System.out.println(thelog.getMessage() + "");
            System.out.println("");
            if (i > 1) {
                return;
            }
            thelog = parent;
            if (parent.getParent() == null) {
                break;
            }
            parent = getCommit(thelog.getParent());
        }
        if (thelog.getMessage().compareTo("initial commit") == 0) {
            System.out.println("===");
            System.out.println("Commit " + thelog.getHash());
            thelog.getDate();
            System.out.println(thelog.getMessage());
            System.out.println("");
            return;
        }

}}

