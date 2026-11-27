package org.firstinspires.ftc.teamcode.OpModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.Commands.AutoDrive;
import org.firstinspires.ftc.teamcode.Configs;
import org.firstinspires.ftc.teamcode.Field;
import org.firstinspires.ftc.teamcode.Inventory;
import org.firstinspires.ftc.teamcode.Subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.Intake.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Odometry;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter.Shooter;
import org.firstinspires.ftc.teamcode.Field.AutoPaths;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="Delta", group="Auto")
public class Autonomous extends LinearOpMode {
    private Intake s_intake;
    private Drivetrain s_drivetrain;
    private Shooter s_shooter;
    private MultipleTelemetry m_telemetry;
    private Odometry o_odometry;
    private Inventory inv;
    private AutoDrive auto;

    private boolean phaseInitialized = false;
    private final ElapsedTime phaseTimer = new ElapsedTime();

    @Override
    public void runOpMode() {
        VoltageSensor vs = hardwareMap.voltageSensor.iterator().next();
        m_telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        // Only initialize hardware actively used in this Auto routine
        s_drivetrain = new Drivetrain(hardwareMap);
        o_odometry = new Odometry(hardwareMap);
        s_intake = new Intake(hardwareMap);
        s_shooter = new Shooter(hardwareMap, vs);
        inv = new Inventory(new char[]{'P', 'P', 'P', 'P'}, null, s_shooter);

        auto = new AutoDrive(o_odometry, s_drivetrain);

        int phase = 0;
        waitForStart();

        o_odometry.setPose(AutoPaths.startPose);
        phaseTimer.reset();

        while (opModeIsActive()) {
            switch (phase) {
                case 0:
                    if (phaseTimer.seconds() < 5.0) {
                        s_intake.closeStopper();
                        s_intake.runIntake();
                        s_shooter.setLSetpoint(Configs.ShooterConfigs.Misc.manualShooterVelocity);
                        s_shooter.setRSetpoint(Configs.ShooterConfigs.Misc.manualShooterVelocity);
                        if (phaseTimer.seconds() >= 3) {
                            s_intake.openStopper();
                        } else {
                            s_intake.closeStopper();
                        }
                    } else {
                        s_shooter.setLSetpoint(0);
                        s_shooter.setRSetpoint(0);
                        s_intake.stop();
                        phase++;
                    }
                    break;

                case 1:
                    if (!phaseInitialized) {
                        auto.init();
                        phaseInitialized = true;
                    }
                    auto.drive(AutoPaths.point1, m_telemetry, telemetry, 0.6, 0.5);
                    if (auto.isFinished()) {
                        phaseInitialized = false;
                        phase++;
                    }
                    break;

                case 2:
                    if (!phaseInitialized) {
                        auto.init();
                        phaseInitialized = true;
                    }
                    auto.drive(AutoPaths.point2, m_telemetry, telemetry, 0.6, 0.5);
                    if (auto.isFinished()) {
                        phaseInitialized = false;
                        phase++;
                    }
                    break;

                default:
                    s_drivetrain.stop();
                    break;
            }

            globalPeriodic();
        }
    }

    private void globalPeriodic() {
        s_drivetrain.periodic();
        o_odometry.periodic(m_telemetry, telemetry);
        s_intake.periodic(m_telemetry, telemetry);
        s_shooter.periodic(m_telemetry, telemetry);
        inv.periodic(m_telemetry, telemetry);

        if (!Configs.Toggles.compMode) {
            m_telemetry.update();
        } else {
            telemetry.update();
        }
    }
}
