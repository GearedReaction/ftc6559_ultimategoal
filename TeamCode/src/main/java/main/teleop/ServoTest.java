package main.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp (name = "Servo Test", group = "Testing")
public class ServoTest extends OpMode {

    Servo myServo;
    Servo myServo2;
    int servoPosition = 0;

    public void init() {
        myServo = hardwareMap.servo.get("myServo");
        myServo2 = hardwareMap.servo.get("myServo2");
    }

    public void start() {
        myServo.setPosition(servoPosition);
        telemetry.addData("Servo Position: ", servoPosition);
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

        if (gamepad1.b) {
            servoPosition = 0;
            myServo.setPosition(servoPosition);
        }

        if (gamepad1.a) {
            servoPosition = 1;
            myServo.setPosition(servoPosition);
        }

    }

    public void stop() {

    }

}
