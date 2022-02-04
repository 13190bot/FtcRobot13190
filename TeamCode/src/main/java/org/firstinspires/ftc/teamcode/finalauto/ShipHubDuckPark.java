package org.firstinspires.ftc.teamcode.finalauto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous(name = "ShipHubDuckPark")
public class ShipHubDuckPark extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        Pose2d startPose = new Pose2d(0, 0, 0);

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        drive.setPoseEstimate(startPose);

        Trajectory shippingHub = drive.trajectoryBuilder(startPose)
                .lineToLinearHeading(new Pose2d(30, 10, Math.toRadians(107)))
                .build();

        Trajectory duck = drive.trajectoryBuilder(shippingHub.end())
                .lineToLinearHeading(new Pose2d(2, 27, Math.toRadians(-107)))
                .build();

        Trajectory warehouse = drive.trajectoryBuilder(duck.end())
                .forward(82)
                .build();

        Trajectory warehouse_2 = drive.trajectoryBuilder(warehouse.end())
                .strafeLeft(35)
                .build();

        Trajectory warehouse_3 = drive.trajectoryBuilder(warehouse_2.end())
                .forward(80)
                .build();



        drive.followTrajectory(shippingHub);
        //Do arm stuff
        drive.followTrajectory(duck);
        drive.followTrajectory(warehouse);
        drive.followTrajectory(warehouse_2);
        drive.followTrajectory(warehouse_3);
    }
}
