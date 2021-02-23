package main.teleop;

import com.qualcomm.robotcore.hardware.Servo;

public class WobbleArmClass extends MecanumDrive {

    Servo wobbleArm;
    Servo wobbleClaw;

    public void init() {
        wobbleArm = hardwareMap.servo.get("wobbleArm");
        wobbleClaw = hardwareMap.servo.get("wobbleClaw");
    }

    public void start() {
        wobbleArm.setPosition(0);
        wobbleClaw.setPosition(clawOpenPos);
    }


    /** Autonomous **/
    public void lowerArm() {
        wobbleArm.setPosition(1);
    }

    public void raiseArm() {
        wobbleArm.setPosition(0);
    }

    public void closeClaw() {
        wobbleClaw.setPosition(clawClosedPos);
    }

    public void openClaw() {
        wobbleClaw.setPosition(clawOpenPos);
    }

    /** TeleOP **/

    public void raiseArmTeleop() {
        armPosition += gamepad1.right_trigger * servoSpeed;
        wobbleArm.setPosition(armPosition);
    }

    public void lowerArmTeleop() {
        armPosition -= gamepad1.left_trigger * servoSpeed;
        wobbleArm.setPosition(armPosition);
    }

}
