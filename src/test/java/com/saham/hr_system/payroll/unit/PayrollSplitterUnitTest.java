package com.saham.hr_system.payroll.unit;

import com.saham.hr_system.modules.payroll.service.PayrollFileService;
import com.saham.hr_system.modules.payroll.service.implementation.PayrollFileServiceImpl;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PayrollSplitterUnitTest {

    @Mock
    private Splitter splitter;

    private final PayrollFileServiceImpl payrollFileService = new PayrollFileServiceImpl("");

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }


}
