package com.tugas_akhir.admin_service.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ExportUtils {

    // Simple placeholder using basic String Builder
    public static ByteArrayInputStream simpleCsvExport(List<String[]> data, String[] headers) {
        StringBuilder sb = new StringBuilder();
        // Append Headers
        sb.append(String.join(",", headers)).append("\n");

        // Append Data
        for (String[] row : data) {
            sb.append(String.join(",", row)).append("\n");
        }

        return new ByteArrayInputStream(sb.toString().getBytes());
    }
}
