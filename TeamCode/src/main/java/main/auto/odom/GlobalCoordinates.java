package main.auto.odom;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.File;

public class GlobalCoordinates implements Runnable {

    DcMotor leftEnc, rightEnc, midEnc;

    double x = 0;

    boolean isRunning = true;

    double leftEncoderPosition, rightEncoderPosition, middleEncoderPosition;
    double changeInOrientation;
    double OLDLeftEncoderPosition, OLDRightEncoderPosition, OLDMiddleEncoderPosition;

    double globalX, globalY, robotOrientation;

    double encoderWheelDistance;
    double middleEncoderTickOffset;
    int sleepTime;

    File sideWheelsSeparationFile = AppUtil.getInstance().getSettingsFile("sideWheelsSeparationFile");
    File middleTickOffsetFile = AppUtil.getInstance().getSettingsFile("middleTickOffsetFile");

    public GlobalCoordinates(DcMotor leftEncoder, DcMotor rightEncoder, DcMotor middleEncoder, double TICKS_PER_INCH, int threadSleepyDelay) {
        this.leftEnc = leftEncoder;
        this.rightEnc = rightEncoder;
        this.midEnc = middleEncoder;
        sleepTime = threadSleepyDelay;

        encoderWheelDistance = Double.parseDouble(ReadWriteFile.readFile(sideWheelsSeparationFile).trim()) * TICKS_PER_INCH;
        middleEncoderTickOffset = Double.parseDouble(ReadWriteFile.readFile(middleTickOffsetFile).trim());
    }

    public void positionUpdate(){

        leftEncoderPosition = leftEnc.getCurrentPosition();
        rightEncoderPosition = -rightEnc.getCurrentPosition();

        x = 10 + robotOrientation;

        double leftChange = leftEncoderPosition - OLDLeftEncoderPosition;
        double rightChange = rightEncoderPosition - OLDRightEncoderPosition;

        changeInOrientation = (leftChange - rightChange) / encoderWheelDistance;
        robotOrientation += changeInOrientation;

        middleEncoderPosition = midEnc.getCurrentPosition();
        double rawHorizontalChange = middleEncoderPosition - OLDMiddleEncoderPosition;
        double horizontalChange = rawHorizontalChange - (changeInOrientation * middleEncoderTickOffset);

        double sides = (rightChange + leftChange)/2;
        double frontBack = horizontalChange;

        globalX = sides * Math.sin(robotOrientation) + frontBack * Math.cos(robotOrientation);
        globalY = sides * Math.cos(robotOrientation) - frontBack * Math.sin(robotOrientation);


        OLDLeftEncoderPosition = leftEncoderPosition;
        OLDRightEncoderPosition = rightEncoderPosition;
        OLDMiddleEncoderPosition = middleEncoderPosition;
    }

    public double returnXCoordinate(){ return globalX;}
    public double returnYCoordinate(){ return globalY;}
    public double returnValue(){ return x;}
    public double returnOrientation(){ return Math.toDegrees(robotOrientation) % 360;}

    public void stop(){ isRunning = false;}

    @Override
    public void run() {
        while (isRunning){
            positionUpdate();
        }
        try {
            Thread.sleep(sleepTime);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
