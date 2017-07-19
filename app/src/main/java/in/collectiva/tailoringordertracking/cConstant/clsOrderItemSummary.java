package in.collectiva.tailoringordertracking.cConstant;

/**
 * Created by dhivya on 01/07/2017.
 */

public class clsOrderItemSummary
{
    public long SerialNo;
    public String ItemName;
    public Integer NoOfQty;

    public clsOrderItemSummary(){
        super();
    }

    @Override
    public String toString() {
        return this.SerialNo + ". " + this.ItemName + " " + this.NoOfQty;
    }
}
