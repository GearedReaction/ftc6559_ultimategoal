package main.auto;

import com.qualcomm.robotcore.hardware.DcMotor;

public class autointake {

    DcMotor frontIntake;
    DcMotor backIntake;

    public void autofrontIntake(int time) throws InterruptedException {
        frontIntake.setPower(1);
        wait(1000* time);
        frontIntake.setPower(0);

    }

    public void autobackIntake(int time) throws InterruptedException {
        backIntake.setPower(-1);
        wait(1000* time);
        backIntake.setPower(0);

    }



}
