import org.opencv.core.RotatedRect;
import org.opencv.core.Point;
import org.opencv.core.Size;

class rect-manipulation {
    public static void main(String[] args) {
        double[] center = new double[]{3, 100};
        RotatedRect rect = new RotatedRect(center);
        System.out.println(rect.toString());
        Point center1 = rect.center;
        Size size = rect.size;
        String s = size.toString();
        s = s.substring(0, s.indexOf("x"));
        int width = (int) Double.parseDouble(s);
        System.out.println(width);
        String c = center1.toString();
        c = c.substring(1, c.length()-1);
        int index = c.indexOf(",");
        int x = (int) Double.parseDouble(c.substring(0,index));
        int y = (int) Double.parseDouble(c.substring(index+1));
        System.out.println(x);
        System.out.println(y);
    }
}
