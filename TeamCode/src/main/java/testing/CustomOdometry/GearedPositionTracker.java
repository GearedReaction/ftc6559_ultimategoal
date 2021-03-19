package testing.CustomOdometry;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ReadWriteFile;

import static java.lang.Math.*;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.File;


public class GearedPositionTracker implements Runnable {

    DcMotor left, mid, right;

    private boolean isRunning = true;

    double leftEncoderPosition = 0, midEncoderPosition = 0, rightEncoderPosition = 0, changeInOrientation = 0;
    private volatile double robotOrientation = 0, robotYPosition = 0, robotXPosition = 0;
    private double lastLeftPosition = 0, lastRightPosition = 0, lastMidPosition = 0;

    private int sleepTime;

    //Files to save coefficients to
    File wheelBaseSeparationFile = AppUtil.getInstance().getSettingsFile("wheelBaseSeparation.txt");
    File horizontalTickOffsetFile = AppUtil.getInstance().getSettingsFile("horizontalTickOffset.txt");

    double wheelBaseSeparation =  Double.parseDouble(ReadWriteFile.readFile(wheelBaseSeparationFile).trim()); //Possibly multiply by Counts/Inch ?
    double horizontalTickOffset = Double.parseDouble(ReadWriteFile.readFile(horizontalTickOffsetFile).trim());

    private int leftMultiplier = 1, rightMultiplier = 1, middleMultiplier = 1;

    public GearedPositionTracker(DcMotor leftEncoder, DcMotor midEncoder, DcMotor rightEncoder, double COUNTS_PER_INCH, int threadSleepDelay){
        this.left = leftEncoder;
        this.mid = midEncoder;
        this.right = rightEncoder;
        sleepTime = threadSleepDelay;

        this.wheelBaseSeparation = Double.parseDouble(ReadWriteFile.readFile(wheelBaseSeparationFile));
        this.horizontalTickOffset = Double.parseDouble(ReadWriteFile.readFile(horizontalTickOffsetFile));

    }



    private void globalCoordinatePositionUpdate(){
        leftEncoderPosition = left.getCurrentPosition() * leftMultiplier;
        rightEncoderPosition = right.getCurrentPosition() * rightMultiplier;


        double leftEncoderChange = leftEncoderPosition - lastLeftPosition;
        double rightEncoderChange = rightEncoderPosition - lastRightPosition;

        //Orientation is in RADIANS
        changeInOrientation = (leftEncoderChange - rightEncoderChange) / wheelBaseSeparation;
        robotOrientation = robotOrientation + changeInOrientation;

        midEncoderPosition = mid.getCurrentPosition() * middleMultiplier;
        double changeInHorizontalPosition = midEncoderPosition - lastMidPosition;
        double adjustedChangeInHorizontalPosition = changeInHorizontalPosition - (changeInOrientation*horizontalTickOffset);

        double p = ((rightEncoderChange + leftEncoderChange)) / 2;
        double n = adjustedChangeInHorizontalPosition;

        robotXPosition = robotXPosition + (p* sin(robotOrientation) + n* cos(robotOrientation));
        robotYPosition = robotYPosition + (p* cos(robotOrientation) - n* sin(robotOrientation));


        //Update last positions to be the current positions
        lastMidPosition = midEncoderPosition;
        lastLeftPosition = leftEncoderPosition;
        lastRightPosition = rightEncoderPosition;

    }

    public double getRobotOrientation() {
        return robotOrientation;
    }

    public double getRobotYPosition() {
        return robotYPosition;
    }

    public double getRobotXPosition() {
        return robotXPosition;
    }

    public void reverseLeftEncoder(){
        leftMultiplier *= -1;
    }

    public void reverseRightEncoder(){
        rightMultiplier *= -1;
    }

    public void reverseNormalEncoder() {
        middleMultiplier *= -1;
    }

    @Override
    public void run() {
        while(isRunning) {
            globalCoordinatePositionUpdate();
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

