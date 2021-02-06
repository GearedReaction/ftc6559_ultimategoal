package main.teleop;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;


public class CustomPID extends DriveFunction {

    DcMotor flyWheel;
    double integral = 0;
    double reps = 0;

    PIDCoefficients testPID = new PIDCoefficients(0,0,0);
    ElapsedTime PIDTimer = new ElapsedTime();

    @Override
    public void runAutonomous() {
        flyWheel = hardwareMap.dcMotor.get("FlyWheel");
        flyWheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flyWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flyWheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

       //waitForStart();
    }

    void spinFlyWheel(double targetVelocity) throws InterruptedException {
        flyWheel.setPower(targetVelocity);
        double velocity = flyWheel.getCurrentPosition() / PIDTimer.time();
        double error = flyWheel.getCurrentPosition();
        double lastError = 0;


        while(targetVelocity != Math.abs(velocity-0.5) && reps < 40){ //9 is not important just needs to be a number
            setMotorRunMode(RUN_USING_ENCODER);
            velocity = flyWheel.getCurrentPosition() / PIDTimer.time();
            error = targetVelocity * 12 / (8 * 3.1415926535) * 360 - velocity;
            double deltaError = lastError - error;
            integral += deltaError * PIDTimer.time();
            double derivative = deltaError / PIDTimer.time();

            double P = testPID.d * error;
            double I = testPID.i * integral;
            double D = testPID.d * derivative;

            flyWheel.setPower(P + I + D);

            error  = lastError;
            reps++;
            PIDTimer.reset();
            setMotorRunMode(STOP_AND_RESET_ENCODER);
            wait(20);

        }

    }
}
