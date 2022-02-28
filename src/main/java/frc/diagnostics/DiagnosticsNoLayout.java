package frc.diagnostics;

import static frc.diagnostics.MotorDataType.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.helpers.CCSparkMax;
/**
 * DiagnosticsNoLayout does not use any tab layouts, just plain row, col positions.
  */
public class DiagnosticsNoLayout implements DiagnosticsIF {

    private MotorDataType[] displayedData = {FAULTS, STICKY_FAULTS, TEMP, INVERTED_STATE, POSITION, VELOCITY};
    enum PowerDataType {VOLTAGE, TEMP, CURRENT, ENERGY };
    private final ShuffleboardTab summaryTab = Shuffleboard.getTab("Summary");
    private final int rowsPerPage = 4;

    private int totalRows = 0;
    private List<ShuffleboardTab> motorTabs = new ArrayList<>();
    private ShuffleboardTab motorTab;
    private NetworkTableEntry faultEntry;
    
    private List<CCSparkMax> motors;

    // key -> motor name, value -> map (key -> DataType, value -> NetworkTableEntry)
    private Map<String, Map<MotorDataType, NetworkTableEntry>> motorEntryMap = new HashMap<>();

    private MotorUpdate motorUpdate;

    public DiagnosticsNoLayout(CCSparkMax... motors) {
        this.motors = Arrays.asList(motors);
    }

    public DiagnosticsNoLayout(List<CCSparkMax> motors) {
        this.motors = motors;
    }

    @Override
    public void init() {
        
        faultEntry = summaryTab
          .add("Fault Indicator", false)
          .withWidget(BuiltInWidgets.kBooleanBox)
          .getEntry();

        motorUpdate = new MotorUpdate(motorEntryMap, motors, faultEntry, displayedData );

        int row = 0;
        
        // for each motor: Faults, Sticky Faults, Temp, Inverted state, position, velocity
        for(CCSparkMax m : motors) {

            if (totalRows++ % rowsPerPage == 0) {
                motorTab = Shuffleboard.getTab("Motors " + ((totalRows/rowsPerPage) + 1));
                motorTabs.add(motorTab);
                row = 0;
            }
            int col = 0;
            Map<MotorDataType, NetworkTableEntry> entryMap = new EnumMap<>(MotorDataType.class);

            // initialize motorEntryMap
            motorEntryMap.put(m.getName(), entryMap);

            final String shortName = m.getShortName();

            for (MotorDataType md : displayedData) {
                int width = md.getWidth();
                String widgetName = shortName + " " + md.getLabel();
                System.out.println("widgetName: " + widgetName);
                entryMap.put(md, motorTab.add(widgetName, md.getDefaultValue())
                .withWidget(md.getWidgetType())
                .withPosition(col, row) 
                .withSize(width, 1)
                .withProperties(md.getProperties())
                .getEntry() ); 
                col += width;   
            }
            row++;
        }

        row = 0;
       
        // select the first motorTab
        Shuffleboard.selectTab(motorTabs.get(0).getTitle());
    }
  
    @Override
    public void updateStatus() {
        motorUpdate.updateStatus();
    }
}