package gitlet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.io.File;

/**Repo.
 * @author jakekim*/
class Repo implements Serializable {
    /**head.*/
    private String head;
    /**head.*/
    private HashMap<String, String> branch;
    /**head.*/
    private HashMap<String, Blob> staging;
    /**head.*/
    private HashMap<String, Blob> untracked;
    /**head.*/
    Repo() {
        Commit initial = Commit.initialize();
        File firstcommit = new
                File(".gitlet/commits/" + initial.getintegerID());
        Utils.writeContents(firstcommit, Utils.serialize(initial));
        branch = new HashMap<String, String>();
        head = "master";
        branch.put(head, initial.getintegerID());
        staging = new HashMap<String, Blob>();
        untracked = new HashMap<String, Blob>();

    }
    /**head.
     * @param args args*/
    public void add(String [] args) {
        File newmade = new File(args[1]);
        if (!newmade.exists()) {
            Utils.message("File does not exist.");
            System.exit(0);
        }
        String newmadesha = Utils.sha1(Utils.readContentsAsString(newmade));
        File namefile = new File(".gitlet/commits/" + branch.get(head));
        Commit prev = Utils.readObject(namefile, Commit.class);
        HashMap<String, Blob> prevfiles = prev.getblobs();
        Blob potentialblob = new Blob(args[1], newmadesha);
        File potentialblobfile = new File(".gitlet/staging/" + newmadesha);
        if (prev == null || prevfiles == null) {
            staging.put(args[1], potentialblob);
            Utils.writeContents(potentialblobfile,
                    Utils.readContentsAsString(newmade));
        } else if (!prevfiles.containsKey(args[1])) {
            staging.put(args[1], potentialblob);
            Utils.writeContents(potentialblobfile,
                    Utils.readContentsAsString(newmade));
        } else if (prevfiles.containsKey(args[1])
                &&
                !prevfiles.get(args[1]).getshacode()
                        .equals(potentialblob.getshacode())) {
            staging.put(args[1], potentialblob);
            Utils.writeContents(potentialblobfile,
                    Utils.readContentsAsString(newmade));
        } else {
            if (potentialblobfile.exists()) {
                staging.remove(args[1]);
            }
        }
        if (untracked.containsKey(args[1])) {
            untracked.remove(args[1]);
        }
    }
    /**head.
     * @param message message*/
    public void commit(String message) {
        if (message.equals("") || message == null) {
            Utils.message("Please enter a commit message.");
            System.exit(0);
        }
        File namefile5 = new File(".gitlet/commits/" + branch.get(head));
        Commit prevcommit = Utils.readObject(namefile5, Commit.class);
        HashMap<String, Blob> filestobeadded = new HashMap<String, Blob>();
        if (prevcommit.getblobs() == null) {
            if (staging.size() != 0 || untracked.size() != 0) {
                for (String addedname : staging.keySet()) {
                    filestobeadded.put(addedname, staging.get(addedname));
                }

                for (String untrackname : untracked.keySet()) {
                    filestobeadded.remove(untrackname);
                }
            } else {
                Utils.message("No changes added to the commit.");
                System.exit(0);
            }
        } else {
            filestobeadded = prevcommit.getblobs();
            if (staging.size() != 0 || untracked.size() != 0) {
                for (String addedname : staging.keySet()) {
                    filestobeadded.put(addedname, staging.get(addedname));
                }

                for (String untrackname : untracked.keySet()) {
                    filestobeadded.remove(untrackname);
                }
            } else {
                Utils.message("No changes added to the commit.");
                System.exit(0);
            }
        }
        Commit [] parent = new Commit [] {prevcommit};
        Commit addedcommit = new Commit(message, parent, filestobeadded);
        File newcommitpath = new
                File(".gitlet/commits/" + addedcommit.getintegerID());
        Utils.writeObject(newcommitpath, addedcommit);

        staging = new HashMap<String, Blob>();
        untracked = new HashMap<String, Blob>();
        branch.put(head, addedcommit.getintegerID());

    }

