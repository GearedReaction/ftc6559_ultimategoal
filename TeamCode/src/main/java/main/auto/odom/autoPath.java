package main.auto.odom;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "FTC 6559 autoPath", group = "ftc6559")
public class autoPath extends AutonomousMovement {

    char path = 'a';
    char side = 'l';
    char color = 'b';
    public void tiltFireR(){
       mecanumETurn(1,20, false);
       //fire
        mecanumETurn(1,20, true);
    }

    public void getRingsWobbleR() {
        tiltFireR();
        mecanumEStrafeRight(1, 6);
        //intake on
        mecanumEMove(1, 5, false);
        if (path == 'b') {
            //fire for certain seconds
        }
        if (path == 'c') {
            //fire for x seconds
        }
        mecanumEMove(1, 13, false);
        //intake off
        mecanumETurn(1,90,false);
        mecanumEMove(1, 12, false);
        //get wobble
        mecanumEStrafeRight(1, 2);
    }
    public void abcPathRr() {
        if (path == 'a') {
            mecanumEMove(1, 36, true);
            //drop wobble
            mecanumEMove(1, 12, false);
            mecanumETurn(1, 90, true);
            getRingsWobbleR();
            mecanumETurn(1, 25, true);
            mecanumEMove(1, 35, true);
            //drop wobble and park

        } else if (path == 'b') {
            //robot pointing down
            mecanumEMove(1, 48, false);
            //drop wobble
            mecanumEMove(1, 24, true);
            mecanumETurn(1, 90, false);
            //fire pew pew
            getRingsWobbleR();
            mecanumEMove(1, 42,true);
            mecanumEMove(1, 12,false);
            //drop wobble*****

        } else if (path == 'c') {
            mecanumEMove(1, 57, true);
            //drop wobble
            mecanumEMove(1, 33, false);
            mecanumETurn(1, 90, true);
            mecanumEStrafeRight(1, 33);
            //fire pew pew
            getRingsWobbleR();
            mecanumETurn(1, 15, true);
            mecanumEMove(1, 56, true);
            mecanumEMove(1, 23, false);

        }
    }

    public void abcPathRl() {
        mecanumEStrafeLeft(1, 30);
        mecanumEMove(1, 6, true);
        //shoot

        if (path == 'a') {
            mecanumEMove(1, 2, true);
            mecanumETurn(1, 90, false);
            mecanumEMove(1, 9, true);
            //drop wobble
            mecanumEMove(1, 33, false);
            //wobble
            mecanumEMove(1, 30, true);
        } else if (path == 'b'){
            mecanumETurn(1, 180, false);
            mecanumEStrafeRight(1,12);
            //drop wobble
            mecanumETurn(1, 90, false);
            mecanumEMove(1, 24, true);
            mecanumETurn(1, 90, false);
            mecanumETurn(1, 90, true);
            //fire
            mecanumEMove(1, 12, false);
            mecanumETurn(1, 90, false);
            mecanumEStrafeRight(1,33);
        } else if (path == 'c'){
            mecanumEMove(1, 8, true);
            mecanumETurn(1, 90, false);
            mecanumEMove(1, 30, true);
            //drop wobble
            mecanumEMove(1, 42, false);
            mecanumETurn(1, 90, true);
            mecanumEMove(1, 7, false);
//fire
            mecanumETurn(1, 90, false);
            mecanumEMove(1, 12, false);
            //wobble
            mecanumEMove(1, 52, true);
            mecanumEMove(1, 24, false);
        }
    }

    public void tiltFireB(){
        mecanumETurn(1,20, true);
        //fire
        mecanumETurn(1,20, false);
    }

    public void getRingsWobbleB() {
        tiltFireB();
        mecanumEStrafeRight(1, 6);
        //intake on
        mecanumEMove(1, 5, true);
        if (path == 'b') {
            //fire for certain seconds
        }
        if (path == 'c') {
            //fire for x seconds
        }
        mecanumEMove(1, 13, true);
        //intake off
        mecanumETurn(1,90,true);
        mecanumEMove(1, 12, true);
        //get wobble
        mecanumEStrafeRight(1, 2);
    }
    public void abcPathBl() {
        if (path == 'a') {
            mecanumEMove(1, 36, false);
            //drop wobble
            mecanumEMove(1,12,true);
            mecanumETurn(1, 90, false);
            getRingsWobbleB();
            mecanumETurn(1, 25, false);
            mecanumEMove(1, 35, false);

            //drop wobble and park
        } else if (path == 'b') {
            //pointing up
            mecanumEMove(1, 48, true);
            //drop wobble
            mecanumEMove(1,24,false);
            mecanumETurn(1, 90, true);
            getRingsWobbleB();
            mecanumEMove(1, 42,false);
            mecanumEMove(1, 12,true);
            //drop wobble*****

        } else if (path == 'c') {
            mecanumEMove(1, 57, false);
            //drop wobble
            mecanumEMove(1, 33, true);
            mecanumETurn(1, 90, false);

            //fire pew pew
            getRingsWobbleB();
            mecanumETurn(1, 15, false);
            mecanumEMove(1, 56, false);
            mecanumEMove(1, 23, true);
        }
    }

    public void abcPathBr() {
        mecanumEStrafeLeft(1, 30);
        mecanumEMove(1, 6, false);
        //shoot

        if (path == 'a') {
            mecanumEMove(1, 2, false);
            mecanumETurn(1, 90, true);
            mecanumEMove(1, 9, false);
            //drop wobble
            mecanumEMove(1, 33, true);
            //wobble
            mecanumEMove(1, 30, false);
        } else if (path == 'b'){
            mecanumETurn(1, 180, true);
            mecanumEStrafeRight(1,12);
            //drop wobble
            mecanumETurn(1, 90, true);
            mecanumEMove(1, 24, false);
            mecanumETurn(1, 90, true);
            mecanumETurn(1, 90, false);
            //fire
            mecanumEMove(1, 12, false);
            mecanumETurn(1, 90, false);
            mecanumEStrafeRight(1,33);
        } else if (path == 'c'){
            mecanumEMove(1, 8, false);
            mecanumETurn(1, 90, true);
            mecanumEMove(1, 30, false);
            //drop wobble
            mecanumEMove(1, 42, true);
            mecanumETurn(1, 90, false);
            mecanumEMove(1, 7, true);
//fire
            mecanumETurn(1, 90, true);
            mecanumEMove(1, 12, true);
            //wobble
            mecanumEMove(1, 52, false);
            mecanumEMove(1, 24, true);
        }
    }

    public void runAutonomous() {
        if (color == 'r') {
            if (side == 'r') {
                abcPathRr();
            } else {
                abcPathRl();
            }
        } else {
            if (side == 'r') {
                abcPathBr();
            } else {
                abcPathBl();
            }
        }
    }

    public void start(){
        runAutonomous();
    }

    public void loop(){

    }
    public void stop(){

    }
}



