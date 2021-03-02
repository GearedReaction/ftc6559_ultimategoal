package testing.CustomOdometry;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.File;

import static java.lang.Math.*;

public class GearedOdomCalibration extends LinearOpMode {

    //Encoder Coefficients for math
    public static final int TICKS_PER_REVOLUTION = 8192;

    public static final double ENCODER_WHEEL_DIAMETER = 100/25.4; //Divide by 25.4 to get Diameter in inches

    public static final double TICKS_PER_INCH = TICKS_PER_REVOLUTION / (PI * ENCODER_WHEEL_DIAMETER);


    //Coefficients for calibrating and for Odometry
    public double wheelBaseSeparation = 0;
    public double horizontalTickOffset = 0;

    public double calibrationPower = 0.5;


    //Files to save coefficients to
    File wheelBaseSeparationFile = AppUtil.getInstance().getSettingsFile("wheelBaseSeparation.txt");
    File horizontalTickOffsetFile = AppUtil.getInstance().getSettingsFile("horizontalTickOffset.txt");


    //Define Hardware
    BNO055IMU imu;

    DcMotor leftFront, rightFront, leftBack, rightBack;

    DcMotor left, mid, right;


    //Define which motor port the odometry encoders are in
    String leftMotorName = "rightfront", midMotorName = "leftBack", rightMotorName = "rightBack";


    //Create a timer for calibration
    ElapsedTime timer = new ElapsedTime();


    /**TODO: Check to see if angle should be negative or not**/

    @Override
    public void runOpMode() throws InterruptedException {

        //Init Motors
        leftFront = hardwareMap.dcMotor.get("leftFront");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        rightBack = hardwareMap.dcMotor.get("rightBack");

        left = hardwareMap.dcMotor.get(leftMotorName);
        mid = hardwareMap.dcMotor.get(midMotorName);
        right = hardwareMap.dcMotor.get(rightMotorName);

        //Set Correct Directions
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);

        //Test code to make sure encoders are set to the right direction --> Change to negative if incorrect direction
        telemetry.addData("Left: ", rightFront.getCurrentPosition());
        telemetry.addData("Mid: ", leftBack.getCurrentPosition());
        telemetry.addData("Right: ", rightBack.getCurrentPosition());
        telemetry.update();

        //Set proper motor runmodes and reset encoders
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        resetOdomEncoders(rightFront, leftBack, rightBack);


        //Initialize IMU hardware map value. PLEASE UPDATE THIS VALUE TO MATCH YOUR CONFIGURATION
        imu = hardwareMap.get(BNO055IMU.class, "imu");

        //Initialize IMU parameters
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
        telemetry.addData("Odometry System Calibration Status", "IMU Init Complete");
        telemetry.clear();

        double orientation = imu.getAngularOrientation().firstAngle;
        double angle; //Angle for calculating coefficients

        waitForStart();

        while(opModeIsActive() && orientation < 90)
        {
            if(orientation > 60) {
                leftFront.setPower(calibrationPower/2);
                leftBack.setPower(calibrationPower/2);
                rightFront.setPower(calibrationPower/2);
                rightBack.setPower(calibrationPower/2);
            }
            else {
                leftFront.setPower(calibrationPower);
                leftBack.setPower(calibrationPower);
                rightFront.setPower(calibrationPower);
                rightBack.setPower(calibrationPower);
            }
        }
        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);

        angle = orientation;

        timer.reset();
        while(timer.milliseconds() < 1000){
            telemetry.addData("Assigning Angle to Orientation:", "In Progress");
            telemetry.clear();
        }

        telemetry.addData("Assigning Angle to Orientation:", "Complete");
        telemetry.clear();

        horizontalTickOffset = mid.getCurrentPosition() / toRadians(angle);

        int encoderDifference = abs(left.getCurrentPosition()) + abs(right.getCurrentPosition());
        double inchesTraveled = encoderDifference / TICKS_PER_INCH;

        wheelBaseSeparation = (inchesTraveled * 180) / (angle * PI);

        ReadWriteFile.writeFile(horizontalTickOffsetFile, toString().valueOf(horizontalTickOffset));
        ReadWriteFile.writeFile(wheelBaseSeparationFile, toString().valueOf(wheelBaseSeparation));

        timer.reset();
        while(timer.milliseconds() < 1000){
            telemetry.addData("Calculating Coefficients", "In Progress");
            telemetry.clear();
        }

        telemetry.addData("Calculating Coefficients", "Complete");
        telemetry.clear();

        telemetry.addData("HorizontalTickOffset", horizontalTickOffset);
        telemetry.addData("WheelbaseSeparation", wheelBaseSeparation);
        telemetry.clear();


    }


    public void resetOdomEncoders(DcMotor left, DcMotor mid, DcMotor right) {
        left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mid.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mid.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }


    }
