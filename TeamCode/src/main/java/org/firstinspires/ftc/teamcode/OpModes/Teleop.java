package org.firstinspires.ftc.teamcode.OpModes;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.util.Timing;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
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

@TeleOp(name="Delta", group="Teleop")
public class Teleop extends LinearOpMode {

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
    Timing.Timer indexTimer;
    Inventory inventory;
    Intake s_intake;
    Drivetrain s_drivetrain;
    Limelight s_limelight;
    ColourSensor s_coloursensor;
    Shooter s_shooter;
    MultipleTelemetry m_telemetry;
    Odometry o_odometry;
    Turret s_turret;
    GPE s_GPE;
    VoltageSensor vs;
    double netIntakePower;

    @Override
    public void runOpMode() {
        vs = hardwareMap.voltageSensor.iterator().next();
        gamepad = new GamepadEx(gamepad1);

        m_telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        inventory = new Inventory(new char[] {'n','n','n','n'}, s_coloursensor, s_shooter);

        s_drivetrain = new Drivetrain(hardwareMap);
        o_odometry = new Odometry(hardwareMap);
        s_limelight = new Limelight(hardwareMap, m_telemetry);
        s_coloursensor = new ColourSensor(hardwareMap);
        s_intake = new Intake(hardwareMap);
        s_shooter = new Shooter(hardwareMap, vs);
        s_turret = new Turret(hardwareMap);
        s_GPE = new GPE(s_limelight, o_odometry);

        s_turret.zero();

        waitForStart();

        while (opModeIsActive()) {
            globalPeriodic();
            initButtons();
            netIntakePower = gamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) - gamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER);

            s_drivetrain.drive(
                    -gamepad.getLeftY(),
                    gamepad.getLeftX(),
                    gamepad.getRightX()
            );


            s_intake.runIntake(netIntakePower);
            s_intake.transfer(netIntakePower);
            if (rbPressed) {
                s_shooter.setCompression(inventory.getNextBall());
                s_shooter.setLSetpoint(Configs.ShooterConfigs.Misc.manualShooterVelocity);
                s_shooter.setRSetpoint(Configs.ShooterConfigs.Misc.manualShooterVelocity);
            } else {
                s_shooter.setLSetpoint(0);
                s_shooter.setRSetpoint(0);
            }

            s_turret.setSetpoint(Configs.TurretConfigs.setpoint);

            if(gamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0) {
                s_shooter.setLSetpoint(Configs.ShooterConfigs.Misc.manualShooterVelocity);
                s_shooter.setRSetpoint(Configs.ShooterConfigs.Misc.manualShooterVelocity);
            } else {
                s_shooter.setLSetpoint(0);
                s_shooter.setRSetpoint(0);
            }

            if (uDpadPressed) {o_odometry.resetHeading();}
            if (dDpadPressed) {o_odometry.resetPosition();}
        }
    }
    public void globalPeriodic() {
        s_drivetrain.periodic(m_telemetry);
        s_limelight.periodic(m_telemetry);
        s_GPE.periodic(m_telemetry);
        s_turret.periodic(m_telemetry);
        s_intake.periodic(m_telemetry);
        s_shooter.periodic(m_telemetry);
        s_coloursensor.periodic(m_telemetry);
        o_odometry.periodic(m_telemetry);
        inventory.periodic(m_telemetry);
        m_telemetry.update();
    }
    public void initButtons() {
        gamepad.readButtons();
        aPressed = gamepad.isDown(GamepadKeys.Button.A);
        xPressed = gamepad.isDown(GamepadKeys.Button.X);
        yPressed = gamepad.isDown(GamepadKeys.Button.Y);
        lDpadPressed = gamepad.wasJustPressed(GamepadKeys.Button.DPAD_LEFT);
        rDpadPressed = gamepad.wasJustPressed(GamepadKeys.Button.DPAD_RIGHT);
        uDpadPressed = gamepad.wasJustPressed(GamepadKeys.Button.DPAD_UP);
        dDpadPressed = gamepad.wasJustPressed(GamepadKeys.Button.DPAD_DOWN);
        lbPressed = gamepad.isDown(GamepadKeys.Button.LEFT_BUMPER);
        rbPressed = gamepad.isDown(GamepadKeys.Button.RIGHT_BUMPER);
    }
}