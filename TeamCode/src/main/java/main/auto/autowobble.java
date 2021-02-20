package main.auto;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class autowobble extends OpMode {

    Servo wobbleArm;
    Servo wobbleClaw;
    double armPosition = 0.0;
    double clawOpenPosition = 0.5;
    double clawClosedPostion = 0.1;

    public void init() {
        wobbleArm = hardwareMap.servo.get("wobbleArm");
        wobbleClaw = hardwareMap.servo.get("wobbleClaw");
    }

    public void start() {
        wobbleArm.setPosition(0);
        wobbleClaw.setPosition(clawOpenPosition);
    }

    public void loop() {

    }

    public void lowerArm() {

    }
}
