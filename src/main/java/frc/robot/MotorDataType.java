package frc.robot;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import static edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets.*;

public enum MotorDataType {
    FAULTS("Faults", kTextView ,"No fault"), 
    STICKY_FAULTS("Sticky Faults", kTextView, "No Fault"), 
    TEMP("Temp",kDial, 0),  
    INVERTED_STATE("Inv. State", kTextView, ""), 
    POSITION("Position", kTextView, 0), 
    VELOCITY("Velocity", kDial, 0);

    private final BuiltInWidgets widget;
    private final String label;
    private final Object defaultValue;

    private MotorDataType(String label, BuiltInWidgets widget, Object defaultValue) {
        this.label = label;
        this.widget = widget;
        this.defaultValue = defaultValue;
    }

    public String getLabel() {
        return label;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public BuiltInWidgets getWidget() {
        return widget;
    }
}