package de.hank.arnim.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hank.arnim.ValueDto;
import de.hank.arnim.dtos.GriddleTableValueDto;
import de.hank.arnim.dtos.MonthViewDataDto;
import de.hank.arnim.dtos.PreparedDataDto;
import org.junit.Test;
import org.junit.runner.JUnitCore;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class DataCorrectionTest {

    @Test
    public void test_fixUpSeries() throws IOException, URISyntaxException {
        Instant start = Instant.parse("2019-10-30T22:00:00.00Z");
        Instant end = Instant.parse("2020-01-30T23:00:00.00Z");

        DataCorrection dataCorrection = new DataCorrection();

        LinkedList<ValueDto> data = new LinkedList<>();

        File inputDataAsFile = new File(this.getClass().getResource("FixUpSeries_201910.json").toURI());

        String dataAsString = Files.readAllLines(inputDataAsFile.toPath()).stream().collect(Collectors.joining());
        ObjectMapper mapper = new ObjectMapper();
        MonthViewDataDto dto = mapper.readValue(dataAsString, MonthViewDataDto.class);

        Map<String, List<ValueDto>> correctedValues = new LinkedHashMap<>();
        Map<String, List<ValueDto>> values = dto.getValues();
        Map<String, List<ValueDto>> stringListMap = dataCorrection.fixUpSeries(values, start, end, 4 * 15);

        System.out.println(stringListMap);

        File reinterpretedDatas = new File(this.getClass().getResource("reinterpretedDatas.json").toURI());
        String reinterpretedDataAsString = Files.readAllLines(reinterpretedDatas.toPath()).stream().collect(Collectors.joining());

        PreparedDataDto preparedDataDto = mapper.readValue(reinterpretedDataAsString, PreparedDataDto.class);


    }

    void fixUpSeriesDivVortag() {
        Instant start = Instant.parse("2018-09-30T22:00:00.00Z");
        Instant end = Instant.parse("2018-11-30T23:00:00.00Z");

        DataCorrection dataCorrection = new DataCorrection();

        LinkedList<ValueDto> data = new LinkedList<>();

        List<ValueDto> valueDtos = dataCorrection.fixUpSeriesDivVortag(data, start, end, "", 4 * 15);


    }
}