package testing.CustomOdometry;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import static java.lang.Math.*;

public class GearedOdomCalibration extends LinearOpMode {

    public static final int TICKS_PER_REVOLUTION = 8192;

    public static final double ENCODER_WHEEL_DIAMETER = 100/25.4; //Divide by 25.4 to get Diameter in inches

    public static final double TICKS_PER_INCH = 8192 / (PI * ENCODER_WHEEL_DIAMETER);

    BNO055IMU imu;

    DcMotor leftFront, rightFront, leftBack, rightBack;

    DcMotor left, mid, right;

    //Define which motor port the encoder is in

    String leftMotorName = "rightfront", midMotorName = "leftBack", rightMotorName = "rightaBack";

    /**INIT MOTORS
     * INIT GYRO
     * SET CORRECT MOTOR DIRECTIONS/REVERSE ENCODERS WHERE NEEDED
     * ROTATE 90 DEGREES TO GET ANGLE
     * CALCULATE COEFFICIENTS
     * SAVE COEFFICIENTS TO A FILE**/

    @Override
    public void runOpMode() throws InterruptedException {
        //Initialize hardware map values. PLEASE UPDATE THESE VALUES TO MATCH YOUR CONFIGURATION


        //Initialize IMU hardware map value. PLEASE UPDATE THIS VALUE TO MATCH YOUR CONFIGURATION
        imu = hardwareMap.get(BNO055IMU.class, "imu");

        //Initialize IMU parameters
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu.initialize(parameters);
        telemetry.addData("Odometry System Calibration Status", "IMU Init Complete");
        telemetry.clear();


    }



    }
