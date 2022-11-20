/*
    TIERNAN X. LINDAUER
    LAST UPDATED ON 11-15-21 AT 7:49 PM
    LAST PUSHED TO GITHUB ON 11-15-21 AT __:__ PM

    CUBE, TEAM SCORING ELEMENT, AND DUCK DETECTION
    PIPELINE FOR THE FTC FREIGHT FRENZY SEASON

    UNAUTHORIZED REPRODUCTION OF THIS CODE IS ILLEGAL
    USE OF CODE IS AUTHORIZED TO ALL MEMBERS OF FTC TEAM 9527
    OR AFTER THE DATE 12/31/22

*/

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.openftc.easyopencv.OpenCvPipeline;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

@Disabled
public class CubeDetectionPipeline extends OpenCvPipeline
{
    Mat cbMat = new Mat();
    Mat thresholdMat = new Mat();
    Mat morphedThreshold = new Mat();
    Mat contoursOnPlainImageMat = new Mat();

    static final int CB_CHAN_MASK_THRESHOLD = 80;

    Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
    Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(6, 6));

    static final Scalar RED = new Scalar(255, 0, 0);
    static final int CB_CHAN_IDX = 2;

    ArrayList<RotatedRect> pos = new ArrayList<RotatedRect>();

    
    @Override
    public Mat processFrame(Mat input)
    {
        for(MatOfPoint contour : findContours(input))
            analyzeContour(contour, input);
        return input;
    }


    ArrayList<MatOfPoint> findContours(Mat input)
    {
        ArrayList<MatOfPoint> contoursList = new ArrayList<>();
        Imgproc.cvtColor(input, cbMat, Imgproc.COLOR_RGB2YCrCb);
        Core.extractChannel(cbMat, cbMat, CB_CHAN_IDX);
        Imgproc.threshold(cbMat, thresholdMat, CB_CHAN_MASK_THRESHOLD, 255, Imgproc.THRESH_BINARY_INV);
        morphMask(thresholdMat, morphedThreshold);

        Imgproc.findContours(morphedThreshold, contoursList, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);
        input.copyTo(contoursOnPlainImageMat);

        return contoursList;
    }

    void morphMask(Mat input, Mat output)
    {
        Imgproc.erode(input, output, erodeElement);
        Imgproc.erode(output, output, erodeElement);
        Imgproc.dilate(output, output, dilateElement);
        Imgproc.dilate(output, output, dilateElement);
    }

    void analyzeContour(MatOfPoint contour, Mat input)
    {
        MatOfPoint2f contour2f = new MatOfPoint2f(contour.toArray());
        RotatedRect rotatedRectFitToContour = Imgproc.minAreaRect(contour2f);
        drawRotatedRect(rotatedRectFitToContour, input);

    }
    void drawRotatedRect(RotatedRect rect, Mat drawOn)
    {
        Point[] points = new Point[4];
        rect.points(points);
        pos.add(rect);
        for(int i = 0; i < 4; ++i)
            Imgproc.line(drawOn, points[i], points[(i+1)%4], RED, 2);
        
    }
    public ArrayList<Integer> getPositions(){
        //YES THIS BS IS NECESSARY TO CONVERT A POINT TO INTS
        ArrayList<Integer> int_pos = new ArrayList<Integer>();
        for(RotatedRect rect : pos){
            Point center1 = rect.center;
            Size size = rect.size;

            String c = center1.toString();
            String s = size.toString();

            c = c.substring(1, c.length()-1);
            String s2 = s.substring(s.indexOf("x"));
            s = s.substring(0, s.indexOf("x"));

            int index = c.indexOf(",");
            int width = (int) Double.parseDouble(s);
            int height = (int) Double.parseDouble(s2);
            int x = (int) Double.parseDouble(c.substring(0,index));
            int y = (int) Double.parseDouble(c.substring(index+1));

            if(width/2 > height){
                int_pos.add(x);//LEFT CUBE
                int_pos.add(y);
                int_pos.add(width/2);
                
                int_pos.add(x+width/2);//RIGHT CUBE
                int_pos.add(y);
                int_pos.add(width/2);
                }                

            else{
            int_pos.add(x);
            int_pos.add(y);
            int_pos.add(width);
            }
        }     

        //RETURNS THE CENTER POSITION OF EACH ELEMENT X FIRST, THEN Y, THEN THE WIDTH. NOTE (0,0) IS THE TOP LEFT OF THE FRAME, NOT THE BOTTOM LEFT.
        return int_pos;
    }
}