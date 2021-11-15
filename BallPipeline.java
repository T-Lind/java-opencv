import org.opencv.core.Point;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import java.util.ArrayList;

class BallPipeline {
    public static ArrayList main() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        ArrayList<Integer> info = new ArrayList<Integer>(run(getPhoto()));
        return info;
    }

    public static Mat getPhoto() {
        VideoCapture capture = new VideoCapture(0);
        Mat image = new Mat();
        if (capture.isOpened()) {
            capture.read(image);
        }
        return image;
    }
    public static ArrayList run(Mat image) {
        ArrayList<Integer> info = new ArrayList<Integer>();
        //REPLACE THESE FILE PATHS WITH LOCAL FILE PATHS
        CascadeClassifier Detector = new CascadeClassifier(("CASCADEPATH/cascade.xml"));
        MatOfRect Detections = new MatOfRect();
        Detector.detectMultiScale(image, Detections);
        int detected_num = Detections.toArray().length;
        for (Rect rect : Detections.toArray()) {
            info.add(rect.x+(rect.width)/2);
            info.add(rect.y+(rect.height)/2);
            info.add(rect.width);
            info.add(rect.height);
            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
        }
        info.add(detected_num);
        return info;
        //(0,0) IS TOP LEFT OF THE FRAME, rect.x IS THE CLOSEST CORNER TO  (0,0)
        //FORMAT: X CENTER, Y CENTER, X WIDTH, Y WIDTH... NUMBER DETECTED
    }
}