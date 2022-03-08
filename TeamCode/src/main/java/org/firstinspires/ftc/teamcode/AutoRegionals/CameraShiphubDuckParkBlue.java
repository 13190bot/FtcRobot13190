package org.firstinspires.ftc.teamcode.AutoRegionals;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

import java.util.List;

@Autonomous(name = "CameraShiphubDuckParkBlue")
public class CameraShiphubDuckParkBlue extends LinearOpMode {

    private DcMotor frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor;
    public DcMotor armRotationMotor;
    public DcMotor intakeMotor;
    public Servo directionServo;
    public DcMotor armEncoder;
    DcMotor duckMotor;

    private static final String TFOD_MODEL_ASSET = "/sdcard/FIRST/vision/customBox.tflite";
    private static final String[] LABELS = {
            "customBox"
    };

    private static final String VUFORIA_KEY =
            "AR1EGWL/////AAABmao6fwhlA0nZgC4AC92PSFIkRoulXKGjKgy0eFqp2+gwuiWL9ULzw2QJD/Jr7os9Xby/GjZHBwwPW3P6vvVfidwd556TIQRTX6NzaGOooiLjLWebMMHcEJdvLD+4VdbHvZaEiXlH4O/Vb+Rqqo+PS5LUE9LQxnYtSYvbtWDVz757S56MSByBrH7Zt7zTFu0a3Rlvr7s7o9wGR74qQ1jI/vIuWWUIWXPUXCb9L+TVqMPFk0yOumhdyUhmTf8JXBPOWnppwXKJ7049tnegzoc6Ov+IuIu7FsKYgrLa2dI9iufeFN8/ITlZTzkmjl17KhdPbQpiJs68rleAN3LIsFsgSpL5ZWxd4ZcZ3WeEFaEREQfn";

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

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

        int level = 3; // This indicates the level detected
        if (tfod != null) {
            tfod.activate();
        }
        while (!isStarted()) {
            if (tfod != null) {
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());
                    int i = 0;
                    for (Recognition recognition : updatedRecognitions) {
                        telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                        telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                recognition.getLeft(), recognition.getTop());
                        telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                recognition.getRight(), recognition.getBottom());

