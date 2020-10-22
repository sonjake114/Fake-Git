package gitlet;

import java.io.Serializable;
/**Log class.
@author jakekim
 */
class Log implements Serializable {
    /**commit.*/
    private String commit;
    /**date.*/
    private String date;
    /**message.*/
    private String message;

  /**log cons.
   * @param comm comm
   * @param dat dat
   * @param mess mess
   */
    Log(String comm, String dat, String mess) {
        this.commit = comm;
        this.date = dat;
        this.message = mess;
    }

  /**getcomm.
   * @return String
   */
    public String getcommit() {
        return commit;
    }

  /**getdate.
   * @return String
   */
    public String getdate() {
        return date;
    }

  /**
   * getmessage.
   * @return String
   */
    public String getmessage() {
        return message;
    }
}
