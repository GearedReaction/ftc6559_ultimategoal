package main.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import static java.lang.Math.pow;

@TeleOp(name = "CLICK THIS PARKER")
public class MecanumTest extends OpMode {

    DcMotor leftFront, leftBack, rightFront, rightBack;
    Double lF, rF, lB, rB, maxVector;


    public void init() {
        leftFront = hardwareMap.dcMotor.get("leftFront");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        rightBack = hardwareMap.dcMotor.get("rightBack");

        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);

    }


    public void loop() {
        mecanumMove(pow(gamepad1.left_stick_x, 3), pow(-gamepad1.left_stick_y, 3), pow(gamepad1.right_stick_x, 3));

        telemetry.addData("Left: ", rightFront.getCurrentPosition());
        telemetry.addData("Mid: ", leftBack.getCurrentPosition());
        telemetry.addData("Right: ", rightBack.getCurrentPosition());
        telemetry.update();

    }


    protected void mecanumMove(double leftX, double leftY, double rightX) {
        lF = leftX + leftY + rightX;
        rF = -leftX + leftY - rightX;
        lB = -leftX + leftY + rightX;
        rB = leftX + leftY - rightX;


        maxVector = Math.max(Math.max(Math.abs(lF), Math.abs(rF)),
                Math.max(Math.abs(lB), Math.abs(rB)));

        maxVector = maxVector > 1 ? maxVector : 1;


        leftFront.setPower(lF / maxVector);
        rightFront.setPower(rF / maxVector);
        leftBack.setPower(lB / maxVector);
        rightBack.setPower(rB / maxVector);

        telemetry.addData("LEFT X:", gamepad1.left_stick_x);
        telemetry.addData("LEFT Y:", gamepad1.left_stick_y);
        telemetry.addData("RIGHT X:", gamepad1.right_stick_x);

        telemetry.addData("LF: ", leftFront.getPower());
        telemetry.addData("RF: ", rightFront.getPower());
        telemetry.addData("LB: ", leftBack.getPower());
        telemetry.addData("RB: ", rightBack.getPower());

    }
}
