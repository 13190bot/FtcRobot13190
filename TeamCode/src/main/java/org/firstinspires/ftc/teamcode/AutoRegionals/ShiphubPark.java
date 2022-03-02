package org.firstinspires.ftc.teamcode.AutoRegionals;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous(name = "ShipHubParkRegional")
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

        Trajectory shippingHub = drive.trajectoryBuilder(startPose)
                .lineToLinearHeading(new Pose2d(40, 0, Math.toRadians(-300)))
                .build();
        drive.followTrajectory(shippingHub);

        boolean done = false;
        directionServo.setPosition(0.38);
        double targetPosition =2450;
        boolean recheck = false;
        intakeMotor.setPower(0.2);
        while(!done){
            telemetry.addData("armPos", armEncoder.getCurrentPosition());
            if(armEncoder.getCurrentPosition() < targetPosition+20 && armEncoder.getCurrentPosition() > targetPosition-20){
                if(recheck == true){
                    armRotationMotor.setPower(0);
                    done = true;
                }else{
                    armRotationMotor.setPower(0);
                    sleep(500);
                    recheck = true;
                }
            }
            else {
                if(armEncoder.getCurrentPosition() < targetPosition && targetPosition-armEncoder.getCurrentPosition() > 300){
                    armRotationMotor.setPower(-0.7);
                }else if(armEncoder.getCurrentPosition() < targetPosition && targetPosition-armEncoder.getCurrentPosition() < 300){
                    armRotationMotor.setPower(-0.25);
                }else if(armEncoder.getCurrentPosition() > targetPosition && armEncoder.getCurrentPosition()-targetPosition > 300){
                    armRotationMotor.setPower(0.7);
                }else{
                    armRotationMotor.setPower(0.25);
                }
            }
            telemetry.update();
        }

        intakeMotor.setPower(-1);
        sleep(2500);
        intakeMotor.setPower(0);

        directionServo.setPosition(0.06);
        while(armEncoder.getCurrentPosition() > 500){
            armRotationMotor.setPower(0.4);
        }
        armRotationMotor.setPower(0);

        Trajectory duck = drive.trajectoryBuilder(shippingHub.end())
                .lineToLinearHeading(new Pose2d(0, 30, Math.toRadians(-460)))
                .build();
        drive.followTrajectory(duck);

        duckMotor.setPower(.7);
        sleep(2000);
        duckMotor.setPower(0);

        Trajectory left = drive.trajectoryBuilder(duck.end())
                .strafeLeft(20)
                .build();
        drive.followTrajectory(left);


        //Moves the robot forward, using this instead of the .forward trajectory from roadrunner
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


