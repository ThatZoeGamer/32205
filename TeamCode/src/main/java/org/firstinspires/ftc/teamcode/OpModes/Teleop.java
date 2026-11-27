package org.firstinspires.ftc.teamcode.OpModes;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.util.Timing;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.teamcode.Commands.AimToHive;
import org.firstinspires.ftc.teamcode.Configs;
import org.firstinspires.ftc.teamcode.Field;
import org.firstinspires.ftc.teamcode.Inventory;
import org.firstinspires.ftc.teamcode.Subsystems.Intake.ColourSensor;
import org.firstinspires.ftc.teamcode.Subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.Intake.Intake;
//import org.firstinspires.ftc.teamcode.Subsystems.Shooter.Turret;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.GPE;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.Limelight;
import org.firstinspires.ftc.teamcode.Subsystems.Odometry;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter.Shooter;

@TeleOp(name="Delta", group="Teleop")
public class Teleop extends LinearOpMode {

    GamepadEx d_gamepad;
    GamepadEx o_gamepad;
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
//    Turret s_turret;
    GPE s_GPE;
    VoltageSensor vs;

    double netIntakePower;
    AimToHive aim;
    Field.Hives currentHive;

    @Override
    public void runOpMode() {
        vs = hardwareMap.voltageSensor.iterator().next();
        d_gamepad = new GamepadEx(gamepad1);
        o_gamepad = new GamepadEx(gamepad2);

        m_telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        s_drivetrain = new Drivetrain(hardwareMap);
        o_odometry = new Odometry(hardwareMap);
        s_limelight = new Limelight(hardwareMap, m_telemetry);
//        s_coloursensor = new ColourSensor(hardwareMap);
        s_intake = new Intake(hardwareMap);
        s_shooter = new Shooter(hardwareMap, vs);
        s_GPE = new GPE(s_limelight, o_odometry);
        inventory = new Inventory(new char[] {'n','n','n','n'}, s_coloursensor, s_shooter);
        aim = new AimToHive(o_odometry, s_drivetrain, s_limelight);
        currentHive = Field.Hives.westHive;

        waitForStart();

        while (opModeIsActive()) {
            initButtons();
            globalPeriodic();

            netIntakePower = d_gamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) - d_gamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER);

            s_drivetrain.drive(
                    -d_gamepad.getLeftY(),
                    d_gamepad.getLeftX(),
                    d_gamepad.getRightX()
            );
            s_intake.runIntake(netIntakePower);

            inventory.addBall(o_gamepad);

            if (d_gamepad.wasJustPressed(GamepadKeys.Button.X)) {o_odometry.resetHeading();}
            if (d_gamepad.wasJustPressed(GamepadKeys.Button.DPAD_LEFT)) {o_odometry.resetPosition();}

            if (o_gamepad.wasJustPressed(GamepadKeys.Button.DPAD_UP)) {s_intake.liftIntake();}
            if (o_gamepad.wasJustPressed(GamepadKeys.Button.DPAD_DOWN)) {s_intake.lowerIntake();}
            if (o_gamepad.wasJustPressed(GamepadKeys.Button.X)) {currentHive = Field.Hives.eastHive;}
            if (o_gamepad.wasJustPressed(GamepadKeys.Button.B)) {currentHive = Field.Hives.westHive;}

            if (o_gamepad.isDown(GamepadKeys.Button.Y) || d_gamepad.isDown(GamepadKeys.Button.LEFT_BUMPER)) {
                s_intake.openStopper();
            } else {
                s_intake.closeStopper();
            }

            if (d_gamepad.isDown(GamepadKeys.Button.RIGHT_BUMPER) || o_gamepad.isDown(GamepadKeys.Button.RIGHT_BUMPER)) {
                s_shooter.setLSetpoint(Configs.ShooterConfigs.Misc.manualShooterVelocity);
                s_shooter.setRSetpoint(Configs.ShooterConfigs.Misc.manualShooterVelocity);
//                aim.aim(currentHive);
            } else {
                s_shooter.setLSetpoint(0);
                s_shooter.setRSetpoint(0);
            }
        }
    }
    public void globalPeriodic() {
        s_drivetrain.periodic(m_telemetry);
        s_limelight.periodic(m_telemetry, telemetry);
//        s_GPE.periodic(m_telemetry);
        s_intake.periodic(m_telemetry, telemetry);
        s_shooter.periodic(m_telemetry, telemetry);
        o_odometry.periodic(m_telemetry, telemetry);
        inventory.periodic(m_telemetry, telemetry);
        if(!Configs.Toggles.compMode) {
            m_telemetry.update();
        } else {
            telemetry.update();
        }
    }
    public void initButtons() {
        d_gamepad.readButtons();
        o_gamepad.readButtons();
        aPressed = d_gamepad.isDown(GamepadKeys.Button.A);
        xPressed = d_gamepad.isDown(GamepadKeys.Button.X);
        yPressed = d_gamepad.isDown(GamepadKeys.Button.Y);
        lDpadPressed = d_gamepad.wasJustPressed(GamepadKeys.Button.DPAD_LEFT);
        rDpadPressed = d_gamepad.wasJustPressed(GamepadKeys.Button.DPAD_RIGHT);
        uDpadPressed = d_gamepad.wasJustPressed(GamepadKeys.Button.DPAD_UP);
        dDpadPressed = d_gamepad.wasJustPressed(GamepadKeys.Button.DPAD_DOWN);
        lbPressed = d_gamepad.isDown(GamepadKeys.Button.LEFT_BUMPER);
        rbPressed = d_gamepad.isDown(GamepadKeys.Button.RIGHT_BUMPER);
    }
}