package frc.parent;

import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import static com.revrobotics.CANSparkMax.IdleMode.*;
import static com.revrobotics.CANSparkMaxLowLevel.MotorType.*;

public enum MotorDef {

    ForwardLeftWheel("Forward Left Wheel", "FL", 2, true, kBrushless, kBrake), 
    ForwardRightWheel("Forward Right Wheel", "FR", 5, false, kBrushless, kBrake),
    BackLeftWheel("Back Left Wheel", "BL", 3, true, kBrushless, kBrake),
    BackRightWheel("Back Right Wheel", "BR", 6, false, kBrushless, kBrake),
    Climber("Climber", "CL", 4, false, kBrushless, kBrake),
    Shooter("Shooter", "SH", 1, false, kBrushless, kCoast),
    Shooter2("Shooter2", "SH2", 7, true, kBrushless, kCoast);

    private final String name;
    private final String shortName;
    private final int canBusAddress;
    private final boolean inverted;
    private final MotorType type;
    private final IdleMode idleMode;

    MotorDef(String name, String shortName, int canBusAddress, boolean invert, MotorType type, IdleMode idleMode) {
        this.name = name;
        this.shortName = shortName;
        this.canBusAddress = canBusAddress;
        this.inverted = invert;
        this.type = type;
        this.idleMode = idleMode;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public int getCANBusAddress() {
        return canBusAddress;
    }

    public boolean isInverted() {
        return inverted;
    }
    
    public MotorType getType() {
        return type;
    }

    public IdleMode getIdleMode() {
        return idleMode;
    }
}
