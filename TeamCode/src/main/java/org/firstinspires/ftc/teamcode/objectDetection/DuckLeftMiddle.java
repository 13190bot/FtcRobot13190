package org.firstinspires.ftc.teamcode.objectDetection;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;


@TeleOp
public class DuckLeftMiddle extends LinearOpMode {

    private static final String TFOD_MODEL_ASSET = "/sdcard/FIRST/vision/customBoxNew.tflite";
    private static final String[] LABELS = {
            "customBox"
    };

    private static final String VUFORIA_KEY =
            "AR1EGWL/////AAABmao6fwhlA0nZgC4AC92PSFIkRoulXKGjKgy0eFqp2+gwuiWL9ULzw2QJD/Jr7os9Xby/GjZHBwwPW3P6vvVfidwd556TIQRTX6NzaGOooiLjLWebMMHcEJdvLD+4VdbHvZaEiXlH4O/Vb+Rqqo+PS5LUE9LQxnYtSYvbtWDVz757S56MSByBrH7Zt7zTFu0a3Rlvr7s7o9wGR74qQ1jI/vIuWWUIWXPUXCb9L+TVqMPFk0yOumhdyUhmTf8JXBPOWnppwXKJ7049tnegzoc6Ov+IuIu7FsKYgrLa2dI9iufeFN8/ITlZTzkmjl17KhdPbQpiJs68rleAN3LIsFsgSpL5ZWxd4ZcZ3WeEFaEREQfn";

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    @Override
    public void runOpMode() {
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
        telemetry.update();

/*
        if (opModeIsActive()) {
            while (opModeIsActive()) {
                while (tfod == null) {
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
                                    if (recognition.getLabel().equals("CustomBox")) {
                                        telemetry.addData(recognition.getLabel(), " Position: Level 1");
                                    }
                                } else if (recognition.getLeft() < 530) {
                                    if (recognition.getLabel().equals("CustomBox")) {
                                        telemetry.addData(recognition.getLabel(), " Position: Level 2");
                                    }
                                } else {
                                    telemetry.addData("No detection", "Level 3");
                                }

                                i++;
                            }
                            telemetry.update();
                        }
                    }
                }
            }
        }*/

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
