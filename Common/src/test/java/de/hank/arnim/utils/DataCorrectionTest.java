package de.hank.arnim.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hanke.arnim.TimeSeriesToolSet.Raster;
import de.hanke.arnim.common.dtos.MonthViewDataDto;
import de.hanke.arnim.common.dtos.PreparedDataDto;
import de.hanke.arnim.common.dtos.ValueDtoMitEinheit;
import de.hanke.arnim.common.lang.DisplayedNames;
import de.hanke.arnim.common.ValueDto;
import de.hanke.arnim.common.utils.DataCorrection;
import de.hanke.arnim.common.utils.SeriesActions;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class DataCorrectionTest {

    @Test
    public void test_imgHtmlTag() {
        assertEquals(BigDecimal.ONE, DataCorrection.parseDataFromValueDtoToBigDecimal("<img height=\"15\" src=\"./STIEBEL ELTRON Reglersteuerung_DS_files/ste-symbol_an-97b765.png"));
    }
    @Test
    public void test_negNumber() {
        assertEquals(BigDecimal.valueOf(-24.7), DataCorrection.parseDataFromValueDtoToBigDecimal("-24,7 °C"));
    }

    @Test
    public void test_fixUpSeries() throws IOException, URISyntaxException {
        Instant start = Instant.parse("2019-11-30T23:00:00.00Z");
        Instant end = Instant.parse("2019-12-10T23:00:00.00Z");

        File inputDataAsFile = new File(this.getClass().getResource("FixUpSeries_201910.json").toURI());

        String dataAsString = Files.readAllLines(inputDataAsFile.toPath()).stream().collect(Collectors.joining());
        ObjectMapper mapper = new ObjectMapper();
        MonthViewDataDto dto = mapper.readValue(dataAsString, MonthViewDataDto.class);

        Map<String, List<ValueDto>> values = dto.getValues();

        File reinterpretedDatas = new File(this.getClass().getResource("reinterpretedDatas.json").toURI());
        String reinterpretedDataAsString = Files.readAllLines(reinterpretedDatas.toPath()).stream().collect(Collectors.joining());

        PreparedDataDto preparedDataDto = mapper.readValue(reinterpretedDataAsString, PreparedDataDto.class);

        SeriesActions seriesActions = new SeriesActions();
        Map<String, List<ValueDto>> stringListMapPT15_SEC = seriesActions.preparingDataForFurtherUse(values, start, end, Raster.PT15S);
        Map<String, List<ValueDto>> stringListMapPT1_DAY = seriesActions.preparingDataForFurtherUse(values, start, end, Raster.PT1D);

        HashMap<String, String> displayedNamesToTSName = new HashMap<>();
        DisplayedNames.MAP_ES_INDEX_TO_DISPLAYED_NAME.entrySet().stream().forEach(stringStringEntry -> displayedNamesToTSName.put(stringStringEntry.getValue(), stringStringEntry.getKey()));


        for (HashMap<String, ValueDtoMitEinheit> map : preparedDataDto.values) {
            ValueDtoMitEinheit uhrzeit = map.get("Uhrzeit");
            LocalDate parse = LocalDate.parse(uhrzeit.data, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            for (Map.Entry<String, ValueDtoMitEinheit> origEntry : map.entrySet().stream().filter(stringValueDtoMitEinheitEntry -> !stringValueDtoMitEinheitEntry.getKey().equals("Uhrzeit")).collect(Collectors.toList())) {
                String displayedName = origEntry.getKey();
                String s = displayedNamesToTSName.get(displayedName);
                if (s == null) {
                    throw new IllegalArgumentException(displayedName);
                }
                for (ValueDto valueDto : stringListMapPT1_DAY.get(s)) {
                    if (valueDto.getDate() == parse.atStartOfDay(ZoneOffset.systemDefault()).toInstant().toEpochMilli()) {
                        if (Double.parseDouble(valueDto.getValue()) != Double.parseDouble(origEntry.getValue().data)) {
                            System.out.println("Kacke " + valueDto.getValue() + " vs " + origEntry.getValue().data + " um " + uhrzeit.data + " bei " + displayedName);
                            //throw new IllegalArgumentException("aölkdsjfölsakdf");
                        }
                    }
                }
            }
        }

// preparedDataDto;

//displayedNamesToTSName;


        // dataCorrection.fixUpSeries()

    }

    void fixUpSeriesDivVortag() {
        Instant start = Instant.parse("2018-09-30T22:00:00.00Z");
        Instant end = Instant.parse("2018-11-30T23:00:00.00Z");

        DataCorrection dataCorrection = new DataCorrection();

        LinkedList<ValueDto> data = new LinkedList<>();

        List<ValueDto> valueDtos = dataCorrection.fixUpSeriesDivVortag(data, start, end, "", 4 * 15);


    }
}