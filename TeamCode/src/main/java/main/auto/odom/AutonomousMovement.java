package main.auto.odom;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_TO_POSITION;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;
import static java.lang.Math.abs;

//import org.firstinspires.ftc.teamcode.Main.HelperClasses.Stats.BaseOpMode;
//
//import static org.firstinspires.ftc.teamcode.Main.HelperClasses.Stats.BaseOpMode.*;

@Disabled
public abstract class AutonomousMovement extends OpMode {

    /**
     * Created by union on 18年9月28日.
     */
    //Camera ID Number
    private ElapsedTime visionRuntime = new ElapsedTime();

    //0 means skystone, 1 means yellow stone
    //-1 for debug, but we can keep it like this because if it works, it should change to either 0 or 255
    private static int valMid;
    private static int valLeft;
    private static int valRight;

    private static float rectHeight = .6f/8f;
    private static float rectWidth = 1.5f/8f;

    private static float offsetX = 0f/8f;//changing this moves the three rects and the three circles left or right, range : (-2, 2) not inclusive
    private static float offsetY = 2f/8f;//changing this moves the three rects and circles up or down, range: (-4, 4) not inclusive

    private static float[] midPos = {4f/8f+offsetX, 4f/8f+offsetY};//0 = col, 1 = row
    private static float[] leftPos = {2f/8f+offsetX, 4f/8f+offsetY};
    private static float[] rightPos = {6f/8f+offsetX, 4f/8f+offsetY};
    //moves all rectangles right or left by amount. units are in ratio to monitor

    public final int rows = 640;
    public final int cols = 480;

    char path = 'a';
    char side = 'r';
    char color = 'r';


    protected DcMotor rightFront, leftFront, leftBack, rightBack;

    protected DcMotor leftIntake, rightIntake;

    double motorSpeed;

    Servo leftGrip, rightGrip, rotatorGrip, rotatorServo, armServo;

    Servo leftPick, rightPick;

    static final double COUNTS_PER_MOTOR_REV = 530;
    static final double WHEEL_DIAMETER_INCHES = 3.93701;     // For figuring circumference
    static final double ROBOT_DIAMETER = 15; //Robot diameter was measured as 19 inches
//    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV) / (WHEEL_DIAMETER_INCHES * Math.PI);
    static final double COUNTS_PER_INCH = 180;

    protected double lF, rF, lB, rB, maxVector;


    protected Thread autoThread = new Thread(new Runnable() {
            @Override
            public void run() {
                runAutonomous();
            }
        });

    private long maxTime;


    public void init(){

        //Wheel Motors
        rightFront = hardwareMap.dcMotor.get("front_right_motor");
        leftFront  = hardwareMap.dcMotor.get("front_left_motor");
        rightBack  = hardwareMap.dcMotor.get("back_right_motor");
        leftBack   = hardwareMap.dcMotor.get("back_left_motor");



    }

    public void init_loop() {
        telemetry.addData("Right Val: ", valRight);
        telemetry.addData("Middle Val:", valMid);
        telemetry.addData("Left Val: ", valLeft);
    }



    @Override
    public void start(){
        autoThread.start();
    }



    @Override
    public void stop(){
        try{
            autoThread.interrupt();


        }catch(Exception e){
            telemetry.addData("ENCOUNTERED AN EXCEPTION", e);
        }

    }

