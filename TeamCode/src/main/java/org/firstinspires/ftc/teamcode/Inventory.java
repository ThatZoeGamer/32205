package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import org.firstinspires.ftc.teamcode.Subsystems.Intake.ColourSensor;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter.Shooter;

public class Inventory {
    char[] inv;
    ColourSensor cs;
    Shooter s;
    private char lastShotType = 'n';
    public Inventory(char[] inv, ColourSensor cs, Shooter s) {
        this.inv = inv;
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
        if (cs.newBall()) {
            for (int i = 0; i < inv.length; i++) {
                if (inv[i] == 'n') {
                    inv[i] = cs.lastBall();
                    break;
                }
            }
        }
    }

    public char getNextBall() {
        return inv[0];
    }

    public boolean isFull() {
        return inv[3] == 'n';
    }

    public void periodic(MultipleTelemetry telemetry) {
        updateInventory();

        if (telemetry != null) {
            telemetry.addData("Iv :----------INVENTORY----------", "Iv");
            telemetry.addData("Iv: Inventory 0", inv[0]);
            telemetry.addData("Iv: Inventory 1", inv[1]);
            telemetry.addData("Iv: Inventory 2", inv[2]);
            telemetry.addData("Iv: Inventory 3", inv[3]);
            telemetry.addData("Iv: last shot ball", lastShotType);
            telemetry.addData("Iv: newest ball", cs.lastBall());
        }
    }
}
