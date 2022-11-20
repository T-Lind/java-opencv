package org.firstinspires.ftc.teamcode;

import org.openftc.easyopencv.OpenCvPipeline;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.text.DecimalFormat;


/**
 * 
 */
public class ColorRegionIdentifier extends OpenCvPipeline{
    /**
     * Matrix to copy the input matrix image to
     */
    private Mat copyInput;
    
        /**
     * Telemetry object to display data to the console
     */
    private Telemetry telemetry = null;

    /**
     * Decimal formatting system to output the brightness percentage on the console
     */
    private final DecimalFormat df = new DecimalFormat("##.00"); 

    
    /**
     * Constructor to assign the telemetry object and actually have telemetry work.
     * This works because the OpenCvPipeline object has this built in interface to
     * use telemetry.
     * @param telemetry the input Telemetry type object.
     */
    public ColorRegionIdentifier(Telemetry telemetry) {
        this.telemetry = telemetry;
    }


    /**
     * Method to convert the frame to grayscale, blur it, and take the average color brightness
     * of a specified region and output it in telemetry, and return the color image with
     * a rectangle outlining the region.
     * @param input the input image as a matrix
     * @return the input image with a black rectangle identifying the averaged region.
     */
    @Override
    public Mat processFrame(Mat input){
        // Define the size of the input image
        int frameWidth = input.cols();
        int frameHeight = input.rows();

        // Draw a rectangle where we'll be detecting
        Imgproc.rectangle(
            input,
            new Point(frameWidth/8,frameHeight/8),
            new Point(frameWidth/5,frameHeight/2),
            new Scalar(20),
            2
        );

        // Copy the input image to a new image
        copyInput = new Mat();
        input.copyTo(copyInput);

        // Blur the new image
        Imgproc.blur(copyInput, copyInput, new Size(5, 5));

        // Take the average of the pixels in the rectangular region
        int pixelRegionAverage = 0;

        for(int r=frameHeight/8;r<frameHeight/2;r++){
            for(int c=frameWidth/8;c<frameWidth/5;c++){
                pixelRegionAverage += copyInput.get(r,c)[0]; // R-0 G-1 B-2
            }
        }

        // Release the copied input frame
        copyInput.release();

        // Divide the sum of the pixels by the number of pixels (rectangular area formula here)
        pixelRegionAverage/=((frameWidth/5-frameWidth/8)*(frameHeight/2-frameHeight/8));

        // Update telemetry with the brightness average of the rectangular region out of 100%

        double regionAverageBrightnessPercent = 100*pixelRegionAverage/255.0;

        telemetry.addLine("Rectangular region BLUE brightness average: "+df.format(regionAverageBrightnessPercent)+"%");
        telemetry.update();


        return input;
    }
}
