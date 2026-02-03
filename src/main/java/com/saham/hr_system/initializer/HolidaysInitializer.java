package com.saham.hr_system.initializer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saham.hr_system.modules.holidays.model.Holiday;
import com.saham.hr_system.modules.holidays.model.HolidayType;
import com.saham.hr_system.modules.holidays.repository.HolidayRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class HolidaysInitializer implements CommandLineRunner {
    private final ObjectMapper objectMapper;
    private final HolidayRepository holidayRepository;

    // Create list of Holiday Objects:
    private static final List<HolidayObject> holidays = List.of(

            new HolidayObject(
                    LocalDate.of(2026, 1, 1),
                    LocalDate.of(2026, 1, 2),
                    "Nouvel An",
                    "PUBLIC",
                    false,
                    1,
                    null
            ),

            new HolidayObject(
                    LocalDate.of(2026, 1, 11),
                    LocalDate.of(2026, 1, 12),
                    "Jour du Manifeste de l'Indépendance",
                    "PUBLIC",
                    false,
                    1,
                    null
            ),

            new HolidayObject(
                    LocalDate.of(2026, 1, 14),
                    LocalDate.of(2026, 1, 15),
                    "Nouvel An Amazigh",
                    "PUBLIC",
                    false,
                    1,
                    null
            ),

            new HolidayObject(
                    LocalDate.of(2026, 3, 20),
                    LocalDate.of(2026, 3, 22),
                    "Aïd el-Fitr (prévisionnel)",
                    "RELIGIOUS",
                    true,
                    2,
                    null
            ),

            new HolidayObject(
                    LocalDate.of(2026, 5, 1),
                    LocalDate.of(2026, 5, 2),
                    "Fête du Travail",
                    "PUBLIC",
                    false,
                    1,
                    null
            ),

            new HolidayObject(
                    LocalDate.of(2026, 5, 27),
                    LocalDate.of(2026, 5, 29),
                    "Aïd al-Adha (prévisionnel)",
                    "RELIGIOUS",
                    true,
                    2,
                    null
            ),

            new HolidayObject(
                    LocalDate.of(2026, 6, 17),
                    LocalDate.of(2026, 6, 18),
                    "Nouvel An islamique (Hijra) (prévisionnel)",
                    "RELIGIOUS",
                    true,
                    1,
                    null
            ),

            new HolidayObject(
                    LocalDate.of(2026, 7, 30),
                    LocalDate.of(2026, 7, 31),
                    "Fête du Trône",
                    "PUBLIC",
                    false,
                    1,
                    null
            ),

            new HolidayObject(
                    LocalDate.of(2026, 8, 14),
                    LocalDate.of(2026, 8, 15),
                    "Jour de Oued Ed-Dahab",
                    "PUBLIC",
                    false,
                    1,
                    null
            ),

            new HolidayObject(
                    LocalDate.of(2026, 8, 20),
                    LocalDate.of(2026, 8, 21),
                    "Anniversaire de la Révolution du Roi et du Peuple",
                    "PUBLIC",
                    false,
                    1,
                    null
            ),

            new HolidayObject(
                    LocalDate.of(2026, 8, 21),
                    LocalDate.of(2026, 8, 22),
                    "Fête de la Jeunesse",
                    "PUBLIC",
                    false,
                    1,
                    null
            ),

            new HolidayObject(
                    LocalDate.of(2026, 8, 25),
                    LocalDate.of(2026, 8, 26),
                    "Anniversaire du Prophète Mohammed (prévisionnel)",
                    "RELIGIOUS",
                    true,
                    1,
                    null
            ),

            new HolidayObject(
                    LocalDate.of(2026, 10, 31),
                    LocalDate.of(2026, 11, 1),
                    "Jour de l’Unité",
                    "PUBLIC",
                    false,
                    1,
                    null
            ),

            new HolidayObject(
                    LocalDate.of(2026, 11, 6),
                    LocalDate.of(2026, 11, 7),
                    "Marche Verte",
                    "PUBLIC",
                    false,
                    1,
                    null
            ),

            new HolidayObject(
                    LocalDate.of(2026, 11, 18),
                    LocalDate.of(2026, 11, 19),
                    "Fête de l'Indépendance",
                    "PUBLIC",
                    false,
                    1,
                    null
            )
    );


    @Override
    public void run(String... args) throws Exception {
        try{
            /*
            List<HolidayObject> holidays = objectMapper
                    .readValue(new File("src/main/resources/static/moroccan_holidays.json"), new TypeReference<List<HolidayObject>>() {});

             */
            if(holidayRepository.count() == 0) {
                holidays
                        .forEach(h -> {
                            Holiday holiday = new Holiday();
                            holiday.setName(h.getName()); // set the name
                            holiday.setStartDate(h.getStartDate()); // set the start date
                            holiday.setEndDate(h.getEndDate()); // set the end date
                            holiday.setFloating(h.isFloating()); // set if it's floating
                            holiday.setType(HolidayType.valueOf(h.getType())); // set the type
                            holiday.setLastUpdate(LocalDateTime.now()); // set the last update
                            holiday.setLeaveDays(h.getLeaveDays()); // set the total leave days                        holiday.setStatus(HolidayStatus.valueOf(h.getStatus()));

                            // save:
                            holidayRepository.save(holiday);
                        });
            }else{
                log.info("Holidays already exist");
            }
        }catch (Exception exception){
            throw new Exception();
        }
    }
}

@Data
@AllArgsConstructor
class HolidayObject{
    private LocalDate startDate;
    private LocalDate endDate;
    private String name;
    private String type;
    private boolean isFloating;
    private int leaveDays;
    private String status;

}
