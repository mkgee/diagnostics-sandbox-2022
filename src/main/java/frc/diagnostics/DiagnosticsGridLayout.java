package frc.diagnostics;

import frc.helpers.CCSparkMax;
import static frc.diagnostics.MotorDataType.*;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;

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

    /* stores a list of the motors */
    private List<CCSparkMax> motors;

    /*
     * The motorEntryMap allows us to map a motor name to a NetworkTableEntry. See
     * the getEntry() method on how
     * to use motorEntryMap.
     */
    // key -> motor name, value -> map (key -> DataType, value -> NetworkTableEntry)
    private Map<String, Map<MotorDataType, NetworkTableEntry>> motorEntryMap = new HashMap<>();

    private MotorUpdate motorUpdate;

    /* contructor, saves the injected motors */
    public DiagnosticsGridLayout(CCSparkMax... motors) {
        this.motors = Arrays.asList(motors);
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

        motorUpdate = new MotorUpdate(motorEntryMap, motors, faultEntry, displayedData );

        // for each motor: Faults, Sticky Faults, Temp, Inverted state, position,
        // velocity
        int layoutRow =0;
        for (CCSparkMax m : motors) {
            int col=0;
            Map<MotorDataType, NetworkTableEntry> entryMap = new EnumMap<>(MotorDataType.class);

            // initialize motorEntryMap
            motorEntryMap.put(m.getName(), entryMap);

            // create the layout
            ShuffleboardLayout motorLayout = motorTab
                    .getLayout(m.getName(), BuiltInLayouts.kGrid)
                    .withSize(9, 1)
                    .withPosition(0, layoutRow++)
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

    @Override
    public void updateStatus() {
        motorUpdate.updateStatus();
    }
}
