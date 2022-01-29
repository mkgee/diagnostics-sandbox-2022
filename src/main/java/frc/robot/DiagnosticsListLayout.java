package frc.robot;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;

import static frc.robot.MotorDataType.*;
/**
 * Diagnostics defines some tabs in the shuffleboard to display diagnostics for the motors 
 * and power distribution panel.
 */
public class DiagnosticsListLayout implements DiagnosticsIF {
 
    /* DataType defines the motor attributes to monitor.  The values here are a sample set. 
       Update this enumeration to define attributes you care about. */
    private MotorDataType[] displayedData = { FAULTS, STICKY_FAULTS, TEMP, INVERTED_STATE, POSITION, VELOCITY };

    /* Define tabs in the shuffleboard */
    private final ShuffleboardTab summaryTab = Shuffleboard.getTab("Summary");
    private final ShuffleboardTab motorTab = Shuffleboard.getTab("Motors List");

    /* Define an entry to display an overall fault status.  This controls a "Fault Indicator" widget
       in the Summary tab */
    private NetworkTableEntry faultEntry;
    
    /* stores an array of the motors */
    private CCSparkMax[] motors;

    /* The motorEntryMap allows us to map a motor name to a NetworkTableEntry.  See the getEntry() method on how
       to use motorEntryMap.  */
    // key -> motor name, value -> map (key -> DataType, value -> NetworkTableEntry)
    private Map<String, Map<MotorDataType, NetworkTableEntry>> motorEntryMap = new HashMap<>();

    private MotorUpdate motorUpdate;

    /* contructor, saves the injected motors */
    public DiagnosticsListLayout(CCSparkMax... motors) {
        this.motors = motors;
    }

    /* Creates the diagnostic widgets and associated NetworkTableEntries in the appropriate tabs.  In this examples,
       it displays a "Fault Indicator" widget in the Summary tab to indicate overall health of all motors. 
       It also displays a row of diagnostic widgets for each motor, so in the Motors tab there will be 4 rows.
       For the Power Distribution Panel, it create a set of widgets in the "Power" tab corresponding to the
       PowerDataType enumerations.  For Current, it displays current for the first 8 channels. */
    @Override
    public void init() {
        
        faultEntry = summaryTab
          .add("List Fault Indicator", false)
          .withWidget(BuiltInWidgets.kBooleanBox)
          .getEntry();

        motorUpdate = new MotorUpdate(motorEntryMap, motors, faultEntry, displayedData );

        int col = 0;

        // for each motor: Faults, Sticky Faults, Temp, Inverted state, position, velocity
        for(CCSparkMax m : motors) {
            
            Map<MotorDataType, NetworkTableEntry> entryMap = new EnumMap<>(MotorDataType.class);

            // initialize motorEntryMap
            motorEntryMap.put(m.getName(), entryMap);

            ShuffleboardLayout motorLayout = motorTab
                .getLayout(m.getName(), BuiltInLayouts.kList)
                .withSize(2, 4) // height can't be more than number of visible rows in shuffleboard
                .withPosition(col, 0)
                .withProperties(Map.of("Label position", "TOP"));
            
            // create the widgets for each displayed MotorDataType
            for (MotorDataType md : displayedData) {
                entryMap.put(md, motorLayout.add(md.getLabel(), md.getDefaultValue())
                        .withWidget(md.getWidgetType())
                        // .withPosition(0, r++)
                        .withProperties(md.getProperties())
                        .getEntry());
            }
           col += 2;
        }
        
        Shuffleboard.selectTab("Motors List");
    }
   
    @Override
    public void updateStatus() {
       motorUpdate.updateStatus();
    }
}
