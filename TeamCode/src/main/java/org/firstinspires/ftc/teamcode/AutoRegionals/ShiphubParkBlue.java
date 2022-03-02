package org.firstinspires.ftc.teamcode.AutoRegionals;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous(name = "ShiphubParkBlueRegional")
public class ShiphubParkBlue extends LinearOpMode {

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

        Trajectory shippingHub = drive.trajectoryBuilder(startPose)
                .lineToLinearHeading(new Pose2d(25, 0, Math.toRadians(-300)))
                .build();
        drive.followTrajectory(shippingHub);
    }
}
