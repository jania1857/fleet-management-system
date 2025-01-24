package pl.jania1857.fmsapi.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jania1857.fmsapi.dto.*;
import pl.jania1857.fmsapi.model.*;
import pl.jania1857.fmsapi.repository.AssignmentRepository;
import pl.jania1857.fmsapi.repository.UserRepository;
import pl.jania1857.fmsapi.repository.VehicleRepository;
import pl.jania1857.fmsapi.service.mapper.*;
import pl.jania1857.fmsapi.utils.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    private final StatusChangeMapper statusChangeMapper;
    private final RefuelingMapper refuelingMapper;
    private final InspectionMapper inspectionMapper;
    private final ServiceMapper serviceMapper;
    private final MileageChangeMapper mileageChangeMapper;
    private final InsuranceMapper insuranceMapper;
    private final AssignmentMapper assignmentMapper;
    private final UserRepository userRepository;
    private final AssignmentRepository assignmentRepository;

    public VehicleDto createVehicle(
            CreateVehicleRequest request
    ) {
        Vehicle vehicle = Vehicle.builder()
                .manufacturer(request.manufacturer())
                .model(request.model())
                .year(request.year())
                .registrationNumber(request.registrationNumber())
                .vin(request.vin())
                .fuelType(request.fuelType())
                .displacement(request.displacement())
                .build();

        MileageChange firstMileage = MileageChange.builder()
                .newMileage(request.mileage())
                .build();

        StatusChange firstStatus = StatusChange.builder()
                .newStatus(Status.READY)
                .build();

        Inspection firstInspection = Inspection.builder()
                .inspectionDate(request.inspectionDate())
                .nextInspectionDate(request.nextInspectionDate())
                .description("Kupiono z przeglądem")
                .passed(true)
                .cost(null)
                .build();

        vehicle.getMileageChanges().add(firstMileage);
        vehicle.getStatusChanges().add(firstStatus);
        vehicle.getInspections().add(firstInspection);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        return vehicleMapper.toDto(savedVehicle);
    }

    public VehicleDto updateVehicle(
            Integer vehicleId,
            UpdateVehicleRequest request
    ) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle with id " + vehicleId + " not found"));

        vehicle.setManufacturer(request.manufacturer());
        vehicle.setModel(request.model());
        vehicle.setYear(request.year());
        vehicle.setRegistrationNumber(request.registrationNumber());
        vehicle.setVin(request.vin());
        vehicle.setFuelType(request.fuelType());
        vehicle.setDisplacement(request.displacement());

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        return vehicleMapper.toDto(savedVehicle);
    }

    public VehicleDto getVehicleById(Integer vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle with id " + vehicleId + " not found"));

        return vehicleMapper.toDto(vehicle);
    }

    public List<VehicleDto> getAllVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        return vehicles.stream().map(vehicleMapper::toDto).toList();
    }

    public ChangeStatusResponse changeStatusForVehicle(
            Integer vehicleId,
            ChangeStatusRequest request
    ) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle with id " + vehicleId + " not found"));

        StatusChange newStatus = StatusChange.builder()
                .newStatus(request.newStatus())
                .build();

        vehicle.getStatusChanges().add(newStatus);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        return new ChangeStatusResponse(
                savedVehicle.getId(),
                newStatus.getNewStatus()
        );
    }

    public RefuelingDto newRefueling(
            Integer vehicleId,
            NewRefuelingRequest request
    ) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle with id " + vehicleId + " not found"));

        Refueling refueling = Refueling.builder()
                .fuel(request.fuel())
                .price(request.price())
                .quantity(request.quantity())
                .amount(request.amount())
                .build();

        Cost refuelingCost = Cost.builder()
                .amount(request.amount())
                .build();

        refueling.setCost(refuelingCost);

        vehicle.getRefuelings().add(refueling);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        return refuelingMapper.toDto(savedVehicle.getRefuelings().stream()
                .max(Comparator.comparing(Refueling::getTimestamp))
                .orElseThrow(() -> new EntityNotFoundException("Refueling with id " + refueling.getId() + " not found")));
    }

    public InspectionDto newInspection(
            Integer vehicleId,
            NewInspectionRequest request
    ) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle with id " + vehicleId + " not found"));

        Cost inspectionCost = Cost.builder()
                .amount(BigDecimal.valueOf(98))
                .build();

        Inspection inspection = Inspection.builder()
                .inspectionDate(request.inspectionDate())
                .description(request.description())
                .passed(request.passed())
                .cost(inspectionCost)
                .build();

        List<Inspection> inspections = vehicle.getInspections();
        Inspection latestPassedInspection = inspections.stream()
                .max(Comparator.comparing(Inspection::getNextInspectionDate))
                .orElseThrow(() -> new EntityNotFoundException("Inspection with id " + inspection.getId() + " not found"));

        if (!request.passed()) {
            inspection.setNextInspectionDate(latestPassedInspection.getNextInspectionDate());
        } else {
            int vehicleAge = Year.now().getValue() - vehicle.getYear();
            int inspectionInterval;
            if (vehicleAge < 3) {
                inspectionInterval = 3;
            } else if (vehicleAge < 5) {
                inspectionInterval = 2;
            } else {
                inspectionInterval = 1;
            }
            LocalDateTime nextInspectionDate = request.inspectionDate().plusYears(inspectionInterval);
            inspection.setNextInspectionDate(nextInspectionDate);
        }

        vehicle.getInspections().add(inspection);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        Inspection latestInspection = savedVehicle.getInspections().stream()
                .max(Comparator.comparing(Inspection::getInspectionDate))
                .orElseThrow(() -> new EntityNotFoundException("Inspection with id " + inspection.getId() + " not found"));

        return inspectionMapper.toDto(latestInspection);
    }

    public ServiceDto newService(
            Integer vehicleId,
            NewServiceRequest request
    ) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle with id " + vehicleId + " not found"));

        Cost serviceCost = Cost.builder()
                .amount(request.cost())
                .build();

        pl.jania1857.fmsapi.model.Service service = pl.jania1857.fmsapi.model.Service.builder()
                .name(request.name())
                .description(request.description())
                .mileageAtTheTime(request.mileageAtTheTime())
                .cost(serviceCost)
                .build();

        vehicle.getServices().add(service);

        MileageChange newMileage = MileageChange.builder()
                .newMileage(request.mileageAtTheTime())
                .build();

        vehicle.getMileageChanges().add(newMileage);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        pl.jania1857.fmsapi.model.Service latestService = vehicle.getServices().stream()
                .max(Comparator.comparing(pl.jania1857.fmsapi.model.Service::getTimestamp))
                .orElseThrow(() -> new EntityNotFoundException("Service with id " + service.getId() + " not found"));

        return serviceMapper.toDto(latestService);
    }

    public InsuranceDto newInsurance(
            Integer vehicleId,
            NewInsuranceRequest request
    ) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle with id " + vehicleId + " not found"));

        Cost insuranceCost = Cost.builder()
                .amount(request.cost())
                .build();

        Insurance insurance = Insurance.builder()
                .type(request.insuranceType())
                .number(request.number())
                .description(request.description())
                .insurer(request.insurer())
                .startDate(request.startDate())
                .endDate(request.startDate().plusYears(1))
                .cost(insuranceCost)
                .build();

        vehicle.getInsurances().add(insurance);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        Insurance latestInsurance = savedVehicle.getInsurances().stream()
                .max(Comparator.comparing(ins -> ins.getCost().getTimestamp()))
                .orElseThrow(() -> new EntityNotFoundException("Insurance with id " + insurance.getId() + " not found"));

        return insuranceMapper.toDto(latestInsurance);
    }

    public MileageChangeDto newMileageChange(
            Integer vehicleId,
            NewMileageChangeRequest request
    ) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle with id " + vehicleId + " not found"));

        MileageChange mileageChange = MileageChange.builder()
                .newMileage(request.newMileage())
                .build();

        vehicle.getMileageChanges().add(mileageChange);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        MileageChange latestMileageChange = savedVehicle.getMileageChanges().stream()
                .max(Comparator.comparing(MileageChange::getTimestamp))
                .orElseThrow(() -> new EntityNotFoundException("Mileage change with id " + mileageChange.getId() + " not found"));

        return mileageChangeMapper.toDto(latestMileageChange);
    }

    public AssignmentDto newAssignment(
            Integer vehicleId,
            Integer userId
    ) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle with id " + vehicleId + " not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));

        List<Assignment> assignments = vehicle.getAssignments().stream()
                .filter(ass -> ass.getEndTime() == null).toList();

        if (!assignments.isEmpty()) {
            throw new IllegalArgumentException("Assignment with vehicle (ID: " + vehicle.getId() + ") already exists");
        }

        Assignment assignment = Assignment.builder()
                .startTime(LocalDateTime.now())
                .vehicle(vehicle)
                .user(user)
                .build();

        Assignment savedAssignment = assignmentRepository.save(assignment);

        return assignmentMapper.toDto(savedAssignment);
    }
}