    /**
     * commit.
     * @param message message
     * @param args args
     */
    public void commit(String message, Commit [] args) {
        if (message.equals("") || message == null) {
            Utils.message("Please enter a commit message.");
            System.exit(0);
        }
        File namefile8 = new File(".gitlet/commits/" + branch.get(head));
        Commit prevcommit = Utils.readObject(namefile8, Commit.class);
        HashMap<String, Blob> filestobeadded = new HashMap<String, Blob>();
        if (prevcommit.getblobs() == null) {
            if (staging.size() != 0 || untracked.size() != 0) {
                for (String addedname : staging.keySet()) {
                    filestobeadded.put(addedname, staging.get(addedname));
                }

                for (String untrackname : untracked.keySet()) {
                    filestobeadded.remove(untrackname);
                }
            } else {
                Utils.message("No changes added to the commit.");
                System.exit(0);
            }
        } else {
            filestobeadded = prevcommit.getblobs();
            if (staging.size() != 0 || untracked.size() != 0) {
                for (String addedname : staging.keySet()) {
                    filestobeadded.put(addedname, staging.get(addedname));
                }

                for (String untrackname : untracked.keySet()) {
                    filestobeadded.remove(untrackname);
                }
            } else {
                Utils.message("No changes added to the commit.");
                System.exit(0);
            }
        }
        Commit [] parent = new Commit [] {prevcommit};
        Commit addedcommit = new Commit(message, args, filestobeadded);
        File newcommitpath = new
                File(".gitlet/commits/" + addedcommit.getintegerID());
        Utils.writeObject(newcommitpath, addedcommit);

        staging = new HashMap<String, Blob>();
        untracked = new HashMap<String, Blob>();
        branch.put(head, addedcommit.getintegerID());
    }
    /**head.
     * @param args args*/
    public void rm(String [] args) {
        File checkfile = new File(args[1]);
        File currentcommitdir = new File(".gitlet/commits/" + branch.get(head));
        Commit currentcommit = Utils.readObject(currentcommitdir, Commit.class);

        if (!checkfile.exists()
                && !currentcommit.getblobs().containsKey(args[1])) {
            Utils.message("No such file exists");
            System.exit(0);
        }
        if (staging.containsKey(args[1])) {
            staging.remove(args[1]);
            if (currentcommit.getblobs() != null
                    && currentcommit.getblobs().containsKey(args[1])) {
                untracked.put(args[1], currentcommit.getblobs().get(args[1]));
                File blobsdeletefile = new File(args[1]);
                if (blobsdeletefile.exists()) {
                    Utils.restrictedDelete(blobsdeletefile);
                }
                return;
            }
            return;
        }
        if (currentcommit.getblobs() != null
                && currentcommit.getblobs().containsKey(args[1])) {
            untracked.put(args[1], currentcommit.getblobs().get(args[1]));
            File blobsdeletefile = new File(args[1]);
            if (blobsdeletefile.exists()) {
                Utils.restrictedDelete(blobsdeletefile);
            }
            return;
        }

        Utils.message("No reason to remove the file.");
        System.exit(0);
    }
    /**head.
   * @param args args*/
    public void log(String [] args) {
        File currentcomdir = new File(".gitlet/commits/" + branch.get(head));
        Commit currentcom = Utils.readObject(currentcomdir, Commit.class);
        Commit [] parent = currentcom.getparents();
        System.out.println("===");
        System.out.println("commit" + " " + currentcom.getintegerID());
        System.out.println("Date:" + " " + currentcom.getdate());
        System.out.println(currentcom.getmessage());
        System.out.println();
        while (parent != null) {
            System.out.println("===");
            System.out.println("commit" + " " + parent[0].getintegerID());
            System.out.println("Date:" + " " + parent[0].getdate());
            System.out.println(parent[0].getmessage());
            System.out.println();
            parent = parent[0].getparents();
        }
    }
    /**head.
     * @param args args*/
    public void firstcheckout(String [] args) {
        File currentcomdir = new File(".gitlet/commits/" + branch.get(head));
        Commit currentcom = Utils.readObject(currentcomdir, Commit.class);
        if (currentcom.getblobs().containsKey(args[2])) {
            File workinrepo = new File(args[2]);
            File filedir = new
                    File(".gitlet/staging/"
                    + currentcom.getblobs().get(args[2]).getshacode());
            Utils.writeContents(workinrepo,
                    Utils.readContentsAsString(filedir));
        } else {
            Utils.message("File does not exist in that commit.");
            System.exit(0);
        }
    }
    /**head.
     * @param args args*/
    public void seccheckout(String [] args) {
        String shaid = args[1];
        shaid = getfullfromsmall(shaid);
        File commitdir = new File(".gitlet/commits");
        for (File dir : commitdir.listFiles()) {
            Commit check = Utils.readObject(dir, Commit.class);
            if (check.getintegerID().equals(shaid)) {
                if (check.getblobs() != null
                        && check.getblobs().containsKey(args[3])) {
                    File workinrepo = new File(args[3]);
                    File filedir = new File(".gitlet/staging/"
                            + check.getblobs().get(args[3]).getshacode());
                    Utils.writeContents(workinrepo,
                            Utils.readContentsAsString(filedir));
                    return;
                } else {
                    Utils.message("File does not exist in that commit.");
                    System.exit(0);
                }
            }
        }
        Utils.message("No commit with that id exists.");
        System.exit(0);

    }

