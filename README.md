# diagnostics-demo-2022
Diagnostics example on 2022 wpilib
This example was built on top of a previous project from the 4550 team.  The Diagnostics was created and called by the Robot class.  It was also updated to work with the 2022 FRC® Control System software (WPILib).  This runs in 2022 WPILib VS Code with the following vendor libs:
    KauaiLabs_navX_FRC https://www.kauailabs.com/dist/frc/2022/navx_frc.json
    CTRE-Phoenix https://maven.ctr-electronics.com/release/com/ctre/phoenix/Phoenix-frc2022-latest.json
    REVLib https://software-metadata.revrobotics.com/REVLib.json

To run this demo:
1. Load this project in 2022 WPILib VS Code
2. Enable simulation mode:
    <Ctrl><Shift>P - type/choose WPILib Change Desktop Support Enable Setting and ensure its enabled
3. Start the shuffleboard:
    <Ctrl><Shift>P - type/choose WPILib: Start Tool, hit return, then select "shuffleboard"
    The WPILib shuffleboard app will start.
4. Start the robot simulation:
    <Ctrl><Shift>P - type/choose WPILib: Simulate Robot Code
    The code will build and after a successful build, you will see Sim GUI checkbox checked, click on OK
    The Robot Simulation app will start.
5.  You can simulate some faults:
    In the Robot Simulation app, in the Other Devices widget, click on "Spark Max [3]", type a "1" in Faults and hit return.  In the Shuffleboard app, click on the "Motors" tab, and you should see a "kBrownout" fault in "Front Left faults".
    Enter other values in Robot Simulation app and see the corresponding values change in Shuffleboard.
6.  Click on the square orange button at the top of VS Code to end the simulation.
