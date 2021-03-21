package org.firstinspires.ftc.teamcode.Testing.FIeldOrientatedDrive;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import net.frogbots.ftcopmodetunercommon.opmode.TunableOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import org.firstinspires.ftc.teamcode.Testing.PurePursuit.MathFunctions;
import org.firstinspires.ftc.teamcode.Testing.StrafeTesting.StrafeOpMode;

import static org.firstinspires.ftc.teamcode.Testing.PurePursuit.MathFunctions.angleWrap;
@Disabled
@TeleOp(name = "Field Orientated Drive")
public class FieldOrientatedDrive extends TunableOpMode {
    DcMotor leftFront, leftBack, rightFront, rightBack;
    Double lF, rF, lB, rB, maxVector;
    private BNO055IMU imu;
    Orientation angles;
    double heading;
    double speed;
    double lFrBPower, rFlBPower;
    int Quadrant;
    double motorPower;
    double driveAngle;
    double turn;
    //Create new IMU Parameters object.
    BNO055IMU.Parameters imuParameters = new  BNO055IMU.Parameters();

    public void init() {

        leftFront = hardwareMap.dcMotor.get("leftFront");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        rightBack = hardwareMap.dcMotor.get("rightBack");

        imu = hardwareMap.get(BNO055IMU.class, "imu");

        // Use degrees as angle unit.
        imuParameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu.initialize(imuParameters);

    }

    public void init_loop(){
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES);

        telemetry.addData("rot about z", angles.firstAngle);
        telemetry.update();
    }

    public void start(){
        resetZAxis();
    }


    public void loop() {
        turn = gamepad1.right_stick_x;

        heading = getAngle();

        speed = getSpeed();

        driveAngle = Math.abs(gamepad1.left_stick_y) == 1 ? Math.toDegrees(Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x)) - heading : 0;
        if(gamepad1.right_trigger > 0.2){
            mecanumMove(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
        }else {
            strafeDrive(Math.toDegrees(Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x)) - heading, speed);
        }

        telemetry.addData("rot about Z", heading);
        telemetry.addData("GAMEPAD ANGLE", Math.toDegrees(Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x)));
        telemetry.addData("Left Front MOTOR POWER: ", leftFront.getPower());
        telemetry.addData("Right Front MOTOR POWER: ", rightFront.getPower());
        telemetry.addData("Left Back MOTOR POWER: ", leftBack.getPower());
        telemetry.addData("Right Back MOTOR POWER: ", rightBack.getPower());

        telemetry.update();

    }


    protected void mecanumMove(double leftX, double leftY, double rightX) {

            lF = -leftX + leftY - rightX;
            rF = -leftX - leftY - rightX;
            lB = leftX + leftY - rightX;
            rB = leftX - leftY - rightX;


        maxVector = Math.max(Math.max(Math.abs(lF), Math.abs(rF)),
                Math.max(Math.abs(lB), Math.abs(rB)));

        maxVector = maxVector > 1 ? maxVector : 1;

        leftFront.setPower(lF / maxVector);
        rightFront.setPower(rF / maxVector);
        leftBack.setPower(lB / maxVector);
        rightBack.setPower(rB / maxVector);
    }

    public double getAngle(){
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        heading = angles.firstAngle;
        return heading;
    }


    public void resetZAxis(){
        BNO055IMU.Parameters imuParams = new BNO055IMU.Parameters();
        imu.initialize(imuParams);
    }

    public double getSpeed() {
        if(turn == 0){ speed = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);}
        else {
            speed = (Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y) + turn) ;
        }
        return speed;
    }

    public double giveMeMotorPowers(double angle){

        angle = angleWrap(angle);

        giveMeAngleQuadrant(angle);

        double a =  0.00000251762;
        double b = -0.000340768;
        double c =  0.0323741;
        double d = -0.996608;

        if(Quadrant == 1){
            motorPower = ((a*Math.pow(angle, 3)) + (b*Math.pow(angle, 2)) + (c*angle + d));
        }else if(Quadrant == 2){
            motorPower = -(a*Math.pow((angle - 90), 3) + b*(Math.pow((angle - 90), 2)) + c*(angle - 90) + d);
        }else if(Quadrant == 3){
            motorPower = -(a*Math.pow((angle - 180), 3) + b*(Math.pow((angle - 180), 2)) + c*(angle - 180) + d);
        }else if(Quadrant == 4){
            motorPower = a*Math.pow((angle - 270), 3) + b*(Math.pow((angle - 270), 2)) + c*(angle - 270) + d;
        }


        return motorPower;
    }

    public double angleWrap(double angle){
        while (angle > 360) {
            angle -= 360;
        } while(angle < 0){
            angle += 360;
        }

        return angle;
    }

    public int giveMeAngleQuadrant(double angle){
        angleWrap(angle);

        if (angle <= 90) {
            Quadrant = 1;
        } else if (angle <= 180) {
            Quadrant = 2;
        } else if (angle <= 270) {
            Quadrant = 3;
        } else if (angle <= 360) {
            Quadrant = 4;
        }

        return Quadrant;
    }

    public void strafeDrive(double angle, double speed){

        giveMeAngleQuadrant(angle);

        switch(Quadrant) {
            case 1:
                lFrBPower = 1 * speed;
                rFlBPower = giveMeMotorPowers(angle) * speed;

                break;

            case 2:
                lFrBPower = giveMeMotorPowers(angle) * speed;
                rFlBPower = 1 * speed;

                break;

            case 3:
                lFrBPower = -1 * speed;
                rFlBPower = giveMeMotorPowers(angle) * speed;

                break;

            case 4:
                lFrBPower = giveMeMotorPowers(angle) * speed;
                rFlBPower= -1 * speed;

                break;

        }

        leftFront.setPower(-lFrBPower - gamepad1.right_stick_x);
        rightFront.setPower(rFlBPower - gamepad1.right_stick_x);
        leftBack.setPower(rFlBPower - gamepad1.right_stick_x);
        rightBack.setPower(lFrBPower - gamepad1.right_stick_x);

    }
}
