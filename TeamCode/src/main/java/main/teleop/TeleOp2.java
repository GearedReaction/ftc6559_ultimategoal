package main.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import static java.lang.StrictMath.abs;

@TeleOp(name = "RV", group = "Drives")
public class TeleOp2 extends OpMode {

    DcMotor LeftFrontMotor, RightFrontMotor, LeftBackMotor, RightBackMotor;

    public void init() {
        LeftFrontMotor = hardwareMap.dcMotor.get("LeftFrontMotor");
        RightFrontMotor = hardwareMap.dcMotor.get("RightFrontMotor");
        LeftBackMotor = hardwareMap.dcMotor.get("LeftBackMotor");
        RightBackMotor = hardwareMap.dcMotor.get("RightBackMotor");

    }

    public void loop() {

        if(abs(gamepad1.right_stick_x) > 0.1) {
            LeftFrontMotor.setPower(-gamepad1.left_stick_y);
            LeftBackMotor.setPower(-gamepad1.left_stick_y);
            RightFrontMotor.setPower(gamepad1.left_stick_y);
            RightBackMotor.setPower(gamepad1.left_stick_y);
        }
        else {
            LeftFrontMotor.setPower(gamepad1.right_stick_x);
            LeftBackMotor.setPower(gamepad1.right_stick_x);
            RightFrontMotor.setPower(gamepad1.right_stick_x);
            RightBackMotor.setPower(gamepad1.right_stick_x);
        }
    }


}
