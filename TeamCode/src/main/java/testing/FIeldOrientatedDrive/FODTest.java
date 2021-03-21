package org.firstinspires.ftc.teamcode.Testing.FIeldOrientatedDrive;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import static java.lang.Math.atan2;

@Disabled
@TeleOp(name = "FOD Test", group = "test")
public class FODTest extends OpMode {
    DcMotor leftFront, rightFront, leftBack, rightBack;
    double lF, rF, lB, rB, maxVector, rFlBPower, lFrBPower;
    double motorPower = 0.0;

    //Define Gyro
    private BNO055IMU imu;

    //Create new IMU Parameters object.
    BNO055IMU.Parameters imuParameters = new  BNO055IMU.Parameters();
    Orientation angles;

    double heading, angle, speed;

    public void init(){
        leftFront = hardwareMap.dcMotor.get("leftFront");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        rightBack = hardwareMap.dcMotor.get("rightBack");
        rightFront = hardwareMap.dcMotor.get("rightFront");

        //Initialize Gyro
        imu = hardwareMap.get(BNO055IMU.class, "imu");

        // Use degrees as angle unit.
        imuParameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu.initialize(imuParameters);
    }

    public void loop(){
        heading = getHeading();
        telemetry.addData("Heading: ", heading);

        angle = atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x) - heading;

        fieldOrientatedDrive(angle, 1);
    }

    public void fieldOrientatedDrive(double angle, double speed){
        angle = angleWrap(angle);

        angle -= heading;

        if(angle <=90) {
            lFrBPower = 1 * speed - gamepad1.right_stick_x;
            rFlBPower = giveMeMotorPowers(angle) * speed - gamepad1.right_stick_x;
        }
        else if(angle <= 180) {
            lFrBPower = giveMeMotorPowers(angle) * speed - gamepad1.right_stick_x;
            rFlBPower = 1 * speed - gamepad1.right_stick_x;
        }
        else if(angle <= 270) {
            lFrBPower = -1 * speed - gamepad1.right_stick_x;
            rFlBPower = giveMeMotorPowers(angle) * speed - gamepad1.right_stick_x;
        }
        else if(angle <= 360) {
            lFrBPower = giveMeMotorPowers(angle) * speed - gamepad1.right_stick_x;
            rFlBPower = -1 * speed - gamepad1.right_stick_x;
        }

        maxVector = Math.max(Math.max(Math.abs(lF), Math.abs(rF)),
                Math.max(Math.abs(lB), Math.abs(rB)));

        maxVector = maxVector > 1 ? maxVector : 1;


        leftFront.setPower(-lFrBPower/maxVector);
        rightFront.setPower(rFlBPower/maxVector);
        leftBack.setPower(rFlBPower/maxVector);
        rightBack.setPower(lFrBPower/maxVector);
    }

    public double getHeading(){
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        heading = angles.firstAngle;
        return heading;
    }

    private double angleWrap(double angle){
        while (angle > 360) {
            angle -= 360;
        } while(angle < 0){
            angle += 360;
        }

        return angle;
    }

    private double giveMeMotorPowers(double angle){

        double a =  0.00000251762;
        double b = -0.000340768;
        double c =  0.0323741;
        double d = -0.996608;

        if(angle <= 90){
            motorPower = ((a*Math.pow(angle, 3)) + (b*Math.pow(angle, 2)) + (c*angle + d));
        }else if(angle <= 180){
            motorPower = -(a*Math.pow((angle - 90), 3) + b*(Math.pow((angle - 90), 2)) + c*(angle - 90) + d);
        }else if(angle <= 270){
            motorPower = -(a*Math.pow((angle - 180), 3) + b*(Math.pow((angle - 180), 2)) + c*(angle - 180) + d);
        }else if(angle <= 360){
            motorPower = a*Math.pow((angle - 270), 3) + b*(Math.pow((angle - 270), 2)) + c*(angle - 270) + d;
        }

        return motorPower;
    }


}
