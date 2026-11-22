package org.firstinspires.ftc.teamcode.Subsystems.Intake;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.HardwareMap;

import com.qualcomm.robotcore.hardware.ColorSensor;

import java.util.LinkedHashMap;
import java.util.Map;

public class ColourSensor extends SubsystemBase {

    public boolean bLedOn = true;
    ColorSensor colourSensor;    // Hardware Device Object
    private boolean currentState;
    private char detectedColour = 'n';
    private final Map<String, Object> csTelemetry = new LinkedHashMap<>();

    public ColourSensor(HardwareMap hardwareMap) {

        boolean bPrevState = false;
        boolean bCurrState = false;

        final float[] values = new float[]{0F,0F,0F};

//        int relativeLayoutId = BlocksOpModeCompanion.hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", BlocksOpModeCompanion.hardwareMap.appContext.getPackageName());
//        final View relativeLayout = ((Activity) BlocksOpModeCompanion.hardwareMap.appContext).findViewById(relativeLayoutId);

        colourSensor = hardwareMap.get(ColorSensor.class, "colourSensor");
    }

    public void bLedOn() { colourSensor.enableLed(bLedOn); }
    public int alpha() {
        return colourSensor.alpha();
    }
    public int red() { return colourSensor.red();}
    public int green() {
        return colourSensor.green();
    }
    public int blue() {return colourSensor.blue();}
    public boolean newBall() {
        boolean lastState = currentState;
        currentState = red() > 80 || blue() > 80 || green() > 120;

        if (currentState) {
            if (red() > 80) {
                detectedColour = 'R';
            } else if (blue() > 80) {
                detectedColour = 'B';
            } else if (green() > 120) {
                detectedColour = 'P';
            }
        }

        return (lastState && !currentState);
    }
    public char lastBall() {
        return detectedColour;
    }

    public void periodic(MultipleTelemetry telemetry) {
        if (telemetry != null) {
            telemetry.addData("CS :----------COLOUR SENSOR----------", "CS");
            telemetry.addData("CS: LED Enabled:", bLedOn ? "True" : "False");
            telemetry.addData("CS: Clear:", alpha());
            telemetry.addData("CS: Red:  ", red());
            telemetry.addData("CS: Green:", green());
            telemetry.addData("CS: Blue: ", blue());
        }
    }
}
