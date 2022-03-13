package org.firstinspires.ftc.teamcode.AutoRegionals;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous(name = "ShiphubParkRegional")
public class ShiphubPark extends LinearOpMode {

    private DcMotor frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor;

    DcMotor duckMotor;
    public DcMotor armRotationMotor;
    public DcMotor intakeMotor;
    public Servo directionServo;
    public DcMotor armEncoder;

    @Override
    public void runOpMode() throws InterruptedException {

        Pose2d startPose = new Pose2d(0, 0, Math.toRadians(0));

        frontLeftMotor = hardwareMap.dcMotor.get("leftFront");
        rearLeftMotor = hardwareMap.dcMotor.get("leftRear");
        frontRightMotor = hardwareMap.dcMotor.get("rightFront");
        rearRightMotor = hardwareMap.dcMotor.get("rightRear");
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        armRotationMotor = hardwareMap.get(DcMotor.class, "rotationMotor");
        armRotationMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armRotationMotor.setDirection(DcMotor.Direction.REVERSE);
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        directionServo = hardwareMap.get(Servo.class, "directionServo");
        armEncoder = hardwareMap.get(DcMotor.class, "armEncoder");
        duckMotor = hardwareMap.dcMotor.get("duckMotor");

        drive.setPoseEstimate(startPose);

        waitForStart();

        Trajectory shipHub = drive.trajectoryBuilder(startPose)
                .lineToLinearHeading(new Pose2d(-28, 0, Math.toRadians(0)))
                .build();
        drive.followTrajectory(shipHub);

        //Code used below is used when robot is started backwards (should be the start position from now on)
        /*
        frontLeftMotor.setPower(-1);
        frontRightMotor.setPower(-1);
        rearLeftMotor.setPower(-1);
        rearRightMotor.setPower(-1);
        sleep(300);
         */
        frontLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        rearLeftMotor.setPower(0);
        rearRightMotor.setPower(0);


        boolean done = false;
        directionServo.setPosition(0.38);
        double targetPosition = 2450;
        boolean recheck = false;
        intakeMotor.setPower(0.2);
        while (!done) {
            telemetry.addData("armPos", armEncoder.getCurrentPosition());
            if (armEncoder.getCurrentPosition() < targetPosition + 20 && armEncoder.getCurrentPosition() > targetPosition - 20) {
                if (recheck == true) {
                    armRotationMotor.setPower(0);
                    done = true;
                } else {
                    armRotationMotor.setPower(0);
                    sleep(500);
                    recheck = true;
                }
            } else {
                if (armEncoder.getCurrentPosition() < targetPosition && targetPosition - armEncoder.getCurrentPosition() > 300) {
                    armRotationMotor.setPower(-0.7);
                } else if (armEncoder.getCurrentPosition() < targetPosition && targetPosition - armEncoder.getCurrentPosition() < 300) {
                    armRotationMotor.setPower(-0.25);
                } else if (armEncoder.getCurrentPosition() > targetPosition && armEncoder.getCurrentPosition() - targetPosition > 300) {
                    armRotationMotor.setPower(0.7);
                } else {
                    armRotationMotor.setPower(0.25);
                }
            }
            telemetry.update();
        }
        intakeMotor.setPower(1);
        sleep(2000);
        intakeMotor.setPower(0);

        //0.06
        directionServo.setPosition(0.5);
        while (armEncoder.getCurrentPosition() > 500) {
            armRotationMotor.setPower(0.4);
        }
        armRotationMotor.setPower(0);

        Trajectory line = drive.trajectoryBuilder(shipHub.end())
                .lineToLinearHeading(new Pose2d(10, 0, Math.toRadians(100)))
                .build();
        drive.followTrajectory(line);

        Trajectory left = drive.trajectoryBuilder(line.end())
                .strafeLeft(32)
                .build();
        drive.followTrajectory(left);

        frontLeftMotor.setPower(1);
        frontRightMotor.setPower(1);
        rearLeftMotor.setPower(1);
        rearRightMotor.setPower(1);
        sleep(1800);
        frontLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        rearLeftMotor.setPower(0);
        rearRightMotor.setPower(0);


    }
}
