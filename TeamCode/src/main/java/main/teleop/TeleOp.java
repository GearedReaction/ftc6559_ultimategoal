package main.teleop;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "TeleOp", group = "ftc6559")
public class TeleOp extends DriveFunction {

    public void runAutonomous() {
        try {


        } catch(Exception e) {
            autoThread.interrupt();
            telemetry.addData("AUTONOMOUS WAS INTERRUPTED", e);
        }
    }
}
