package de.hank.arnim.utils;

import de.hank.arnim.ValueDto;
import org.joda.time.Interval;
import sun.awt.SunHints;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static de.hank.arnim.utils.Raster.*;

public class SeriesActions {

    private DataCorrection dataCorrection = new DataCorrection();

    /**
     * Preparing the data for the user
     *
     * @param data Data from the server
     * @param start start-timestamp
     * @param end end-timestamp
     * @param interval Interval for the series
     */
    public void preparingDataForFurtherUse(Map<String, List<ValueDto>> data, Instant start, Instant end, Raster interval) {
        replaceCommaDecimalWithPoint(data);

        int intervalAsNumber = 0;
        switch (interval) {
            case PT15_SEC:
                intervalAsNumber = 15 * 1000;
                break;
            case PT1_DAY:
                intervalAsNumber = 24 * 60 * 60 * 1000;
                break;
        }

        Map<String, List<ValueDto>> fixedValues = dataCorrection.fixUpSeries(data, start, end, intervalAsNumber);
        const filledSerie = fillSeries(fixedValues, start, end.add(intervalAsNumber, 'ms'));
        const aggregatedSerie = aggregateSeries(filledSerie, start, end, intervalAsNumber);
        return aggregatedSerie;
    }

    public void replaceCommaDecimalWithPoint(data: {[key: string]: ValueDto[]}) {
        for (const key in data) {
        const vals: ValueDto[] = data[key];
            for (let i = 0; i < vals.length; i++) {
                vals[i].value = vals[i].value.replace(',', '.');
            }
        }
    }

    /**
     * Füllt die Zeitreihe auf und aggregiert diese auf den entsprechenden Zeitraum
     *
     * @param data Not-Filled Data
     * @param start Start-TimeStamp
     * @param end End-TimeStamp
     */
    public Map<String, List<ValueDto>> fillSeries(data: {[key: string]: ValueDto[]}, start: moment.Moment, end: moment.Moment): {[key: string]: ValueDto[]} {
    const ret: {[key: string]: ValueDto[]} = {};
        for (const key in data) {
            if (data[key] !== undefined) {
            const retValues: ValueDto[] = new Array<ValueDto>();
            const values: ValueDto[] = data[key];
                let date = moment(start);
                for (let i = 0; i < values.length; i++) {
                const actualValueDto = values[i];
                    // Der aktuelle Wert liegt nicht vor dem Startzeitpunkt
                    if (!moment(actualValueDto.date).isBefore(start)) {
                        if (i === values.length - 1) {
                            while (moment(end).isSameOrAfter(date)) {
                                retValues.push(new ValueDto(actualValueDto.value.replace(',', '.'), date.toDate().getTime()));
                                date = date.add(15 * 1000, 'ms');
                            }
                        } else {
                        const nextValueDto = values[i + 1];
                            while (moment(nextValueDto.date).isAfter(date)) {
                                retValues.push(new ValueDto(actualValueDto.value.replace(',', '.'), date.toDate().getTime()));
                                date = date.add(15 * 1000, 'ms');
                            }
                        }
                        // Der aktuelle Wert liegt vor dem Startzeitpunkt
                    } else {
                        if (i === values.length - 1) {
                            while (moment(date).isSameOrBefore(end)) {
                                retValues.push(new ValueDto(actualValueDto.value.replace(',', '.'), date.toDate().getTime()));
                                date = date.add(15 * 1000, 'ms');
                            }
                        } else {
                        const nextValueDto = values[i + 1];
                            while (moment(date).isBefore(nextValueDto.date)) {
                                retValues.push(new ValueDto(actualValueDto.value.replace(',', '.'), date.toDate().getTime()));
                                date = date.add(15 * 1000, 'ms');
                            }
                        }

                    }
                }
                ret[key] = retValues;
            }
        }
        return ret;
    }

