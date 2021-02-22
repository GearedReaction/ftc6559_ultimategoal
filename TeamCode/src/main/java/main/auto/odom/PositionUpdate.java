package main.auto.odom;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "Odom Position Test", group = "Odometry")
public class PositionUpdate extends LinearOpMode {

    DcMotor leftFront, rightFront, leftBack, rightBack;

    static final double TICKS_PER_REV = 8192;
    static final double WHEEL_DIAMETER = 38/25.4;

    static final double TICKS_PER_INCH = WHEEL_DIAMETER * Math.PI / TICKS_PER_REV;

    GlobalCoordinates positionUpdate;

    public void runOpMode(){
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        rightBack = hardwareMap.dcMotor.get("rightBack");


        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);

        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        resetOdomEncoders();

        waitForStart();

        positionUpdate = new GlobalCoordinates(rightFront, rightBack, leftBack, TICKS_PER_INCH, 100);
        Thread position = new Thread(positionUpdate);
        position.start();

        while(opModeIsActive()){
            mecanumMove(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
            telemetry.addData("X POSITION: ", positionUpdate.returnXCoordinate() / TICKS_PER_INCH);
            telemetry.addData("Y POSITION: ", positionUpdate.returnYCoordinate() / TICKS_PER_INCH);
            telemetry.addData("VALUE: ", positionUpdate.returnValue());
            telemetry.addData("ORIENTATION: ", positionUpdate.returnOrientation());
            telemetry.update();
        }
        positionUpdate.stop();
    }


    Double lF, rF, lB, rB, maxVector;

    protected void mecanumMove(double leftX, double leftY, double rightX) {
        lF = leftX - leftY + rightX;
        rF = leftX + leftY + rightX;
        lB = leftX + leftY + rightX;
        rB = -leftX + leftY + rightX;


        maxVector = Math.max(Math.max(Math.abs(lF), Math.abs(rF)),
                Math.max(Math.abs(lB), Math.abs(rB)));

        maxVector = maxVector > 1 ? maxVector : 1;


        leftFront.setPower(lF / maxVector);
        rightFront.setPower(rF / maxVector);
        leftBack.setPower(lB / maxVector);
        rightBack.setPower(rB / maxVector);


    }

    void resetOdomEncoders(){
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

//        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

}
