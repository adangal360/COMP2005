package com.example.app.service;

import com.example.app.client.HospitalApiClient;
import com.example.app.model.Admission;
import com.example.app.model.Allocation;
import com.example.app.model.Employee;
import com.example.app.model.RoomAllocation;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class HospitalServiceTest {

    @Test
    void getRoomsUsedByPatientReturnsDistinctRooms() {
        // Arrange
        HospitalApiClient fakeClient = new FakeHospitalApiClient();
        HospitalService service = new HospitalService(fakeClient);

        // Act
        List<Integer> actualRooms = service.getRoomsUsedByPatient(10);

        // Assert
        assertEquals(List.of(1, 2), actualRooms);
    }

    private static class FakeHospitalApiClient implements HospitalApiClient {

        @Override
        public List<Admission> getAdmissions() {
            Admission admission1 = new Admission(100, "2026-04-01", null, 10);
            Admission admission2 = new Admission(101, "2026-04-02", null, 10);
            Admission admission3 = new Admission(102, "2026-04-03", null, 99);

            return List.of(admission1, admission2, admission3);
        }

        @Override
        public List<RoomAllocation> getRoomAllocations() {
            RoomAllocation room1 = new RoomAllocation(1, 100, 1, "2026-04-01T10:00:00", null);
            RoomAllocation room2 = new RoomAllocation(2, 101, 2, "2026-04-02T10:00:00", null);
            RoomAllocation duplicateRoom = new RoomAllocation(3, 101, 2, "2026-04-02T12:00:00", null);
            RoomAllocation otherPatientRoom = new RoomAllocation(4, 102, 9, "2026-04-03T10:00:00", null);

            return List.of(room1, room2, duplicateRoom, otherPatientRoom);
        }

        @Override
        public List<Allocation> getAllocations() {
            return List.of();
        }

        @Override
        public List<Employee> getEmployees() {
            return List.of();
        }
    }

    @Test
    void returnsEmptyListWhenPatientHasNoAdmissions() {
        // Arrange
        HospitalApiClient fakeClient = new FakeHospitalApiClient();
        HospitalService service = new HospitalService(fakeClient);

        // Act
        List<Integer> result = service.getRoomsUsedByPatient(999);

        // Assert
        assertEquals(List.of(), result);
    }

    @Test
    void returnsEmptyListWhenNoRoomAllocationsMatch() {
        // Arrange
        HospitalApiClient fakeClient = new HospitalApiClient() {
            @Override
            public List<Admission> getAdmissions() {
                return List.of(new Admission(200, "2026-04-01", null, 10));
            }

            @Override
            public List<RoomAllocation> getRoomAllocations() {
                return List.of(); // no rooms
            }

            @Override
            public List<Allocation> getAllocations() {
                return List.of();
            }

            @Override
            public List<Employee> getEmployees() {
                return List.of();
            }
        };

        HospitalService service = new HospitalService(fakeClient);

        // Act
        List<Integer> result = service.getRoomsUsedByPatient(10);

        // Assert
        assertEquals(List.of(), result);
    }

    @Test
    void getPatientsInRoomLast7DaysReturnsMatchingPatients() {
        // Arrange
        HospitalApiClient fakeClient = new HospitalApiClient() {
            @Override
            public List<Admission> getAdmissions() {
                return List.of(
                        new Admission(100, "2026-04-01", null, 10),
                        new Admission(101, "2026-04-02", null, 20),
                        new Admission(102, "2026-04-03", null, 30)
                );
            }

            @Override
            public List<RoomAllocation> getRoomAllocations() {
                String recent = LocalDateTime.now()
                        .minusDays(2)
                        .format(DateTimeFormatter.ISO_DATE_TIME);

                String old = LocalDateTime.now()
                        .minusDays(10)
                        .format(DateTimeFormatter.ISO_DATE_TIME);

                return List.of(
                        new RoomAllocation(1, 100, 5, recent, null),
                        new RoomAllocation(2, 101, 5, recent, null),
                        new RoomAllocation(3, 102, 5, old, null)
                );
            }

            @Override
            public List<Allocation> getAllocations() {
                return List.of();
            }

            @Override
            public List<Employee> getEmployees() {
                return List.of();
            }
        };

        HospitalService service = new HospitalService(fakeClient);

        // Act
        List<Integer> result = service.getPatientsInRoomLast7Days(5);

        // Assert
        assertEquals(List.of(10, 20), result);
    }

    @Test
    void getPatientsInRoomLast7DaysReturnsEmptyListWhenNoRecentRoomAllocations() {
        // Arrange
        HospitalApiClient fakeClient = new HospitalApiClient() {
            @Override
            public List<Admission> getAdmissions() {
                return List.of(
                        new Admission(100, "2026-04-01", null, 10)
                );
            }

            @Override
            public List<RoomAllocation> getRoomAllocations() {
                String old = LocalDateTime.now()
                        .minusDays(10)
                        .format(DateTimeFormatter.ISO_DATE_TIME);

                return List.of(
                        new RoomAllocation(1, 100, 5, old, null)
                );
            }

            @Override
            public List<Allocation> getAllocations() {
                return List.of();
            }

            @Override
            public List<Employee> getEmployees() {
                return List.of();
            }
        };

        HospitalService service = new HospitalService(fakeClient);

        // Act
        List<Integer> result = service.getPatientsInRoomLast7Days(5);

        // Assert
        assertEquals(List.of(), result);
    }

    @Test
    void getLeastUsedRoomReturnsRoomWithLowestUsageCount() {
        // Arrange
        HospitalApiClient fakeClient = new HospitalApiClient() {
            @Override
            public List<Admission> getAdmissions() {
                return List.of();
            }

            @Override
            public List<RoomAllocation> getRoomAllocations() {
                return List.of(
                        new RoomAllocation(1, 100, 1, "2026-04-01T10:00:00", null),
                        new RoomAllocation(2, 101, 1, "2026-04-02T10:00:00", null),
                        new RoomAllocation(3, 102, 2, "2026-04-03T10:00:00", null)
                );
            }

            @Override
            public List<Allocation> getAllocations() {
                return List.of();
            }

            @Override
            public List<Employee> getEmployees() {
                return List.of();
            }
        };

        HospitalService service = new HospitalService(fakeClient);

        // Act
        Integer result = service.getLeastUsedRoom();

        // Assert
        assertEquals(2, result);
    }

    @Test
    void getLeastUsedRoomReturnsNullWhenNoRoomAllocationsExist() {
        // Arrange
        HospitalApiClient fakeClient = new HospitalApiClient() {
            @Override
            public List<Admission> getAdmissions() {
                return List.of();
            }

            @Override
            public List<RoomAllocation> getRoomAllocations() {
                return List.of();
            }

            @Override
            public List<Allocation> getAllocations() {
                return List.of();
            }

            @Override
            public List<Employee> getEmployees() {
                return List.of();
            }
        };

        HospitalService service = new HospitalService(fakeClient);

        // Act
        Integer result = service.getLeastUsedRoom();

        // Assert
        assertNull(result);
    }

    @Test
    void getStaffResponsibleForThreeOrMorePatientsConcurrentlyReturnsMatchingStaff() {
        // Arrange
        HospitalApiClient fakeClient = new HospitalApiClient() {
            @Override
            public List<Admission> getAdmissions() {
                return List.of();
            }

            @Override
            public List<RoomAllocation> getRoomAllocations() {
                return List.of();
            }

            @Override
            public List<Allocation> getAllocations() {
                return List.of(
                        new Allocation(1, 100, 1, 1, "2026-04-01T10:00:00", "2026-04-01T12:00:00"),
                        new Allocation(2, 101, 1, 2, "2026-04-01T10:30:00", "2026-04-01T12:30:00"),
                        new Allocation(3, 102, 1, 3, "2026-04-01T11:00:00", "2026-04-01T13:00:00"),

                        new Allocation(4, 103, 2, 1, "2026-04-01T10:00:00", "2026-04-01T11:00:00"),
                        new Allocation(5, 104, 2, 2, "2026-04-01T12:00:00", "2026-04-01T13:00:00"),
                        new Allocation(6, 105, 2, 3, "2026-04-01T14:00:00", "2026-04-01T15:00:00")
                );
            }

            @Override
            public List<Employee> getEmployees() {
                return List.of(
                        new Employee(1, "Smith", "Anna"),
                        new Employee(2, "Jones", "Ben")
                );
            }
        };

        HospitalService service = new HospitalService(fakeClient);

        // Act
        List<Integer> result = service.getStaffResponsibleForThreeOrMorePatientsConcurrently();

        // Assert
        assertEquals(List.of(1), result);
    }
}