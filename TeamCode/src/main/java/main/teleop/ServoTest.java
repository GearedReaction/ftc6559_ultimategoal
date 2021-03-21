package main.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp (name = "Servo Test", group = "Testing")
public class ServoTest extends OpMode {

    Servo myServo;
    Servo myServo2;
    int servoPosition = 0;
    int clawPos = 0;

    public void init() {
        myServo = hardwareMap.servo.get("wobbleArm");
        myServo2 = hardwareMap.servo.get("wobbleClaw");
    }

    public void start() {
        myServo.setPosition(servoPosition);
        myServo2.setPosition(clawPos);
        telemetry.addData("Arm Position: ", servoPosition);
    }

    public void loop() {

        telemetry.update();

        while (gamepad1.right_trigger > 0.01) {
            servoPosition += gamepad1.right_trigger;
            myServo.setPosition(servoPosition);
        }

        while (gamepad1.left_trigger > 0.01) {
            servoPosition -= gamepad1.left_trigger;
            myServo.setPosition(servoPosition);
        }

        while (gamepad1.right_bumper) {
            clawPos += 0.1;
            myServo2.setPosition(clawPos);
        }

        while (gamepad1.left_bumper) {
            clawPos -= 0.1;
            myServo2.setPosition(clawPos);
        }

        if (gamepad1.b) {
            servoPosition = 0;
            myServo.setPosition(servoPosition);
        }

        if (gamepad1.a) {
            servoPosition = 1;
            myServo.setPosition(servoPosition);
        }

        if (gamepad1.x) {
            clawPos = 0;
            myServo2.setPosition(servoPosition);
        }

        if (gamepad1.y) {
            clawPos = 1;
            myServo2.setPosition(servoPosition);
        }

    }

    public void stop() {

    }

}
