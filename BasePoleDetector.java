package org.firstinspires.ftc.teamcode;

import org.openftc.easyopencv.OpenCvPipeline;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Class to apply a canny edge detector in an EasyOpenCV pipeline.
 */
public class BasePoleDetector extends OpenCvPipeline {
    /**
     * Telemetry object to display data to the console
     */
    private Telemetry telemetry;


    /**
     * Constructor to assign the telemetry object and actually have telemetry work.
     * This works because the OpenCvPipeline object has this built in interface to
     * use telemetry.
     * @param telemetry the input Telemetry type object.
     */
    public BasePoleDetector(Telemetry telemetry) {
        this.telemetry = telemetry;

    }


   /**
    * Apply a canny edge detector to an input matrix.
    * @param src is the source matrix - what is put into the function
    * @return a mat with only the edges of the main bodies given some contrast threshold.
    */
   public Mat canny(Mat src){
      // Define the size of the input image
      int frameWidth = src.cols();
      int frameHeight = src.rows();

      Mat gray = new Mat(frameHeight, frameWidth, src.type());
      Mat edges = new Mat(frameHeight, frameWidth, src.type());

      Imgproc.cvtColor(src, gray, Imgproc.COLOR_RGB2GRAY);
      Imgproc.blur(gray, gray, new Size(3, 3));
      Imgproc.Canny(gray, edges, 5, 100);

      Mat lines = new Mat();
      Imgproc.HoughLines(edges, lines, 1, Math.PI/170, 180);

      for (int x = 0; x < lines.rows(); x++) {
         double rho = lines.get(x, 0)[0],
                 theta = lines.get(x, 0)[1];
         double a = Math.cos(theta), b = Math.sin(theta);
         double x0 = a*rho, y0 = b*rho;
         Point pt1 = new Point(Math.round(x0 + 1000*(-b)), Math.round(y0 + 1000*(a)));
         Point pt2 = new Point(Math.round(x0 - 1000*(-b)), Math.round(y0 - 1000*(a)));
         if(Math.abs((pt2.y - pt1.y)/(pt2.x - pt1.y)) > 4){
            // telemetry.addData("Slope", Math.abs((pt2.y - pt1.y)/(pt2.x - pt1.y)));
            Imgproc.line(src, pt1, pt2, new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
         }
     }


      lines.release();
      gray.release();
      edges.release();

      telemetry.addLine("Pole Detector Running!");
      telemetry.update();

      return src;
   }

   /**
    * Process the frame by returning the canny edge of the input image
    * @param img the input image to this pipeline.
    * @return The canny edge version of the input image.
    */
    @Override
   public Mat processFrame(Mat img) {
      return canny(img);
   }
}