package org.firstinspires.ftc.teamcode.Subsystems;
import android.app.Activity;
import android.view.View;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion;

import com.qualcomm.robotcore.hardware.ColorSensor;

public class ColourSensor extends SubsystemBase {

    public boolean bLedOn = true;
    ColorSensor colourSensor;    // Hardware Device Object

    public ColourSensor(HardwareMap hardwareMap) {

        boolean bPrevState = false;
        boolean bCurrState = false;

        float hsvValues[] = {0F,0F,0F};
        final float values[] = hsvValues;

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
    public int blue() {
        return colourSensor.blue();
    }
    public void periodic(MultipleTelemetry telemetry) {
            telemetry.addData("LED Enabled:", bLedOn ? "True" : "False");
            telemetry.addData("Clear:", alpha());
            telemetry.addData("Red:  ", red());
            telemetry.addData("Green:", green());
            telemetry.addData("Blue: ", blue());
    }
}
