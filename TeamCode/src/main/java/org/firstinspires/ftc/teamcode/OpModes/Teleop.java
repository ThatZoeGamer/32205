package org.firstinspires.ftc.teamcode.OpModes;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.util.Timing;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import org.firstinspires.ftc.teamcode.Configs;
import org.firstinspires.ftc.teamcode.Subsystems.ColourSensor;
import org.firstinspires.ftc.teamcode.Subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Limelight;
import org.firstinspires.ftc.teamcode.Subsystems.Odometry;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter;
import org.firstinspires.ftc.teamcode.Subsystems.Turret;

import java.util.Arrays;

@TeleOp(name="Delta", group="Teleop")
public class Teleop extends LinearOpMode {

    GamepadEx gamepad;
    ColourSensor s_coloursensor;

    boolean aPressed;
    boolean xPressed;
    boolean yPressed;
    boolean lbPressed;
    boolean rbPressed;
    boolean lDpadPressed;
    boolean rDpadPressed;
    GamepadKeys.Trigger leftTrigger = GamepadKeys.Trigger.LEFT_TRIGGER;
    GamepadKeys.Trigger rightTrigger = GamepadKeys.Trigger.RIGHT_TRIGGER;
    Timing.Timer indexTimer;
    Intake s_intake;
    Drivetrain s_drivetrain;
    Limelight s_limelight;
    Shooter s_shooter;

    Turret s_turret;
    MultipleTelemetry m_telemetry;
    Odometry o_odometry;
    double netIntakePower;

    @Override
    public void runOpMode() {
        gamepad = new GamepadEx(gamepad1);

        m_telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        s_drivetrain = new Drivetrain(hardwareMap);
        o_odometry = new Odometry(hardwareMap);
        s_limelight = new Limelight(hardwareMap);
        s_coloursensor = new ColourSensor(hardwareMap);
        s_intake = new Intake(hardwareMap);
        s_shooter = new Shooter(hardwareMap);
        s_turret = new Turret(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {
            s_drivetrain.periodic(m_telemetry);
            s_limelight.periodic(m_telemetry);
            s_intake.periodic(m_telemetry);
            o_odometry.periodic(m_telemetry);
            s_shooter.periodic(m_telemetry);
            s_coloursensor.periodic(m_telemetry);
            s_turret.periodic(m_telemetry);

            s_turret.setTurretPosition(s_turret.getSetpoint());

            m_telemetry.update();

            s_drivetrain.drive(
                    -gamepad.getLeftY(),
                    gamepad.getLeftX(),
                    -gamepad.getRightX()
            );
            netIntakePower = Math.max(0, (gamepad.getTrigger(leftTrigger) - Configs.intakeConfigs.intakeTriggerThresh))
                    - Math.max(0, (gamepad.getTrigger(rightTrigger) - Configs.intakeConfigs.outtakeTriggerThresh));
            s_intake.runIntake(netIntakePower);

            aPressed = gamepad.isDown(GamepadKeys.Button.A);
            xPressed = gamepad.isDown(GamepadKeys.Button.X);
            yPressed = gamepad.isDown(GamepadKeys.Button.Y);
            lDpadPressed = gamepad.wasJustPressed(GamepadKeys.Button.DPAD_LEFT);
            rDpadPressed = gamepad.wasJustPressed(GamepadKeys.Button.DPAD_RIGHT);
            lbPressed = gamepad.isDown(GamepadKeys.Button.LEFT_BUMPER);
            rbPressed = gamepad.isDown(GamepadKeys.Button.RIGHT_BUMPER);

            gamepad.readButtons();

            while(lDpadPressed) {
                s_shooter.setPower();
            }
            while(lDpadPressed) {
                s_shooter.stopShooters();
            }

//
//            String Ball = "";
//            int totalCount = 0;
//            String[] Balls = new String[3];

//            if(s_coloursensor.red() > 80) {
//                Ball = "Red Nectar";
//                Balls[totalCount] = "Red";
//                totalCount++;
//            } else if(s_coloursensor.blue() > 80) {
//                Ball = "Blue Nectar";
//                Balls[totalCount] = "Blue";
//                totalCount++;
//            } else if(s_coloursensor.green() > 120) {
//                Ball = "Pollen";
//                Balls[totalCount] = "Pollen";
//                totalCount++;
//            }

//            yPressed = gamepad.isDown(GamepadKeys.Button.Y);
//            if(yPressed = true) {
//                totalCount = 0;
//                for (int i = 0; i < 5; i++) {
//                    Balls[i] = "null";
//                }
//            }
//            String order = Arrays.toString(Balls);
//
//            telemetry.addData("Ball in Intake: ", Ball);
//            telemetry.addData("Total Objects Counted", totalCount);
//            telemetry.addData("Order of Balls", order);

            if (xPressed) {
                o_odometry.resetHeading();
            }

            if (aPressed) {
                o_odometry.resetPosition();
            }
        }
    }
}