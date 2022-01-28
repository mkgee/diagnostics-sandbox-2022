package frc.robot;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.FaultID;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;

import static frc.robot.MotorDataType.*;

/**
 * Diagnostics defines some tabs in the shuffleboard to display diagnostics for
 * the motors
 * and power distribution panel.
 */
public class DiagnosticsGridLayout implements DiagnosticsIF {

    /* define the set of MotorDataType data to display */
    private MotorDataType[] displayedData = { FAULTS, STICKY_FAULTS, INVERTED_STATE, TEMP, POSITION, VELOCITY };

    /* Define tabs in the shuffleboard */
    private final ShuffleboardTab summaryTab = Shuffleboard.getTab("Summary");
    private final ShuffleboardTab motorTab = Shuffleboard.getTab("Motors Grid");

    /*
     * Define an entry to display an overall fault status. This controls a
     * "Fault Indicator" widget
     * in the Summary tab
     */
    private NetworkTableEntry faultEntry;

    /* stores an array of the motors */
    private CCSparkMax[] motors;

    /*
     * The motorEntryMap allows us to map a motor name to a NetworkTableEntry. See
     * the getEntry() method on how
     * to use motorEntryMap.
     */
    // key -> motor name, value -> map (key -> DataType, value -> NetworkTableEntry)
    private Map<String, Map<MotorDataType, NetworkTableEntry>> motorEntryMap = new HashMap<>();

    /* contructor, saves the injected motors */
    public DiagnosticsGridLayout(CCSparkMax... motors) {
        this.motors = motors;
    }

    /*
     * Creates the diagnostic widgets and associated NetworkTableEntries in the
     * appropriate tabs. In this examples,
     * it displays a "Fault Indicator" widget in the Summary tab to indicate overall
     * health of all motors.
     * It also displays a row of diagnostic widgets for each motor, so in the Motors
     * tab there will be 4 rows.
     * For the Power Distribution Panel, it create a set of widgets in the "Power"
     * tab corresponding to the
     * PowerDataType enumerations. For Current, it displays current for the first 8
     * channels.
     */
    @Override
    public void init() {

        faultEntry = summaryTab
                .add("Grid Fault Indicator", false)
                .withWidget(BuiltInWidgets.kBooleanBox)
                .getEntry();

        // for each motor: Faults, Sticky Faults, Temp, Inverted state, position,
        // velocity
        for (CCSparkMax m : motors) {
            int col=0;
            Map<MotorDataType, NetworkTableEntry> entryMap = new EnumMap<>(MotorDataType.class);

            // initialize motorEntryMap
            motorEntryMap.put(m.getName(), entryMap);

            // create the layout
            ShuffleboardLayout motorLayout = motorTab
                    .getLayout(m.getName(), BuiltInLayouts.kGrid)
                    .withSize(9, 1)
                    // .withPosition(0, layoutRow--)
                    .withProperties(Map.of(
                            "Label position", "LEFT",
                            "Number of Columns", displayedData.length, // defines how many widgets in a row
                            "Number of Rows", 1));

                            // create the widgets for each displayed MotorDataType
            for (MotorDataType md : displayedData) {
                entryMap.put(md, motorLayout.add(md.getLabel(), md.getDefaultValue())
                        .withWidget(md.getWidgetType())
                        .withPosition(col++, 0)
                        .withProperties(md.getProperties())
                        .getEntry());
            }
        }

        Shuffleboard.selectTab("Motors Grid");
    }

    private void updateFaultStatus(NetworkTableEntry entry, CCSparkMax motor) {
        int fault = motor.getFaults();
        String faultMsg = "No fault";
        if (fault != 0) {
            StringJoiner sj = new StringJoiner(",");
            for (CANSparkMax.FaultID faultId : FaultID.values()) {
                if (motor.getFault(faultId)) {
                    sj.add(faultId.name());
                }
            }
            faultMsg = sj.toString();
        }
        entry.setString(faultMsg);
        SmartDashboard.putString(entry.getName() + " faults", faultMsg);
    }

    private void updateStickyFaultStatus(NetworkTableEntry entry, CCSparkMax motor) {
        int fault = motor.getStickyFaults();
        String faultMsg = "No fault";
        if (fault != 0) {
            StringJoiner sj = new StringJoiner(",");
            for (CANSparkMax.FaultID faultId : FaultID.values()) {
                if (motor.getStickyFault(faultId)) {
                    sj.add(faultId.name());
                }
            }
            faultMsg = sj.toString();
        }
        entry.setString(faultMsg);
    }

    /**
     * getEntry returns a NetworkTableEntry for a given motor and DataType.
     * 
     * @param motor the specific motor
     * @param type  motor attribute type
     * @return the NetworkTableEntry for the given inputs
     */
    private NetworkTableEntry getEntry(CCSparkMax motor, MotorDataType type) {
        return motorEntryMap.get(motor.getName()).get(type);

    }

    private void updateFaultStatus(CCSparkMax motor, MotorDataType type) {
        if (type.equals(MotorDataType.FAULTS)) {
            updateFaultStatus(getEntry(motor, type), motor);
        } else {
            updateStickyFaultStatus(getEntry(motor, type), motor);
        }
    }

    private void updateDoubleStatus(CCSparkMax motor, MotorDataType type) {

        NetworkTableEntry entry = getEntry(motor, type);
        switch (type) {
            case TEMP:
                if (entry != null) {
                    entry.setDouble(motor.getMotorTemperature());
                    SmartDashboard.putNumber(motor.getName() + " temp", motor.getMotorTemperature());
                }
                break;
            case POSITION:
                if (entry != null) {
                    entry.setString(Double.toString(motor.getEncoder().getPosition()));
                    SmartDashboard.putNumber(motor.getName() + " pos", motor.getEncoder().getPosition());
                }
                break;
            case VELOCITY:
                if (entry != null) {
                    entry.setDouble(motor.getEncoder().getVelocity());
                    SmartDashboard.putNumber(motor.getName() + " vel", motor.getEncoder().getVelocity());
                }
                break;
            default:
                break;
        }
    }

    private void updateStatus(CCSparkMax motor, MotorDataType type) {

        switch (type) {
            case FAULTS:
            case STICKY_FAULTS:
                updateFaultStatus(motor, type);
                break;
            case TEMP:
            case POSITION:
            case VELOCITY:
                updateDoubleStatus(motor, type);
                break;
            case INVERTED_STATE: {
                NetworkTableEntry entry = getEntry(motor, type);
                if (entry != null) {
                    String msg = motor.getInverted() ? "inverted" : "";
                    entry.setString(msg);
                }
            }
        }
    }

    @Override
    public void updateStatus() {

        int allFaults = 0;
        for (CCSparkMax motor : motors) {
            allFaults += motor.getFaults();
        }

        // boolean status
        faultEntry.setBoolean(allFaults == 0);

        // update status on SparkMax controllers
        for (CCSparkMax motor : motors) {
            for (MotorDataType type : displayedData) {
                updateStatus(motor, type);
            }
        }

    }
}
