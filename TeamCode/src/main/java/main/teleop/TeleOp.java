package org.firstinspires.ftc.teamcode.ftc6559;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static java.lang.StrictMath.abs;

@Autonomous(name = "TeleOp", group = "ftc6559")
public class TeleOp1 extends DriveFunction {

    public void runAutonomous() {
        try {
            if(gamepad1.right_stick_y > 0.1) {
                moveBot("f", 1, -gamepad1.left_stick_y);
            } else if (gamepad1.right_stick_x < -0.1) {
                moveBot("r", 1, gamepad1.left_stick_y);
            }
            else {

            }

        } catch(Exception e) {
            autoThread.interrupt();
            telemetry.addData("AUTONOMOUS WAS INTERRUPTED", e);
        }
    }
}
