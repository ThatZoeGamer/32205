package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystems.Intake.ColourSensor;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter.Shooter;

public class Inventory {
    public static char[] inv;
    ColourSensor cs;
    Shooter s;
    private char lastShotType = 'n';
    public Inventory(char[] inv, ColourSensor cs, Shooter s) {
        Inventory.inv = inv;
        this.cs = cs;
        this.s = s;
    }
    public void updateInventory() {
        if(s.justShotBall()) {
            lastShotType = inv[0];
            inv[0] = inv[1];
            inv[1] = inv[2];
            inv[2] = inv[3];
            inv[3] = 'n';
//            lastShotType = inv[0];
//            for (int i = 0 ; i < inv.length ; i++) {
//                inv[i] = inv[i+1];
//            }
//            inv[inv.length-1] = 'N';
        }
//        if (gamepad.wasJustPressed(GamepadKeys.Button.A)) {
//            for (int i = 0; i < inv.length; i++) {
//                if (inv[i] == 'n') {
//                    inv[i] = 'P';
//                    break;
//                }
//            }
//        }
    }

    public void addBall(GamepadEx gamepad) {
        if (gamepad.wasJustPressed(GamepadKeys.Button.A)) {
            for (int i = 0; i < inv.length; i++) {
                if (inv[i] == 'n') {
                    inv[i] = 'P';
                    break;
                }
            }
        }
    }


    public char getNextBall() {
        return inv[0];
    }

    public static boolean isFull() {
        return inv[3] != 'n';
    }

    public static boolean isEmpty() {
        return inv[0] == 'n';
    }

    public void periodic(MultipleTelemetry m_telemetry, Telemetry telemetry) {
        updateInventory();

        if (!Configs.Toggles.compMode) {
            m_telemetry.addData("Iv :----------INVENTORY----------", "Iv");
            m_telemetry.addData("Iv: Inventory 0", inv[0]);
            m_telemetry.addData("Iv: Inventory 1", inv[1]);
            m_telemetry.addData("Iv: Inventory 2", inv[2]);
            m_telemetry.addData("Iv: Inventory 3", inv[3]);
            m_telemetry.addData("Iv: last shot ball", lastShotType);
        } else {
            telemetry.addData("Iv :----------INVENTORY----------", "Iv");
            telemetry.addData("Iv: Inventory 0", inv[0]);
            telemetry.addData("Iv: Inventory 1", inv[1]);
            telemetry.addData("Iv: Inventory 2", inv[2]);
            telemetry.addData("Iv: Inventory 3", inv[3]);
            telemetry.addData("Iv: last shot ball", lastShotType);
        }
    }
}
