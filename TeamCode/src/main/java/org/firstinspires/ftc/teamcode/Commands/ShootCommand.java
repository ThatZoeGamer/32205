package org.firstinspires.ftc.teamcode.Commands;

import org.firstinspires.ftc.teamcode.Configs;
import org.firstinspires.ftc.teamcode.Subsystems.Intake.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Odometry;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter.Shooter;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.Limelight;

public class ShootCommand {
    Shooter shooter;
    Odometry odometry;
    Intake intake;
    Limelight limelight;
    public ShootCommand(Shooter s, Odometry o, Intake i, Limelight l) {
        shooter = s;
        intake = i;
        odometry = o;
        limelight = l;
    }

    public void shoot() {
        intake.closeStopper();
        shooter.runShooterL(Configs.ShooterConfigs.Misc.manualShooterVelocity);
        shooter.runShooterR(Configs.ShooterConfigs.Misc.manualShooterVelocity);
        if (shooter.atSetpoint()) {
            intake.openStopper();
        }
    }
}
