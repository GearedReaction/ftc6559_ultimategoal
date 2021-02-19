package main.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import static java.lang.Math.*;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "Mecanum Drive", group = "Drives")


public class MecanumDrive extends OpMode {
    DcMotor leftFrontMotor, leftBackMotor, rightFrontMotor, rightBackMotor;
    Servo wobbleArm, wobbleClaw;
    double leftX, leftY;
    double LFPower,RFPower,LBPower,RBPower;
    double maxMPower;
    double speedMode = 1;
    double armPosition, clawPosition;
    double clawOpenPos = 0.2;
    double clawClosedPos = 0.05;
    double servoSpeed = 0.5;

    public void init(){
        leftFrontMotor = hardwareMap.dcMotor.get("leftFrontMotor");
        leftBackMotor = hardwareMap.dcMotor.get("leftBackMotor");
        rightFrontMotor = hardwareMap.dcMotor.get("rightFrontMotor");
        rightBackMotor = hardwareMap.dcMotor.get("rightBackMotor");

        wobbleArm = hardwareMap.servo.get("wobbleArm");
        wobbleClaw = hardwareMap.servo.get("wobbleClaw");

        wobbleArm.setPosition(0);
        wobbleClaw.setPosition(clawOpenPos);
    }

    public void loop(){
        mecanumDrive(gamepad1.left_stick_x,-gamepad1.left_stick_y,gamepad1.right_stick_x);
        //Modes depending on Dpad Input
        if (gamepad1.dpad_right) {
            //Slow Mode
            speedMode = 0.5;
        } else if (gamepad1.dpad_left) {
            //Super Slow Mode
            speedMode = 0.25;
        } else if (gamepad1.dpad_up) {
            //Normal
            speedMode = 1;
        } else if (gamepad1.dpad_down) {
            //Reverse
            speedMode = -1;
        }

        armPosition = wobbleArm.getPosition();
        while (gamepad1.right_trigger > 0.1 && armPosition < 1 ) {
            armPosition += gamepad1.right_trigger * servoSpeed;
            wobbleArm.setPosition(armPosition);
        }
        while (gamepad1.left_trigger > 0.1 && armPosition > 0 ) {
            armPosition -= gamepad1.left_trigger * servoSpeed;
            wobbleArm.setPosition(armPosition);
        }
        clawPosition = wobbleClaw.getPosition();
        while (gamepad1.right_bumper && clawPosition < clawOpenPos) {
            clawPosition += servoSpeed;
            wobbleClaw.setPosition(clawPosition);
        }
        while (gamepad1.left_bumper && clawPosition > clawClosedPos) {
            clawPosition -= servoSpeed;
            wobbleClaw.setPosition(clawPosition);
        }
    }

    public void mecanumDrive(double leftX, double leftY,double rightX){
        LFPower = leftY + leftX + rightX;
        RFPower = leftY - leftX - rightX;
        LBPower = leftY - leftX + rightX;
        RBPower = leftY + leftX - rightX;

        maxMPower = Math.max(max(max(abs(LFPower),abs(RFPower)),abs(RBPower)),abs(LBPower));

        maxMPower = maxMPower > 1 ? maxMPower : 1;
        maxMPower *= speedMode;


        LFPower /= maxMPower;
        RFPower /= maxMPower;
        LBPower /= maxMPower;
        RBPower /= maxMPower;

        leftFrontMotor.setPower(LFPower);
        leftBackMotor.setPower(LBPower);
        rightFrontMotor.setPower(RFPower);
        rightBackMotor.setPower(RBPower);
    }
}
