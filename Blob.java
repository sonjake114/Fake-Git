package gitlet;

import java.io.Serializable;

/**Blob class.
 *@author jakekim  */
class Blob implements Serializable {
    /**name.*/
    private String name;
    /**shacode.*/
    private String shacode;

  /**
   * Blob method.
   * @param nam name
   * @param id id
   */
    Blob(String nam, String id)  {
        this.name = nam;
        this.shacode = id;
    }

  /**
   * getname.
   * @return name
   */
    public String getname() {
        return this.name;
    }

  /**
   * getsha.
   * @return sha
   */
    public String getshacode() {
        return this.shacode;
    }

  /**
   * tostring.
   * @return string
   */
    public String toString() {
        return String.format(
                "name %s" + '\n' +  "code %s", name, shacode);
    }
}