    /**
     * Aggregates the Series for the given interval
     *
     * @param data Not-aggregated Data
     * @param start start-timestamp for the aggregation
     * @param end end-timestamp for the aggregation
     * @param interval interval for the aggregation
     */
    public Map<String, List<ValueDto>> aggregateSeries(data: {[key: string]: ValueDto[]}, start: moment.Moment, end: moment.Moment, interval: number): {[key: string]: ValueDto[]} {
        if (interval === 15 * 1000) {
            return data;
        }
    const ret: {[key: string]: ValueDto[]} = {};

        for (const key in data) {
            if (data[key] !== undefined) {
            const values = data[key];
            const retValues: ValueDto[] = aggregateSeiresForGivenAggregationType(config[key], values, start, end, interval, key);
                ret[key] = retValues;
            }
        }

        return ret;
    }

    List<ValueDto> aggregateSeiresForGivenAggregationType(aggregationType: AggregationTypes, data: ValueDto[], start: moment.Moment, end: moment.Moment, interval: number, id: string)
                                                : ValueDto[] {
        if (data === undefined || data.length === 0) {
            return [];
        }

        switch (aggregationType) {
            case AggregationTypes.COUNT:
                return aggregateSeriesCount(data, start, end, interval, id);
            case AggregationTypes.AVERAGE:
                return aggregateSeriesAverage(data, start, end, interval, id);
            case AggregationTypes.MAX:
                return aggregateSeriesMax(data, start, end, interval, id);
            case AggregationTypes.MAX_BEFOR_MINOR:
                return aggregateSeriesMaxBeforeMinor(data, start, end, interval, id);
            case AggregationTypes.DIV_VORTAG:
                return aggregateSeriesDivVortag(data, start, end, interval, id);

        }
    }

    /**
     * Für das gegebene Intervall werden die Veränderung von 'LEER' zu 'Enthält etwas' gezählt
     *
     * @param data
     * @param start
     * @param end
     * @param interval
     * @param id
     */
    public List<ValueDto> aggregateSeriesCount(data: ValueDto[], start: moment.Moment, end: moment.Moment, interval: number, id: string) {
        console.log('aggregateSeriesCount ' + id);

    const ret: ValueDto[] = [];
    const startAsMilliseconds = start.toDate().getTime();
    const date: moment.Moment = moment(start);
        let foundIntervals = 0;
        let countForInterval = 0;
        let i = 0;
        let lastFoundStatus = data[i].value;
        while (date.isSameOrBefore(end)) {
            if (startAsMilliseconds + (foundIntervals + 1) * interval === date.toDate().getTime()) {
                ret.push(new ValueDto('' + countForInterval, moment(start.toDate().getTime() + (foundIntervals) * interval).toDate().getTime()));
                foundIntervals++;
                countForInterval = 0;
                if (data[i].value !== '') {
                    countForInterval++;
                    lastFoundStatus = data[i].value;
                }
                i++;
                date.add(15, 's');
            } else {
                if (data[i].value !== '' && lastFoundStatus !== data[i].value) {
                    countForInterval++;
                    lastFoundStatus = data[i].value;
                } else if (data[i].value === '') {
                    lastFoundStatus = '';
                }
                i++;
                date.add(15, 's');
            }
        }

        return ret;
    }

    /**
     * Für das gegebene Intervall wird der Durchschnitt gebildet
     *
     * @param data
     * @param start
     * @param end
     * @param interval
     * @param id
     */
    function aggregateSeriesAverage(data: ValueDto[], start: moment.Moment, end: moment.Moment, interval: number, id: string): ValueDto[] {
        console.log('aggregateSeriesAverage ' + id);

    const ret: ValueDto[] = [];
    const startAsMilliseconds = start.toDate().getTime();
    const date: moment.Moment = moment(start);
        let sumForInterval = 0;
        let i = 0;
        let foundIntervals = 0;
        while (date.isSameOrBefore(end)) {
            if (startAsMilliseconds + (foundIntervals + 1) * interval === date.toDate().getTime()) {
            const value = sumForInterval / (interval / 15);
                ret.push(new ValueDto('' + value, moment(start.toDate().getTime() + (foundIntervals) * interval).toDate().getTime()));
                foundIntervals++;
                sumForInterval = 0;
                let parsedValue = Number.parseFloat(data[i].value);
                if (isNaN(parsedValue)) {
                    parsedValue = 0;
                }
                sumForInterval += parsedValue;
                i++;
                date.add(15, 's');
            } else {
                let parsedValue = Number.parseFloat(data[i].value);
                if (isNaN(parsedValue)) {
                    parsedValue = 0;
                }
                sumForInterval += parsedValue;
                i++;
                date.add(15, 's');
            }
        }

        return ret;
    }

