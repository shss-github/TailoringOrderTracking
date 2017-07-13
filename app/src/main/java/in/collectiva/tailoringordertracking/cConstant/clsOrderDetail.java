package in.collectiva.tailoringordertracking.cConstant;

/**
 * Created by dhakchina on 7/8/2017.
 */

public class clsOrderDetail
{
    public long OrderDetailId;
    public long OrderId;
    public long ItemId;
    public int Qty;
    public double Rate;
    public double Amount;
    public String ItemName;

    public clsOrderDetail(){
        super();
    }

    public clsOrderDetail(long lOrderDetailId, long lOrderId, long lItemId, int lQty, double lRate, double lAmount) {
        super();
        this.OrderDetailId = lOrderDetailId;
        this.OrderId = lOrderId;
        this.ItemId = lItemId;
        this.Qty = lQty;
        this.Rate = lRate;
        this.Amount = lAmount;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        clsOrderDetail other = (clsOrderDetail) obj;
        if (ItemId != other.ItemId)
            return false;
        return true;
    }
}