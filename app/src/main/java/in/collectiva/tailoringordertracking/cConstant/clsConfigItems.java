package in.collectiva.tailoringordertracking.cConstant;

/**
 * Created by Gopi on 24/07/2017.
 */

public class clsConfigItems
{
    public long SerialNo;
    public int ConfigItemId;
    public String ConfigItemName;
    public String Gender;

    public clsConfigItems(){
        super();
    }


    @Override
    public String toString() {
        return this.SerialNo + ". " + this.ConfigItemName ;
    }
}