    /**
     * Error check.
     * @param args args
     */
    public void errorcheck(String [] args) {
        String branchname = args[1];
        if (!branch.containsKey(branchname)) {
            Utils.message("No such branch exists.");
            System.exit(0);
        }
        if (branchname.equals(head)) {
            Utils.message("No need to checkout the current branch.");
            System.exit(0);
        }
    }
    /**head.
     * @param args args*/
    public void thircheckout(String [] args) {
        String branchname = args[1];
        errorcheck(args);
        File currentcomdir = new File(".gitlet/commits/" + branch.get(head));
        Commit currentcom = Utils.readObject(currentcomdir, Commit.class);
        File current = new File(System.getProperty("user.dir"));
        for (File fileslisted : current.listFiles()) {
            if (currentcom.getblobs() == null) {
                if (notimportantfile(fileslisted)) {
                    Utils.message("There"
                            + " is an untracked file in the "
                            + "way; delete it or add it first.");
                    System.exit(0);
                }
            } else {
                if (notimportantfile(fileslisted)) {
                    if (!currentcom.getblobs().containsKey
                            (fileslisted.getName())
                            && !staging.containsKey(fileslisted.getName())) {
                        Utils.message("There is an untracked file "
                                + "in the way; delete it or add it first.");
                        System.exit(0);
                    }
                }
            }
        }
        File commitdir = new File(".gitlet/commits/" + branch.get(branchname));
        Commit branchcommit = Utils.readObject(commitdir, Commit.class);
        File current1 = new File(System.getProperty("user.dir"));
        if (branchcommit.getblobs() == null) {
            for (File fileslisted : current1.listFiles()) {
                if (notimportantfile(fileslisted)) {
                    Utils.restrictedDelete(fileslisted);
                }
            }
        } else {
            for (File fileslisted : current1.listFiles()) {
                if (!branchcommit.getblobs().containsKey(fileslisted.getName())
                        && notimportantfile(fileslisted)) {
                    Utils.restrictedDelete(fileslisted);
                }
            }
        }

        if (branchcommit.getblobs() != null) {
            for (Blob files : branchcommit.getblobs().values()) {
                File newfiles = new File(files.getname());
                File oldfiles = new
                        File(".gitlet/staging/" + files.getshacode());
                Utils.writeContents(newfiles,
                        Utils.readContentsAsString(oldfiles));
            }
        }
        staging = new HashMap<String, Blob>();
        untracked = new HashMap<String, Blob>();
        head = branchname;

    }

    /**
     * global.
     * @param args args
     */
    public void globallog(String [] args) {
        File commitdir = new File(".gitlet/commits");
        for (File dir : commitdir.listFiles()) {
            Commit check = Utils.readObject(dir, Commit.class);
            System.out.println("===");
            System.out.println("commit" + " " + check.getintegerID());
            System.out.println("Date:" + " " + check.getdate());
            System.out.println(check.getmessage());
            System.out.println();
        }

    }

    /**
     * find.
     * @param args args
     */
    public void find(String [] args) {
        String message = args[1];
        File commitdir = new File(".gitlet/commits");
        int count = 0;
        for (File dir : commitdir.listFiles()) {
            Commit check = Utils.readObject(dir, Commit.class);
            if (check.getmessage().equals(message)) {
                System.out.println(check.getintegerID());
                count++;
            }
        }
        if (count == 0) {
            Utils.message("Found no commit with that message.");
            System.exit(0);
        }

    }