        public void mecanumEMove(double speed, double distance, boolean forward) {

            /** distance is in INCHES **/

            setMotorRunMode(STOP_AND_RESET_ENCODER);

            int newTargetLF;
            int newTargetLB;
            int newTargetRF;
            int newTargetRB;

            if (forward) {
                newTargetLF = leftFront.getCurrentPosition() - (int) (distance * COUNTS_PER_INCH);
                newTargetLB = leftBack.getCurrentPosition() - (int) (distance * COUNTS_PER_INCH);
                newTargetRF = rightFront.getCurrentPosition() + (int) (distance * COUNTS_PER_INCH);
                newTargetRB = rightBack.getCurrentPosition() + (int) (distance * COUNTS_PER_INCH);
            } else {
                newTargetLF = leftFront.getCurrentPosition() + (int) (distance * COUNTS_PER_INCH);
                newTargetLB = leftBack.getCurrentPosition() + (int) (distance * COUNTS_PER_INCH);
                newTargetRF = rightFront.getCurrentPosition() - (int) (distance * COUNTS_PER_INCH);
                newTargetRB = rightBack.getCurrentPosition() - (int) (distance * COUNTS_PER_INCH);
            }

            leftFront.setTargetPosition(newTargetLF);
            leftBack.setTargetPosition(newTargetLB);
            rightFront.setTargetPosition(newTargetRF);
            rightBack.setTargetPosition(newTargetRB);


            setMotorRunMode(RUN_TO_POSITION);



            leftFront.setPower(speed);
            leftBack.setPower(speed);
            rightFront.setPower(speed);
            rightBack.setPower(speed);



            try {
                while (leftFront.isBusy() && leftBack.isBusy() && rightFront.isBusy() && rightBack.isBusy()) {

                }
            } catch (Exception e) {
                telemetry.addData("Error", e);
            }

            leftFront.setPower(0);
            leftBack.setPower(0);
            rightFront.setPower(0);
            rightBack.setPower(0);

            setMotorRunMode(RUN_USING_ENCODER);


        }

        
        public void mecanumEStrafeRight(double speed, double distance) {

        /** distance is in INCHES **/

        setMotorRunMode(STOP_AND_RESET_ENCODER);

        int newTargetLF;
        int newTargetLB;
        int newTargetRF;
        int newTargetRB;

        newTargetLF = leftFront.getCurrentPosition() - (int) (distance * COUNTS_PER_INCH);
        newTargetLB = leftBack.getCurrentPosition() + (int) (distance * COUNTS_PER_INCH);
        newTargetRF = rightFront.getCurrentPosition() - (int) (distance * COUNTS_PER_INCH);
        newTargetRB = rightBack.getCurrentPosition() + (int) (distance * COUNTS_PER_INCH);

        leftFront.setTargetPosition(newTargetLF);
        leftBack.setTargetPosition(newTargetLB);
        rightFront.setTargetPosition(newTargetRF);
        rightBack.setTargetPosition(newTargetRB);

        setMotorRunMode(RUN_TO_POSITION);

        leftFront.setPower(speed);
        leftBack.setPower(speed);
        rightFront.setPower(speed);
        rightBack.setPower(speed);



            try {
                while (leftFront.isBusy() && leftBack.isBusy() && rightFront.isBusy() && rightBack.isBusy()) {

                }
            } catch (Exception e) {
                telemetry.addData("Error", e);
            }

        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);

