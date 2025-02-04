import obd
import time

def calculateDistance(speed, time):
    speedKmh = speed.value.to('km/h').magnitude
    return ((speedKmh / 3.6 * time) / 1000)



connection = obd.OBD(portstr="COM6", baudrate=38400) # Połączenie z interfejsem OBD

if connection.is_connected():

    try:
        with open("total_distance", "r") as file:
            total_distance = float(file.read())
    except FileNotFoundError:
        total_distance = 0

    iterationTime = 1
    startTime = time.perf_counter()

    while True:

        speed = connection.query(obd.commands.SPEED) # Odczytaj prędkość pojazdu

        if speed is None:
            continue

        # reset licznika iteracji
        iterationTime = time.perf_counter() - startTime
        startTime = time.perf_counter()

        # obliczenie przejechanego dystansu
        distanceTravelled = calculateDistance(speed, iterationTime)
        total_distance += distanceTravelled

        print(f"{speed.value}")
        print(f"{total_distance:.2f}")

        try:
            with open("total_distance.tmp", "w") as file:
                file.write(f"{total_distance:.2f}")
            import os
            os.replace("total_distance.tmp", "total_distance")
        except Exception as e:
            continue

        time.sleep(1)

else:
    print("Brak połączenia z interfejsem OBD")