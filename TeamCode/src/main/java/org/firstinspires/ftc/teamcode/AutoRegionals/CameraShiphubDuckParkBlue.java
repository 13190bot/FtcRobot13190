package org.firstinspires.ftc.teamcode.AutoRegionals;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
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

@Disabled
@Autonomous(name = "CameraShiphubDuckParkBlueOld")
public class CameraShiphubDuckParkBlue extends LinearOpMode {

    private DcMotor frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor;
    public DcMotor armRotationMotor;
    public DcMotor intakeMotor;
    public Servo directionServo;
    public DcMotor armEncoder;
    DcMotor duckMotor;

    private static final String TFOD_MODEL_ASSET = "/sdcard/FIRST/vision/customBoxNew.tflite";
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

        initVuforia();
        initTfod();

        if (tfod != null) {
            tfod.activate();
        }
        int level = 3; // This indicates the level detected

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
                        } else if (recognition.getLeft() < 600) {
                            level = 2;
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
        Trajectory shipHub;
        double targetPosition;
        switch(level){
            case 1:
                directionServo.setPosition(0.38);
                targetPosition = 2550;
                shipHub = drive.trajectoryBuilder(startPose)
                        .lineToLinearHeading(new Pose2d(51, 7, Math.toRadians(340)))
                        .build();
                break;
            case 2:
                directionServo.setPosition(0.22);
                targetPosition = 3530;
                shipHub = drive.trajectoryBuilder(startPose)
                        .lineToLinearHeading(new Pose2d(37, 15, Math.toRadians(200)))
                        .build();
                break;
            default:
                directionServo.setPosition(0.14);
                targetPosition = 4150;
                shipHub = drive.trajectoryBuilder(startPose)
                        .lineToLinearHeading(new Pose2d(55, 7, Math.toRadians(340)))
                        .build();
        }
        drive.followTrajectory(shipHub);
            boolean done = false;
            boolean recheck = false;
            intakeMotor.setPower(-0.2);
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
                        armRotationMotor.setPower(-0.15);
                    } else if (armEncoder.getCurrentPosition() > targetPosition && armEncoder.getCurrentPosition() - targetPosition > 400) {
                        armRotationMotor.setPower(0.7);
                    } else {
                        armRotationMotor.setPower(0.15);
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
                    .lineToLinearHeading(new Pose2d(10, 0, Math.toRadians(110)))
                    .build();
            drive.followTrajectory(left);

            frontLeftMotor.setPower(1);
            frontRightMotor.setPower(1);
            rearLeftMotor.setPower(1);
            rearRightMotor.setPower(1);
            sleep(2000);
            frontLeftMotor.setPower(0);
            frontRightMotor.setPower(0);
            rearLeftMotor.setPower(0);
        rearRightMotor.setPower(0);



    }

    private void initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam");

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
