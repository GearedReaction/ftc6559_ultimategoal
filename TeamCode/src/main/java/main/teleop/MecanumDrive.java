package main.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import static java.lang.Math.*;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "Mecanum Drive", group = "Drives")


public class MecanumDrive extends OpMode {
    DcMotor leftFrontMotor, leftBackMotor, rightFrontMotor, rightBackMotor;
    double leftX, leftY;
    double LFPower,RFPower,LBPower,RBPower;
    double maxMPower;
    public void init(){
        leftFrontMotor = hardwareMap.dcMotor.get("leftFrontMotor");
        leftBackMotor = hardwareMap.dcMotor.get("leftBackMotor");
        rightFrontMotor = hardwareMap.dcMotor.get("rightFrontMotor");
        rightBackMotor = hardwareMap.dcMotor.get("rightBackMotor");



    }

    public void loop(){
        mecanumDrive(gamepad1.left_stick_x,-gamepad1.left_stick_y,gamepad1.right_stick_x);

    }

    public void mecanumDrive(double leftX, double leftY,double rightX){
        LFPower = leftY + leftX + rightX;
        RFPower = leftY - leftX - rightX;
        LBPower = leftY - leftX + rightX;
        RBPower = leftY + leftX - rightX;

        maxMPower = Math.max(max(max(abs(LFPower),abs(RFPower)),abs(RBPower)),abs(LBPower));

        maxMPower = maxMPower > 1 ? maxMPower : 1;

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
