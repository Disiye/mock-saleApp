package parking;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static parking.ParkingStrategy.NO_PARKING_LOT;

public class InOrderParkingStrategyTest {

    public Mockito mock;
	@Test
    public void testCreateReceipt_givenACarAndAParkingLog_thenGiveAReceiptWithCarNameAndParkingLotName() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
	    ParkingLot parkingLot = mock(ParkingLot.class);
	    Car car = mock(Car.class);
	    InOrderParkingStrategy inOrderParkingStrategy = spy(new InOrderParkingStrategy());
        Method method = inOrderParkingStrategy.getClass().getDeclaredMethod("createReceipt", ParkingLot.class, Car.class);
        method.setAccessible(true);
        when(method.invoke(parkingLot,car)).thenReturn(mock(Receipt.class));
    }

    @Test
    public void testCreateNoSpaceReceipt_givenACar_thenGiveANoSpaceReceipt() {

        /* Exercise 1, Write a test case on InOrderParkingStrategy.createNoSpaceReceipt()
         * With using Mockito to mock the input parameter */

    }

    @Test
    public void testPark_givenNoAvailableParkingLot_thenCreateNoSpaceReceipt(){
        InOrderParkingStrategy inOrderParkingStrategy = spy(new InOrderParkingStrategy());
        List<ParkingLot> parkingLots = new ArrayList<>();
        Car car = mock(Car.class);

    }


    @Test
    public void testPark_givenThereIsOneParkingLotWithSpace_thenCreateReceipt(){

        /* Exercise 2: Test park() method. Use Mockito.spy and Mockito.verify to test the situation for one available parking lot */

    }

    @Test
    public void testPark_givenThereIsOneFullParkingLot_thenCreateReceipt(){

        /* Exercise 2: Test park() method. Use Mockito.spy and Mockito.verify to test the situation for one available parking lot but it is full */

    }

    @Test
    public void testPark_givenThereIsMultipleParkingLotAndFirstOneIsFull_thenCreateReceiptWithUnfullParkingLot(){

        ParkingLot parklotA = new ParkingLot("parklotA", 1);
        ParkingLot parklotB = new ParkingLot("parklotB", 1);

        parklotA.getParkedCars().add(new Car("John's car"));

        InOrderParkingStrategy inOrderParkingStrategy = spy(new InOrderParkingStrategy());

        Car car = new Car("Bill's car");
        inOrderParkingStrategy.park(Arrays.asList(parklotA, parklotB),car);

        verify(inOrderParkingStrategy, times(0)).createNoSpaceReceipt(car);
        verify(inOrderParkingStrategy, times(1)).createReceipt(parklotB,car);

    }



}
