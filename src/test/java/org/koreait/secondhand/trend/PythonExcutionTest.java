package org.koreait.secondhand.trend;

import org.junit.jupiter.api.Test;

public class PythonExcutionTest {
    @Test
    void test1() throws Exception {
        ProcessBuilder builder = new ProcessBuilder("C:/trend/venv/Scripts/activate.bat");
        Process process=builder.start();
        int statusCode=process.waitFor();
        if (statusCode!=0) return;
        builder= new ProcessBuilder("C:/trend/venv/Scripts/python.exe","C:/trend/trend.py","C:/uploads/trend");
        process=builder.start();
        statusCode=process.waitFor();
        System.out.println();

    }
}
