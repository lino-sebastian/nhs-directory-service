# nhs-directory-service
Microservice designed to perform data querying for mock health care patient and admission data.
The service `nhs-directory-service` performs the functionality of reading data from patient,admission csv files and stores data into MYSQL DB.
The service also provides API endpoint to Query the stored data.

## Architecture
### Hexagonal Architecture
Hexagonal architecture is a model or pattern for designing software applications. 
The idea behind it is to put inputs and outputs at the edges of your design. In doing so, you isolate the central logic (the core) of your application from outside concerns. Having inputs and outputs at the edge means you can swap out their handlers without changing the core code.
One major appeal of using hexagonal architecture is that it makes your code easier to test. 
You can swap in fakes for testing, which makes the tests more stable.

## Build and Run
In the project directory, you can run:

### `mvn clean install`

After the build is successful, move to `nhs-directory-service-infra` directory

### `docker compose up`

MYSQL Server gets started up and then you are ready to start the application.
In the same directory,run command to start the application

### `mvn spring-boot:run`

Once the application is successfully started up,you can test the APIs on [URL](http://localhost:8080/nhs-directory-service)

For inserting data into Database,drop the patient and admission consisting data CSVs into `data_drop` folder.
Drop the CSV files one by one to load the data into DB.

### Video Demo
Please refer this [video](https://drive.google.com/file/d/1EMFTWxdtm-miwGKlp6ZPjLLx4ssSwCuV/view?usp=sharing) demo for testing the application