        setMotorRunMode(RUN_USING_ENCODER);

    }

        public void mecanumEStrafeLeft(double speed, double distance) {

        /** distance is in INCHES **/

        setMotorRunMode(STOP_AND_RESET_ENCODER);

        int newTargetLF;
        int newTargetLB;
        int newTargetRF;
        int newTargetRB;

        newTargetLF = leftFront.getCurrentPosition() + (int) (distance * COUNTS_PER_INCH);
        newTargetLB = leftBack.getCurrentPosition() - (int) (distance * COUNTS_PER_INCH);
        newTargetRF = rightFront.getCurrentPosition() + (int) (distance * COUNTS_PER_INCH);
        newTargetRB = rightBack.getCurrentPosition() - (int) (distance * COUNTS_PER_INCH);

        leftFront.setTargetPosition(newTargetLF);
        leftBack.setTargetPosition(newTargetLB);
        rightFront.setTargetPosition(newTargetRF);
        rightBack.setTargetPosition(newTargetRB);


        setMotorRunMode(RUN_TO_POSITION);

        leftFront.setPower(speed);
        leftBack.setPower(speed);
        rightFront.setPower(speed);
        rightBack.setPower(speed);



            try {
                while (leftFront.isBusy() && leftBack.isBusy() && rightFront.isBusy() && rightBack.isBusy()) {


                }
            } catch (Exception e) {
                telemetry.addData("Error", e);
            }

        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);

        setMotorRunMode(RUN_USING_ENCODER);

    }

        public void mecanumETurn(double speed, double degrees, boolean clockwise) {
            /** distance is in INCHES **/

            setMotorRunMode(STOP_AND_RESET_ENCODER);

            degrees = ((ROBOT_DIAMETER * Math.PI) / 360) * degrees;

            int newTargetLF;
            int newTargetLB;
            int newTargetRF;
            int newTargetRB;

            if (clockwise) {
                newTargetLF = leftFront.getCurrentPosition() - (int) (degrees * COUNTS_PER_INCH);
                newTargetLB = leftBack.getCurrentPosition() - (int) (degrees * COUNTS_PER_INCH);
                newTargetRF = rightFront.getCurrentPosition() - (int) (degrees * COUNTS_PER_INCH);
                newTargetRB = rightBack.getCurrentPosition() - (int) (degrees * COUNTS_PER_INCH);
            } else {
                newTargetLF = leftFront.getCurrentPosition() + (int) (degrees * COUNTS_PER_INCH);
                newTargetLB = leftBack.getCurrentPosition() + (int) (degrees * COUNTS_PER_INCH);
                newTargetRF = rightFront.getCurrentPosition() + (int) (degrees * COUNTS_PER_INCH);
                newTargetRB = rightBack.getCurrentPosition() + (int) (degrees * COUNTS_PER_INCH);
            }

            leftFront.setTargetPosition(newTargetLF);
            leftBack.setTargetPosition(newTargetLB);
            rightFront.setTargetPosition(newTargetRF);
            rightBack.setTargetPosition(newTargetRB);

            setMotorRunMode(RUN_TO_POSITION);

            leftFront.setPower(speed);
            leftBack.setPower(speed);
            rightFront.setPower(speed);
            rightBack.setPower(speed);


            try {
                while (leftFront.isBusy() && leftBack.isBusy() && rightFront.isBusy() && rightBack.isBusy()) {

                }
            } catch (Exception e) {
                telemetry.addData("Error", e);
            }

            leftFront.setPower(0);
            leftBack.setPower(0);
            rightFront.setPower(0);
            rightBack.setPower(0);

           setMotorRunMode(RUN_USING_ENCODER);
        }

    public void loop(){


    }

    protected void pause(long millis){
        maxTime = System.currentTimeMillis() + millis;
        while(System.currentTimeMillis() < maxTime && !autoThread.isInterrupted()) {}
        }


    public void encoderTurn(double power, boolean clockwise){
        setMotorRunMode(RUN_USING_ENCODER);

        if(clockwise) {
            leftFront.setPower(-power);
            rightFront.setPower(-power);
            leftBack.setPower(-power);
            rightBack.setPower(-power);
        }
        else{
            leftFront.setPower(power);
            rightFront.setPower(power);
            leftBack.setPower(power);
            rightBack.setPower(power);
        }

    }

    public void setMotorRunMode(DcMotor.RunMode runMode){
        leftFront.setMode(runMode);
        rightFront.setMode(runMode);
        leftBack.setMode(runMode);
        rightBack.setMode(runMode);
    }

    public double getEncoderMotorPowers(int currentPosition, int targetPosition, double speed){
        int countsLeft = abs(targetPosition - currentPosition);

        if(countsLeft > 500 && !(speed >= 0.8)){
            speed = speed * 0.2 * currentPosition/50;
        }else speed = 0.8;

        if(countsLeft <= 500  && !(speed >= 0.8)){
            speed = speed * 0.2 * countsLeft/100;
        }else speed = 0.8;

        return speed;
    }

    public abstract void runAutonomous();
}
