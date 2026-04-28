package com.example.app.gui;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PatientRoomLookupApp extends JFrame {

    private final JTextField patientIdField;
    private final JTextArea resultArea;

    public PatientRoomLookupApp() {
        setTitle("Patient Room Lookup");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Find rooms used by a patient");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Patient ID:"));

        patientIdField = new JTextField(10);
        inputPanel.add(patientIdField);

        JButton searchButton = new JButton("Find Rooms");
        inputPanel.add(searchButton);

        resultArea = new JTextArea();
        resultArea.setEditable(false);

        add(titleLabel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(new JScrollPane(resultArea), BorderLayout.SOUTH);

        searchButton.addActionListener(e -> findRooms());
    }

    private void findRooms() {
        String patientId = patientIdField.getText();

        if (patientId.isBlank()) {
            resultArea.setText("Please enter a patient ID.");
            return;
        }

        try {
            Integer.parseInt(patientId);

            String response = callApi(patientId);
            resultArea.setText(response);

        } catch (NumberFormatException ex) {
            resultArea.setText("Invalid patient ID.");
        } catch (Exception ex) {
            resultArea.setText("Could not connect to the API.");
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PatientRoomLookupApp app = new PatientRoomLookupApp();
            app.setVisible(true);
        });
    }
}
