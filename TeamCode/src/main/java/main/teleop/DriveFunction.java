package main.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_TO_POSITION;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;
import static java.lang.Math.abs;


public abstract class DriveFunction extends OpMode {

    public final double TICKS_PER_ROTATION = 747;
    public final double TICKS_PER_INCH = 140;
    public double speedMode = 1;

    private long maxTime;


    DcMotor rfMotor;
    DcMotor rbMotor;
    DcMotor lfMotor;
    DcMotor lbMotor;

    String direction;

    protected Thread autoThread = new Thread(new Runnable() {
        @Override
        public void run() {
            runAutonomous();
        }
    });

    @Override
    public void init() {
        rfMotor = hardwareMap.dcMotor.get("front_right_motor");
        rbMotor = hardwareMap.dcMotor.get("back_right_motor");
        lfMotor = hardwareMap.dcMotor.get("front_left_motor");
        lbMotor = hardwareMap.dcMotor.get("back_left_motor");

        lfMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        lbMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void start() {
        autoThread.start();
    }

    @Override
    public void loop() {
        //Modes depending on Dpad Input
        if (gamepad1.dpad_up) {
            //TurboMode
            speedMode = 2;
        } else if (gamepad1.dpad_down) {
            //Slow Mode
            speedMode = 0.25;
        } else if (gamepad1.dpad_right) {
            //Normal
            speedMode = 0;
        } else if (gamepad1.dpad_left) {
            //Reverse
            speedMode = -1;
        }
    }

    public void moveBot(String direction, double distance, double speed) {

        setMotorRunMode(STOP_AND_RESET_ENCODER);

        int newTargetLF;
        int newTargetLB;
        int newTargetRF;
        int newTargetRB;

        if (speedMode == -1) {
            switch (direction) {
                case "f":
                    direction = "b";
                    break;
                case "r":
                    direction = "l";
                    break;
                case "l":
                    direction = "r";
                    break;
                case "b":
                    direction = "f";
                    break;

            }
        } else {
            speed *= speedMode;
        }
        speed = abs(speed) >= 1 ? 1 : speed;

        double positionDiff = distance * TICKS_PER_INCH;

        switch (direction) {
            case "f":
                newTargetLF = lfMotor.getCurrentPosition() + (int) (positionDiff);
                newTargetLB = lbMotor.getCurrentPosition() + (int) (positionDiff);
                newTargetRF = rfMotor.getCurrentPosition() + (int) (positionDiff);
                newTargetRB = rbMotor.getCurrentPosition() + (int) (positionDiff);
                break;
            case "r":
                newTargetLF = lfMotor.getCurrentPosition() + (int) (positionDiff);
                newTargetLB = lbMotor.getCurrentPosition() - (int) (positionDiff);
                newTargetRF = rfMotor.getCurrentPosition() - (int) (positionDiff);
                newTargetRB = rbMotor.getCurrentPosition() + (int) (positionDiff);
                break;
            case "l":
                newTargetLF = lfMotor.getCurrentPosition() - (int) (positionDiff);
                newTargetLB = lbMotor.getCurrentPosition() + (int) (positionDiff);
                newTargetRF = rfMotor.getCurrentPosition() + (int) (positionDiff);
                newTargetRB = rbMotor.getCurrentPosition() - (int) (positionDiff);
                break;
            case "b":
                newTargetLF = lfMotor.getCurrentPosition() - (int) (positionDiff);
                newTargetLB = lbMotor.getCurrentPosition() - (int) (positionDiff);
                newTargetRF = rfMotor.getCurrentPosition() - (int) (positionDiff);
                newTargetRB = rbMotor.getCurrentPosition() - (int) (positionDiff);
                break;
            default:
                newTargetLF = lfMotor.getCurrentPosition();
                newTargetLB = lbMotor.getCurrentPosition();
                newTargetRF = rfMotor.getCurrentPosition();
                newTargetRB = rbMotor.getCurrentPosition();
        }


        rfMotor.setTargetPosition(newTargetRF);
        rbMotor.setTargetPosition(newTargetRB);
        lfMotor.setTargetPosition(newTargetLF);
        lbMotor.setTargetPosition(newTargetLB);

        setMotorRunMode(RUN_TO_POSITION);

        lfMotor.setPower(speed);
        lbMotor.setPower(speed);
        rfMotor.setPower(speed);
        rbMotor.setPower(speed);

        try {
            while (lfMotor.isBusy() && lbMotor.isBusy() && rfMotor.isBusy() && rbMotor.isBusy()) {

            }
        } catch (Exception e) {
            telemetry.addData("Error", e);
        }

        lfMotor.setPower(0);
        lbMotor.setPower(0);
        rfMotor.setPower(0);
        rbMotor.setPower(0);

        setMotorRunMode(RUN_USING_ENCODER);
    }

    public void turnBot(String direction, double distance, double speed) {

        setMotorRunMode(STOP_AND_RESET_ENCODER);

        int newTargetLF;
        int newTargetLB;
        int newTargetRF;
        int newTargetRB;

        if (speedMode == -1) {
            switch (direction) {
                case "r":
                    direction = "l";
                    break;
                case "l":
                    direction = "r";
                    break;
            }
        } else {
            speed *= speedMode;
        }
        speed = abs(speed) >= 1 ? 1 : speed;

        double positionDiff = distance * TICKS_PER_INCH;

        switch (direction) {
            case "l":
                newTargetLF = lfMotor.getCurrentPosition() - (int) (positionDiff);
                newTargetLB = lbMotor.getCurrentPosition() - (int) (positionDiff);
                newTargetRF = rfMotor.getCurrentPosition() + (int) (positionDiff);
                newTargetRB = rbMotor.getCurrentPosition() + (int) (positionDiff);
                break;
            case "r":
                newTargetLF = lfMotor.getCurrentPosition() + (int) (positionDiff);
                newTargetLB = lbMotor.getCurrentPosition() + (int) (positionDiff);
                newTargetRF = rfMotor.getCurrentPosition() - (int) (positionDiff);
                newTargetRB = rbMotor.getCurrentPosition() - (int) (positionDiff);
                break;
            default:
                newTargetLF = lfMotor.getCurrentPosition();
                newTargetLB = lbMotor.getCurrentPosition();
                newTargetRF = rfMotor.getCurrentPosition();
                newTargetRB = rbMotor.getCurrentPosition();
        }


        rfMotor.setTargetPosition(newTargetRF);
        rbMotor.setTargetPosition(newTargetRB);
        lfMotor.setTargetPosition(newTargetLF);
        lbMotor.setTargetPosition(newTargetLB);

        setMotorRunMode(RUN_TO_POSITION);

        lfMotor.setPower(speed);
        lbMotor.setPower(speed);
        rfMotor.setPower(speed);
        rbMotor.setPower(speed);

        try {
            while (lfMotor.isBusy() && lbMotor.isBusy() && rfMotor.isBusy() && rbMotor.isBusy()) {

            }
        } catch (Exception e) {
            telemetry.addData("Error", e);
        }

        lfMotor.setPower(0);
        lbMotor.setPower(0);
        rfMotor.setPower(0);
        rbMotor.setPower(0);

        setMotorRunMode(RUN_USING_ENCODER);
    }



    public void setMotorRunMode(DcMotor.RunMode runMode){
        rfMotor.setMode(runMode);
        rbMotor.setMode(runMode);
        lfMotor.setMode(runMode);
        lbMotor.setMode(runMode);
    }


    @Override
    public void stop(){
        try{
            autoThread.interrupt();

        }catch(Exception e){
            telemetry.addData("ENCOUNTERED AN EXCEPTION", e);
        }

    }

    protected void pause(long millis){
        maxTime = System.currentTimeMillis() + millis;
        while(System.currentTimeMillis() < maxTime && !autoThread.isInterrupted()) {}
    }

    public final double ROBOT_DIAMETER = 19.15;

    public void mecanumETurn(boolean clockwise, double speed, double degrees) {
        // distance is in INCHES /

        setMotorRunMode(STOP_AND_RESET_ENCODER);

        degrees = ((ROBOT_DIAMETER * Math.PI) / 360) * degrees;

        int newTargetLF;
        int newTargetLB;
        int newTargetRF;
        int newTargetRB;

        double positionDiff = degrees * TICKS_PER_INCH;

        if (clockwise) {
            newTargetLF = lfMotor.getCurrentPosition() + (int) (positionDiff);
            newTargetLB = lbMotor.getCurrentPosition() + (int) (positionDiff);
            newTargetRF = lfMotor.getCurrentPosition() - (int) (positionDiff);
            newTargetRB = lbMotor.getCurrentPosition() - (int) (positionDiff);
        } else {
            newTargetLF = lfMotor.getCurrentPosition() - (int) (positionDiff);
            newTargetLB = lbMotor.getCurrentPosition() - (int) (positionDiff);
            newTargetRF = rfMotor.getCurrentPosition() + (int) (positionDiff);
            newTargetRB = rbMotor.getCurrentPosition() + (int) (positionDiff);
        }

        lfMotor.setTargetPosition(newTargetLF);
        lbMotor.setTargetPosition(newTargetLB);
        rfMotor.setTargetPosition(newTargetRF);
        rbMotor.setTargetPosition(newTargetRB);

        setMotorRunMode(RUN_TO_POSITION);

        lfMotor.setPower(speed);
        lbMotor.setPower(speed);
        rfMotor.setPower(speed);
        rbMotor.setPower(speed);


        try {
            while (lfMotor.isBusy() && lbMotor.isBusy() && rfMotor.isBusy() && rbMotor.isBusy()) {

            }
        } catch (Exception e) {
            telemetry.addData("Error", e);
        }

        lfMotor.setPower(0);
        lbMotor.setPower(0);
        rfMotor.setPower(0);
        rbMotor.setPower(0);

        setMotorRunMode(RUN_USING_ENCODER);
    }

    public abstract void runAutonomous();
}

