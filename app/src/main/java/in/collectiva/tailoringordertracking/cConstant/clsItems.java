package in.collectiva.tailoringordertracking.cConstant;

/**
 * Created by dhivya on 01/07/2017.
 */

public class clsItems
{
    public long SerialNo;
    public int ItemId;
    public String ItemName;
    public Double Amount;

    public clsItems(){
        super();
    }


    @Override
    public String toString() {
        return this.SerialNo + ". " + this.ItemName + " [$" + this.Amount + "]";
    }
}
