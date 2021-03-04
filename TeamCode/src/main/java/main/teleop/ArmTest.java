package main.teleop;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
@Disabled
@TeleOp(name = "Test Arm")
public class ArmTest extends OpMode {
    boolean picksUp = true, lastLButton, lastB, lastDRight, lastA, lastLBumper, lastRBumper, lastY, lastX, lastDUp, lastDDown, lastDLeft, slomo,
            intakeIn = true, intakeOut, isReverseDrive = false, lastRightTrigger, lastLeftTrigger;

    //Servo Positions
    double rotatorIntakePosition = 1;
    double rotatorRotationPositionOne = 0.852;
    double rotatorRotationPositionTwo = 0.97;
    double rotatorDepositPosition = 0.35;

    double rotatorOneBlockPosition = 0.2535;
    double rotatorTwoBlockPosition = 0.324;
    double rotatorThreeBlockPosition = 0.354;
    double rotatorFourBlockPosition = 0.418;

    double rotatorGripIntakePosition = 0.075;
    double rotatorGripFlipPosition = 0.8;

    double leftGripOpenPosition = 0.31;
    double leftGripClosedPosition = 0.207;

    double rightGripOpenPosition = 0.658;
    double rightGripClosedPosition = 0.722;

    double leftPickUpPosition = 0.34;
    double leftPickDownPosition = 0.659;

    double rightPickUpPosition = 0.472;
    double rightPickDownPosition = 0.147;

    double armIntakePosition = 0.2414;
    double armRotatePositionOne = 0.48;
    double armRotatePositionTwo = 0.4;
    double armMaxHeightPosition = .654;
    double armUpper45Position = .775;
    double armMaxReachPosition = .927;
    double armDownPosition = 1;

    double rotatorServoModifier = 0;

    boolean gripOpen = true;
    boolean rotatorGripFlipped = false;
    double leftGripPosition = leftGripOpenPosition;
    double rightGripPosition = rightGripOpenPosition;
    double rotatorGripPosition = rotatorGripIntakePosition;
    double rotatorServoPosition = rotatorIntakePosition;
    double armServoPosition = armIntakePosition;
    double armState = 0;

    Servo leftGrip, rightGrip, rotatorGrip, rotatorServo, armServo;
    DcMotor leftIntake, rightIntake;

    DcMotor leftFront, leftBack, rightFront, rightBack;
    Double lF, rF, lB, rB, maxVector;

    ElapsedTime runTime = new ElapsedTime();
    ElapsedTime backTime = new ElapsedTime();
    boolean runTimeReset = false;
    boolean backTimeReset = false;
    boolean blockInGrip = false;
    boolean backPressed = false;
    boolean gripsReset = false;


    public void init(){

        //Initialize Servos
        //Grip Motors
        leftGrip = hardwareMap.servo.get("leftGrip");
        rightGrip = hardwareMap.servo.get("rightGrip");
        rotatorGrip = hardwareMap.servo.get("rotatorGrip");

        //Arm Motors
        rotatorServo = hardwareMap.servo.get("rotatorServo");
        armServo = hardwareMap.servo.get("armServo");

        //Intake Motors
        leftIntake = hardwareMap.dcMotor.get("leftIntake");
        rightIntake = hardwareMap.dcMotor.get("rightIntake");

        leftFront = hardwareMap.dcMotor.get("leftFront");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        rightBack = hardwareMap.dcMotor.get("rightBack");
    }

    public void loop(){
        mecanumMove(pow(gamepad1.left_stick_x, 3)/2, pow(gamepad1.left_stick_y, 3)/2, pow(gamepad1.right_stick_x, 3)/2);
        controlArm();
        controlIntake();
        updateBooleans();
        telemetry.addData("Arm State", armState);
        telemetry.addData("Arm Servo Position", armServo.getPosition());
    }

