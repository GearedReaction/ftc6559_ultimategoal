package main.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.concurrent.Callable;

import static java.lang.Math.*;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "Mecanum Drive", group = "Drives")


public class MecanumDrive extends OpMode {
    DcMotor leftFrontMotor, leftBackMotor, rightFrontMotor, rightBackMotor, flyWheelMotor;
    Servo shootServo;
    double leftX, leftY;
    double LFPower,RFPower,LBPower,RBPower;
    double maxMPower;
    double speedMode = 1;
    int rings = 1;

    boolean lastADown = false;
    boolean lastBDown = false;

    double highGoalV = 555;
    double powerShotV = 555;


    public void init(){
        leftFrontMotor = hardwareMap.dcMotor.get("leftFrontMotor");
        leftBackMotor = hardwareMap.dcMotor.get("leftBackMotor");
        rightFrontMotor = hardwareMap.dcMotor.get("rightFrontMotor");
        rightBackMotor = hardwareMap.dcMotor.get("rightBackMotor");

        flyWheelMotor = hardwareMap.dcMotor.get("flyWheelMotor");
        flyWheelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
      //  flyWheelMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shootServo = hardwareMap.servo.get("shootServo");

    }

    public void loop(){
        mecanumDrive(gamepad1.left_stick_x,-gamepad1.left_stick_y,gamepad1.right_stick_x);
        /**Modes depending on Dpad Input**/
        if (gamepad1.dpad_up) {
            //TurboMode
            speedMode = 2;
        } else if (gamepad1.dpad_down) {
            //Slow Mode
            speedMode = 0.25;
        } else if (gamepad1.dpad_right) {
            //Normal
            speedMode = 1;
        } else if (gamepad1.dpad_left) {
            //Reverse
            speedMode = -1;
        }

        /**Shooting**/
        //Button Inputs
        if(gamepad1.a && !lastADown){
            try {
                shootFunction(highGoalV, 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if(gamepad1.b && !lastBDown){
            try {
                shootFunction(highGoalV, 3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //Button Logic
        lastADown = gamepad1.a;
        lastBDown = gamepad1.b;

    }

    public void mecanumDrive(double leftX, double leftY,double rightX){
        LFPower = leftY + leftX + rightX;
        RFPower = leftY - leftX - rightX;
        LBPower = leftY - leftX + rightX;
        RBPower = leftY + leftX - rightX;

        maxMPower = Math.max(max(max(abs(LFPower),abs(RFPower)),abs(RBPower)),abs(LBPower));

        maxMPower = maxMPower > 1 ? maxMPower : 1;
        if (speedMode == 2) {
            maxMPower *= speedMode;
            maxMPower = maxMPower > 1 ? maxMPower : 1;
        } else {
            maxMPower *= speedMode;
        }


        LFPower /= maxMPower;
        RFPower /= maxMPower;
        LBPower /= maxMPower;
        RBPower /= maxMPower;
       

        leftFrontMotor.setPower(LFPower);
        leftBackMotor.setPower(LBPower);
        rightFrontMotor.setPower(RFPower);
        rightBackMotor.setPower(RBPower);
    }
    public void shootFunction(double spinPower, int times) throws InterruptedException {
        CustomPID shooter = new CustomPID();
        shooter.spinFlyWheel(spinPower);
            for(int i = 0; i < times; i++){
                shootServo.setPosition(.25);
                wait(200);
                shootServo.setPosition(.25);
                wait(200);
        }



    }
}
