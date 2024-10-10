package edu.project.eventmanagement.service;

import java.io.IOException;

public class PythonServiceLauncher {

    public static void startPythonMicroservice() {
        String pythonPath = "path/to/python";
        String scriptPath = "path/to/python-microservice/bert_service.py";

        ProcessBuilder processBuilder = new ProcessBuilder(pythonPath, scriptPath);

        try {
            processBuilder.start();
            System.out.println("Python microservice started successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to start Python microservice.");
        }
    }
}
