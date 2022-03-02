package org.firstinspires.ftc.teamcode.AutoRegionals;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous(name = "DuckParkRegionalBlue")
public class DuckParkRegionalBlue extends LinearOpMode {

    DcMotor duckMotor;
    private DcMotor frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor;

    @Override
    public void runOpMode() throws InterruptedException {

        Pose2d startPose = new Pose2d(0, 0, Math.toRadians(0));

        frontLeftMotor = hardwareMap.dcMotor.get("leftFront");
        rearLeftMotor = hardwareMap.dcMotor.get("leftRear");
        frontRightMotor = hardwareMap.dcMotor.get("rightFront");
        rearRightMotor = hardwareMap.dcMotor.get("rightRear");
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        duckMotor = hardwareMap.get(DcMotor.class, "duckMotor");

        drive.setPoseEstimate(startPose);

        Trajectory duck = drive.trajectoryBuilder(startPose)
                .strafeRight(25)
                .build();
        drive.followTrajectory(duck);

        duckMotor.setPower(0.7);
        sleep(2100);
        duckMotor.setPower(0);

        Trajectory forward = drive.trajectoryBuilder(duck.end())
                .lineToLinearHeading(new Pose2d(25, 0, Math.toRadians(111)))
                .build();
        drive.followTrajectory(forward);

        //Moves the robot forward, using this instead of .forward from roadrunner
        frontLeftMotor.setPower(1);
        frontRightMotor.setPower(1);
        rearLeftMotor.setPower(1);
        rearRightMotor.setPower(1);
        sleep(8000);
        frontLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        rearLeftMotor.setPower(0);
        rearRightMotor.setPower(0);



    }


}
