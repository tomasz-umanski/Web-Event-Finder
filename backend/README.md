# Spring Boot Application Setup

This guide walks you through setting up and running a Spring Boot application locally using Maven.

## Prerequisites

Before you begin, ensure you have the following requirements met:

- **JDK 21:** Make sure Java Development Kit (JDK) version 21 is installed. You can verify by running the following command:
```
java -version
```
If not installed, download and install it from [Oracle JDK 21](https://www.oracle.com/pl/java/technologies/downloads/#java21).

- **Apache Maven 3+:** Maven is required to manage project dependencies and build the application. Verify Maven installation by running:
```
mvn -v
```
If Maven is not installed, refer to [Maven Installation Guide](https://maven.apache.org/install.html).

## Getting Started

### 1. Configure Application Properties

In the backend/src/main/resources directory, you'll need to create `application-local.properties` file for the application configuration.

You can start by copying the provided `application-example.properties` file:
```
cp application-example.properties application-local.properties
```

- Make sure to customize values in the `application-local.properties` file to suit your local setup.

### 2. Install dependencies
Once inside the project's `backend` directory, install the required dependencies by running:
```
mvn clean install
```
This command will clean up any previous builds, resolve all necessary dependencies, and prepare the application for running.

### 3. Running the Application
To run the Spring Boot application, use the following Maven command:
```
mvn spring-boot:run -D spring.profiles.active=local
```

## Setting Up Your Project in IntelliJ IDEA

If you’re using IntelliJ IDEA, follow these steps to properly set up your Spring Boot project:

### 1. Open IntelliJ IDEA:
If you haven’t already installed IntelliJ, you can download it from [here](https://www.jetbrains.com/idea/download/?section=mac). You can use community or enterprise version.

### 2. Import the Project:

- Go to **File > Open.**
- Navigate to your project directory and select it.
- IntelliJ will detect the Maven build files (pom.xml) and prompt you to import the Maven project. Select **Yes.**
- If you were not prompted and your maven project isn't imported, then right-click on the pom.xml file and select **Add as Maven Project**

### 3. Set the Project JDK:
- Navigate to **File > Project Structure > Project.**
- In the SDK section, select **JDK 21** and select **Language level** to **21**. If **JDK 21** is not listed, click **Download JDK**, then select the version to 21 and choose vendor e.g. **Amazon Corretto**.

### 4. Run/Debug Configurations:
To run your Spring Boot application directly from IntelliJ:
- Go to **Run > Edit Configurations**.
- Click the **+** button and select **Maven**.
- Provide desired name e.g. **backend**.
- In the run field add the following command:
```
clean compile spring-boot:run --activate-profiles local
```
- Leave the rest options in default.
- Click Apply and then OK.
- You can now **run or debug** the application directly by clicking the Run or Debug icons in the toolbar.

## Additional Notes

### Used Technologies
- Java 21
- Maven 4.0.0
- Spring Boot 3.4.0
