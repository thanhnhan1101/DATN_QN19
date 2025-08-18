package poly.util;

public class ImageUtils {
    
    public static String getImageDataUrl(String base64Data) {
        if (base64Data == null || base64Data.trim().isEmpty()) {
            return "";
        }
        return "data:image/jpeg;base64," + base64Data;
    }
}
