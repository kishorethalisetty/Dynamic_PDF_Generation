# Dynamic_PDF_Generation

# PDF Generation Application

This is a Spring Boot application that provides a REST API for generating dynamic PDFs based on invoice data. The application accepts invoice information, generates a PDF document, and allows users to download it. The generated PDFs are cached, ensuring that identical requests do not result in duplicate PDF generation.

## Features

- Generate PDFs based on seller and buyer information.
- Include line items in the generated PDF.
- Cache generated PDFs to optimize performance for identical requests.
- Download generated PDFs via a unique hash identifier.

## Technologies Used

- Java
- Spring Boot
- iText (5.5.13.4) for PDF generation
- Lombok for reducing boilerplate code
- JUnit 5 and MockMvc for testing

## Prerequisites

- JDK 11 or higher
- Maven
- An IDE like IntelliJ IDEA or Eclipse


- The application will start on http://localhost:8080.

## API Endpoints
Generate PDF
 **POST** `/api/pdf/generate`

![Request](https://github.com/kishorethalisetty/Dynamic_PDF_Generation/blob/main/InvoiceRequest.png)
 
Response

- Success: Returns a message indicating the PDF has been generated along with a download link.
- Failure: Returns an error message.

Download PDF
**GET** `/api/pdf/download/{hash}`

![Response](https://github.com/kishorethalisetty/Dynamic_PDF_Generation/blob/main/InvoiceResponse.png)

Parameters

- `hash`: The unique identifier for the generated PDF.

Response

- Success: Returns the generated PDF file.
- Failure: Returns an error message if the PDF is not found.

## Testing
To run the test cases included in the project, execute the following command:

 The test suite covers:

- PDF generation.
- PDF downloading.
- Caching behavior.



