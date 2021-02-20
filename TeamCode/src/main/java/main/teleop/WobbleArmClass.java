package main.teleop;

import com.qualcomm.robotcore.hardware.Servo;

public class WobbleArmClass extends DriveFunction {

    Servo wobbleArm;
    Servo wobbleClaw;
    double armPosition = 0.0;
    double clawOpenPosition = 0.5;
    double clawClosedPostion = 0.1;

    @Override
    public void runAutonomous() {
        wobbleArm = hardwareMap.servo.get("wobbleArm");
        wobbleClaw = hardwareMap.servo.get("wobbleClaw");
    }

    public void lowerArm() {
        wobbleArm.setPosition(1);
    }

    public void raiseArm() {
        wobbleArm.setPosition(0);
    }

    public void closeClaw() {
        wobbleClaw.setPosition(clawClosedPostion);
    }

    public void openClaw() {
        wobbleClaw.setPosition(clawOpenPosition);

    }


}
