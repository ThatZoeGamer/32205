package org.firstinspires.ftc.teamcode.OpModes;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.util.Timing;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import org.firstinspires.ftc.teamcode.Configs;
import org.firstinspires.ftc.teamcode.Inventory;
import org.firstinspires.ftc.teamcode.Subsystems.ColourSensor;
import org.firstinspires.ftc.teamcode.Subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Transfer;
import org.firstinspires.ftc.teamcode.Subsystems.Turret;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.GPE;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.Limelight;
import org.firstinspires.ftc.teamcode.Subsystems.Odometry;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="Delta", group="Auto")
public class Autonomous extends LinearOpMode {

    GamepadEx gamepad;
    boolean aPressed;
    boolean xPressed;
    boolean yPressed;
    boolean lbPressed;
    boolean rbPressed;
    boolean lDpadPressed;
    boolean rDpadPressed;
    boolean uDpadPressed;
    boolean dDpadPressed;
    GamepadKeys.Trigger leftTrigger = GamepadKeys.Trigger.LEFT_TRIGGER;
    GamepadKeys.Trigger rightTrigger = GamepadKeys.Trigger.RIGHT_TRIGGER;
    Timing.Timer indexTimer;
    Intake s_intake;
    Drivetrain s_drivetrain;
    Limelight s_limelight;
    ColourSensor s_coloursensor;
    Shooter s_shooter;
    MultipleTelemetry m_telemetry;
    Odometry o_odometry;
    Turret s_turret;
    Transfer s_transfer;
    GPE s_GPE;
    Inventory inv;
    VoltageSensor vs;

    @Override
    public void runOpMode() {
        vs = hardwareMap.voltageSensor.iterator().next();
        gamepad = new GamepadEx(gamepad1);

        m_telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        s_drivetrain = new Drivetrain(hardwareMap);
        o_odometry = new Odometry(hardwareMap);
        s_limelight = new Limelight(hardwareMap, m_telemetry);
        s_coloursensor = new ColourSensor(hardwareMap);
        s_intake = new Intake(hardwareMap);
        s_shooter = new Shooter(hardwareMap, vs);
        s_turret = new Turret(hardwareMap);
        s_GPE = new GPE(s_limelight, o_odometry);
        s_transfer = new Transfer(hardwareMap);
        inv = new Inventory(new char[] {'B','P', 'R', 'B'}, s_coloursensor, s_shooter);
        s_turret.zero();
        ElapsedTime shootTimer = new ElapsedTime();

        waitForStart();

        while (opModeIsActive() && shootTimer.seconds() < 10.0) {
            globalPeriodic();
            s_intake.runIntake();
            s_transfer.transfer();
            s_shooter.setRSetpoint(Configs.ShooterConfigs.Misc.manualShooterVelocity);
            s_shooter.setLSetpoint(Configs.ShooterConfigs.Misc.manualShooterVelocity);
        }

        s_intake.stop();
        s_transfer.transfer(gamepad);
        s_shooter.stopShooters();
        s_shooter.setRSetpoint(0);
        s_shooter.setLSetpoint(0);
        s_drivetrain.drive(0, 0, 0);

        double startingX = o_odometry.getPose().getX(DistanceUnit.INCH);
        double forwardTarget = startingX + 39.3701;
        ElapsedTime forwardTimer = new ElapsedTime();

        while (opModeIsActive()
                && forwardTimer.seconds() < 5.0
                && o_odometry.getPose().getX(DistanceUnit.INCH) < forwardTarget) {
            globalPeriodic();
            s_drivetrain.drive(0, 0.35, 0);
        }
        s_drivetrain.drive(0, 0, 0);

        ElapsedTime returnDelayTimer = new ElapsedTime();
        while (opModeIsActive() && returnDelayTimer.seconds() < 5.0) {
            globalPeriodic();
        }

        double returnTarget = startingX;
        ElapsedTime returnTimer = new ElapsedTime();
        while (opModeIsActive()
                && returnTimer.seconds() < 5.0
                && o_odometry.getPose().getX(DistanceUnit.INCH) > returnTarget) {
            globalPeriodic();
            s_drivetrain.drive(0, -0.35, 0);
        }
        s_drivetrain.drive(0, 0, 0);

        while (opModeIsActive()) {
            globalPeriodic();
        }
    }
    public void globalPeriodic() {
        s_drivetrain.periodic(m_telemetry);
        o_odometry.periodic(m_telemetry);
        s_shooter.periodic(m_telemetry);
        inv.periodic(m_telemetry);
        m_telemetry.update();
    }
}