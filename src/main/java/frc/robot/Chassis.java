package frc.robot;

import frc.parent.*;
import frc.helpers.CCSparkMax;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;

public class Chassis {

    //These control the main 4 motors on the robot

    public static CCSparkMax fLeft = new CCSparkMax(MotorDef.ForwardLeftWheel);
    public static CCSparkMax fRight = new CCSparkMax(MotorDef.ForwardRightWheel);
    public static CCSparkMax bLeft = new CCSparkMax(MotorDef.BackLeftWheel);
    public static CCSparkMax bRight = new CCSparkMax(MotorDef.BackRightWheel);

    //AHRS gyro measures the angle of the bot
    public static AHRS gyro = new AHRS(SPI.Port.kMXP);

    //Solenoids
    public static Solenoid shiftOne = new Solenoid(PneumaticsModuleType.CTREPCM, RobotMap.SHIFT_SOLENOID_ONE);
    public static Solenoid shiftTwo = new Solenoid(PneumaticsModuleType.CTREPCM,RobotMap.SHIFT_SOLENOID_TWO);

    //To be used in TeleOP
    //Takes in two axises, most likely the controller axises
    //Optimized for a west coast or standard chassis
    //DO NOT USE THIS FOR SWERV DRIVE 
    public static void axisDrive(double yAxis, double xAxis, double max){
        fLeft.set(-OI.normalize((yAxis - xAxis), -max, max));
        fRight.set(-OI.normalize((yAxis + xAxis), -max, max));
        bLeft.set(-OI.normalize((yAxis - xAxis), -max, max));
        bRight.set(-OI.normalize((yAxis + xAxis), -max, max));
    }

    public static void setFastMode(boolean on){
        shiftOne.set(!on);
        shiftTwo.set(on);
    }

    //To be used on Auto/PIDs
    //Simply sets the motor controllers to a certain percent output
    public static void driveSpd(double lSpeed, double rSpeed){
        fLeft.set(OI.normalize(lSpeed, -1.0, 1.0));
        fRight.set(OI.normalize(rSpeed, -1.0, 1.0));
        bLeft.set(OI.normalize(lSpeed, -1.0, 1.0));
        bRight.set(OI.normalize(rSpeed, -1.0, 1.0));
    }

    public static void setFactor(double factor){
        //0.048 slow, 0.109 fast
        fLeft.setPositionConversionFactor(factor);
        fLeft.setPositionConversionFactor(factor);
        fLeft.setPositionConversionFactor(factor);
        fLeft.setPositionConversionFactor(factor);

    }

   
    //Sets the gyro and encoders to zero
    public static void reset(){
        // gyro.reset();
        fLeft.reset();
        fRight.reset();
        bLeft.reset();
        bRight.reset();   
        
    }

    public static double getLDist(){
        double dist = (fLeft.getPosition() + bLeft.getPosition())/2;
        return dist;
    }

    public static double getRDist(){
        double dist = (fRight.getPosition() + bRight.getPosition())/2;
        return dist;
    }

    // public static double getAngle(){
    //     return gyro.getAngle();
    // }

    /*
        "Whosever holds these loops, if he be worthy, shall posses the power of AJ"
    */

    //Drives the robot to a certain distance
    //Kinda complex -> DO NOT TOUCH
    public static void driveDist(double goal, double aPer, double kp, double max, boolean debug){
        setFactor(0.048);
        double aError = goal*aPer;

        double lPos = getLDist();
        double lError = goal-lPos;
        double lSpd = 0;

        double rPos = getRDist();
        double rError = goal-rPos;
        double rSpd = 0; 

        while(true){
            lPos = getLDist();
            lError = goal-lPos;
            lSpd = lError*kp;
            lSpd = OI.normalize(lSpd, -max, max);

            rPos = getRDist();
            rError = goal-rPos;
            rSpd = rError*kp;
            rSpd = OI.normalize(rSpd, -max, max);

            driveSpd(lSpd, rSpd);

            if(debug){
                System.out.println("Left - Left Speed: " + lSpd + 
                                        " Left Error: " + lError + 
                                        " Left Position: " + lPos);
                System.out.println("Right - Right Speed: " + rSpd + 
                                        " Right Error: " + rError + 
                                        " Right Position" + rPos);
                Timer.delay(0.5);
            }

            if(lError <= aError && rError <= aError){
                driveSpd(0.0, 0.0);
                System.out.println("YOINK, ya made it");
                break; 
            }
        }
    }

    //Turns the robot to a certain angle, a positive angle will turn right
    //Kinda complex -> DO NOT TOUCH
    public static void turnToAngle(double goal, double aPer, double kp, double max, boolean debug){
        double aError = goal*aPer;

        double angl = gyro.getAngle();
        double error = goal-angl;
        double input = 0;

        while(true){
            angl = gyro.getAngle();
            error = goal-angl;
            input = error*kp;
            input = OI.normalize(input, -max, max);

            driveSpd(input, -input);

            if(debug){
                System.out.println("Input: " + input);
                System.out.println("Error: " + error);
                System.out.println("Angle: " + angl);
                Timer.delay(0.5);
            }

            if(error <= aError){
                driveSpd(0.0, 0.0);
                System.out.println("YOINK, ya made it");
                break; 
            }
        }
    }
    
}