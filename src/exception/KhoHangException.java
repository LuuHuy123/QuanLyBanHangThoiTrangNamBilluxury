package exception;

public class KhoHangException
        extends BiLuxuryException {

    public KhoHangException(String tenSanPham,
                            int tonKho,
                            int soLuongYeuCau) {

        super("Sản phẩm '" + tenSanPham
                + "' chỉ còn "
                + tonKho
                + " sản phẩm, không đủ số lượng yêu cầu ("
                + soLuongYeuCau + ").");
    }

    public KhoHangException(String message) {
        super(message);
    }
}