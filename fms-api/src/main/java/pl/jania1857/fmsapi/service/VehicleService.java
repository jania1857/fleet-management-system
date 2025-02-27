package pl.jania1857.fmsapi.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import pl.jania1857.fmsapi.dto.*;
import pl.jania1857.fmsapi.model.*;
import pl.jania1857.fmsapi.repository.*;
import pl.jania1857.fmsapi.service.mapper.*;
import pl.jania1857.fmsapi.utils.Fuel;
import pl.jania1857.fmsapi.utils.Status;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    private final RefuelingMapper refuelingMapper;
    private final InspectionMapper inspectionMapper;
    private final ServiceMapper serviceMapper;
    private final MileageChangeMapper mileageChangeMapper;
    private final InsuranceMapper insuranceMapper;
    private final AssignmentMapper assignmentMapper;
    private final UserRepository userRepository;
    private final AssignmentRepository assignmentRepository;
    private final CostRepository costRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public CreateVehicleResponse createVehicle(
            CreateVehicleRequest request
    ) {
        Vehicle vehicle = Vehicle.builder()
                .manufacturer(request.manufacturer())
                .model(request.model())
                .year(request.year())
                .registrationNumber(request.registrationNumber())
                .fuelCardNumber(request.fuelCardNumber())
                .vin(request.vin())
                .fuelType(request.fuelType())
                .displacement(request.displacement())
                .build();

        CreateIotAccountResponse created = userService.newIotAccount(vehicle);
        User iotUser = created.iotUser();
        vehicle.setIotDevice(iotUser);

        MileageChange firstMileage = MileageChange.builder()
                .newMileage(request.mileage())
                .vehicle(vehicle)
                .build();

        StatusChange firstStatus = StatusChange.builder()
                .newStatus(Status.READY)
                .vehicle(vehicle)
                .build();

        Inspection firstInspection = Inspection.builder()
                .inspectionDate(request.inspectionDate())
                .nextInspectionDate(request.nextInspectionDate())
                .description("Kupiono z przeglądem")
                .passed(true)
                .cost(null)
                .vehicle(vehicle)
                .build();

        Insurance firstInsurance = Insurance.builder()
                .type(request.insuranceType())
                .number(request.insuranceNumber())
                .description("Kupiono z ubezpieczeniem")
                .insurer(request.insurer())
                .startDate(request.insuranceStart())
                .endDate(request.insuranceEnd())
                .cost(null)
                .vehicle(vehicle)
                .build();

        vehicle.addStatusChange(firstStatus);
        vehicle.addInspection(firstInspection);
        vehicle.addInsurance(firstInsurance);
        vehicle.addMileageChange(firstMileage);
        vehicle.setRefuelings(new ArrayList<>());
        vehicle.setServices(new ArrayList<>());
        vehicle.setAssignments(new ArrayList<>());

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        VehicleDto vehicleDto = vehicleMapper.toDto(savedVehicle);
        return new CreateVehicleResponse(
                vehicleDto.id(),
                vehicleDto.manufacturer(),
                vehicleDto.model(),
                vehicleDto.year(),
                vehicleDto.registrationNumber(),
                vehicleDto.vin(),
                vehicleDto.fuelType(),
                vehicleDto.displacement(),
                vehicleDto.fuelCardNumber(),
                vehicleDto.statusChanges(),
                vehicleDto.refuelings(),
                vehicleDto.inspections(),
                vehicleDto.services(),
                vehicleDto.insurances(),
                vehicleDto.mileageChanges(),
                vehicleDto.assignments(),
                iotUser.getUsername(),
                created.password()
        );
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

    public void deleteVehicleById(Integer vehicleId) {
        vehicleRepository.deleteById(vehicleId);
    }

    public ChangeStatusResponse changeStatusForVehicle(
            Integer vehicleId,
            ChangeStatusRequest request
    ) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle with id " + vehicleId + " not found"));

        StatusChange newStatus = StatusChange.builder()
                .newStatus(request.newStatus())
                .vehicle(vehicle)
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
                .timestamp(LocalDateTime.now())
                .vehicle(vehicle)
                .build();

        Cost refuelingCost = Cost.builder()
                .amount(request.amount())
                .build();

        Cost savedCost = costRepository.save(refuelingCost);

        refueling.setCost(savedCost);

        vehicle.getRefuelings().add(refueling);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        return refuelingMapper.toDto(savedVehicle.getRefuelings().stream()
                .max(Comparator.comparing(Refueling::getTimestamp))
                .orElseThrow(() -> new EntityNotFoundException("Refueling with id " + refueling.getId() + " not found")));
    }

    public void newRefuelingByTimestamp(
            Integer vehicleId,
            LocalDateTime timestamp,
            BigDecimal quantity,
            BigDecimal unitPrice,
            Fuel fuel
    ) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle with id " + vehicleId + " not found"));

        Refueling refueling = Refueling.builder()
                .fuel(fuel)
                .price(unitPrice)
                .quantity(quantity)
                .timestamp(timestamp)
                .amount(quantity.multiply(unitPrice))
                .vehicle(vehicle)
                .build();

        Cost refuelingCost = Cost.builder()
                .amount(quantity.multiply(unitPrice))
                .build();

        Cost savedCost = costRepository.save(refuelingCost);

        refueling.setCost(savedCost);

        vehicle.getRefuelings().add(refueling);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
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

        Cost savedCost = costRepository.save(inspectionCost);

        Inspection inspection = Inspection.builder()
                .inspectionDate(request.inspectionDate())
                .description(request.description())
                .passed(request.passed())
                .vehicle(vehicle)
                .build();


        inspection.setCost(savedCost);

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

        Cost savedCost = costRepository.save(serviceCost);

        pl.jania1857.fmsapi.model.Service service = pl.jania1857.fmsapi.model.Service.builder()
                .name(request.name())
                .description(request.description())
                .mileageAtTheTime(request.mileageAtTheTime())
                .cost(savedCost)
                .vehicle(vehicle)
                .build();

        vehicle.getServices().add(service);

        MileageChange newMileage = MileageChange.builder()
                .newMileage(request.mileageAtTheTime())
                .vehicle(vehicle)
                .build();

        vehicle.getMileageChanges().add(newMileage);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        pl.jania1857.fmsapi.model.Service latestService = savedVehicle.getServices().stream()
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

        Cost savedCost = costRepository.save(insuranceCost);

        Insurance insurance = Insurance.builder()
                .type(request.insuranceType())
                .number(request.number())
                .description(request.description())
                .insurer(request.insurer())
                .startDate(request.startDate())
                .endDate(request.startDate().plusYears(1))
                .vehicle(vehicle)
                .cost(savedCost)
                .build();

        vehicle.getInsurances().add(insurance);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        Insurance latestInsurance;
        if (savedVehicle.getInsurances().size() < 2) {
            latestInsurance = savedVehicle.getInsurances().getFirst();
        } else {
            latestInsurance = savedVehicle.getInsurances().stream()
                    .filter(ins -> ins.getCost() != null)
                    .max(Comparator.comparing(ins -> ins.getCost().getTimestamp()))
                    .orElseThrow(() -> new EntityNotFoundException("Insurance with id " + insurance.getId() + " not found"));
        }


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
                .vehicle(vehicle)
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

    public byte[] getIotDeviceConfig(
            Integer vehicleId
    ) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle with id " + vehicleId + " not found"));

        User iotUser = vehicle.getIotDevice();
        String iotPassword = userService.generateSecurePassword();
        iotUser.setPassword(passwordEncoder.encode(iotPassword));

        userRepository.save(iotUser);

        String configContent =
                "[Api Credentials]\n"
                        + "username = " + iotUser.getUsername() + "\n"
                        + "password = " + iotPassword + "\n";

        return configContent.getBytes(StandardCharsets.UTF_8);
    }


}
