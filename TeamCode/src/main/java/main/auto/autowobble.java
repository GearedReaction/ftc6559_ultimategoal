package main.auto;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;


public class autowobble extends OpMode {

    Servo wobbleServo;
    double servoPosition = 0.0;

    public void init() {
        wobbleServo = hardwareMap.servo.get("wobbleServo");

    }

    public void start() {
        wobbleServo.setPosition(0);

        for (int i = 0; i < 1; i += 0.01) {
            servoPosition += i;
            wobbleServo.setPosition(servoPosition);

        }

        for (int i = 1; i > 0; i -= 0.01) {
            servoPosition -= i;
            wobbleServo.setPosition(servoPosition);

        }
    }

    public void loop() {

    }

    public void stop() {

    }
}
