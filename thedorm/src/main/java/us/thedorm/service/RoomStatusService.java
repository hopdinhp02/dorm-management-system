package us.thedorm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.thedorm.models.Branch;
import us.thedorm.models.Dorm;
import us.thedorm.models.Room;
import us.thedorm.repositories.ResidentHistoryRepository;
import us.thedorm.repositories.SlotRepository;

import java.time.LocalDate;

@Service
public class RoomStatusService {
    @Autowired
    private ResidentHistoryRepository residentHistoryRepository;
    @Autowired
    private SlotRepository slotRepository;
    public int[] getNumOfAvailableSlotByMonthInDayOfRoom(Room room, int year){
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
        int[] availableSlot = new int[endMonth];
        int i = 0;
        LocalDate firstDayOfYear = LocalDate.of(year, 1, 1);
        LocalDate endDayOfLoop = firstDayOfYear.plusMonths(endMonth);
        for (LocalDate localDate = firstDayOfYear; localDate.isBefore(endDayOfLoop); localDate = localDate.plusMonths(1)) {
            availableSlot[i++] += room.getSlots().size() - residentHistoryRepository.findByRoomIdAndDate(room.getId(),localDate).size();
        }
        return availableSlot;
    }

    public int[] getNumOfBookedSlotByMonthInDayOfRoom(Room room, int year){
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
        int[] bookedSlot = new int[endMonth];
        int i = 0;
        LocalDate firstDayOfYear = LocalDate.of(year, 1, 1);
        LocalDate endDayOfLoop = firstDayOfYear.plusMonths(endMonth);
        for (LocalDate localDate = firstDayOfYear; localDate.isBefore(endDayOfLoop); localDate = localDate.plusMonths(1)) {
            bookedSlot[i++] += residentHistoryRepository.findByRoomIdAndDate(room.getId(),localDate).size();
        }
        return bookedSlot;
    }

    public int[] getNumOfAvailableSlotByMonthInDayOfDorm(Dorm dorm, int year){
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
        int[] availableSlot = new int[endMonth];
        for (Room room : dorm.getRooms()) {
            int i = 0;
            LocalDate firstDayOfYear = LocalDate.of(year, 1, 1);
            LocalDate endDayOfLoop = firstDayOfYear.plusMonths(endMonth);
            for (LocalDate localDate = firstDayOfYear; localDate.isBefore(endDayOfLoop); localDate = localDate.plusMonths(1)) {
                availableSlot[i++] += room.getSlots().size() - residentHistoryRepository.findByRoomIdAndDate(room.getId(),localDate).size();
            }
        }

        return availableSlot;
    }

    public int[] getNumOfBookedSlotByMonthInDayOfDorm(Dorm dorm, int year){
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
        int[] bookedSlot = new int[endMonth];
        for (Room room : dorm.getRooms()) {
            int i = 0;
            LocalDate firstDayOfYear = LocalDate.of(year, 1, 1);
            LocalDate endDayOfLoop = firstDayOfYear.plusMonths(endMonth);
            for (LocalDate localDate = firstDayOfYear; localDate.isBefore(endDayOfLoop); localDate = localDate.plusMonths(1)) {
                bookedSlot[i++] += residentHistoryRepository.findByRoomIdAndDate(room.getId(),localDate).size();
            }
        }
        return bookedSlot;
    }

    public int[] getNumOfAvailableSlotByMonthInDayOfBranch(Branch branch, int year){
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
        int[] availableSlot = new int[endMonth];
        for (Dorm dorm: branch.getDorms()) {
            for (Room room : dorm.getRooms()) {
                int i = 0;
                LocalDate firstDayOfYear = LocalDate.of(year, 1, 1);
                LocalDate endDayOfLoop = firstDayOfYear.plusMonths(endMonth);
                for (LocalDate localDate = firstDayOfYear; localDate.isBefore(endDayOfLoop); localDate = localDate.plusMonths(1)) {
                    availableSlot[i++] += room.getSlots().size() - residentHistoryRepository.findByRoomIdAndDate(room.getId(),localDate).size();
                }
            }
        }
        return availableSlot;
    }

    public int[] getNumOfBookedSlotByMonthInDayOfBranch(Branch branch, int year){
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
        int[] bookedSlot = new int[endMonth];
        for (Dorm dorm: branch.getDorms()) {
            for (Room room : dorm.getRooms()) {
                int i = 0;
                LocalDate firstDayOfYear = LocalDate.of(year, 1, 1);
                LocalDate endDayOfLoop = firstDayOfYear.plusMonths(endMonth);
                for (LocalDate localDate = firstDayOfYear; localDate.isBefore(endDayOfLoop); localDate = localDate.plusMonths(1)) {
                    bookedSlot[i++] += residentHistoryRepository.findByRoomIdAndDate(room.getId(),localDate).size();
                }
            }
        }
        return bookedSlot;
    }

    public int[] getNumOfAvailableSlotByMonthInDay(int year){
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
        int[] availableSlot = new int[endMonth];

                int i = 0;
                LocalDate firstDayOfYear = LocalDate.of(year, 1, 1);
                LocalDate endDayOfLoop = firstDayOfYear.plusMonths(endMonth);
                for (LocalDate localDate = firstDayOfYear; localDate.isBefore(endDayOfLoop); localDate = localDate.plusMonths(1)) {
                    availableSlot[i++] += slotRepository.findAll().size() - residentHistoryRepository.findAllByDate(localDate).size();
                }
        return availableSlot;
    }

    public int[] getNumOfBookedSlotByMonthInDay(int year){
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
        int[] bookedSlot = new int[endMonth];
        int i = 0;
        LocalDate firstDayOfYear = LocalDate.of(year, 1, 1);
        LocalDate endDayOfLoop = firstDayOfYear.plusMonths(endMonth);
        for (LocalDate localDate = firstDayOfYear; localDate.isBefore(endDayOfLoop); localDate = localDate.plusMonths(1)) {
            bookedSlot[i++] += residentHistoryRepository.findAllByDate(localDate).size();
        }
        return bookedSlot;
    }
}
