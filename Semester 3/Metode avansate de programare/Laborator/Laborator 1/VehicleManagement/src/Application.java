import domain.Vehicle;
import repository.VehicleRepository;
import utils.MileageUnit;

public class Application {

	public static void main(String[] args) {
		Vehicle vehicle = new Vehicle("CJ14DGC", 100.0, 2011, MileageUnit.KM);
		
		VehicleRepository repository = new VehicleRepository();
		repository.addVehicle(vehicle);
	
		for (int i = 0; i < repository.getNumberOfVehicles(); i++) {
			Vehicle retrievedVehicle = repository.getVehicleAtPosition(i);
			retrievedVehicle.printVehicleDetails();
		}
	}

}