    /**
     * Ermittelt das Maximum für eine Zeitreihe
     *
     * @param data
     * @param start
     * @param end
     * @param interval
     * @param id
     */
    function aggregateSeriesMax(data: ValueDto[], start: moment.Moment, end: moment.Moment, interval: number, id: string): ValueDto[] {
        console.log('aggregateSeriesMax ' + id);

    const ret: ValueDto[] = [];
    const startAsMilliseconds = start.toDate().getTime();
    const date: moment.Moment = moment(start);
        let foundIntervals = 0;
        let lastmax = Number.parseFloat(data[0].value);
        if (isNaN(lastmax)) {
            lastmax = 0;
        }
        let i = 0;
        while (date.isSameOrBefore(end)) {
            if (i < data.length) {
                if (startAsMilliseconds + (foundIntervals + 1) * interval === date.toDate().getTime()) {
                    ret.push(new ValueDto('' + lastmax, moment(start.toDate().getTime() + (foundIntervals) * interval).toDate().getTime()));
                    foundIntervals++;
                    lastmax = Number.parseFloat(data[i].value);
                    if (isNaN(lastmax)) {
                        lastmax = 0;
                    }
                    i++;
                    date.add(15, 's');
                } else {
                    let parsedValue = Number.parseFloat(data[i].value);
                    if (isNaN(parsedValue)) {
                        parsedValue = 0;
                    }
                    if (lastmax < parsedValue) {
                        lastmax = parsedValue;
                    }
                    i++;
                    date.add(15, 's');
                }
            } else {
                break;
            }
        }

        return ret;
    }

    /**
     * Aggregiert die Zeitreihe; Taktik ist dass solange ein größerer Wert gesucht wird, bis der Wert signifikant abfällt
     *
     * @param data
     * @param start
     * @param end
     * @param interval
     * @param id
     */
    function aggregateSeriesMaxBeforeMinor(data: ValueDto[], start: moment.Moment, end: moment.Moment, interval: number, id: string): ValueDto[] {
        console.log('aggregateSeriesMaxBeforeMinor ' + id);

    const ret: ValueDto[] = [];
        let lastMax = 0;
        let lastCommitedValue = 0;
        let aktInterval = 0;

        for (let i = 0; i < data.length; i++) {
        const parsedValue =  Number.parseFloat(data[i].value);
            if (moment(data[i].date).isSameOrAfter(end)) { break; }
            if (aktInterval === interval) {
                // Ende des Intervalls erreicht => Übernahme des Wertes und Intervallsuche von neuem starten
                ret.push(new ValueDto('' + lastMax, data[i].date - interval));
                lastCommitedValue = lastMax;
                aktInterval = 0;
                lastMax = 0;
            } else {
                if (lastMax < parsedValue && lastCommitedValue != parsedValue) {
                    lastMax = parsedValue;
                }
            }
            aktInterval += 15 * 1000;
        }
        return ret;
    }

    /**
     * Aggregiert die Zeitreihe; Taktik ist, dass über die Werte iteriert wird und bei jedem vollständigen Intervall der Wert genommen wird.
     *
     * @param data
     * @param start
     * @param end
     * @param interval
     * @param id
     */
    function aggregateSeriesDivVortag(data: ValueDto[], start: moment.Moment, end: moment.Moment, interval: number, id: string): ValueDto[] {
        console.log('aggregateSeriesDivVortag ' + id);
    const ret: ValueDto[] = [];

        let aktInterval = 0;

        if (data.length > 0) {
            ret.push(new ValueDto( '' + data[0].value, data[0].date));
        }
        for (let i = 0; i < data.length; i++) {
            if (moment(data[i].date).isSameOrAfter(end.add(interval, 'ms'))) { break; }
            if (aktInterval === interval) {
                // Ende des Intervalls erreicht => Übernahme des Wertes und Intervallsuche von neuem starten
                ret.push(new ValueDto( '' + data[i].value, data[i].date));
                aktInterval = 0;
            }
            aktInterval += 15 * 1000;
        }
        return ret;
    }


}
