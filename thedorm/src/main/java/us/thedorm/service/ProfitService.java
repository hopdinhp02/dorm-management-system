package us.thedorm.service;

import jakarta.persistence.ManyToMany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.thedorm.models.*;
import us.thedorm.repositories.FacilityDetailRepository;
import us.thedorm.repositories.MaintenanceRepository;
import us.thedorm.repositories.ResidentHistoryRepository;
import us.thedorm.repositories.SlotRepository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ProfitService {
    @Autowired
    private SlotRepository slotRepository;
    @Autowired
    private ResidentHistoryRepository residentHistoryRepository;

    @Autowired
    private FacilityDetailRepository facilityDetailRepository;
    @Autowired
    private MaintenanceRepository maintenanceRepository;

    ////Slot's Profit
    ///
    //
    public double getSlotRevenueByDay(Slot slot, Date date) {
        if(!date.after(new Date())) {
            ResidentHistory residentHistory = residentHistoryRepository.findBySlotIdWithDateBetweenStartDateAndEndDate(slot.getId(), date);
            if (residentHistory != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                return (double) slot.getRoom().getBasePrice().getSlotPrice() / daysInMonth;
            }
        }
        return 0;
    }

    public double getSlotDepreciationByDay(Slot slot, Date date) {
        double depreciation = 0;
        if(!date.after(new Date())){
            List<FacilityDetail> facilityDetails = facilityDetailRepository.findFacilityBySlotIdWithDateBetweenStartDateAndEndDate(slot.getId(), date);
            for (FacilityDetail facilityDetail : facilityDetails) {
                if (!facilityDetail.getExpirationDate().before(date) && !facilityDetail.getProducingDate().after(date)) {
                    long dateBeforeInMs = facilityDetail.getProducingDate().getTime();
                    long dateAfterInMs = facilityDetail.getExpirationDate().getTime();
                    long timeDiff = Math.abs(dateAfterInMs - dateBeforeInMs);
                    long daysDiff = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
                    depreciation += (double) facilityDetail.getPrice() / daysDiff;
                }
            }
        }
        return depreciation;
    }

    public double getSlotMaintenanceFeeByDay(Slot slot, Date date) throws ParseException {
        double maintenaceFeeByDay = 0;
        if(!date.after(new Date())) {
            DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            List<Maintenance> maintenances = new ArrayList<>();
            List<FacilityDetail> facilityDetails = facilityDetailRepository.findFacilityBySlotIdWithDateBetweenStartDateAndEndDate(slot.getId(), date);
            for (FacilityDetail facilityDetail : facilityDetails) {

                maintenances.addAll(maintenanceRepository.findByFacilityDetailAndFixDate(facilityDetail.getId(), formatter.parse(formatter.format(date))));
            }
            for (Maintenance maintenance : maintenances) {
                maintenaceFeeByDay += maintenance.getPrice();
            }
        }
        return maintenaceFeeByDay;
    }

    public double getSlotMoneyByMonth(String type, Slot slot, int month, int year) throws ParseException {
        double money = 0;
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        LocalDate firstDayOfNextMonth = firstDayOfMonth.plusMonths(1);

        switch (type) {
            case "revenue":
                for (LocalDate localDate = firstDayOfMonth; localDate.isBefore(firstDayOfNextMonth); localDate = localDate.plusDays(1)) {
                    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    money += getSlotRevenueByDay(slot, date);
                }
                break;
            case "depreciation":
                for (LocalDate localDate = firstDayOfMonth; localDate.isBefore(firstDayOfNextMonth); localDate = localDate.plusDays(1)) {
                    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    money += getSlotDepreciationByDay(slot, date);
                }
                break;
                case "maintenance":
                for (LocalDate localDate = firstDayOfMonth; localDate.isBefore(firstDayOfNextMonth); localDate = localDate.plusDays(1)) {
                    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    money += getSlotMaintenanceFeeByDay(slot, date);
                }
                break;
        }
        return money;
    }

    public double getSlotMoneyByYear(String type, Slot slot, int year) throws ParseException {
        double money = 0;
        LocalDate firstDayOfYear = LocalDate.of(year, 1, 1);
        LocalDate firstDayOfNextYear = firstDayOfYear.plusYears(1);
        for (LocalDate localDate = firstDayOfYear; localDate.isBefore(firstDayOfNextYear); localDate = localDate.plusMonths(1)) {
            money += getSlotMoneyByMonth(type, slot, localDate.getMonthValue(), year);
        }
        return money;
    }

    public double[] getSlotMoneyByDaysInMonth(String type, Slot slot, int month, int year) throws ParseException {
        int endDay = 0;
        LocalDate curDate = LocalDate.now();
        int daysInMonth = LocalDate.of(year, month, 1).lengthOfMonth();
        if (year > curDate.getYear()) {
            endDay = 0;
        } else if (curDate.getYear() == year && month > curDate.getMonthValue()) {
            endDay = 0;
        } else if (curDate.getYear() == year && curDate.getMonthValue() == month) {
            endDay = curDate.getDayOfMonth();
        } else if (curDate.getYear() == year && month < curDate.getMonthValue()) {
            endDay = daysInMonth;
        } else {
            endDay = daysInMonth;
        }
        double[] money = new double[endDay];
        int i = 0;
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        LocalDate endDayOfLoop = firstDayOfMonth.plusDays(endDay);
        switch (type) {
            case "revenue":
                for (LocalDate localDate = firstDayOfMonth; localDate.isBefore(endDayOfLoop); localDate = localDate.plusDays(1)) {
                    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    money[i++] = getSlotRevenueByDay(slot, date);
                }
                break;
            case "depreciation":
                for (LocalDate localDate = firstDayOfMonth; localDate.isBefore(endDayOfLoop); localDate = localDate.plusDays(1)) {
                    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    money[i++] = getSlotDepreciationByDay(slot, date);
                }
                break;
            case "maintenance":
                for (LocalDate localDate = firstDayOfMonth; localDate.isBefore(endDayOfLoop); localDate = localDate.plusDays(1)) {
                    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    money[i++] = getSlotMaintenanceFeeByDay(slot, date);
                }
                break;
            default:
                money = new double[0];
        }
        return money;
    }

    public double[] getSlotMoneyByMonthsInYear(String type, Slot slot, int year) throws ParseException {
        int endMonth = 0;
        LocalDate curDate = LocalDate.now();
        int month = curDate.getMonthValue();
        if (year > curDate.getYear()) {
            endMonth = 0;
        } else if (curDate.getYear() == year) {
            endMonth = curDate.getMonthValue();
        } else {
            endMonth = 12;
        }
        double[] money = new double[endMonth];
        int i = 0;
        LocalDate firstDayOfYear = LocalDate.of(year, 1, 1);
        LocalDate endDayOfLoop = firstDayOfYear.plusMonths(endMonth);
        for (LocalDate localDate = firstDayOfYear; localDate.isBefore(endDayOfLoop); localDate = localDate.plusMonths(1)) {
            money[i++] = getSlotMoneyByMonth(type, slot, localDate.getMonthValue(), year);
        }
        return money;
    }


    ////Room's Profit
    ///
    //
    public double getRoomRevenueByDay(Room room, Date date) {
        double revenue = 0;
        for (Slot slot : room.getSlots()) {
            revenue += getSlotRevenueByDay(slot, date);
        }
        return revenue;
    }

    public double getRoomDepreciationByDay(Room room, Date date) {
        double depreciation = 0;
        for (Slot slot : room.getSlots()) {
            depreciation += getSlotDepreciationByDay(slot, date);
        }
        List<FacilityDetail> facilityDetails = facilityDetailRepository.findFacilityByRoomIdWithDateBetweenStartDateAndEndDate(room.getId(), date);
        for (FacilityDetail facilityDetail : facilityDetails) {
            if (!facilityDetail.getExpirationDate().before(date) && !facilityDetail.getProducingDate().after(date)) {
                long dateBeforeInMs = facilityDetail.getProducingDate().getTime();
                long dateAfterInMs = facilityDetail.getExpirationDate().getTime();
                long timeDiff = Math.abs(dateAfterInMs - dateBeforeInMs);
                long daysDiff = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
                depreciation += (double) facilityDetail.getPrice() / daysDiff;
            }
        }
        return depreciation;
    }

    public double getRoomMaintenanceFeeByDay(Room room, Date date) throws ParseException {
        double maintenaceFeeByDay = 0;
        for (Slot slot : room.getSlots()) {
            maintenaceFeeByDay += getSlotMaintenanceFeeByDay(slot, date);
        }
        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        List<Maintenance> maintenances = new ArrayList<>();
        List<FacilityDetail> facilityDetails = facilityDetailRepository.findFacilityByRoomIdWithDateBetweenStartDateAndEndDate(room.getId(), date);
        for (FacilityDetail facilityDetail : facilityDetails) {

            maintenances.addAll(maintenanceRepository.findByFacilityDetailAndFixDate(facilityDetail.getId(), formatter.parse(formatter.format(date))));
        }
        for (Maintenance maintenance : maintenances) {
            maintenaceFeeByDay += maintenance.getPrice();
        }
        return maintenaceFeeByDay;
    }

    public double getRoomMoneyByMonth(String type, Room room, int month, int year) throws ParseException {
        double money = 0;
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        LocalDate firstDayOfNextMonth = firstDayOfMonth.plusMonths(1);

        switch (type) {
            case "revenue":
                for (LocalDate localDate = firstDayOfMonth; localDate.isBefore(firstDayOfNextMonth); localDate = localDate.plusDays(1)) {
                    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    money += getRoomRevenueByDay(room, date);
                }
                break;
            case "depreciation":
                for (LocalDate localDate = firstDayOfMonth; localDate.isBefore(firstDayOfNextMonth); localDate = localDate.plusDays(1)) {
                    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    money += getRoomDepreciationByDay(room, date);
                }
                break;
            case "maintenance":
                for (LocalDate localDate = firstDayOfMonth; localDate.isBefore(firstDayOfNextMonth); localDate = localDate.plusDays(1)) {
                    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    money += getRoomMaintenanceFeeByDay(room, date);
                }
                break;
        }
        return money;
    }

    public double getRoomMoneyByYear(String type, Room room, int year) throws ParseException {
        double money = 0;
        LocalDate firstDayOfYear = LocalDate.of(year, 1, 1);
        LocalDate firstDayOfNextYear = firstDayOfYear.plusYears(1);
        for (LocalDate localDate = firstDayOfYear; localDate.isBefore(firstDayOfNextYear); localDate = localDate.plusMonths(1)) {
            money += getRoomMoneyByMonth(type, room, localDate.getMonthValue(), year);
        }
        return money;
    }

    public double[] getRoomMoneyByDaysInMonth(String type, Room room, int month, int year) throws ParseException {
        int endDay = 0;
        LocalDate curDate = LocalDate.now();
        int daysInMonth = LocalDate.of(year, month, 1).lengthOfMonth();
        if (year > curDate.getYear()) {
            endDay = 0;
        } else if (curDate.getYear() == year && month > curDate.getMonthValue()) {
            endDay = 0;
        } else if (curDate.getYear() == year && curDate.getMonthValue() == month) {
            endDay = curDate.getDayOfMonth();
        } else if (curDate.getYear() == year && month < curDate.getMonthValue()) {
            endDay = daysInMonth;
        } else {
            endDay = daysInMonth;
        }
        double[] money = new double[endDay];
        int i = 0;
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        LocalDate endDayOfLoop = firstDayOfMonth.plusDays(endDay);
        switch (type) {
            case "revenue":
                for (LocalDate localDate = firstDayOfMonth; localDate.isBefore(endDayOfLoop); localDate = localDate.plusDays(1)) {
                    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    money[i++] = getRoomRevenueByDay(room, date);
                }
                break;
            case "depreciation":
                for (LocalDate localDate = firstDayOfMonth; localDate.isBefore(endDayOfLoop); localDate = localDate.plusDays(1)) {
                    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    money[i++] = getRoomDepreciationByDay(room, date);
                }
                break;
            case "maintenance":
                for (LocalDate localDate = firstDayOfMonth; localDate.isBefore(endDayOfLoop); localDate = localDate.plusDays(1)) {
                    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    money[i++] = getRoomMaintenanceFeeByDay(room, date);
                }
                break;
            default:
                money = new double[0];
        }
        return money;
    }

    public double[] getRoomMoneyByMonthsInYear(String type, Room room, int year) throws ParseException {
        int endMonth = 0;
        LocalDate curDate = LocalDate.now();
        int month = curDate.getMonthValue();
        if (year > curDate.getYear()) {
            endMonth = 0;
        } else if (curDate.getYear() == year) {
            endMonth = curDate.getMonthValue();
        } else {
            endMonth = 12;
        }
        double[] money = new double[endMonth];
        int i = 0;
        LocalDate firstDayOfYear = LocalDate.of(year, 1, 1);
        LocalDate endDayOfLoop = firstDayOfYear.plusMonths(endMonth);
        for (LocalDate localDate = firstDayOfYear; localDate.isBefore(endDayOfLoop); localDate = localDate.plusMonths(1)) {
            money[i++] = getRoomMoneyByMonth(type, room, localDate.getMonthValue(), year);
        }
        return money;
    }


    ////Dorm's Profit
    ///
    //

    public double getDormRevenueByDay(Dorm dorm, Date date) {
        double revenue = 0;
        for (Room room : dorm.getRooms()) {
            revenue += getRoomRevenueByDay(room, date);
        }
        return revenue;
    }


    public double getDormDepreciationByDay(Dorm dorm, Date date) {
        double depreciation = 0;
        for (Room room : dorm.getRooms()) {
            depreciation += getRoomDepreciationByDay(room, date);
        }
        List<FacilityDetail> facilityDetails = facilityDetailRepository.findFacilityByDormIdWithDateBetweenStartDateAndEndDate(dorm.getId(), date);
        for (FacilityDetail facilityDetail : facilityDetails) {
            if (!facilityDetail.getExpirationDate().before(date) && !facilityDetail.getProducingDate().after(date)) {
                long dateBeforeInMs = facilityDetail.getProducingDate().getTime();
                long dateAfterInMs = facilityDetail.getExpirationDate().getTime();
                long timeDiff = Math.abs(dateAfterInMs - dateBeforeInMs);
                long daysDiff = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
                depreciation += (double) facilityDetail.getPrice() / daysDiff;
            }
        }
        return depreciation;
    }


    public double getDormMaintenanceFeeByDay(Dorm dorm, Date date) throws ParseException {
        double maintenaceFeeByDay = 0;
        for (Room room : dorm.getRooms()) {
            maintenaceFeeByDay += getRoomMaintenanceFeeByDay(room, date);
        }
        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        List<Maintenance> maintenances = new ArrayList<>();
        List<FacilityDetail> facilityDetails = facilityDetailRepository.findFacilityByDormIdWithDateBetweenStartDateAndEndDate(dorm.getId(), date);
        for (FacilityDetail facilityDetail : facilityDetails) {
            maintenances.addAll(maintenanceRepository.findByFacilityDetailAndFixDate(facilityDetail.getId(), formatter.parse(formatter.format(date))));
        }
        for (Maintenance maintenance : maintenances) {
            maintenaceFeeByDay += maintenance.getPrice();
        }
        return maintenaceFeeByDay;
    }


    public double getDormMoneyByMonth(String type, Dorm dorm, int month, int year) throws ParseException {
        double money = 0;
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        LocalDate firstDayOfNextMonth = firstDayOfMonth.plusMonths(1);

        switch (type) {
            case "revenue":
                for (LocalDate localDate = firstDayOfMonth; localDate.isBefore(firstDayOfNextMonth); localDate = localDate.plusDays(1)) {
                    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    money += getDormRevenueByDay(dorm, date);
                }
                break;
            case "depreciation":
                for (LocalDate localDate = firstDayOfMonth; localDate.isBefore(firstDayOfNextMonth); localDate = localDate.plusDays(1)) {
                    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    money += getDormDepreciationByDay(dorm, date);
                }
                break;
            case "maintenance":
                for (LocalDate localDate = firstDayOfMonth; localDate.isBefore(firstDayOfNextMonth); localDate = localDate.plusDays(1)) {
                    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    money += getDormMaintenanceFeeByDay(dorm, date);
                }
                break;
        }
        return money;
    }

    public double getDormMoneyByYear(String type, Dorm dorm, int year) throws ParseException {
        double money = 0;
        LocalDate firstDayOfYear = LocalDate.of(year, 1, 1);
        LocalDate firstDayOfNextYear = firstDayOfYear.plusYears(1);
        for (LocalDate localDate = firstDayOfYear; localDate.isBefore(firstDayOfNextYear); localDate = localDate.plusMonths(1)) {
            money += getDormMoneyByMonth(type, dorm, localDate.getMonthValue(), year);
        }
        return money;
    }

    public double[] getDormMoneyByDaysInMonth(String type, Dorm dorm, int month, int year) throws ParseException {
        int endDay = 0;
        LocalDate curDate = LocalDate.now();
        int daysInMonth = LocalDate.of(year, month, 1).lengthOfMonth();
        if (year > curDate.getYear()) {
            endDay = 0;
        } else if (curDate.getYear() == year && month > curDate.getMonthValue()) {
            endDay = 0;
        } else if (curDate.getYear() == year && curDate.getMonthValue() == month) {
            endDay = curDate.getDayOfMonth();
        } else if (curDate.getYear() == year && month < curDate.getMonthValue()) {
            endDay = daysInMonth;
        } else {
            endDay = daysInMonth;
        }
        double[] money = new double[endDay];
        int i = 0;
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        LocalDate endDayOfLoop = firstDayOfMonth.plusDays(endDay);
        switch (type) {
            case "revenue":
                for (LocalDate localDate = firstDayOfMonth; localDate.isBefore(endDayOfLoop); localDate = localDate.plusDays(1)) {
                    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    money[i++] = getDormRevenueByDay(dorm, date);
                }
                break;
            case "depreciation":
                for (LocalDate localDate = firstDayOfMonth; localDate.isBefore(endDayOfLoop); localDate = localDate.plusDays(1)) {
                    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    money[i++] = getDormDepreciationByDay(dorm, date);
                }
                break;
            case "maintenance":
                for (LocalDate localDate = firstDayOfMonth; localDate.isBefore(endDayOfLoop); localDate = localDate.plusDays(1)) {
                    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    money[i++] = getDormMaintenanceFeeByDay(dorm, date);
                }
                break;
            default:
                money = new double[0];
        }
        return money;
    }

    public double[] getDormMoneyByMonthsInYear(String type, Dorm dorm, int year) throws ParseException {
        int endMonth = 0;
        LocalDate curDate = LocalDate.now();
        if (year > curDate.getYear()) {
            endMonth = 0;
        } else if (curDate.getYear() == year) {
            endMonth = curDate.getMonthValue();
        } else {
            endMonth = 12;
        }
        double[] money = new double[endMonth];
        int i = 0;
        LocalDate firstDayOfYear = LocalDate.of(year, 1, 1);
        LocalDate endDayOfLoop = firstDayOfYear.plusMonths(endMonth);
        for (LocalDate localDate = firstDayOfYear; localDate.isBefore(endDayOfLoop); localDate = localDate.plusMonths(1)) {
            money[i++] = getDormMoneyByMonth(type, dorm, localDate.getMonthValue(), year);
        }
        return money;
    }

    ////Branch's profit
    ///
    //

    public double getBranchRevenueByDay(Branch branch, Date date) {
        double revenue = 0;
        for (Dorm dorm : branch.getDorms()) {
            revenue += getDormRevenueByDay(dorm, date);
        }
        return revenue;
    }

    public double getBranchDepreciationByDay(Branch branch, Date date) {
        double depreciation = 0;
        for (Dorm dorm : branch.getDorms()) {
            depreciation += getDormDepreciationByDay(dorm, date);
        }
        List<FacilityDetail> facilityDetails = facilityDetailRepository.findFacilityByBranchIdWithDateBetweenStartDateAndEndDate(branch.getId(), date);
        for (FacilityDetail facilityDetail : facilityDetails) {
            if (!facilityDetail.getExpirationDate().before(date) && !facilityDetail.getProducingDate().after(date)) {
                long dateBeforeInMs = facilityDetail.getProducingDate().getTime();
                long dateAfterInMs = facilityDetail.getExpirationDate().getTime();
                long timeDiff = Math.abs(dateAfterInMs - dateBeforeInMs);
                long daysDiff = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
                depreciation += (double) facilityDetail.getPrice() / daysDiff;
            }
        }
        return depreciation;
    }

    public double getBranchMaintenanceFeeByDay(Branch branch, Date date) throws ParseException {
        double maintenaceFeeByDay = 0;
        for (Dorm dorm : branch.getDorms()) {
            maintenaceFeeByDay += getDormMaintenanceFeeByDay(dorm, date);
        }
        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        List<Maintenance> maintenances = new ArrayList<>();
        List<FacilityDetail> facilityDetails = facilityDetailRepository.findFacilityByBranchIdWithDateBetweenStartDateAndEndDate(branch.getId(), date);
        for (FacilityDetail facilityDetail : facilityDetails) {
            maintenances.addAll(maintenanceRepository.findByFacilityDetailAndFixDate(facilityDetail.getId(), formatter.parse(formatter.format(date))));
        }
        for (Maintenance maintenance : maintenances) {
            maintenaceFeeByDay += maintenance.getPrice();
        }
        return maintenaceFeeByDay;
    }

    public double getBranchMoneyByMonth(String type, Branch branch, int month, int year) throws ParseException {
        double money = 0;
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        LocalDate firstDayOfNextMonth = firstDayOfMonth.plusMonths(1);

        switch (type) {
            case "revenue":
                for (LocalDate localDate = firstDayOfMonth; localDate.isBefore(firstDayOfNextMonth); localDate = localDate.plusDays(1)) {
                    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    money += getBranchRevenueByDay(branch, date);
                }
                break;
            case "depreciation":
                for (LocalDate localDate = firstDayOfMonth; localDate.isBefore(firstDayOfNextMonth); localDate = localDate.plusDays(1)) {
                    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    money += getBranchDepreciationByDay(branch, date);
                }
                break;
            case "maintenance":
                for (LocalDate localDate = firstDayOfMonth; localDate.isBefore(firstDayOfNextMonth); localDate = localDate.plusDays(1)) {
                    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    money += getBranchMaintenanceFeeByDay(branch, date);
                }
                break;
        }
        return money;
    }

    public double getBranchMoneyByYear(String type, Branch branch, int year) throws ParseException {
        double money = 0;
        LocalDate firstDayOfYear = LocalDate.of(year, 1, 1);
        LocalDate firstDayOfNextYear = firstDayOfYear.plusYears(1);
        for (LocalDate localDate = firstDayOfYear; localDate.isBefore(firstDayOfNextYear); localDate = localDate.plusMonths(1)) {
            money += getBranchMoneyByMonth(type, branch, localDate.getMonthValue(), year);
        }
        return money;
    }

    public double[] getBranchMoneyByDaysInMonth(String type, Branch branch, int month, int year) throws ParseException {
        int endDay = 0;
        LocalDate curDate = LocalDate.now();
        int daysInMonth = LocalDate.of(year, month, 1).lengthOfMonth();
        if (year > curDate.getYear()) {
            endDay = 0;
        } else if (curDate.getYear() == year && month > curDate.getMonthValue()) {
            endDay = 0;
        } else if (curDate.getYear() == year && curDate.getMonthValue() == month) {
            endDay = curDate.getDayOfMonth();
        } else if (curDate.getYear() == year && month < curDate.getMonthValue()) {
            endDay = daysInMonth;
        } else {
            endDay = daysInMonth;
        }
        double[] money = new double[endDay];
        int i = 0;
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        LocalDate endDayOfLoop = firstDayOfMonth.plusDays(endDay);
        switch (type) {
            case "revenue":
                for (LocalDate localDate = firstDayOfMonth; localDate.isBefore(endDayOfLoop); localDate = localDate.plusDays(1)) {
                    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    money[i++] = getBranchRevenueByDay(branch, date);
                }
                break;
            case "depreciation":
                for (LocalDate localDate = firstDayOfMonth; localDate.isBefore(endDayOfLoop); localDate = localDate.plusDays(1)) {
                    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    money[i++] = getBranchDepreciationByDay(branch, date);
                }
                break;
            case "maintenance":
                for (LocalDate localDate = firstDayOfMonth; localDate.isBefore(endDayOfLoop); localDate = localDate.plusDays(1)) {
                    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    money[i++] = getBranchMaintenanceFeeByDay(branch, date);
                }
                break;
            default:
                money = new double[0];
        }
        return money;
    }

    public double[] getBranchMoneyByMonthsInYear(String type, Branch branch, int year) throws ParseException {
        int endMonth = 0;
        LocalDate curDate = LocalDate.now();
        if (year > curDate.getYear()) {
            endMonth = 0;
        } else if (curDate.getYear() == year) {
            endMonth = curDate.getMonthValue();
        } else {
            endMonth = 12;
        }
        double[] money = new double[endMonth];
        int i = 0;
        LocalDate firstDayOfYear = LocalDate.of(year, 1, 1);
        LocalDate endDayOfLoop = firstDayOfYear.plusMonths(endMonth);
        for (LocalDate localDate = firstDayOfYear; localDate.isBefore(endDayOfLoop); localDate = localDate.plusMonths(1)) {
            money[i++] = getBranchMoneyByMonth(type, branch, localDate.getMonthValue(), year);
        }
        return money;
    }

    ////All Branchs' profit
    ///
    //
    public double getAllBranchsRevenueByDay(Date date) {
        double revenue = 0;
        if(!date.after(new Date())){
            List<ResidentHistory> residentHistories = residentHistoryRepository.findByDateBetweenStartDateAndEndDate(date);
            for (ResidentHistory residentHistory : residentHistories) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                revenue += (double) residentHistory.getSlot().getRoom().getBasePrice().getSlotPrice() / daysInMonth;
            }
        }

        return revenue;
    }

    public double getAllBranchsDepreciationByDay(Date date) {
        double depreciation = 0;
        if(!date.after(new Date())) {
            List<FacilityDetail> facilityDetails = facilityDetailRepository.findAll();
            for (FacilityDetail facilityDetail : facilityDetails) {
                if (!facilityDetail.getExpirationDate().before(date) && !facilityDetail.getProducingDate().after(date)) {
                    long dateBeforeInMs = facilityDetail.getProducingDate().getTime();
                    long dateAfterInMs = facilityDetail.getExpirationDate().getTime();
                    long timeDiff = Math.abs(dateAfterInMs - dateBeforeInMs);
                    long daysDiff = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
                    depreciation += (double) facilityDetail.getPrice() / daysDiff;
                }
            }
        }
        return depreciation;
    }

    public double getAllBranchsMaintenanceFeeByDay( Date date) throws ParseException {
        double maintenaceFeeByDay = 0;
        if(!date.after(new Date())) {
            DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            List<Maintenance> maintenances = maintenanceRepository.findByFixDate(formatter.parse(formatter.format(date)));
            for (Maintenance maintenance : maintenances) {
                maintenaceFeeByDay += maintenance.getPrice();
            }
        }
        return maintenaceFeeByDay;
    }

    public double getAllBranchsMoneyByMonth(String type, int month, int year) throws ParseException {
        double money = 0;
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        LocalDate firstDayOfNextMonth = firstDayOfMonth.plusMonths(1);

        switch (type) {
            case "revenue":
                for (LocalDate localDate = firstDayOfMonth; localDate.isBefore(firstDayOfNextMonth); localDate = localDate.plusDays(1)) {
                    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    money += getAllBranchsRevenueByDay(date);
                }
                break;
            case "depreciation":
                for (LocalDate localDate = firstDayOfMonth; localDate.isBefore(firstDayOfNextMonth); localDate = localDate.plusDays(1)) {
                    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    money += getAllBranchsDepreciationByDay(date);
                }
                break;
            case "maintenance":
                for (LocalDate localDate = firstDayOfMonth; localDate.isBefore(firstDayOfNextMonth); localDate = localDate.plusDays(1)) {
                    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    money += getAllBranchsMaintenanceFeeByDay(date);
                }
                break;
        }
        return money;
    }

    public double getAllBranchsMoneyByYear(String type, int year) throws ParseException {
        double money = 0;
        LocalDate firstDayOfYear = LocalDate.of(year, 1, 1);
        LocalDate firstDayOfNextYear = firstDayOfYear.plusYears(1);
        for (LocalDate localDate = firstDayOfYear; localDate.isBefore(firstDayOfNextYear); localDate = localDate.plusMonths(1)) {
            money += getAllBranchsMoneyByMonth(type, localDate.getMonthValue(), year);
        }
        return money;
    }

    public double[] getAllBranchsMoneyByDaysInMonth(String type, int month, int year) throws ParseException {
        int endDay = 0;
        LocalDate curDate = LocalDate.now();
        int daysInMonth = LocalDate.of(year, month, 1).lengthOfMonth();
        if (year > curDate.getYear()) {
            endDay = 0;
        } else if (curDate.getYear() == year && month > curDate.getMonthValue()) {
            endDay = 0;
        } else if (curDate.getYear() == year && curDate.getMonthValue() == month) {
            endDay = curDate.getDayOfMonth();
        } else if (curDate.getYear() == year && month < curDate.getMonthValue()) {
            endDay = daysInMonth;
        } else {
            endDay = daysInMonth;
        }
        double[] money = new double[endDay];
        int i = 0;
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        LocalDate endDayOfLoop = firstDayOfMonth.plusDays(endDay);
        switch (type) {
            case "revenue":
                for (LocalDate localDate = firstDayOfMonth; localDate.isBefore(endDayOfLoop); localDate = localDate.plusDays(1)) {
                    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    money[i++] = getAllBranchsRevenueByDay(date);
                }
                break;
            case "depreciation":
                for (LocalDate localDate = firstDayOfMonth; localDate.isBefore(endDayOfLoop); localDate = localDate.plusDays(1)) {
                    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    money[i++] = getAllBranchsDepreciationByDay( date);
                }
                break;
            case "maintenance":
                for (LocalDate localDate = firstDayOfMonth; localDate.isBefore(endDayOfLoop); localDate = localDate.plusDays(1)) {
                    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    money[i++] = getAllBranchsMaintenanceFeeByDay(date);
                }
                break;
            default:
                money = new double[0];
        }
        return money;
    }

    public double[] getAllBranchsMoneyByMonthsInYear(String type, int year) throws ParseException {
        int endMonth = 0;
        LocalDate curDate = LocalDate.now();
        if (year > curDate.getYear()) {
            endMonth = 0;
        } else if (curDate.getYear() == year) {
            endMonth = curDate.getMonthValue();
        } else {
            endMonth = 12;
        }
        double[] money = new double[endMonth];
        int i = 0;
        LocalDate firstDayOfYear = LocalDate.of(year, 1, 1);
        LocalDate endDayOfLoop = firstDayOfYear.plusMonths(endMonth);
        for (LocalDate localDate = firstDayOfYear; localDate.isBefore(endDayOfLoop); localDate = localDate.plusMonths(1)) {
            money[i++] = getAllBranchsMoneyByMonth(type, localDate.getMonthValue(), year);
        }
        return money;
    }
}