    /**
     * status.
     * @param args args
     */
    public void status2(String [] args) {
        System.out.println("=== Branches ===");
        String [] listofnames = new String [branch.size()];
        int count = 0;
        for (String name : branch.keySet()) {
            listofnames[count] = name;
            count++;
        }
        Arrays.sort(listofnames);
        for (String a : listofnames) {
            if (a.equals(head)) {
                System.out.println("*" + a);
            } else {
                System.out.println(a);
            }
        }
        System.out.println();
        System.out.println("=== Staged Files ===");
        Object [] stagingname = staging.keySet().toArray();
        Arrays.sort(stagingname);
        for (Object b: stagingname) {
            System.out.println(b);
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        Object [] a = untracked.keySet().toArray();
        Arrays.sort(a);
        for (Object c : a) {
            System.out.println(c);
        }
        System.out.println();
    }
    /**
     * asdasd.
     * @param args args
     */
    public void status(String [] args) {
        status2(args);
        System.out.println("=== Modifications Not Staged For Commit ===");
        ArrayList<String> lexiprint = new ArrayList<String>();
        if (staging.size() != 0) {
            for (Blob stage : staging.values()) {
                File filedir = new
                        File(".gitlet/staging/"
                        + stage.getshacode());
                String staginginging = Utils.readContentsAsString(filedir);
                File originalfile = new File(stage.getname());
                if (originalfile.exists()) {
                    String originafilestring =
                            Utils.readContentsAsString(originalfile);
                    if (!originafilestring.equals(staginginging)) {
                        lexiprint.add(stage.getname() + " " + "(modified)");
                    }
                } else {
                    lexiprint.add(stage.getname() + " " + "(deleted)");
                }
            }
        }
        File currentcomdir = new File(".gitlet/commits/" + branch.get(head));
        Commit currentcom = Utils.readObject(currentcomdir, Commit.class);
        if (currentcom.getblobs() != null) {
            for (Blob val : currentcom.getblobs().values()) {
                File filedir = new
                        File(".gitlet/staging/"
                        + val.getshacode());
                String staginging = Utils.readContentsAsString(filedir);
                File originalfile = new File(val.getname());
                if (originalfile.exists()) {
                    String originalfilestring =
                            Utils.readContentsAsString(originalfile);
                    if (!staginging.equals(originalfilestring)
                            && !staging.containsKey(val.getname())) {
                        lexiprint.add(val.getname() + " " + "(modified)");
                    }
                } else {
                    if (!untracked.containsKey(val.getname())) {
                        lexiprint.add(val.getname() + " " + "(deleted)");
                    }
                }
            }
        }
        Collections.sort(lexiprint);
        for (String printplz: lexiprint) {
            System.out.println(printplz);
        }
        System.out.println();
        ending();

    }

    /**
     * ending.
     */
    public void ending() {
        System.out.println("=== Untracked Files ===");
        File currentcomdired = new File(".gitlet/commits/" + branch.get(head));
        Commit currentcomed = Utils.readObject(currentcomdired, Commit.class);
        File current = new File(System.getProperty("user.dir"));
        for (File fileslisted : current.listFiles()) {
            if (currentcomed.getblobs() != null) {
                if (notimportantfile(fileslisted)) {
                    if (staging.size() != 0) {
                        if (!staging.containsKey(fileslisted.getName())
                                && !currentcomed.getblobs().containsKey
                                (fileslisted.getName())) {
                            System.out.println(fileslisted.getName());
                        }
                    } else {
                        if (!currentcomed.getblobs().containsKey
                                (fileslisted.getName())) {
                            System.out.println(fileslisted.getName());
                        }
                    }

                }
            } else {
                if (notimportantfile(fileslisted)) {
                    if (!staging.containsKey(fileslisted.getName())) {
                        System.out.println(fileslisted.getName());
                    }
                }
            }
        }
    }

    /**
     * branch.
     * @param args args
     */
    public void branch(String [] args) {
        if (branch.containsKey(args[1])) {
            Utils.message(" A branch with that name already exists.");
        } else {
            branch.put(args[1], branch.get(head));
        }
    }

    /**
     * rmbranch.
     * @param args args
     */
    public void rmbranch(String [] args) {
        if (branch.containsKey(args[1])) {
            if (head.equals(args[1])) {
                Utils.message("Cannot remove the current branch.");
                System.exit(0);
            } else {
                branch.remove(args[1]);
            }
        } else {
            Utils.message("A branch with that name does not exist.");
            System.exit(0);
        }
    }

    /**
     * reset.
     * @param args args
     */
    public void reset(String [] args) {
        String commitid = args[1];
        commitid = getfullfromsmall(commitid);
        File currentcomdir = new File(".gitlet/commits/" + branch.get(head));
        File newcomdir = new File(".gitlet/commits/" + commitid);
        if (!newcomdir.exists()) {
            Utils.message("No commit with that id exists.");
            System.exit(0);
        }
        Commit currentcom = Utils.readObject(currentcomdir, Commit.class);
        Commit newcom = Utils.readObject(newcomdir, Commit.class);

        File current = new File(System.getProperty("user.dir"));
        for (File fileslisted : current.listFiles()) {
            if (currentcom.getblobs() == null) {
                if (notimportantfile(fileslisted)) {
                    Utils.message("There "
                            + "is an untracked file in "
                            + "the way; delete it or add it first.");
                    System.exit(0);
                }
            } else {
                if (notimportantfile(fileslisted)) {
                    if (!currentcom.getblobs().containsKey
                            (fileslisted.getName())
                            && !staging.containsKey(fileslisted.getName())) {
                        Utils.message("There is "
                                + "an untracked file in the "
                                + "way; delete it or add it first.");
                        System.exit(0);
                    }
                }
            }

        }
        for (File file : current.listFiles()) {
            if (notimportantfile(file)) {
                if (!newcom.getblobs().containsKey(file.getName())) {
                    Utils.restrictedDelete(file);
                }
            }
        }
        for (String filename : newcom.getblobs().keySet()) {
            File file = new File(".gitlet/staging/"
                    + newcom.getblobs().get(filename).getshacode());
            Utils.writeContents(new File(filename),
                    Utils.readContentsAsString(file));
        }
        staging = new HashMap<String, Blob>();
        branch.put(head, commitid);
    }

    /**
     * checkuntracked.
     * @param args args
     */
    public void checkuntracked(String [] args) {
        String mastename = branch.get(head);
        File masterfile = new File(".gitlet/commits/" + mastename);
        Commit mastercommit = Utils.readObject(masterfile, Commit.class);
        String braname = branch.get(args[1]);
        File branchfile = new File(".gitlet/commits/" + braname);
        Commit branchcommit = Utils.readObject(branchfile, Commit.class);
        String splitpoint2 = lastestcommonanc(head, args[1]);
        Commit splitpoint = Utils.readObject
                (new File(".gitlet/commits/" + splitpoint2), Commit.class);
        File current = new File(System.getProperty("user.dir"));
        for (File fileslisted : current.listFiles()) {
            if (mastercommit.getblobs() == null) {
                if (notimportantfile(fileslisted)) {
                    Utils.message("There is an "
                            + "untracked file in the way; "
                            + "delete it or add it first.");
                    System.exit(0);
                }
            } else {
                if (notimportantfile(fileslisted)) {
                    if (!mastercommit.getblobs().containsKey
                            (fileslisted.getName())
                            && !staging.containsKey(fileslisted.getName())) {
                        Utils.message("There is an untracked "
                                + "file in the way; "
                                + "delete it or add it first.");
                        System.exit(0);
                    }
                }
            }
        }
    }

    /**
     * errordisplay.
     * @param args args
     */
    public void errordisplay(String [] args) {
        if (staging.size() != 0 || untracked.size() != 0) {
            Utils.message("You have uncommitted changes.");
            return;
        }
        if (!branch.containsKey(args[1])) {
            Utils.message("A branch with that name does not exist.");
            return;
        }
        if (args[1].equals(head)) {
            Utils.message("Cannot merge a branch with itself.");
            return;
        }
    }
    /**
     * merge.
     * @param args args
     */
    public void merge(String [] args) {
        errordisplay(args);
        String split = lastestcommonanc(args[1], head);
        if (split.equals(branch.get(args[1]))) {
            Utils.message("Given branch is an ancestor of the current branch.");
            return;
        }
        if (split.equals(branch.get(head))) {
            branch.put(head, branch.get(args[1]));
            Utils.message("Current branch fast-forwarded.");
            return;
        }
        checkuntracked(args);
        String mastename = branch.get(head);
        File masterfile = new File(".gitlet/commits/" + mastename);
        Commit mastercommit = Utils.readObject(masterfile, Commit.class);
        String braname = branch.get(args[1]);
        File branchfile = new File(".gitlet/commits/" + braname);
        Commit branchcommit = Utils.readObject(branchfile, Commit.class);
        String splitpoint2 = lastestcommonanc(head, args[1]);
        Commit splitpoint = Utils.readObject
                (new File(".gitlet/commits/" + splitpoint2), Commit.class);
        File current = new File(System.getProperty("user.dir"));
        ArrayList<String> allthefilenames = new ArrayList<String>();
        for (String file : mastercommit.getblobs().keySet()) {
            allthefilenames.add(file);
        }
        for (String file : branchcommit.getblobs().keySet()) {
            allthefilenames.add(file);
            if (!splitpoint.getblobs().containsKey(file)
                    && !mastercommit.getblobs().containsKey(file)) {
                seccheckout(new String []{"checkout",
                        branch.get(args[1]), "--", file});
                add(new String[] {"add", file});
            }
        }
        for (String file : splitpoint.getblobs().keySet()) {
            allthefilenames.add(file);
            if (mastercommit.getblobs().containsKey(file)
                    && branchcommit.getblobs().containsKey(file)) {
                if (!branchcommit.getblobs().get(file).getshacode().equals
                        (splitpoint.getblobs().get(file).getshacode())
                        && mastercommit.getblobs().get(file).getshacode().equals
                        (splitpoint.getblobs().get(file).getshacode()))   {
                    seccheckout(new String[]{"checkout",
                            branch.get(args[1]), "--", file});
                    add(new String[] {"add", file});
                }
            }
            if (!branchcommit.getblobs().containsKey(file)
                    && mastercommit.getblobs().get(file).getshacode().equals
                    (splitpoint.getblobs().get(file).getshacode())) {
                rm(new String [] {"rm", file});
            }
        }
        ending(allthefilenames, args);
    }

    /**
     * ending.
     * @param allthefilenames allthefilenames
     * @param args args
     */
    public void ending(ArrayList<String> allthefilenames, String [] args) {
        String mastename = branch.get(head);
        File masterfile = new File(".gitlet/commits/" + mastename);
        Commit mastercommit = Utils.readObject(masterfile, Commit.class);
        String braname = branch.get(args[1]);
        File branchfile = new File(".gitlet/commits/" + braname);
        Commit branchcommit = Utils.readObject(branchfile, Commit.class);
        String splitpoint2 = lastestcommonanc(head, args[1]);
        Commit splitpoint = Utils.readObject
                (new File(".gitlet/commits/" + splitpoint2), Commit.class);
        File current = new File(System.getProperty("user.dir"));
        boolean conflicthappen = false;
        for (String file : allthefilenames) {
            if (checkmergeconflict(file, branchcommit.getblobs(),
                    mastercommit.getblobs(), splitpoint.getblobs())) {
                conflicthappen = true;
                mergeconflict(args[1], file);
            }
        }
        String lastmaster = branch.get(head);
        File lastmasterfile = new File(".gitlet/commits/" + mastename);
        Commit lastmastercommit = Utils.readObject(masterfile, Commit.class);
        String lastbranchname = branch.get(args[1]);
        File lastbranchfile = new File(".gitlet/commits/" + lastbranchname);
        Commit lastbranchcommit = Utils.readObject(branchfile, Commit.class);
        Commit[] parents = new Commit[]{lastmastercommit, lastbranchcommit};
        String out = "Merged " + args[1] + " into " + head + ".";
        commit(out, parents);
        if (conflicthappen) {
            Utils.message("Encountered a merge conflict.");
        }
    }

    /**
     * lastestcommonac.
     * @param branch1 branch1
     * @param branch2 branch2
     * @return String
     */
    public String lastestcommonanc(String branch1, String branch2) {
        String branch1code = branch.get(branch1);
        String branch2code = branch.get(branch2);

        ArrayList<String> branch1list = new ArrayList<String>();

        while (branch1code != null) {
            branch1list.add(branch1code);
            Commit getcommit = Utils.readObject
                    (new File(".gitlet/commits/" + branch1code), Commit.class);
            branch1code = getcommit.getparentshacode();
        }

        while (branch2code != null) {
            if (branch1list.contains(branch2code)) {
                return branch2code;
            }
            Commit parent2 = Utils.readObject
                    (new File(".gitlet/commits/" + branch2code), Commit.class);
            branch2code = parent2.getparentshacode();
        }
        return "";




    }

    /**
     * checkmergeconflict.
     * @param filename filename
     * @param branchmap branchmap
     * @param mastermap mastermap
     * @param splitpointmap splitpointmap
     * @return boolean
     */
    public boolean checkmergeconflict(String filename,
                                      HashMap<String, Blob> branchmap,
                                      HashMap<String, Blob> mastermap,
                                      HashMap<String, Blob> splitpointmap) {
        String branchsha = "";
        String mastersha = "";
        String splitsha = "";

        if (branchmap.containsKey(filename)) {
            branchsha = branchmap.get(filename).getshacode();
        } else {
            branchsha = "s";
        }
        if (mastermap.containsKey(filename)) {
            mastersha = mastermap.get(filename).getshacode();
        } else {
            mastersha = "s";
        }

        if (splitpointmap.containsKey(filename)) {
            splitsha = splitpointmap.get(filename).getshacode();
        } else {
            splitsha = "s";
        }

        if (!branchsha.equals(splitsha) && !mastersha.equals(splitsha)) {
            if (!branchsha.equals(mastersha)) {
                return true;
            }
        }
        return false;



    }

    /**mergeconflict.
     * @param branchName branchName
     * @param fileName fileName
     */
    public void mergeconflict(String branchName, String fileName) {

        File masterfile = new File(".gitlet/commits/" + branch.get(head));
        Commit mastercommit = Utils.readObject(masterfile, Commit.class);

        String brancname = branch.get(branchName);
        File branchfile = new File(".gitlet/commits/" + brancname);
        Commit branchcommit = Utils.readObject(branchfile, Commit.class);


        String splitcommit3 = lastestcommonanc(head, branchName);
        Commit splitcommit = Utils.readObject
                (new File(".gitlet/commits/" + splitcommit3), Commit.class);

        String mastercommitcont = "";
        String branchcommitcont = "";


        if (mastercommit.getblobs().containsKey(fileName)) {
            File mastercommitfile = new File(".gitlet/staging/"
                    + mastercommit.getblobs().get(fileName).getshacode());
            mastercommitcont = Utils.readContentsAsString(mastercommitfile);
        }

        if (branchcommit.getblobs().containsKey(fileName)) {
            File branchcommitfile = new File(".gitlet/staging/"
                    + branchcommit.getblobs().get(fileName).getshacode());
            branchcommitcont = Utils.readContentsAsString(branchcommitfile);
        }
        String output = "<<<<<<< HEAD\n" + mastercommitcont
                + "=======\n" + branchcommitcont + ">>>>>>>\n";
        Utils.writeContents(new File(fileName), output);
        add(new String [] {"add", fileName});


    }
    /**head.
     * @param small small
     * @return String*/
    public String getfullfromsmall(String small) {
        File currentcomdir = new File(".gitlet/commits");
        for (File commits : currentcomdir.listFiles()) {
            if (commits.getName().contains(small)) {
                return commits.getName();
            }
        }
        Utils.message("No commit with that id exists.");
        System.exit(0);
        return "";
    }

    /**
     * notimportantfile.
     * @param file file
     * @return boolean
     */
    public boolean notimportantfile(File file) {
        for (int i = 0; i < importantfiles.length; i++) {
            if (importantfiles[i].equals(file.getName())) {
                return false;
            }
        }
        return true;
    }

    /**
     * important files.
     */
    private String [] importantfiles = {".gitlet", "gitlet-design.txt",
                                        ".idea", "gitlet", ".gitignore",
                                        "testing", "proj3.iml", "Makefile",
                                        "out", ".DS_Store"};

}
