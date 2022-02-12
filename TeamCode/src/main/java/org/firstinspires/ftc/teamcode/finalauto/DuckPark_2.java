package org.firstinspires.ftc.teamcode.finalauto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous(name = "DuckPark_2.0")
public class DuckPark_2 extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        DcMotor duckMotor;

        Pose2d startPose = new Pose2d(0, 0, Math.toRadians(0));

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        duckMotor = hardwareMap.get(DcMotor.class, "duckMotor");


        drive.setPoseEstimate(startPose);

        Trajectory forward = drive.trajectoryBuilder(startPose)
                .strafeTo(new Vector2d(40, 0))
                .build();
        Trajectory duck = drive.trajectoryBuilder(forward.end())
                .lineToLinearHeading(new Pose2d(0, 27, Math.toRadians(-111)))
                .build();

        Trajectory warehouse = drive.trajectoryBuilder(duck.end())
                .strafeTo(new Vector2d(-20, -52))
                .build();

        Trajectory left = drive.trajectoryBuilder(warehouse.end())
                .strafeLeft(40)
                .build();

        Trajectory park = drive.trajectoryBuilder(left.end())
                .forward(75)
                .build();

        Trajectory right = drive.trajectoryBuilder(park.end())
                .strafeRight(30)
                .build();

        drive.followTrajectory(forward);
        drive.followTrajectory(duck);
        duckMotor.setPower(.7);
        sleep(2100);
        duckMotor.setPower(0);
        drive.followTrajectory(warehouse);
        drive.followTrajectory(left);
        drive.followTrajectory(park);
        drive.followTrajectory(right);



    }
}
