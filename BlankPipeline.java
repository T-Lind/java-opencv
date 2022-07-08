package org.firstinspires.ftc.teamcode;

import org.openftc.easyopencv.OpenCvPipeline;
import org.opencv.core.Mat;

public class BlankPipeline extends OpenCvPipeline{
    
    /**
     * Simply return the input frame to display on the console.
     * @param input The input frame as a matrix
     * @return the identical input frame.
     */
    @Override
    public Mat processFrame(Mat input){
        return input;
    }
}