    public void controlArm(){

        //Control Grips
        if(gripOpen)
        {
            leftGripPosition = leftGripOpenPosition;
            rightGripPosition = rightGripOpenPosition;
        }
        else {
            leftGripPosition = leftGripClosedPosition;
            rightGripPosition = rightGripClosedPosition;
        }



        //Control Arm and Rotator Servos and Rotator Grip

        if(gamepad1.dpad_right & rotatorServoPosition < 1){
            rotatorServoModifier += 0.003;
        }

        if(gamepad1.dpad_left & rotatorServoPosition > 0){
            rotatorServoModifier -= 0.003;
        }


        if(armState == 0){
            rotatorServoModifier = 0;

            armServoPosition = armIntakePosition;
            rotatorServoPosition = rotatorIntakePosition;
            rotatorGripPosition = rotatorGripIntakePosition;
            runTimeReset = false;
            blockInGrip = true;

        }

        else if(armState == 0.5){
            rotatorGripPosition = rotatorGripIntakePosition;
            armServoPosition = armRotatePositionTwo;
            rotatorServoPosition = 0.85;
        }

        else if(armState == 1) {
            if(!runTimeReset) {
                runTime.reset();
                runTimeReset = true;
            }

            if(blockInGrip) {
                gripOpen = false;

                while (runTime.milliseconds() > 250 && runTime.milliseconds() < 500) {
                    armServoPosition = armRotatePositionOne;
                    rotatorServoPosition = rotatorRotationPositionOne;
                    rotatorGripPosition = rotatorGripIntakePosition;
                    intakeIn = false;
                }

                while (runTime.milliseconds() > 900 && runTime.milliseconds() < 1250) {
                    rotatorGripPosition = rotatorGripFlipPosition;
                }
                while (runTime.milliseconds() > 1600 && runTime.milliseconds() < 1750) {
                    armServoPosition = armRotatePositionTwo;
                    rotatorServoPosition = rotatorRotationPositionTwo;
                }
            }
            else{
                armServoPosition = armRotatePositionTwo;
                rotatorServoPosition = rotatorRotationPositionTwo;
                rotatorGripPosition = rotatorGripFlipPosition;

            }

        }

        else if(armState == 2){

            armServoPosition = armMaxHeightPosition;
            rotatorServoPosition = rotatorFourBlockPosition + rotatorServoModifier;
            rotatorGripPosition = rotatorGripFlipPosition;
            intakeIn = false;
            intakeOut = false;

            runTimeReset = false;
            blockInGrip = false;
        }
        else if(armState == 3){

            armServoPosition = armUpper45Position;
            rotatorServoPosition = rotatorThreeBlockPosition + rotatorServoModifier;
            rotatorGripPosition = rotatorGripFlipPosition;
        }
        else if(armState == 4){

            armServoPosition = armMaxReachPosition;
            rotatorServoPosition = rotatorTwoBlockPosition + rotatorServoModifier;
            rotatorGripPosition = rotatorGripFlipPosition;
        }
        else if(armState == 5){

            armServoPosition = armDownPosition;
            rotatorServoPosition = rotatorOneBlockPosition + rotatorServoModifier;
            rotatorGripPosition = rotatorGripFlipPosition;
        }


        rotatorServo.setPosition(rotatorServoPosition);
        leftGrip.setPosition(leftGripPosition);
        rightGrip.setPosition(rightGripPosition);
        rotatorGrip.setPosition(rotatorGripPosition);
        armServo.setPosition(armServoPosition);

    }

    public void controlIntake(){

        if(intakeIn){
            leftIntake.setPower(-1);
            rightIntake.setPower(1);
        }else if(intakeOut){
            leftIntake.setPower(1);
            rightIntake.setPower(-1);
        } else{
            leftIntake.setPower(0);
            rightIntake.setPower(0);
        }

    }

    public void updateBooleans() {
        if (gamepad1.y && !lastY) picksUp = !picksUp;
        if (gamepad1.y) lastY = true;
        else lastY = false;

//        if(!picksUp) slomo = true;

        if (gamepad1.b && !lastB) gripOpen = !gripOpen;
        if (gamepad1.b) lastB = true;
        else lastB = false;

        if (gamepad1.a & !lastA) isReverseDrive = !isReverseDrive;
        if (gamepad1.a) lastA = true;
        else lastA = false;

        if (gamepad1.left_bumper) lastLBumper = true;
        else lastLBumper = false;

        if (gamepad1.right_bumper) lastRBumper = true;
        else lastRBumper = false;

        if (gamepad1.dpad_up && !lastDUp && armState < 6) {
            if (armState == 0.5) armState += 0.5;
            else armState += 1;
        }
        if (gamepad1.dpad_up) lastDUp = true;
        else lastDUp = false;

        if (gamepad1.dpad_down && !lastDDown && armState > 0) {
            if (armState == 1 || armState == 0.5) armState -= 0.5;
            else armState -= 1;
        }
        if (gamepad1.dpad_down) lastDDown = true;
        else lastDDown = false;

        if (gamepad1.x & !lastX) slomo = !slomo;
        if (gamepad1.x) lastX = true;
        else lastX = false;

        if (gamepad1.right_trigger > 0.2 & !lastRightTrigger) {
            intakeIn = !intakeIn;
            intakeOut = false;
        }
        if (gamepad1.right_trigger > 0.2) lastRightTrigger = true;
        else lastRightTrigger = false;

        if (gamepad1.left_trigger > 0.2 & !lastLeftTrigger) {
            intakeOut = !intakeOut;
            intakeIn = false;
        }

        if (gamepad1.left_trigger > 0.2) lastLeftTrigger = true;
        else lastLeftTrigger = false;

        if(gamepad1.back) backPressed = true;

        if (backPressed) {
            if (!backTimeReset) {
                backTime.reset();
                backTimeReset = true;
            }

            if (armState >= 1) {
                armState = backTime.milliseconds() < 300 ? 1 : 0;
            }

        }

        if(armState == 0){
            backTimeReset = false;
            backPressed = false;
        }
    }


    protected void mecanumMove(double leftX, double leftY, double rightX) {
        lF = -leftX + leftY - rightX;
        rF = -leftX - leftY - rightX;
        lB = leftX + leftY - rightX;
        rB = leftX - leftY - rightX;


        maxVector = Math.max(Math.max(Math.abs(lF), Math.abs(rF)),
                Math.max(Math.abs(lB), Math.abs(rB)));

        maxVector = maxVector > 1 ? maxVector : 1;

        leftFront.setPower(lF / maxVector);
        rightFront.setPower(rF / maxVector);
        leftBack.setPower(lB / maxVector);
        rightBack.setPower(rB / maxVector);
    }

}
