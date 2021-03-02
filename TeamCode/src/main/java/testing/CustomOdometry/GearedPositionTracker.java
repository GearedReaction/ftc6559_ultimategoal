package testing.CustomOdometry;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.File;


public class GearedPositionTracker implements Runnable {

    DcMotor left, mid, right;

    double globalOrientation = 0;
    int lastLeftPosition = 0, lastMidPosition = 0, lastRightPosition = 0;

    //Files to save coefficients to
    File wheelBaseSeparationFile = AppUtil.getInstance().getSettingsFile("wheelBaseSeparation.txt");
    File horizontalTickOffsetFile = AppUtil.getInstance().getSettingsFile("horizontalTickOffset.txt");

    double wheelBaseSeparation =  Double.parseDouble(ReadWriteFile.readFile(wheelBaseSeparationFile).trim()); //Possibly multiply by Counts/Inch ?
    double horizontalTickOffset = Double.parseDouble(ReadWriteFile.readFile(horizontalTickOffsetFile).trim());

    public GearedPositionTracker(DcMotor leftEncoder, DcMotor midEncoder, DcMotor rightEncoder, double COUNTS_PER_INCH, int threadSleepDelay){
        this.left = leftEncoder;
        this.mid = midEncoder;
        this.right = rightEncoder;

    }


    private void globalCoordinatePositionUpdate(){
        int leftEncoderPosition = left.getCurrentPosition();
        int rightEncoderPositon = right.getCurrentPosition();


        double leftEncoderChange = left.getCurrentPosition() - lastLeftPosition;
        double rightEncoderChange = right.getCurrentPosition() - lastRightPosition;
        double changeInOrientation = leftEncoderChange - rightEncoderChange / wheelBaseSeparation;



        globalOrientation = globalOrientation + changeInOrientation;
        lastLeftPosition = leftEncoderPosition;
        lastRightPosition = rightEncoderPositon;




    }
}

