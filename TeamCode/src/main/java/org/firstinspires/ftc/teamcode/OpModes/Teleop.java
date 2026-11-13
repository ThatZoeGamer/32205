package org.firstinspires.ftc.teamcode.OpModes;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.util.Timing;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import org.firstinspires.ftc.teamcode.Configs;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.Subsystems.ColourSensor;
import org.firstinspires.ftc.teamcode.Subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Transmission;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.GPE;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.Limelight;
import org.firstinspires.ftc.teamcode.Subsystems.Odometry;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter;
import org.firstinspires.ftc.teamcode.Subsystems.Turret;

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
    Transmission s_transmission;
    GPE s_GPE;
    double netIntakePower;

    @Override
    public void runOpMode() {
        gamepad = new GamepadEx(gamepad1);

        m_telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        s_drivetrain = new Drivetrain(hardwareMap, m_telemetry);
        o_odometry = new Odometry(hardwareMap, m_telemetry);
        s_limelight = new Limelight(hardwareMap, m_telemetry);
        s_coloursensor = new ColourSensor(hardwareMap, m_telemetry);
        s_intake = new Intake(hardwareMap, m_telemetry);
        s_shooter = new Shooter(hardwareMap, m_telemetry);
        s_turret = new Turret(hardwareMap, m_telemetry);
        s_GPE = new GPE(m_telemetry, s_limelight, o_odometry);
        s_transmission = new Transmission(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {
            globalPeriodic();
            initButtons();

            gamepad.readButtons();

            s_drivetrain.drive(
                    -gamepad.getLeftY(),
                    gamepad.getLeftX(),
                    gamepad.getRightX()
            );

            s_intake.runIntake(gamepad);
            s_transmission.transfer(gamepad);

            gamepad.readButtons();

//            if(lDpadPressed) {
//                s_turret.turnLeft();
//            } else if(rDpadPressed) {
//                s_turret.turnRight();
//            } else {
//                s_turret.zero();
//            }
        }
        if (uDpadPressed) {o_odometry.resetHeading();}

        if (dDpadPressed) {o_odometry.resetPosition();}
    }
    public void globalPeriodic() {
//        s_drivetrain.periodic(m_telemetry);
        s_limelight.periodic(m_telemetry);
        s_GPE.periodic(m_telemetry);
//        s_intake.periodic(m_telemetry, Configs.toggles.intakeTelemetry);
//        s_shooter.periodic(m_telemetry, Configs.shooterConfigs.manualShooterVelocity);
//        s_coloursensor.periodic(m_telemetry, Configs.toggles.colourSensorTelemetry);
        o_odometry.periodic(m_telemetry, Configs.toggles.odomTelemetry);
        m_telemetry.update();
    }

    public void initButtons() {
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
