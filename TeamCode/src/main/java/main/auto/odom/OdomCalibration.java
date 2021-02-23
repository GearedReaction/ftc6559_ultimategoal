package main.auto.odom;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.File;

@Autonomous(name = "OdomCalibration", group = "Odometry")
public class OdomCalibration extends LinearOpMode {

    DcMotor leftFront, leftBack, rightFront, rightBack;
    //DcMotor leftEnc, rightEnc, midEnc;

    BNO055IMU imu;

    ElapsedTime timer = new ElapsedTime();

    static final double calibrationSpeed = 0;

    static final double TICKS_PER_REV = 8192;
    static final double WHEEL_DIAMETER = 100/25.4;

    static final double TICKS_PER_INCH = WHEEL_DIAMETER * Math.PI / TICKS_PER_REV;

    File sideWheelsSeparationFile = AppUtil.getInstance().getSettingsFile("sideWheelsSeparationFile");
    File middleTickOffsetFile = AppUtil.getInstance().getSettingsFile("middleTickOffsetFile");

    public void runOpMode(){

        leftFront = hardwareMap.dcMotor.get("leftFront");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        rightBack = hardwareMap.dcMotor.get("rightBack");

        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addData("Left: ", rightFront.getCurrentPosition());
        telemetry.addData("Mid: ", leftBack.getCurrentPosition());
        telemetry.addData("Right: ", rightBack.getCurrentPosition());
        telemetry.update();


        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        resetOdomEncoders();

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        telemetry.addData("STATUS: ", "Ready!");
        telemetry.update();

        waitForStart();

        while(imu.getAngularOrientation().firstAngle < 90 && opModeIsActive()){
            telemetry.addData("Orientation: ", imu.getAngularOrientation().firstAngle);
            telemetry.update();
            leftFront.setPower(-calibrationSpeed);
            leftBack.setPower(-calibrationSpeed);
            rightFront.setPower(-calibrationSpeed);
            rightBack.setPower(-calibrationSpeed);
            if(imu.getAngularOrientation().firstAngle < 60) {
                leftFront.setPower(-calibrationSpeed);
                leftBack.setPower(-calibrationSpeed);
                rightFront.setPower(-calibrationSpeed);
                rightBack.setPower(-calibrationSpeed);
            }
            else{
                leftFront.setPower(-calibrationSpeed / 2);
                leftBack.setPower(-calibrationSpeed / 2);
                rightFront.setPower(-calibrationSpeed / 2);
                rightBack.setPower(-calibrationSpeed / 2);
            }
        }
        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);

        timer.reset();
        while(timer.seconds() < 1 && opModeIsActive()){
            telemetry.addData("STATUS: ", "Calibrating");
            telemetry.update();
        }

        double angle = imu.getAngularOrientation().firstAngle;
        double encoderDifference = Math.abs(Math.abs(rightFront.getCurrentPosition()) - Math.abs(rightBack.getCurrentPosition()));
        double sideEncoderTickOffset = encoderDifference / angle;
        double sideWheelSeparation = (180 * sideEncoderTickOffset) / (TICKS_PER_INCH * Math.PI);
        double middleTickOffset = leftBack.getCurrentPosition() / Math.toRadians(imu.getAngularOrientation().firstAngle);

        ReadWriteFile.writeFile(sideWheelsSeparationFile, String.valueOf(sideWheelSeparation));
        ReadWriteFile.writeFile(middleTickOffsetFile, String.valueOf(middleTickOffset));

        timer.reset();
        while(timer.seconds() < 1 && opModeIsActive()){
            telemetry.addData("STATUS: ", "COMPLETE");
            telemetry.update();
        }

        telemetry.addData("SideWheelSeparation: ", sideWheelSeparation);
        telemetry.addData("MiddleTickOffset: ", middleTickOffset);
        telemetry.update();

    }


    void resetOdomEncoders(){
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

}
