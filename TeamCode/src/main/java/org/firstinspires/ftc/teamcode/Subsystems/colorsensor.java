package org.firstinspires.ftc.teamcode.Subsystems;
import android.app.Activity;
import android.view.View;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion;

import com.qualcomm.robotcore.hardware.ColorSensor;

public class colorsensor extends SubsystemBase {

    public boolean bLedOn = true;
    ColorSensor colorSensor;    // Hardware Device Object


    public colorsensor(HardwareMap hardwareMap) {

        boolean bPrevState = false;
        boolean bCurrState = false;


        float hsvValues[] = {0F,0F,0F};
        final float values[] = hsvValues;



        colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");
    }

    public void bLedOn() {
        colorSensor.enableLed(bLedOn);
    }

    public int alpha() {
        return colorSensor.alpha();
    }
    public int red() {
        return colorSensor.red();
    }
    public int green() {
        return colorSensor.green();
    }
    public int blue() {
        return colorSensor.blue();
    }
}
