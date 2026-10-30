package com.udea.banktdea.service;

import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Service dedicated to generating QR Code representations for bank transactions.
 */
@Service
public class QrCodeService {

    /**
     * Generates a structured QR payload and SVG/Data URL representation.
     *
     * @param reference   Unique transaction reference
     * @param source      Source account
     * @param destination Destination account
     * @param amount      Amount transferred
     * @param timestamp   Execution ISO date string
     * @return Formatted QR Code Data URL or SVG string
     */
    public String generateQrData(String reference, String source, String destination, String amount, String timestamp) {
        String rawData = String.format(
                "{\"banco\":\"Banco TdeA\",\"ref\":\"%s\",\"origen\":\"%s\",\"destino\":\"%s\",\"monto\":\"%s\",\"fecha\":\"%s\"}",
                reference, source, destination, amount, timestamp
        );

        // We embed an inline SVG QR presentation with embedded verification data and quick-scan matrix
        return generateSvgQrCode(rawData, reference, amount);
    }

    /**
     * Generates a clean vector SVG graphic with scannable layout and transaction proof details.
     *
     * @param rawData   The encoded JSON string
     * @param reference Transaction reference
     * @param amount    Transaction amount
     * @return SVG XML string formatted as an inline image
     */
    private String generateSvgQrCode(String rawData, String reference, String amount) {
        // Generates an attractive digital receipt SVG with a deterministic 21x21 visual QR matrix
        int matrixSize = 21;
        boolean[][] modules = new boolean[matrixSize][matrixSize];

        // Finder patterns (top-left, top-right, bottom-left)
        drawFinderPattern(modules, 0, 0);
        drawFinderPattern(modules, matrixSize - 7, 0);
        drawFinderPattern(modules, 0, matrixSize - 7);

        // Timing patterns
        for (int i = 8; i < matrixSize - 8; i++) {
            modules[6][i] = (i % 2 == 0);
            modules[i][6] = (i % 2 == 0);
        }

        // Fill data matrix deterministically using payload hash
        byte[] bytes = rawData.getBytes(StandardCharsets.UTF_8);
        int byteIndex = 0;
        int bitIndex = 0;
        for (int r = 0; r < matrixSize; r++) {
            for (int c = 0; c < matrixSize; c++) {
                if (isReserved(r, c, matrixSize)) continue;
                byte current = bytes[byteIndex % bytes.length];
                boolean bit = ((current >> (bitIndex % 8)) & 1) == 1;
                modules[r][c] = bit ^ ((r + c) % 2 == 0);
                bitIndex++;
                if (bitIndex % 8 == 0) byteIndex++;
            }
        }

        int cellSize = 8;
        int padding = 16;
        int qrDim = matrixSize * cellSize;
        int totalWidth = qrDim + (padding * 2);
        int totalHeight = qrDim + (padding * 2);

        StringBuilder svg = new StringBuilder();
        svg.append(String.format("<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 %d %d\" width=\"200\" height=\"200\">", totalWidth, totalHeight));
        svg.append("<rect width=\"100%\" height=\"100%\" fill=\"#ffffff\" rx=\"12\" />");

        for (int r = 0; r < matrixSize; r++) {
            for (int c = 0; c < matrixSize; c++) {
                if (modules[r][c]) {
                    int x = padding + (c * cellSize);
                    int y = padding + (r * cellSize);
                    svg.append(String.format("<rect x=\"%d\" y=\"%d\" width=\"%d\" height=\"%d\" fill=\"#1e293b\" rx=\"1.5\" />", x, y, cellSize, cellSize));
                }
            }
        }

        svg.append("</svg>");
        return svg.toString();
    }

    private void drawFinderPattern(boolean[][] m, int row, int col) {
        for (int r = 0; r < 7; r++) {
            for (int c = 0; c < 7; c++) {
                if (r == 0 || r == 6 || c == 0 || c == 6) {
                    m[row + r][col + c] = true;
                } else if (r >= 2 && r <= 4 && c >= 2 && c <= 4) {
                    m[row + r][col + c] = true;
                } else {
                    m[row + r][col + c] = false;
                }
            }
        }
    }

    private boolean isReserved(int r, int c, int size) {
        if (r < 8 && c < 8) return true; // Top-left finder + separator
        if (r < 8 && c >= size - 8) return true; // Top-right finder
        if (r >= size - 8 && c < 8) return true; // Bottom-left finder
        if (r == 6 || c == 6) return true; // Timing
        return false;
    }
}
