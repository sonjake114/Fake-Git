package gitlet;
import java.util.Arrays;
import java.util.Date;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.HashMap;


/**Commit class.
 * @author jakekim
 */
class Commit implements Serializable {
    /**parent.*/
    private Commit [] parent;
    /**message.*/
    private String message;
    /**timestamp.*/
    private String timestamp;
    /**shaid.*/
    private String integerID;
    /**blobs.*/
    private HashMap<String, Blob> blobscontent;

    /**getfirstparent.
     * @return parent */
    Commit getfirstparent() {
        if (parent.length != 0) {
            return parent[0];
        } else {
            return null;
        }
    }

    /**getparentcode.
     * @return
     */
    public String getparentshacode() {
        if (parent != null) {
            return parent[0].getintegerID();
        }
        return null;
    }


  /**getblobs().
   *@return Hashmap
   */
    public HashMap<String, Blob> getblobs() {
        return blobscontent;
    }

  /**getparents().
   *@return commt
   */
    public Commit [] getparents() {
        return this.parent;
    }
    /**getmessage().
          *@return commt
   */
    public String getmessage() {
        return this.message;
    }
    /**getintid().
          *@return Strin
   */
    public String getintegerID() {
        return this.integerID;
    }
    /**getdate().
          *@return Strin
   */
    public String getdate() {
        return timestamp;
    }

    /**setdate().
     * @param date date
   */
    public void setdate(String date) {
        timestamp = date;
    }

  /**
   * constructor.
   * @param mess mess
   * @param listofparents parents
   * @param blobs blobs.
   */
    Commit(String mess, Commit [] listofparents,
                  HashMap<String, Blob> blobs) {
        this.message = mess;
        this.parent = listofparents;
        this.blobscontent = blobs;
        Date d = new Date();
        this.timestamp = dateformattingtool.format(d) + " -0800";
        this.integerID = shahashcode();
    }

  /**
   * constructor.
   */
    Commit() {
        this.message = null;
        this.parent = null;
        this.blobscontent = null;
        this.timestamp = null;
        this.integerID = null;
    }

  /**
   * init.
   * @return
   */
    public static Commit initialize() {
        Commit before =  new Commit("initial commit", null, null);
        before.setdate("Wed Dec 31 16:00:00 1969 -0800");
        return before;
    }

  /**
   * shacode.
   * @return String
   */
    public String shahashcode() {
        String parents = Arrays.toString(this.parent);
        String blobsreading;
        if (blobscontent == null) {
            blobsreading = "";
        } else {
            blobsreading = blobscontent.toString();
        }
        return Utils.sha1(parents, blobsreading, message, timestamp);
    }

  /**
   * design doc.
   */
    private static SimpleDateFormat dateformattingtool
            = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy");



}

