package frc.robot;
import frc.helpers.CCSparkMax;
import frc.helpers.Timer;
import frc.parent.MotorDef;
// import edu.wpi.first.wpilibj.PneumaticsModuleType;
// import edu.wpi.first.wpilibj.Solenoid;
//import edu.wpi.first.wpilibj.Timer;

/**
 * Praise be Mr. Ballin' himself
 * @param RawPower - infinity
 * @param Flaws - zero
 * @param Potential - also infinity
 */
public class TedBallin {

//template:public static CCSparkMax fLeft = new CCSparkMax("Front Left", "FL", RobotMap.FORWARD_LEFT, MotorType.kBrushless, IdleMode.kBrake, RobotMap.FORWARD_LEFT_REVERSE);

    //declares Motor Controllers
    // public static CCSparkMax shooter = new CCSparkMax("Shooter 1", "S1", RobotMap.SHOOTER,
    //     MotorType.kBrushless, IdleMode.kCoast, RobotMap.SHOOTER_REVERSE, true);

    // public static CCSparkMax shooter2 = new CCSparkMax("Shooter 2", "S2", RobotMap.SHOOTER2,
    //     MotorType.kBrushless, IdleMode.kCoast, RobotMap.SHOOTER_REVERSE, true);

    public static CCSparkMax shooter = new CCSparkMax(MotorDef.Shooter);

    public static CCSparkMax shooter2 = new CCSparkMax(MotorDef.Shooter2);

    // public static CCSparkMax loader = new CCSparkMax("Loader", "L", RobotMap.LOADER,
    //     MotorType.kBrushless, IdleMode.kBrake, RobotMap.LOADER_REVERSE, true);

    public static void nothing(){};

    public static void setShoot(double set){
        shooter.set(set);
        shooter2.set(-set);
        // loader.set(-set);
    }

    public static Integer trigger = -1;
    public static Timer timer = new Timer(69);
    public static void setShoot(double set, int trig, double time){
        if(trig != trigger){
            shooter.set(set);
            shooter2.set(-set);
            timer.set(time);
            trigger = trig;
            return;
        }
        if(!timer.triggered()) return;
        System.out.println("timer");
        // loader.set(-set);
    }
    
    /** 
        *@param triggerOne what triggers shooting with positive speed (takes precedence over triggerTwo). Suggest passing in a button or axis input
        *@param triggerTwo what triggers shooting with negative speed. Suggest passing in a button or axis input
        *@param hardStop will set speed to 0 (takes precedence over triggers one and two)
        *@param speed the shoot speed
        *@param time how long it will take for the shooter to start after the indexer
    */
    public static double velocity = 0;
    public static void shoot(boolean triggerOne, boolean triggerTwo, boolean hardStop, double speed, double time, double decel){
        if(hardStop){
            decel = 0.5;
            triggerOne = false;
            triggerTwo = false;
        }
        if(triggerOne){
            setShoot(speed, 0, time);
            velocity = speed;
            return;
        }
        if(triggerTwo){
            setShoot(-speed, 1, time);
            velocity = speed;
            return;
        }
        velocity -= Timer.deltaTime / decel;
        if(velocity <= 0) velocity = 0;
        setShoot(velocity);
        trigger = -1;
    }

}