                        if (recognition.getLeft() < 200) {
                            level = 1;
                        } else if (recognition.getLeft() < 530) {
                            level = 2;
                        } else {
                            level = 3;
                        }
                        i++;
                        telemetry.addData("Level: ", level);
                    }
                    telemetry.update();
                }
            }
        }

        waitForStart();

        //TODO add driving and arm code
        if (level == 1) {
            Trajectory shipHub = drive.trajectoryBuilder(startPose)
                    .lineToLinearHeading(new Pose2d(50, 0, Math.toRadians(240)))
                    .build();
            drive.followTrajectory(shipHub);
            boolean done = false;
            directionServo.setPosition(0.14);
            double targetPosition = 4050;
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
                    if (armEncoder.getCurrentPosition() < targetPosition && targetPosition - armEncoder.getCurrentPosition() > 400) {
                        armRotationMotor.setPower(-0.7);
                    } else if (armEncoder.getCurrentPosition() < targetPosition && targetPosition - armEncoder.getCurrentPosition() < 400) {
                        armRotationMotor.setPower(-0.25);
                    } else if (armEncoder.getCurrentPosition() > targetPosition && armEncoder.getCurrentPosition() - targetPosition > 400) {
                        armRotationMotor.setPower(0.7);
                    } else {
                        armRotationMotor.setPower(0.25);
                    }
                }
                telemetry.update();
            }
            intakeMotor.setPower(-1);
            sleep(2500);
            intakeMotor.setPower(0);

            directionServo.setPosition(0.06);
            while (armEncoder.getCurrentPosition() > 500) {
                armRotationMotor.setPower(0.4);
            }
            armRotationMotor.setPower(0);

            Trajectory duck = drive.trajectoryBuilder(shipHub.end())
                    .lineToLinearHeading(new Pose2d(0, 30, Math.toRadians(-460)))
                    .build();
            drive.followTrajectory(duck);

            duckMotor.setPower(0.7);
            sleep(2100);
            duckMotor.setPower(0);

            Trajectory left = drive.trajectoryBuilder(duck.end())
                    .strafeLeft(30)
                    .build();
            drive.followTrajectory(left);

            frontLeftMotor.setPower(1);
            frontRightMotor.setPower(1);
            rearLeftMotor.setPower(1);
            rearRightMotor.setPower(1);
            sleep(8000);
            frontLeftMotor.setPower(0);
            frontRightMotor.setPower(0);
            rearLeftMotor.setPower(0);
            rearRightMotor.setPower(0);


        } else if (level == 2) {
            Trajectory shipHub = drive.trajectoryBuilder(startPose)
                    .lineToLinearHeading(new Pose2d(25, 0, Math.toRadians(240)))
                    .build();
            drive.followTrajectory(shipHub);
            boolean done = false;
            directionServo.setPosition(0.22);
            double targetPosition = 3430;
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
            sleep(2500);
            intakeMotor.setPower(0);

            directionServo.setPosition(0.06);
            while (armEncoder.getCurrentPosition() > 500) {
                armRotationMotor.setPower(0.4);
            }
            armRotationMotor.setPower(0);

            drive.turnAsync(Math.toRadians(180));
            Trajectory left2 = drive.trajectoryBuilder(shipHub.end())
                    .strafeLeft(30)
                    .build();
            drive.followTrajectory(left2);

            frontLeftMotor.setPower(-1);
            frontRightMotor.setPower(-1);
            rearLeftMotor.setPower(-1);
            rearRightMotor.setPower(-1);
            sleep(2100);
            frontLeftMotor.setPower(0);
            frontRightMotor.setPower(0);
            rearLeftMotor.setPower(0);
            rearRightMotor.setPower(0);

            duckMotor.setPower(0.7);
            sleep(2100);
            duckMotor.setPower(0);

            Trajectory left = drive.trajectoryBuilder(left2.end())
                    .strafeLeft(30)
                    .build();
            drive.followTrajectory(left);

            frontLeftMotor.setPower(1);
            frontRightMotor.setPower(1);
            rearLeftMotor.setPower(1);
            rearRightMotor.setPower(1);
            sleep(8000);
            frontLeftMotor.setPower(0);
            frontRightMotor.setPower(0);
            rearLeftMotor.setPower(0);
            rearRightMotor.setPower(0);


        } else {
            Trajectory shipHub = drive.trajectoryBuilder(startPose)
                    .lineToLinearHeading(new Pose2d(62, 7, Math.toRadians(240)))
                    .build();
            drive.followTrajectory(shipHub);
            boolean done = false;
            directionServo.setPosition(0.38);
            double targetPosition = 2550;
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
            sleep(1000);
            intakeMotor.setPower(0);

            directionServo.setPosition(0.5);
            while (armEncoder.getCurrentPosition() > 500) {
                armRotationMotor.setPower(0.4);
            }
            armRotationMotor.setPower(0);

            Trajectory duck = drive.trajectoryBuilder(shipHub.end())
                    .lineToLinearHeading(new Pose2d(0, -30, Math.toRadians(0)))
                    .build();
            drive.followTrajectory(duck);

            duckMotor.setPower(-0.7);
            sleep(3000);
            duckMotor.setPower(0);

            Trajectory left = drive.trajectoryBuilder(duck.end())
                    .lineToLinearHeading(new Pose2d(10, 0, Math.toRadians(120)))
                    .build();
            drive.followTrajectory(left);

            frontLeftMotor.setPower(1);
            frontRightMotor.setPower(1);
            rearLeftMotor.setPower(1);
            rearRightMotor.setPower(1);
            sleep(2500);
            frontLeftMotor.setPower(0);
            frontRightMotor.setPower(0);
            rearLeftMotor.setPower(0);
            rearRightMotor.setPower(0);


        }


    }

    private void initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromFile(TFOD_MODEL_ASSET, LABELS);
    }
}
