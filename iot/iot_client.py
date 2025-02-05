import obd
import time
import requests
import configparser
import sys

def calculate_distance(input_speed, input_time):
    speed_kmh = input_speed.value.magnitude
    return (speed_kmh / 3.6 * input_time) / 1000
def authenticate(input_username, input_password):
    auth_payload = {
        "username": input_username,
        "password": input_password,
    }

    auth_response = requests.post("http://localhost:8080/api/v1/public/login", json=auth_payload)
    return auth_response.json()['token']
def send_new_mileage(input_mileage):
    headers = {
        "Authorization": "Bearer " + authenticate(username, password),
        "accept": "*/*"
    }
    try:
        new_mileage_response = requests.post(f"http://localhost:8080/api/v1/iot/newMileageChangeIOT?newMileage={input_mileage}", headers=headers)
        new_mileage_response.raise_for_status()
        print(new_mileage_response.status_code)
    except requests.exceptions.Timeout:
        print("Błąd: Przekroczono limit czasu")

    except requests.exceptions.ConnectionError:
        print("Błąd: Problem z połączeniem")

    except requests.exceptions.HTTPError as err:
        print(f"Błąd HTTP: {err.response.status_code} - {err}")

    except requests.exceptions.RequestException as err:
        print(f"Nieznany błąd: {err}")

config = configparser.ConfigParser()
config.read('config.ini')
username = config['Api Credentials']['username']
password = config['Api Credentials']['password']

connection = obd.OBD(portstr="COM6", baudrate=38400) # Połączenie z interfejsem OBD

if connection.is_connected():

    try:
        with open("total_distance", "r") as file:
            total_distance = float(file.read())
    except FileNotFoundError:
        total_distance = 0

    iteration_time = 1
    start_time = time.perf_counter()
    last_mileage_update_time = start_time
    mileage_update_interval_seconds = 120

    while True:

        speed = connection.query(obd.commands.SPEED) # Odczytaj prędkość pojazdu

        if speed is None:
            continue

        # reset licznika iteracji
        iteration_time = time.perf_counter() - start_time
        start_time = time.perf_counter()

        # obliczenie przejechanego dystansu
        distance_travelled = calculate_distance(speed, iteration_time)
        total_distance += distance_travelled

        try:
            with open("total_distance.tmp", "w") as file:
                file.write(f"{total_distance:.2f}")
            import os
            os.replace("total_distance.tmp", "total_distance")
        except Exception as e:
            continue

        if (time.perf_counter() - last_mileage_update_time) > mileage_update_interval_seconds:
            send_new_mileage(round(total_distance))
            print(f"Wysłano: {round(total_distance)}")
            last_mileage_update_time = time.perf_counter()

        time.sleep(1)

else:
    print("Brak połączenia z interfejsem OBD")