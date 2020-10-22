package gitlet;

import java.io.File;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author jakekim
 */
public class Main {
    /**Main folder.*/
    static final File MAIN_FOLDER = new File(".gitlet");
    /**current.*/
    private static Repo current;
    /**path.*/
    private static String repopath = ".gitlet/repo";
    /**get repo path.
     * @return String*/
    public static String getrepopath() {
        return repopath;
    }


    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        try {
            if (args.length == 0) {
                Utils.message("Must have at least one argument");
                System.exit(0);
            }
            if (args[0].equals("init")) {
                if (repoinitcheck()) {
                    Utils.message("A Gitlet version-control "
                            + "system already exists"
                            + " in the current directory.");
                    System.exit(0);
                } else {
                    setuppersistance();
                    current = new Repo();
                    File repopa = new File(repopath);
                    Utils.writeObject(repopa, current);
                }

            } else {
                if (repoinitcheck()) {
                    current = getcurrentrepo();
                    directcommand(args);
                    File repopa = new File(repopath);
                    Utils.writeObject(repopa, current);

                } else {
                    Utils.message("Not in an initialized Gitlet directory.");
                    System.exit(0);
                }
            }
        } catch (GitletException e) {
            System.exit(0);
        }

    }

    /**
     * execute.
     * @param args args
     */
    public static void directcommand(String [] args) {
        args[0] = args[0].trim();
        switch (args[0]) {
        case "add" :
            current.add(args);
            break;
        case "commit" :
            current.commit(args[1]);
            break;
        case "rm" :
            current.rm(args);
            break;
        case "log" :
            current.log(args);
            break;
        case "checkout" :
            if (args[1].equals("--") && args.length == 3) {
                current.firstcheckout(args);
            } else if (args.length == 2) {
                current.thircheckout(args);
            } else if (args[2].equals("--") && args.length == 4) {
                current.seccheckout(args);
            } else {
                Utils.message("Incorrect operands.");
                System.exit(0);
            }
            break;
        case "global-log" :
            current.globallog(args);
            break;
        case "find":
            current.find(args);
            break;
        case "status":
            current.status(args);
            break;
        case "branch" :
            current.branch(args);
            break;
        case "rm-branch" :
            current.rmbranch(args);
            break;
        case "reset" :
            current.reset(args);
            break;
        case "merge" :
            current.merge(args);
            break;
        default:
            Utils.message(String.format("Unknown command: %s", args[0]));
            System.exit(0);
        }
    }

    /**
     * repoinitcheck.
     * @return boolean
     */
    public static boolean repoinitcheck() {
        File gitletfile = new File(".gitlet");
        return gitletfile.exists();
    }

    /**
     * exiting.
     * @param message message
     */
    public static void exiting(String message) {
        if (message != null && !message.equals("")) {
            System.out.println(message);
        }
        System.exit(0);
    }

    /**
     * setup.
     */
    public static void setuppersistance() {

        MAIN_FOLDER.mkdir();
        File commits = new File(".gitlet/commits");
        commits.mkdir();
        File staging = new File(".gitlet/staging");
        staging.mkdir();

    }

    /**getcurrentrpepo.
     * @return Repo
     */
    public static Repo getcurrentrepo() {
        File repopa = new File(repopath);
        return Utils.readObject(repopa, Repo.class);
    }

    /**
     * validatenum.
     * @param cmd cmd
     * @param args args
     * @param n n
     */
    public static void validateNumArgs(String cmd, String[] args, int n) {
        if (args.length != n) {
            throw new RuntimeException(
                    String.format("Invalid number of arguments for: %s.", cmd));
        }
    }


}
