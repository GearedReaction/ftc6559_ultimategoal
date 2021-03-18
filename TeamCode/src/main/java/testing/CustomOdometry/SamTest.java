package testing.CustomOdometry;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import static java.lang.Math.pow;

@TeleOp(name = "CLICK THIS SAM")
public class SamTest extends OpMode {

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
        if(gamepad1.dpad_up) leftFront.setPower(1);
        else leftFront.setPower(0);

        if(gamepad1.dpad_down) leftBack.setPower(1);
        else leftBack.setPower(0);

        if(gamepad1.y) rightFront.setPower(1);
        else rightFront.setPower(0);

        if(gamepad1.a) rightBack.setPower(1);
        else rightBack.setPower(0);


        telemetry.addData("Left: ", rightFront.getCurrentPosition());
        telemetry.addData("Mid: ", leftBack.getCurrentPosition());
        telemetry.addData("Right: ", rightBack.getCurrentPosition());
        telemetry.update();

    }

}
