package com.example.app.gui;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatientRoomLookupApp extends JFrame {

    private final JTextField patientIdField;
    private final JLabel statusLabel;
    private final JTextArea resultArea;

    public PatientRoomLookupApp() {
        setTitle("Patient Room Lookup");
        setSize(560, 360);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(12, 12));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JLabel titleLabel = new JLabel("Patient Room Lookup");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel instructionLabel = new JLabel("Enter a patient ID to find rooms used by that patient. Example: try 1.");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 4, 4));
        headerPanel.add(titleLabel);
        headerPanel.add(instructionLabel);

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JLabel patientIdLabel = new JLabel("Patient ID:");
        patientIdLabel.setFont(new Font("Arial", Font.BOLD, 15));

        patientIdField = new JTextField(12);
        patientIdField.setFont(new Font("Arial", Font.PLAIN, 15));

        JButton searchButton = new JButton("Find Rooms");
        searchButton.setFont(new Font("Arial", Font.BOLD, 15));
        searchButton.setPreferredSize(new Dimension(130, 34));

        inputPanel.add(patientIdLabel);
        inputPanel.add(patientIdField);
        inputPanel.add(searchButton);

        statusLabel = new JLabel("Ready.");
        statusLabel.setOpaque(true);
        statusLabel.setBackground(new Color(230, 240, 255));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        resultArea = new JTextArea(6, 30);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Arial", Font.PLAIN, 15));
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setText("Results will appear here.");

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(inputPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(8, 8));
        bottomPanel.add(statusLabel, BorderLayout.NORTH);
        bottomPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        searchButton.addActionListener(e -> findRooms());
    }

    private void findRooms() {
        String patientId = patientIdField.getText().trim();

        if (patientId.isBlank()) {
            showMessage("Input needed", "Please enter a whole number patient ID.", new Color(255, 245, 200));
            return;
        }

        try {
            Integer.parseInt(patientId);

            String response = callApi(patientId);
            String formattedResult = formatResult(patientId, response);

            showMessage("Search complete", formattedResult, new Color(220, 245, 220));

        } catch (NumberFormatException ex) {
            showMessage("Invalid input", "Please enter numbers only, for example 1.", new Color(255, 225, 225));
        } catch (Exception ex) {
            showMessage("Connection error", "Could not connect to the API. Please check the Spring Boot server is running.", new Color(255, 225, 225));
        }
    }

    private String callApi(String patientId) throws IOException, InterruptedException {
        String url = "http://localhost:8080/api/f1/" + patientId;

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    private String formatResult(String patientId, String response) {
        if (response == null || response.equals("[]") || response.isBlank()) {
            return "No rooms found for patient " + patientId + ".";
        }

        Matcher matcher = Pattern.compile("\\d+").matcher(response);
        StringBuilder rooms = new StringBuilder();

        while (matcher.find()) {
            if (!rooms.isEmpty()) {
                rooms.append(", ");
            }
            rooms.append(matcher.group());
        }

        return "Rooms used by patient " + patientId + ": " + rooms;
    }

    private void showMessage(String status, String result, Color background) {
        statusLabel.setText(status);
        statusLabel.setBackground(background);
        resultArea.setText(result);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PatientRoomLookupApp app = new PatientRoomLookupApp();
            app.setVisible(true);
        });
    }
}