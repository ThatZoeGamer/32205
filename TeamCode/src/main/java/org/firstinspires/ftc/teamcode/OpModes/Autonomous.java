package org.firstinspires.ftc.teamcode.OpModes;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.util.Timing;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.teamcode.Configs;
import org.firstinspires.ftc.teamcode.Inventory;
import org.firstinspires.ftc.teamcode.Subsystems.Intake.ColourSensor;
import org.firstinspires.ftc.teamcode.Subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.Intake.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter.Turret;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.GPE;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.Limelight;
import org.firstinspires.ftc.teamcode.Subsystems.Odometry;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter.Shooter;

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
        inv = new Inventory(new char[] {'B','P', 'R', 'B'}, s_coloursensor, s_shooter);
        s_turret.zero();

        waitForStart();

        while (opModeIsActive()) {
            globalPeriodic();
            s_intake.runIntake();
            s_intake.transfer();
            s_shooter.setRSetpoint(Configs.ShooterConfigs.Misc.manualShooterVelocity);
            s_shooter.setLSetpoint(Configs.ShooterConfigs.Misc.manualShooterVelocity);
        }
    }
    public void globalPeriodic() {
        s_drivetrain.periodic();
//        s_limelight.periodic(m_telemetry);
//        s_GPE.periodic(m_telemetry);
//        s_turret.periodic();
//        s_intake.periodic();
        s_shooter.periodic(m_telemetry);
//        s_coloursensor.periodic();
//        o_odometry.periodic();
        inv.periodic(m_telemetry);
        m_telemetry.update();
    }
}