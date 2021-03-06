package frc.robot;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import frc.helpers.CCSparkMax;
import frc.helpers.PneumaticsSystem;
import frc.parent.MotorDef;
import frc.parent.RobotMap;


public class Arms {
 
    public static CCSparkMax climber = new CCSparkMax(MotorDef.Climber);

    public static PneumaticsSystem armSols = new PneumaticsSystem(PneumaticsModuleType.CTREPCM, RobotMap.ARM_SOLENOID_ONE, RobotMap.ARM_SOLENOID_TWO);

    public static void nothing(){};

    public static void setArms(boolean on){
        armSols.set(on);
    }

    /** 
     * Toggles the climbing arms.
    */
    public static void toggleArms(){
        armSols.toggle();
    }

    /** 
     * Will run the elevator based on what triggers are true or false
     *@param upTrigger what triggers the elevator with positive speed (takes precedence over triggerTwo). Suggest passing in a button or axis input.
     *@param downTrigger what triggers the elevator with negative speed. Suggest passing in a button or axis input.
     *@param hardStop will set speed to 0 (takes precedence over triggers one and two)
     *@param speed the elevator speed
    */
    public static void runElevator(boolean upTrigger, boolean downTrigger, boolean hardStop, double speed){
        if(hardStop){
            climber.set(0);
            return;
        }
        if(upTrigger){
            climber.set(speed);
            return;
        }
        if(downTrigger){
            climber.set(-speed);
            return;
        }
        climber.set(0);
    }

    /** 
     * Toggles the climbing arms. Will only trigger again after trigger is false
     *@param trigger what will trigger the toggle. Suggest passing in a button or axis input.
    */
    public static void toggleArms(boolean trigger){
        armSols.triggerSystem(trigger);
    }
    
}